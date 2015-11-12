package com.salesforceiq.augmenteddriver.asserts;

import com.salesforceiq.augmenteddriver.util.AugmentedFunctions;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

/**
 * Utility class to be called on the TestCases.
 */
public class AugmentedAssert {

    /**
     * Asserts that a By is present. If it is not present after timeoutInSeconds, it will throw an AssertionError exception
     */
    public static void assertElementIsPresentAfter(AugmentedFunctions driver, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(by);
        try {
            driver.findElementPresentAfter(by, timeoutInSeconds);
        } catch (TimeoutException e) {
            throw new AssertionError(String.format("Element %s is not present after %s seconds", by, timeoutInSeconds), e);
        }
    }

    /**
     * Asserts that a By is visible. If it is not visible after timeoutInSeconds, it will throw an AssertionError exception
     */
    public static void assertElementIsVisibleAfter(AugmentedFunctions driver, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(by);
        try {
            driver.findElementsVisibleAfter(by, timeoutInSeconds);
        } catch (TimeoutException e) {
            throw new AssertionError(String.format("Element %s is not visible safter %s seconds", by, timeoutInSeconds), e);
        }
    }

    /**
     * Asserts that a By is clickable. If it is not clickable after timeoutInSeconds, it will throw an AssertionError exception
     */
    public static void assertElementIsClickableAfter(AugmentedFunctions driver, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(by);
        try {
            driver.findElementClickableAfter(by, timeoutInSeconds);
        } catch (TimeoutException e) {
            throw new AssertionError(String.format("Element %s is not clickable safter %s seconds", by, timeoutInSeconds), e);
        }
    }

    /**
     * Asserts that a By contains a specific text. If there is not by that contains text after timeoutInSeconds, it will throw an AssertionError exception
     */
    public static void assertElementContainsAfter(AugmentedFunctions driver, By by, String text, int timeoutInSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));
        try {
            driver.findElementContainAfter(by, text, timeoutInSeconds);
        } catch (TimeoutException e) {
            throw new AssertionError(String.format("Element %s is not clickable safter %s seconds", by, timeoutInSeconds), e);
        }
    }
}
