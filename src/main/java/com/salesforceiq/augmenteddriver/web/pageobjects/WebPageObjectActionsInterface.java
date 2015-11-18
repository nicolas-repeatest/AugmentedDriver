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
}
