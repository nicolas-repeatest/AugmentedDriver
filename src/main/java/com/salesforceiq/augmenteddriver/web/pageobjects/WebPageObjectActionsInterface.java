package com.salesforceiq.augmenteddriver.web.pageobjects;

import com.salesforceiq.augmenteddriver.web.AugmentedWebDriver;
import com.salesforceiq.augmenteddriver.web.AugmentedWebElement;
import com.salesforceiq.augmenteddriver.web.AugmentedWebFunctions;

/**
 * Common functionality to all WebPageObjects
 */
public interface WebPageObjectActionsInterface {
    /**
     * returns the PageObject represented by the input
     */
    <T extends WebPageObject> T get(Class<T> clazz);

    /**
     * returns the PageContainerObject represented by the input
     */
    <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container);

    /**
     * @return the WebDriver to use.
     */
    AugmentedWebDriver driver();

    /**
     * @return the Augmented Web Functions to use.
     */
    AugmentedWebFunctions augmented();

    /**
     * Executes an action and returns the landing Page Object
     */
    <T extends WebPageObject> T action(Action action, Class<T> landingPageObject);

    /**
     * Executes an action in a container and returns the landing Page Object
     */
    <T extends WebPageContainerObject> T action(Action action, AugmentedWebElement container, Class<T> landingPageObject);
}
