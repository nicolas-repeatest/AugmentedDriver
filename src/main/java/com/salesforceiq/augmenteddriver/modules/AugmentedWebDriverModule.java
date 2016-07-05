package com.salesforceiq.augmenteddriver.modules;

import com.salesforceiq.augmenteddriver.web.*;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.salesforceiq.augmenteddriver.web.applitools.AugmentedEyes;
import com.salesforceiq.augmenteddriver.web.applitools.AugmentedEyesFactory;
import com.salesforceiq.augmenteddriver.web.applitools.AugmentedEyesImpl;
import org.openqa.selenium.WebDriver;

/**
 * Module for AugmentedWebDriver.
 */
public class AugmentedWebDriverModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(WebDriver.class).to(AugmentedWebDriver.class);
        bind(AugmentedWebDriver.class).toProvider(AugmentedWebDriverProvider.class);
        install(new FactoryModuleBuilder()
                .implement(AugmentedWebElement.class, AugmentedWebElement.class)
                .build(AugmentedWebElementFactory.class));
        install(new FactoryModuleBuilder()
                .implement(AugmentedWebFunctions.class, AugmentedWebFunctions.class)
                .build(AugmentedWebFunctionsFactory.class));
        install(new FactoryModuleBuilder()
                .implement(AugmentedEyes.class, AugmentedEyesImpl.class)
                .build(AugmentedEyesFactory.class));
    }
}
