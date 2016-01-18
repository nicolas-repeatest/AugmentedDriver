package com.salesforceiq.augmenteddriver.web;

import org.openqa.selenium.WebElement;

/**
 * Factory used by Gucie to create AugmentedWebElements.
 */
public interface AugmentedWebElementFactory {

    /**
     * Creates an AugmentedWebElement based on a WebElement.
     *
     * @param webElement The WebElement to use.
     * @return the AugmentedWebElement.
     */
    AugmentedWebElement create(WebElement webElement);
}
