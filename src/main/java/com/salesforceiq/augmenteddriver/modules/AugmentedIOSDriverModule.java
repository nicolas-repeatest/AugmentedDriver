package com.salesforceiq.augmenteddriver.modules;

import com.salesforceiq.augmenteddriver.mobile.AugmentedMobileFunctions;
import com.salesforceiq.augmenteddriver.mobile.ios.*;
import com.salesforceiq.augmenteddriver.util.AugmentedFunctions;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.salesforceiq.augmenteddriver.web.AugmentedWebFunctions;
import com.salesforceiq.augmenteddriver.web.AugmentedWebFunctionsFactory;
import org.openqa.selenium.WebDriver;

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
