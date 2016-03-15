package com.salesforceiq.augmenteddriver.mobile.ios.pageobjects;


import com.google.common.base.Predicate;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSDriver;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSElement;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSFunctions;
import com.salesforceiq.augmenteddriver.util.PageObjectWaiter;

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
     * Initializes a IosPageObject instance.
     *
     * @param clazz the IOSPageObject class to initialize.
     * @param waitUntil Will also wait until that predicate is true.
     * @param <T> The type of the IOSPageObject to return.
     * @return the PageObject represented by the input
     */
    <T extends IOSPageObject> T get(Class<T> clazz, Predicate<T> waitUntil);

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
     * Initializes a IOSPageContainerObject instance.
     *
     * @param clazz the IOSPageContainerObject class to initialize.
     * @param waitUntil Will also wait until that predicate is true.
     * @param container the container element that is used as a root.
     * @param <T> The type of the IOSPageContainerObject to return.
     * @return the PageContainerObject represented by the input
     */
    <T extends IOSPageContainerObject> T get(Class<T> clazz, AugmentedIOSElement container, Predicate<T> waitUntil);

    /**
     * @return the AugmentedIOSDriver to use.
     */
    AugmentedIOSDriver driver();

    /**
     * @return the Augmented IOS Functions to use.
     */
    AugmentedIOSFunctions augmented();

    /**
     * @return the waiter for polling predicates.
     */
    PageObjectWaiter waiter();
}
