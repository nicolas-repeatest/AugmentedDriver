package augmenteddriver.modules;

import augmenteddriver.mobile.AugmentedMobileFunctions;
import augmenteddriver.mobile.ios.*;
import augmenteddriver.util.AugmentedFunctions;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.openqa.selenium.WebDriver;

public class AugmentedIOSDriverModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<AugmentedFunctions<AugmentedIOSElement>>() {})
                .to(new TypeLiteral<AugmentedIOSFunctions>() {});
        bind(new TypeLiteral<AugmentedMobileFunctions<AugmentedIOSElement>>() {})
                .to(new TypeLiteral<AugmentedIOSFunctions>() {});
        bind(WebDriver.class).to(AugmentedIOSDriver.class);
        bind(AugmentedIOSDriver.class).toProvider(AugmentedIOSDriverProvider.class);

        install(new FactoryModuleBuilder()
                .implement(AugmentedIOSElement.class, AugmentedIOSElement.class)
                .build(AugmentedIOSElementFactory.class));

    }
}
