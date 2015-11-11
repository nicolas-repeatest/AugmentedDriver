package augmenteddriver.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import augmenteddriver.runners.TestMethodRunner;
import augmenteddriver.runners.TestRunner;
import augmenteddriver.runners.TestRunnerFactory;
import augmenteddriver.runners.TestSuiteRunner;

import java.io.ByteArrayOutputStream;

/**
 * Guice Module for the TestRunner.
 */
public class TestRunnerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TestSuiteRunner.class);
        bind(TestMethodRunner.class);
        bind(ByteArrayOutputStream.class);
        install(new FactoryModuleBuilder()
                .implement(TestRunner.class, TestRunner.class)
                .build(TestRunnerFactory.class));

    }
}
