package com.salesforceiq.augmenteddriver.asserts;

import com.salesforceiq.augmenteddriver.util.AugmentedFunctions;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * Utility class to be called on the TestCases.
 */
public class AugmentedAssert {

    /**
     * Asserts that a By is present. If it is not present after timeoutInSeconds, it will throw an AssertionError exception
     *
     * @param driver the Driver to use.
     * @param by the element that has to be present.
     * @param timeoutInSeconds how much time to wait.
     */
    @Step("Asserting element ${1} is present after ${2} seconds")
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
     *
     * @param driver the Driver to use.
     * @param by the element that has to be visible.
     * @param timeoutInSeconds how much time to wait.
     */
    @Step("Asserting element ${1} is visible after ${2} seconds")
    public static void assertElementIsVisibleAfter(AugmentedFunctions driver, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(by);

        try {
            driver.findElementsVisibleAfter(by, timeoutInSeconds);
        } catch (TimeoutException e) {
            throw new AssertionError(String.format("Element %s is not visible after %s seconds", by, timeoutInSeconds), e);
        }
    }

    /**
     * Asserts that a By is clickable. If it is not clickable after timeoutInSeconds, it will throw an AssertionError exception
     *
     * @param driver the Driver to use.
     * @param by the element that has to be clickable.
     * @param timeoutInSeconds how much time to wait.
     */
    @Step("Asserting element ${1} is clickable after ${2} seconds")
    public static void assertElementIsClickableAfter(AugmentedFunctions driver, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(by);

        try {
            driver.findElementClickableAfter(by, timeoutInSeconds);
        } catch (TimeoutException e) {
            throw new AssertionError(String.format("Element %s is not clickable after %s seconds", by, timeoutInSeconds), e);
        }
    }

    /**
     * Asserts that a By contains a specific text. If there is not by that contains text after timeoutInSeconds, it will throw an AssertionError exception
     *
     * @param driver the Driver to use.
     * @param by the element that has to be present.
     * @param text the text that the element has to contain.
     * @param timeoutInSeconds how much time to wait.
     */
    @Step("Asserting element ${1} containes text ${2} after ${3} seconds")
    public static void assertElementContainsAfter(AugmentedFunctions driver, By by, String text, int timeoutInSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));

        try {
            driver.findElementContainAfter(by, text, timeoutInSeconds);
        } catch (TimeoutException e) {
            throw new AssertionError(String.format("Element %s does not contain %s after %s seconds", by, text, timeoutInSeconds), e);
        }
    }

    /**
     * Asserts that an element is not visible.
     *
     * @param driver the Driver to use.
     * @param by the element that has not to be visible.
     * @param timeoutInSeconds how much time to wait.
     */
    @Step("Asserting element ${1} is not visible after ${2} seconds")
    public static void assertElementIsNotVisibleAfter(AugmentedFunctions driver, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(by);

        try {
            driver.findElementsVisibleAfter(by, timeoutInSeconds);
            org.junit.Assert.fail(String.format("Element %s is visible", by));
        } catch (TimeoutException e) {
            // Element was not visible.
        }
    }

    /**
     * Asserts that an element is not clickable.
     *
     * @param driver the Driver to use.
     * @param by the element that has not to be clickable.
     * @param timeoutInSeconds how much time to wait.
     */
    @Step("Asserting element ${1} is not clickable after ${2} seconds")
    public static void assertElementIsNotClickableAfter(AugmentedFunctions driver, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(by);

        try {
            driver.findElementClickableAfter(by, timeoutInSeconds);
            org.junit.Assert.fail(String.format("Element %s is clickable", by));
        } catch (TimeoutException e) {
            // Element was not visible.
        }
    }

    /**
     * Asserts that an element is not present.
     *
     * @param driver the Driver to use.
     * @param by the element that has not to be present.
     * @param timeoutInSeconds how much time to wait.
     */
    @Step("Asserting element ${1} is not present after ${2} seconds")
    public static void assertElementIsNotPresentAfter(AugmentedFunctions driver, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(by);

        try {
            driver.findElementPresentAfter(by, timeoutInSeconds);
            org.junit.Assert.fail(String.format("Element %s is present", by));
        } catch (TimeoutException e) {
            // Element was not visible.
        }
    }
}
