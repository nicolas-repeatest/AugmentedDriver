package com.salesforceiq.augmenteddriver.mobile.ios.pageobjects;


import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSDriver;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSElement;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSFunctions;

/**
 * Common functionality to all IOS Page Objects
 */
public interface IOSPageObjectActionsInterface {

    /**
     * Initializes a IosPageObject instance.
     *
     * @param clazz the IOSPageObject class to initialize.
     * @param <T> The type of the IOSPageObject to return.
     * @return the PageObject represented by the input
     */
    <T extends IOSPageObject> T get(Class<T> clazz);

    /**
     * Initializes a IOSPageContainerObject instance.
     *
     * @param clazz the IOSPageContainerObject class to initialize.
     * @param container the container element that is used as a root.
     * @param <T> The type of the IOSPageContainerObject to return.
     * @return the PageContainerObject represented by the input
     */
    <T extends IOSPageContainerObject> T get(Class<T> clazz, AugmentedIOSElement container);

    /**
     * @return the AugmentedIOSDriver to use.
     */
    AugmentedIOSDriver driver();

    /**
     * @return the Augmented IOS Functions to use.
     */
    AugmentedIOSFunctions augmented();
}
