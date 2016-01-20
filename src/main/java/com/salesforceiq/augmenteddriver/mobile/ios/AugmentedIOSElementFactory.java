package com.salesforceiq.augmenteddriver.mobile.ios;

import org.openqa.selenium.WebElement;

/**
 * Guice Factory.
 */
public interface AugmentedIOSElementFactory {

    /**
     * Creates an AugmentedIOSElement based on a generic WebElement.
     *
     * @param webElement the Generic WebElement.
     * @return the AugmentedIOSElement that wraps the WebElement.
     */
    AugmentedIOSElement create(WebElement webElement);
}
