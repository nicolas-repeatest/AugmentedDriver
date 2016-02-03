package com.salesforceiq.augmenteddriver.runners;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.runner.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
import com.salesforceiq.augmenteddriver.integrations.IntegrationManager;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.modules.TestRunnerConfigModule;
import com.salesforceiq.augmenteddriver.modules.TestRunnerModule;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import com.salesforceiq.augmenteddriver.util.Util;

/**
 * Main class for running one test.
 */
@Singleton
public class TestMethodRunner implements Callable<List<Result>> {
    private static final Logger logger = LoggerFactory.getLogger(TestMethodRunner.class);

    private final Method method;
    private final int quantity;
    private final ListeningExecutorService executor;
    private final List<Result> results;
    private final int timeoutInMinutes;
    private final TestRunnerFactory testRunnerFactory;
    private final int parallel;

    @Inject
    public TestMethodRunner(TestRunnerConfig testRunnerConfig,
                            TestRunnerFactory testRunnerFactory) {
        this.method = Preconditions.checkNotNull(testRunnerConfig.test());
        this.testRunnerFactory = Preconditions.checkNotNull(testRunnerFactory);
        this.quantity = testRunnerConfig.numberOfExecutions();
        this.results = Lists.newArrayList();
        this.parallel = testRunnerConfig.parallel();
        this.executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(parallel));
        this.timeoutInMinutes = testRunnerConfig.timeoutInMinutes();
    }

    @Override
    public List<Result> call() throws Exception {
        String testName = String.format("%s#%s", method.getDeclaringClass().getCanonicalName(), method.getName());
        long start = System.currentTimeMillis();
        logger.info(String.format("STARTING TestMethodRunner %s, running it %s times %s in parallel", testName, quantity, parallel));
        
        for (int index = 0; index < this.quantity; index++) {
            Util.pause(Util.getRandom(500, 2000));
            ListenableFuture<AugmentedResult> future = executor.submit(testRunnerFactory.create(method, String.valueOf(index)));
            Futures.addCallback(future, createCallback(method));
        }
        
        executor.awaitTermination(timeoutInMinutes, TimeUnit.MINUTES);
        logger.info(String.format("FINISHED TestMethodRunner %s in %s", testName,Util.TO_PRETTY_FORMAT.apply(System.currentTimeMillis() - start)));
        
        return ImmutableList.copyOf(results);
    }

    private FutureCallback<AugmentedResult> createCallback(Method method) {
        return new FutureCallback<AugmentedResult>() {
            @Override
            public void onSuccess(AugmentedResult result) {
                results.add(result.getResult());
                logger.info(String.format("Test %s finished of %s", results.size(), quantity));
                
                if (results.size() == quantity) {
                    executor.shutdown();
                }
                
                processOutput(result.getOut());
            }

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
        Preconditions.checkNotNull(arguments.clazz(), "You should specify a class with -clazz parameter");
        Preconditions.checkNotNull(arguments.test(), "You should specify a test with -test parameter");
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
        TestMethodRunner runner = injector.getInstance(TestMethodRunner.class);
        List<Result> results = runner.call();
        List<Result> failed = failedTests(results);
        
        if (!failed.isEmpty()) {
            System.exit(1);
        }
    }
    
}
