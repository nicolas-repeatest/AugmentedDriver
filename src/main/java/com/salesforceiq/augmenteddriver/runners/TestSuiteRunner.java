package com.salesforceiq.augmenteddriver.runners;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.integrations.IntegrationFactory;
import com.salesforceiq.augmenteddriver.modules.CommandLineArgumentsModule;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.modules.TestRunnerModule;
import com.salesforceiq.augmenteddriver.util.Quarantine;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import com.salesforceiq.augmenteddriver.util.TestsFinder;
import com.salesforceiq.augmenteddriver.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Main class for running a set of suites.
 */
@Singleton
public class TestSuiteRunner implements Callable<List<AugmentedResult>> {
    private static final Logger LOG = LoggerFactory.getLogger(TestSuiteRunner.class);

    private final TestRunnerFactory testRunnerFactory;
    private final List<String> suites;
    private final String suitesPackage;
    private final int timeoutInMinutes;
    private final ListeningExecutorService executor;
    private final List<AugmentedResult> results;
    private final int parallel;
    private final boolean quarantine;
    private final IntegrationFactory integrationFactory;
    private int totalTests;

    @Inject
    public TestSuiteRunner(
            @Named(PropertiesModule.TIMEOUT_IN_MINUTES) String timeOutInMinutes,
            TestRunnerConfig arguments,
            TestRunnerFactory testRunnerFactory,
            IntegrationFactory integrationFactory) {
        this.testRunnerFactory = Preconditions.checkNotNull(testRunnerFactory);
        this.suites = arguments.suites();
        this.suitesPackage = arguments.suitesPackage();
        this.timeoutInMinutes = Integer.valueOf(timeOutInMinutes);
        this.parallel = arguments.parallel();
        this.executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(parallel));
        this.totalTests = 0;
        this.quarantine = arguments.quarantine();
        this.results = Collections.synchronizedList(Lists.newArrayList());
        this.integrationFactory = Preconditions.checkNotNull(integrationFactory);
    }

    @Override
    public List<AugmentedResult> call() throws Exception {
        long start = System.currentTimeMillis();
        LOG.info(String.format("STARTING TestSuiteRunner for suites [%s], running %s tests in parallel", suites, parallel));
        List<Class> classesToTest = TestsFinder.getTestClassesOfPackage(suites, suitesPackage);
        LOG.info(String.format("Test Classes to run: %s", classesToTest));
        try {
            if (integrationFactory.slack().isEnabled()) {
                integrationFactory.slack().initialize();
            }
            classesToTest.stream()
                    .forEach(test -> Lists.newArrayList(test.getMethods())
                            .stream()
                            .filter(method -> method.isAnnotationPresent(Test.class)
                                    && !method.isAnnotationPresent(Ignore.class)
                                    && method.isAnnotationPresent(Quarantine.class) == quarantine)

                            .forEach(method -> {
                                totalTests++;
                                Util.pause(Util.getRandom(500, 2000));
                                ListenableFuture<AugmentedResult> future = executor.submit(testRunnerFactory.create(method, "", true));
                                Futures.addCallback(future, createCallback(method));
                            }));
            LOG.info(String.format("Total tests running: %s", totalTests));
            executor.awaitTermination(timeoutInMinutes, TimeUnit.MINUTES);
            LOG.info(String.format("FINISHED TestSuiteRunner for suites [%s] in %s", suites, Util.TO_PRETTY_FORMAT.apply(System.currentTimeMillis() - start)));
            if (integrationFactory.slack().isEnabled()) {
                integrationFactory.slack().digest(String.format("Suite Results: %s", suites), results);
            }
        } finally {
            if (integrationFactory.slack().isEnabled()) {
                integrationFactory.slack().close();
            }
        }
        return ImmutableList.copyOf(results);
    }

    /**
     * Creates a callback that is used to retry tests that fail.
     *
     * @param method the test to run.
     * @return the Callback that will retry if a test fails.
     */
    private FutureCallback<AugmentedResult> createCallback(Method method) {
        return new FutureCallback<AugmentedResult>() {
            @Override
            public void onSuccess(AugmentedResult result) {
                results.add(result);
                LOG.info(String.format("Test %s finished of %s", results.size(), totalTests));
                if (results.size() == totalTests) {
                    executor.shutdown();
                }
                processOutput(result.getOut());
            }

            /**
             * For debugging, if everything goes well should never happen.
             *
             * @param t the throwable that caused the error.
             */
            @Override
            public void onFailure(Throwable t) {
                System.out.println("-------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------");
                System.out.println("UNEXPECTED FAILURE");
                System.out.println(String.format("FAILED %s#%s", method.getDeclaringClass(), method.getName()));
                System.out.println("REASON: " + t.getMessage());
                System.out.println("STACKTRACE:");
                System.out.println(ExceptionUtils.getStackTrace(t));
                System.out.println("-------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------");
            }

            private void processOutput(ByteArrayOutputStream outputStream) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                int oneByte;
                synchronized (System.out) {
                    while ((oneByte = inputStream.read()) != -1) {
                        System.out.write(oneByte);
                    }
                }
            }
        };
    }

    private static List<AugmentedResult> failedTests(List<AugmentedResult> results) {
        return results.stream()
                .filter(result -> !result.getResult().wasSuccessful())
                .collect(Collectors.toList());
    }

    private static void checkArguments(TestRunnerConfig arguments) {
        Preconditions.checkNotNull(arguments.suites(), "There should be at least one suite passed in the -suites argument");
        Preconditions.checkArgument(!arguments.suites().isEmpty(), "There should be at least one suite passed in the -suites argument");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(arguments.suitesPackage()), "-suitesPackage should be defined");
        Preconditions.checkNotNull(arguments.capabilities(), "You should specify capabilites with -capabilities parameter");
    }

    public static void main(String[] args) throws Exception {
        TestRunnerConfig arguments = TestRunnerConfig.initialize(args);
        checkArguments(arguments);
        List<Module> modules = com.google.common.collect.Lists.newArrayList(
                new CommandLineArgumentsModule(),
                new PropertiesModule(),
                new TestRunnerModule());
        Injector injector = Guice.createInjector(modules);
        TestSuiteRunner runner = injector.getInstance(TestSuiteRunner.class);
        List<AugmentedResult> results = runner.call();
        List<AugmentedResult> failed = failedTests(results);
        if (!failed.isEmpty()) {
            System.exit(1);
        }
    }
}
