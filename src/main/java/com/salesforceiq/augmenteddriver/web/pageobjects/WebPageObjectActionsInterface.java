package com.salesforceiq.augmenteddriver.web.pageobjects;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.salesforceiq.augmenteddriver.util.PageObjectWaiter;
import com.salesforceiq.augmenteddriver.web.AugmentedWebDriver;
import com.salesforceiq.augmenteddriver.web.AugmentedWebElement;
import com.salesforceiq.augmenteddriver.web.AugmentedWebFunctions;

/**
 * Common functionality to all WebPageObjects
 */
public interface WebPageObjectActionsInterface {

    /**
     * Initializes a WebPageObject instance.
     *
     * @param clazz the WebPageObject class to initialize.
     * @param <T> The type of the WebPageObject to return.
     * @return the PageObject represented by the input
     */
    <T extends WebPageObject> T get(Class<T> clazz);

    /**
     * Initializes a WebPageObject instance.
     *
     * @param clazz the WebPageObject class to initialize.
     * @param waitUntil Will also wait until that predicate is true.
     * @param <T> The type of the WebPageObject to return.
     * @return the PageObject represented by the input
     */
    <T extends WebPageObject> T get(Class<T> clazz, Predicate<T> waitUntil);

    /**
     * Initializes a WebPageContainerObject instance.
     *
     * @param clazz the WebPageObject class to initialize.
     * @param container the container element that is used as a root.
     * @param <T> The type of the WebPageContainerObject to return.
     * @return the PageContainerObject represented by the input
     */
    <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container);

    /**
     * Initializes a WebPageContainerObject instance.
     *
     * @param clazz the WebPageObject class to initialize.
     * @param container the container element that is used as a root.
     * @param waitUntil Will also wait until that predicate is true.
     * @param <T> The type of the WebPageContainerObject to return.
     * @return the PageContainerObject represented by the input
     */
    <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container, Predicate<T> waitUntil);

    /**
     * @return the WebDriver to use.
     */
    AugmentedWebDriver driver();

    /**
     * @return the Augmented Web Functions to use.
     */
    AugmentedWebFunctions augmented();

    /**
     * @return the waiter for polling predicates.
     */
    PageObjectWaiter waiter();
}
