package augmenteddriver.modules;

import augmenteddriver.mobile.AugmentedMobileFunctions;
import augmenteddriver.mobile.android.*;
import augmenteddriver.util.AugmentedFunctions;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.openqa.selenium.WebDriver;

public class AugmentedAndroidDriverModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<AugmentedFunctions<AugmentedAndroidElement>>() {})
                .to(new TypeLiteral<AugmentedAndroidFunctions>() {});
        bind(new TypeLiteral<AugmentedMobileFunctions<AugmentedAndroidElement>>() {})
                .to(new TypeLiteral<AugmentedAndroidFunctions>() {
                });
        bind(WebDriver.class).to(AugmentedAndroidDriver.class);
        bind(AugmentedAndroidDriver.class).toProvider(AugmentedAndroidDriverProvider.class);
        install(new FactoryModuleBuilder()
                .implement(AugmentedAndroidElement.class, AugmentedAndroidElement.class)
                .build(AugmentedAndroidElementFactory.class));



    }
}

