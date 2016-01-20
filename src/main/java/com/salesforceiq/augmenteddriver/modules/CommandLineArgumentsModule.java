package com.salesforceiq.augmenteddriver.modules;

import com.google.inject.AbstractModule;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;

/**
 * Guice Module for TestRunnerConfig.
 *
 * Basically binds TestRunnerConfig to the static instance that should have been already initialized.
 */
public class CommandLineArgumentsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TestRunnerConfig.class).toInstance(TestRunnerConfig.ARGUMENTS);
    }
}
