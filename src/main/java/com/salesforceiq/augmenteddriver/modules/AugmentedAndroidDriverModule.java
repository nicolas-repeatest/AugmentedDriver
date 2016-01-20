package com.salesforceiq.augmenteddriver.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.salesforceiq.augmenteddriver.mobile.android.*;
import org.openqa.selenium.WebDriver;

/**
 * Module for AugmentedAndroidDriver.
 */
public class AugmentedAndroidDriverModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(WebDriver.class).to(AugmentedAndroidDriver.class);
        install(new FactoryModuleBuilder()
                .implement(AugmentedAndroidFunctions.class, AugmentedAndroidFunctions.class)
                .build(AugmentedAndroidFunctionsFactory.class));
        bind(AugmentedAndroidDriver.class).toProvider(AugmentedAndroidDriverProvider.class);
        install(new FactoryModuleBuilder()
                .implement(AugmentedAndroidElement.class, AugmentedAndroidElement.class)
                .build(AugmentedAndroidElementFactory.class));
    }
}

