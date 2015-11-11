package augmenteddriver.web.pageobjects;

import augmenteddriver.web.AugmentedWebDriver;
import augmenteddriver.web.AugmentedWebElement;

/**
 * Generic interface for executing an action in a container.
 */
public interface ActionContainer {
    void execute(AugmentedWebDriver driver, AugmentedWebElement container);
}
