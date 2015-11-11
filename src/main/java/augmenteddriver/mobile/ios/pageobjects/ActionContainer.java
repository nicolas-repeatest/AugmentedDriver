package augmenteddriver.mobile.ios.pageobjects;

import augmenteddriver.mobile.ios.AugmentedIOSDriver;
import augmenteddriver.mobile.ios.AugmentedIOSElement;

/**
 * Generic interface for executing an action in a container.
 */
public interface ActionContainer {
    void execute(AugmentedIOSDriver driver, AugmentedIOSElement container);
}
