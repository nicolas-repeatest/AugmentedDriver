package com.salesforceiq.augmenteddriver.mobile.android.pageobjects;

import com.google.common.base.Predicate;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidDriver;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidElement;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidFunctions;
import com.salesforceiq.augmenteddriver.util.PageObjectWaiter;
import com.salesforceiq.augmenteddriver.web.AugmentedWebElement;
import com.salesforceiq.augmenteddriver.web.pageobjects.WebPageContainerObject;
import com.salesforceiq.augmenteddriver.web.pageobjects.WebPageObject;

/**
 * Common functionality to all AndroidPageObjects
 */
public interface AndroidPageObjectActionsInterface {

    /**
     * Initializes a AndroidPageObject instance.
     *
     * @param clazz the AndroidPageObject class to initialize.
     * @param <T> The type of the AndroidPageObject to return.
     * @return the PageObject represented by the input
     */
    <T extends AndroidPageObject> T get(Class<T> clazz);

    /**
     * Initializes a AndroidPageObject instance.
     *
     * @param clazz the AndroidPageObject class to initialize.
     * @param waitUntil Will also wait until that predicate is true.
     * @param <T> The type of the AndroidPageObject to return.
     * @return the PageObject represented by the input
     */
    <T extends AndroidPageObject> T get(Class<T> clazz, Predicate<T> waitUntil);

    /**
     * Initializes a AndroidPageContainerObject instance.
     *
     * @param clazz the AndroidPageContainerObject class to initialize.
     * @param container the container element that is used as a root.
     * @param <T> The type of the AndroidPageContainerObject to return.
     * @return the PageContainerObject represented by the input
     */
    <T extends AndroidPageContainerObject> T get(Class<T> clazz, AugmentedAndroidElement container);

    /**
     * Initializes a AndroidPageContainerObject instance.
     *
     * @param clazz the AndroidPageContainerObject class to initialize.
     * @param container the container element that is used as a root.
     * @param waitUntil Will also wait until that predicate is true.
     * @param <T> The type of the AndroidPageContainerObject to return.
     * @return the PageContainerObject represented by the input
     */
    <T extends AndroidPageContainerObject> T get(Class<T> clazz, AugmentedAndroidElement container, Predicate<T> waitUntil);


    /**
     * @return the AugmentedAndroidDriver to use.
     */
    AugmentedAndroidDriver driver();

    /**
     * @return the Augmented Android Functions to use.
     */
    AugmentedAndroidFunctions augmented();

    /**
     * @return the waiter for polling predicates.
     */
    PageObjectWaiter waiter();
}
