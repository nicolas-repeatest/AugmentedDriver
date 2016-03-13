package com.salesforceiq.augmenteddriver.runners;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.google.common.util.concurrent.*;
import com.google.inject.*;
import com.google.inject.name.Named;
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
public class TestSuiteRunner implements Callable<List<Result>> {
    private static final Logger LOG = LoggerFactory.getLogger(TestSuiteRunner.class);

    private final TestRunnerFactory testRunnerFactory;
    private final List<String> suites;
    private final String suitesPackage;
    private final int timeoutInMinutes;
    private final ListeningExecutorService executor;
    private final List<Result> results;
    private final int maxRetries;
    private final int parallel;
    private final boolean quarantine;
    private int totalTests;
    private final Multiset<Method> countTests;

    @Inject
    public TestSuiteRunner(TestRunnerConfig arguments,
                           TestRunnerFactory testRunnerFactory,
                           @Named(PropertiesModule.MAX_RETRIES) String maxRetries) {
        this.testRunnerFactory = Preconditions.checkNotNull(testRunnerFactory);
        this.suites = arguments.suites();
        this.suitesPackage = arguments.suitesPackage();
        this.timeoutInMinutes = arguments.timeoutInMinutes();
        this.parallel = arguments.parallel();
        this.executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(parallel));
        this.totalTests = 0;
        this.maxRetries = Integer.valueOf(Preconditions.checkNotNull(maxRetries));
        this.quarantine = arguments.quarantine();
        this.results = Collections.synchronizedList(Lists.newArrayList());
        this.countTests = ConcurrentHashMultiset.create();
    }

    @Override
    public List<Result> call() throws Exception {
        long start = System.currentTimeMillis();
        LOG.info(String.format("STARTING TestSuiteRunner for suites [%s], running %s tests in parallel", suites, parallel));
        List<Class> classesToTest = TestsFinder.getTestClassesOfPackage(suites, suitesPackage);
        LOG.info(String.format("Test Classes to run: %s", classesToTest));
        classesToTest.stream()
                .forEach(test -> Lists.newArrayList(test.getMethods())
                        .stream()
                        .filter(method -> method.isAnnotationPresent(Test.class)
                                && !method.isAnnotationPresent(Ignore.class)
                                && method.isAnnotationPresent(Quarantine.class) == quarantine)
                        .forEach(method -> {
                            totalTests++;
                            Util.pause(Util.getRandom(500, 2000));
                            ListenableFuture<AugmentedResult> future = executor.submit(testRunnerFactory.create(method, ""));
                            Futures.addCallback(future, createCallback(method));
                        }));
        LOG.info(String.format("Total tests running: %s", totalTests));
        executor.awaitTermination(timeoutInMinutes, TimeUnit.MINUTES);
        LOG.info(String.format("FINISHED TestSuiteRunner for suites [%s] in %s", suites, Util.TO_PRETTY_FORMAT.apply(System.currentTimeMillis() - start)));
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
                countTests.add(method);
                // If the test succeeds, we add it to the result list and we shut down if it is the
                // last one.
                if (result.getResult().wasSuccessful()) {
                    results.add(result.getResult());
                    LOG.info(String.format("Test %s finished of %s", results.size(), totalTests));
                    if (results.size() == totalTests) {
                        executor.shutdown();
                    }
                    processOutput(result.getOut());
                } else {
                    // If it fails but still has retries, we do not count it and add it back
                    // to the executor.
                    if (countTests.count(method) < maxRetries) {
                        LOG.info(String.format("Test %s#%s failed, retrying", method.getDeclaringClass().getCanonicalName(), method.getName()));
                        ListenableFuture<AugmentedResult> future = executor.submit(testRunnerFactory.create(method, ""));
                        Futures.addCallback(future, createCallback(method));
                    } else {
                        // It failed and reached the max retries, we add it to the result list and shut down
                        // if it was the last one.
                        results.add(result.getResult());
                        LOG.info(String.format("Test %s finished of %s", results.size(), totalTests));
                        if (results.size() == totalTests) {
                            executor.shutdown();
                        }
                        processOutput(result.getOut());
                    }
                }
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

    private static List<Result> failedTests(List<Result> results) {
        return results.stream()
                .filter(result -> !result.wasSuccessful())
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
        List<Result> results = runner.call();
        List<Result> failed = failedTests(results);
        if (!failed.isEmpty()) {
            System.exit(1);
        }
    }
}
