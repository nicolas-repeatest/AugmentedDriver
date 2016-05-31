package com.salesforceiq.augmenteddriver.server.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.salesforceiq.augmenteddriver.server.slack.RunCommandCallable;
import com.salesforceiq.augmenteddriver.server.slack.RunCommandCallableFactory;

public class AugmentedDriverServerModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(RunCommandCallable.class, RunCommandCallable.class)
                .build(RunCommandCallableFactory.class));
    }
}
