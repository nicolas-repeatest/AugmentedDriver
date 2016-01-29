package com.salesforceiq.augmenteddriver.runners;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.google.common.util.concurrent.*;
import com.google.inject.*;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.integrations.IntegrationManager;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import com.salesforceiq.augmenteddriver.modules.TestRunnerConfigModule;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.modules.TestRunnerModule;
import com.salesforceiq.augmenteddriver.util.Quarantine;
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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Main class for running a set of suites.
 */
@Singleton
public class TestSuiteRunner implements Callable<List<Result>> {
    private static final Logger logger = LoggerFactory.getLogger(TestSuiteRunner.class);

    private final TestRunnerFactory testRunnerFactory;
    private final List<String> suites;
    private final String suitesPackage;
    private final int timeoutInMinutes;

    private final List<Result> results;
    private final int maxRetries;
    private final int parallel;
    private final boolean runQuarantine;
    private final Multiset<Method> countTests;
    private final IntegrationManager integrationManager;

    private int totalTests;
    private ListeningExecutorService executor;

    @Inject
    public TestSuiteRunner(TestRunnerConfig arguments,
                           TestRunnerFactory testRunnerFactory,
                           @Named(PropertiesModule.MAX_RETRIES) String maxRetries,
                           IntegrationManager integrationManager) {
        this.testRunnerFactory = Preconditions.checkNotNull(testRunnerFactory);
        this.suites = arguments.suites();
        this.suitesPackage = arguments.suitesPackage();
        this.timeoutInMinutes = arguments.timeoutInMinutes();
        this.parallel = arguments.parallel();
        this.totalTests = 0;
        this.maxRetries = Integer.valueOf(Preconditions.checkNotNull(maxRetries));
        this.runQuarantine = arguments.quarantine();
        this.results = Lists.newArrayList();
        this.countTests = HashMultiset.create();
        this.integrationManager = integrationManager;
    }

    private ListeningExecutorService getExecutor() {
        if (executor == null) {
            executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(parallel));
        }

        return executor;
    }

    @Override
    public List<Result> call() throws Exception {
        integrationManager.initIntegrations();

        long start = System.currentTimeMillis();
        logger.info(String.format("STARTING TestSuiteRunner for suites [%s], running %s tests in parallel", suites, parallel));

        List<Class> classesToTest = TestsFinder.getTestClassesOfPackage(suites, suitesPackage);
        logger.info(String.format("Test Classes to run: %s", classesToTest));

        classesToTest.stream()
                .forEach(test -> Lists.newArrayList(test.getMethods())
                        .stream()
                        .filter(validTest())
                        .forEach(method -> {
                            totalTests++;
                            Util.pause(Util.getRandom(500, 2000));
                            ListenableFuture<AugmentedResult> future = getExecutor().submit(testRunnerFactory.create(method, ""));
                            Futures.addCallback(future, createCallback(method));
                        }));

        logger.info(String.format("Total tests running: %s", totalTests));
        getExecutor().awaitTermination(timeoutInMinutes, TimeUnit.MINUTES);
        logger.info(String.format("FINISHED TestSuiteRunner for suites [%s] in %s", suites, Util.TO_PRETTY_FORMAT.apply(System.currentTimeMillis() - start)));

        return ImmutableList.copyOf(results);
    }

    protected Predicate<Method> validTest() {
        return method -> method.isAnnotationPresent(Test.class)
                && !method.isAnnotationPresent(Ignore.class)
                && (!method.isAnnotationPresent(Quarantine.class) || runQuarantine);
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
                    logger.info(String.format("Test %s finished of %s", results.size(), totalTests));
                    if (results.size() == totalTests) {
                        getExecutor().shutdown();
                    }
                    processOutput(result.getOut());
                } else {
                    // If it fails but still has retries, we do not count it and add it back
                    // to the executor.
                    if (countTests.count(method) < maxRetries) {
                        logger.info(String.format("Test %s#%s failed, retrying", method.getDeclaringClass().getCanonicalName(), method.getName()));
                        ListenableFuture<AugmentedResult> future = getExecutor().submit(testRunnerFactory.create(method, ""));
                        Futures.addCallback(future, createCallback(method));
                    } else {
                        // It failed and reached the max retries, we add it to the result list and shut down
                        // if it was the last one.
                        results.add(result.getResult());
                        logger.info(String.format("Test %s finished of %s", results.size(), totalTests));
                        if (results.size() == totalTests) {
                            getExecutor().shutdown();
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
                logger.error("-------------------------------------------------------------");
                logger.error("-------------------------------------------------------------");
                logger.error("-------------------------------------------------------------");
                logger.error("-------------------------------------------------------------");
                logger.error("UNEXPECTED FAILURE");
                logger.error(String.format("FAILED %s#%s", method.getDeclaringClass(), method.getName()));
                logger.error("REASON: " + t.getMessage());
                logger.error("STACKTRACE:");
                logger.error(ExceptionUtils.getStackTrace(t));
                logger.error("-------------------------------------------------------------");
                logger.error("-------------------------------------------------------------");
                logger.error("-------------------------------------------------------------");
            }

            private void processOutput(ByteArrayOutputStream outputStream) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

                synchronized (System.out) {
                    int oneByte;
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
        IntegrationManager.args = args;

        List<Module> modules = Lists.newArrayList(
                new TestRunnerConfigModule(),
                new PropertiesModule(),
                new TestRunnerModule()
        );

        Injector injector = Guice.createInjector(modules);
        TestSuiteRunner runner = injector.getInstance(TestSuiteRunner.class);

        List<Result> results = runner.call();
        List<Result> failed = failedTests(results);

        if (!failed.isEmpty()) {
            System.exit(1);
        }
    }

}
