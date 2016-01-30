package com.salesforceiq.augmenteddriver.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.salesforceiq.augmenteddriver.runners.TestMethodRunner;
import com.salesforceiq.augmenteddriver.runners.TestRunner;
import com.salesforceiq.augmenteddriver.runners.TestRunnerFactory;
import com.salesforceiq.augmenteddriver.runners.TestSuiteRunner;

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
