package com.salesforceiq.augmenteddriver.mobile.android;

import org.openqa.selenium.WebElement;

/**
 * Guice Factory.
 */
public interface AugmentedAndroidElementFactory {

    /**
     * Creates an AugmentedAndroidElement based on a generic WebElement.
     *
     * @param webElement the Generic WebElement.
     * @return the AugmentedAndroidElement that wraps the WebElement.
     */
    AugmentedAndroidElement create(WebElement webElement);
}
