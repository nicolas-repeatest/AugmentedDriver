package augmenteddriver.modules;

import augmenteddriver.web.*;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import augmenteddriver.util.AugmentedFunctions;
import org.openqa.selenium.WebDriver;

public class AugmentedWebDriverModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<AugmentedFunctions<AugmentedWebElement>>() {})
                .to(new TypeLiteral<AugmentedWebFunctions>() {
                });
        bind(WebDriver.class).to(AugmentedWebDriver.class);
        bind(AugmentedWebDriver.class).toProvider(AugmentedWebDriverProvider.class);
        install(new FactoryModuleBuilder()
                .implement(AugmentedWebElement.class, AugmentedWebElement.class)
                .build(AugmentedWebElementFactory.class));
    }
}
