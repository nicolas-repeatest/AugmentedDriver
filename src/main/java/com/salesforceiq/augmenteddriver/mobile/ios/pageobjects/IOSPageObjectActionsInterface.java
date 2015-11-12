package com.salesforceiq.augmenteddriver.mobile.ios.pageobjects;


import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSDriver;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSElement;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSFunctions;

/**
 * Common functionality to all WebPageObjects
 */
public interface IOSPageObjectActionsInterface {
    /**
     * returns the PageObject represented by the input
     */
    <T extends IOSPageObject> T get(Class<T> clazz);

    /**
     * returns the PageContainerObject represented by the input
     */
    <T extends IOSPageContainerObject> T get(Class<T> clazz, AugmentedIOSElement container);

    /**
     * @return the AugmentedWebDriver to use.
     */
    AugmentedIOSDriver driver();

    /**
     * @return the Augmented IOS Functions to use.
     */
    AugmentedIOSFunctions augmented();

    /**
     * Executes an action and returns the landing Page Object
     */
    <T extends IOSPageObject> T action(Action action, Class<T> landingPageObject);

    /**
     * Executes an action in a container and returns the landing Page Object
     */
    <T extends IOSPageContainerObject> T action(ActionContainer action, AugmentedIOSElement container, Class<T> landingPageObject);
}
