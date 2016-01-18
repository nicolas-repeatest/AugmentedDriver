package com.salesforceiq.augmenteddriver.web;

import org.openqa.selenium.By;

/**
 * Augmented functionality only for Web.
 */
public interface AugmentedWebOnlyFunctions {

    /**
     * Scrolls down the page to the bottom.
     */
    void scrollToBottom();

    /**
     * Scrolls the page to an element.
     */
    void scrollToElement(By by);
}
