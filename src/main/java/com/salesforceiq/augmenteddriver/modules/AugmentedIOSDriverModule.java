package com.salesforceiq.augmenteddriver.modules;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.salesforceiq.augmenteddriver.mobile.ios.*;
import org.openqa.selenium.WebDriver;

/**
 * Module for AugmentedIOSDriver.
 */
public class AugmentedIOSDriverModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(WebDriver.class).to(AugmentedIOSDriver.class);
        bind(AugmentedIOSDriver.class).toProvider(AugmentedIOSDriverProvider.class);

        install(new FactoryModuleBuilder()
                .implement(AugmentedIOSElement.class, AugmentedIOSElement.class)
                .build(AugmentedIOSElementFactory.class));
        install(new FactoryModuleBuilder()
                .implement(AugmentedIOSFunctions.class, AugmentedIOSFunctions.class)
                .build(AugmentedIOSFunctionsFactory.class));
    }
}
