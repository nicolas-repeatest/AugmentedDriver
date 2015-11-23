package com.salesforceiq.augmenteddriver.modules;

import com.salesforceiq.augmenteddriver.mobile.AugmentedMobileFunctions;
import com.salesforceiq.augmenteddriver.mobile.android.*;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSFunctions;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSFunctionsFactory;
import com.salesforceiq.augmenteddriver.util.AugmentedFunctions;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.openqa.selenium.WebDriver;

public class AugmentedAndroidDriverModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<AugmentedMobileFunctions<AugmentedAndroidElement>>() {})
                .to(new TypeLiteral<AugmentedAndroidFunctions>() {
                });
        bind(WebDriver.class).to(AugmentedAndroidDriver.class);
        bind(AugmentedAndroidDriver.class).toProvider(AugmentedAndroidDriverProvider.class);
        install(new FactoryModuleBuilder()
                .implement(AugmentedAndroidElement.class, AugmentedAndroidElement.class)
                .build(AugmentedAndroidElementFactory.class));
        install(new FactoryModuleBuilder()
                .implement(AugmentedAndroidFunctions.class, AugmentedAndroidFunctions.class)
                .build(AugmentedAndroidFunctionsFactory.class));
    }
}

