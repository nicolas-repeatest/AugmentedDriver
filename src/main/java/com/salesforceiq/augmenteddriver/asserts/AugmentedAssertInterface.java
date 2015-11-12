package com.salesforceiq.augmenteddriver.asserts;

import org.openqa.selenium.By;

/**
 * Defines some assertions handy on the different tests.
 */
public interface AugmentedAssertInterface {

    /**
     * Asserts that an element is present. Fail if the element is not present during the timeout.
     */
    void assertElementIsPresentAfter(By by, int timeoutInSeconds);

    /**
     * Asserts that an element is present. Fail if the element is not present during the default timeout.
     */
    void assertElementIsPresent(By by);

    /**
     * Asserts that an element is visible. Fail if the element is not visible during the timeout.
     */
    void assertElementIsVisibleAfter(By by, int timeoutInSeconds);

    /**
     * Asserts that an element is vibible. Fail if the element is not visible during the default timeout.
     */
    void assertElementIsVisible(By by);

    /**
     * Asserts that an element is clickable. Fail if the element is not clickable during the timeout.
     */
    void assertElementIsClickableAfter(By by, int timeoutInSeconds);

    /**
     * Asserts that an element is clickable. Fail if the element is not clickable during the default timeout.
     */
    void assertElementIsClickable(By by);

    /**
     * Asserts that an element contains a text. Fail if the element does not contain the text during the timeout.
     */
    void assertElementContainsAfter(By by, String text, int timeoutInSeconds);

    /**
     * Asserts that an element contains a text. Fail if the element does not contain the text during the default timeout.
     */
    void assertElementContains(By by, String text);
}
