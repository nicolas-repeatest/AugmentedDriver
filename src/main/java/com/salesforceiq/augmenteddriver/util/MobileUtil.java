package com.salesforceiq.augmenteddriver.util;

import com.google.common.base.Preconditions;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

/**
 * Common utilities for IOS and Android.
 */
public class MobileUtil {

    private static final int VERTICAL_OFFSET = 10;
    private static final int BIG_NUMBER = 9999999;

    /**
     * Taps on a location determined by the coordinated of the element plus the offsets.
     *
     * @param driver The AppiumDriver used to tap.
     * @param augmentedFunctions all the augmented functions.
     * @param by the by that identifies the element
     * @param offsetX horizontal offset (can be negative) where the tap will occur from the element
     * @param offsetY vertical offset (can be negative) where the tap will occur from the element.
     * @param waitTimeInSeconds how much time to wait until the element becomes visible
     * @param pressInMilliSeconds how much time to press the element before swiping.
     * @param fingers how many fingers to press.
     * @return the element that was used to determine the coordinates.
     */
    public static WebElement tapAfter(AppiumDriver<?> driver, AugmentedFunctions<?> augmentedFunctions,
                                 By by, int offsetX, int offsetY, int waitTimeInSeconds, int pressInMilliSeconds, int fingers) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);
        Preconditions.checkNotNull(by);

        WebElement element = augmentedFunctions.findElementPresentAfter(by, waitTimeInSeconds);
        driver.tap(fingers, element.getLocation().getX() + offsetX, element.getLocation().getY() + offsetY, pressInMilliSeconds);
        return element;
    }

    /**
     * Swipes up on an element, until the other element becomes visible.
     *
     * @param driver The AppiumDriver used to swipe up.
     * @param augmentedFunctions all the augmented functions.
     * @param swipeElement form which element the swipe up should start
     * @param elementVisible which element should become visible
     * @param pressInMilliSeconds how much time to press the element before swiping.
     * @param waitTimeInSeconds how much time to wait until the element becomes visible
     * @param quantity how many times should you swipe before giving up.
     * @return the element visible.
     */
    public static WebElement swipeUpWaitVisibleAfter(AppiumDriver<?> driver,
                                                AugmentedFunctions<?> augmentedFunctions,
                                                By swipeElement,
                                                By elementVisible,
                                                int waitTimeInSeconds,
                                                int quantity,
                                                int pressInMilliSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);
        Preconditions.checkNotNull(swipeElement);
        Preconditions.checkNotNull(elementVisible);

        return swipeVerticalWaitVisibleAfter(driver, augmentedFunctions, swipeElement,
                elementVisible, waitTimeInSeconds, -BIG_NUMBER, quantity, pressInMilliSeconds);
    }


    /**
     * Swipes down on an element, until the other element becomes visible.
     *
     * @param driver The AppiumDriver used to swipe down.
     * @param augmentedFunctions all the augmented functions.
     * @param swipeElement form which element the swipe up should start
     * @param elementVisible which element should become visible
     * @param pressInMilliSeconds how much time to press the element before swiping.
     * @param quantity how many times should you swipe before giving up.
     * @param waitTimeInSeconds how much time to wait until the element becomes visible
     * @return the element visible.
     */
    public static WebElement swipeDownWaitVisible(AppiumDriver<?> driver,
                                                AugmentedFunctions<?> augmentedFunctions,
                                                By swipeElement,
                                                By elementVisible,
                                                int waitTimeInSeconds,
                                                int quantity,
                                                int pressInMilliSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);
        Preconditions.checkNotNull(swipeElement);
        Preconditions.checkNotNull(elementVisible);

        return swipeVerticalWaitVisibleAfter(driver, augmentedFunctions, swipeElement,
                elementVisible, waitTimeInSeconds, BIG_NUMBER, quantity, pressInMilliSeconds);
    }

    /**
     * Swipes up on an element, pressing a default amount of time before swiping.
     *
     * @param driver The AppiumDriver used to swipe Up.
     * @param augmentedFunctions all the augmented functions.
     * @param swipeBy from which element the swipe up should start.
     * @param waitTimeInSeconds how much time to wait until the element becomes visible
     * @param pressInMilliSeconds how much time to press the element before swiping.
     */
    public static void swipeUpAfter(AppiumDriver<?> driver,
                               AugmentedFunctions<?> augmentedFunctions,
                               By swipeBy,
                               int waitTimeInSeconds,
                               int pressInMilliSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);
        Preconditions.checkNotNull(swipeBy);

        swipeVerticalAfter(driver, augmentedFunctions, swipeBy, waitTimeInSeconds, -BIG_NUMBER, pressInMilliSeconds);
    }

    /**
     * Swipes down on an element, pressing a default amount of time before swiping.
     *
     * @param driver The AppiumDriver used to swipe Up.
     * @param augmentedFunctions all the augmented functions.
     * @param swipeBy from which element the swipe down should start.
     * @param waitTimeInSeconds how much time to wait until the element becomes visible
     * @param pressInMilliSeconds how much time to press the element before swiping.
     */
    public static void swipeDownAfter(AppiumDriver<?> driver,
                               AugmentedFunctions<?> augmentedFunctions,
                               By swipeBy,
                               int waitTimeInSeconds,
                               int pressInMilliSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);
        Preconditions.checkNotNull(swipeBy);

        swipeVerticalAfter(driver, augmentedFunctions, swipeBy, waitTimeInSeconds, BIG_NUMBER, pressInMilliSeconds);
    }

    /**
     * Swipes vertical (depends on the offset if negative or positive) on an element, pressing
     * for a specific duration before swiping.
     *
     * @param driver The AppiumDriver used to swipe Up.
     * @param augmentedFunctions all the augmented functions.
     * @param swipeBy from which element the swipe should start.
     * @param offset The offset where to end the swipe.
     * @param waitTimeInSeconds how much time to wait until the element becomes visible
     * @param durationInMilliSeconds time to press before swiping.
     */
    public static void swipeVerticalAfter(AppiumDriver<?> driver,
                                     AugmentedFunctions<?> augmentedFunctions,
                                     By swipeBy,
                                     int waitTimeInSeconds,
                                     int offset,
                                     int durationInMilliSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);
        Preconditions.checkNotNull(swipeBy);

        WebElement elementPresent = augmentedFunctions.findElementPresentAfter(swipeBy, waitTimeInSeconds);
        int x = elementPresent.getLocation().getX() + elementPresent.getSize().getWidth() / 2;
        int y = elementPresent.getLocation().getY() + elementPresent.getSize().getHeight() /  2;
        int swipe = getVerticalOffset(driver, y, offset);
        driver.swipe(x, y, x, swipe, durationInMilliSeconds);
    }

    /**
     * Swipes vertical on an element until another becomes visible. How much to swipe depends on the
     * offset, and how much time to press before swiping depends on durationInMilliSeconds.
     *
     * Finally quantity is the amount of times it will swipe before giving up and failing since the
     * element did not show up.
     *
     * @param driver The AppiumDriver used to swipe.
     * @param augmentedFunctions all the augmented functions.
     * @param swipeElement from which element the swipe should start.
     * @param elementVisible which element has to become visibile.
     * @param waitTimeInSeconds how much time to wait until the element becomes visible
     * @param offset The offset where to end the swipe.
     * @param durationInMilliSeconds time to press before swiping.
     * @param quantity how many time to try swiping.
     * @return the element that became visible.
     */
    public static WebElement swipeVerticalWaitVisibleAfter(AppiumDriver<?> driver,
                                    AugmentedFunctions<?> augmentedFunctions,
                                    By swipeElement,
                                    By elementVisible,
                                    int waitTimeInSeconds,
                                    int offset,
                                    int quantity,
                                    int durationInMilliSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);
        Preconditions.checkNotNull(swipeElement);
        Preconditions.checkNotNull(elementVisible);

        WebElement elementPresent = augmentedFunctions.findElementPresentAfter(swipeElement, waitTimeInSeconds);
        int x = elementPresent.getLocation().getX() + elementPresent.getSize().getWidth() / 2;
        int y = elementPresent.getLocation().getY() + elementPresent.getSize().getHeight() /  2;

        int swipe = getVerticalOffset(driver, y, offset);

        for(int iteration = 0; iteration < quantity; iteration++) {
            driver.swipe(x, y, x, swipe, durationInMilliSeconds);
            if (augmentedFunctions.isElementVisibleAfter(elementVisible, 3)) {
                return augmentedFunctions.findElementVisible(elementVisible);
            }
        }
        throw new AssertionError(String.format("Swiped %s with an offest of %s times but element %s not found",
                        quantity, offset, elementVisible));
    }

    /**
     * Swipes right from the left of the screen to the right of the screen on the vertical alignment of an
     * element.
     *
     * @param driver The AppiumDriver used to swipe.
     * @param augmentedFunctions all the augmented functions.
     * @param by the element to find the vertical coordinates
     * @param waitTimeInSeconds how much time to wait until the element becomes present.
     * @param pressInMilliSeconds Time to press the element before swiping.
     */
    public static void swipeFullRightAfter(AppiumDriver<?> driver, AugmentedFunctions<?> augmentedFunctions, By by,
                                           int waitTimeInSeconds, int pressInMilliSeconds) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);
        Preconditions.checkNotNull(by);

        WebElement element = augmentedFunctions.findElementPresentAfter(by, waitTimeInSeconds);
        Dimension size = driver.manage().window().getSize();
        int to = size.getWidth() * 85 / 100;
        int from = size.getWidth() * 15 / 100;
        int y = element.getLocation().getY() + element.getSize().getHeight() / 2;
        driver.swipe(from, y, to, y, pressInMilliSeconds);
    }

    /**
     * Swipes left from the right of the screen to the left of the screen on the vertical alignment of an
     * element.
     *
     * @param driver The AppiumDriver used to swipe.
     * @param augmentedFunctions all the augmented functions.
     * @param by the element to find the vertical coordinates
     * @param waitTimeInSeconds how much time to wait until the element becomes present.
     * @param pressInMilliSeconds Time to press the element before swiping.
     */
    public static void swipeFullLeftAfter(AppiumDriver<?> driver, AugmentedFunctions<?> augmentedFunctions, By by,
                                          int waitTimeInSeconds, int pressInMilliSeconds) {
        Preconditions.checkNotNull(by);
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);

        WebElement element = augmentedFunctions.findElementPresentAfter(by, waitTimeInSeconds);
        Dimension size = driver.manage().window().getSize();
        int from = size.getWidth() * 85 / 100;
        int to = size.getWidth() * 15 / 100;
        int y = element.getLocation().getY() + element.getSize().getHeight() / 2;
        driver.swipe(from, y, to, y, pressInMilliSeconds);
    }

    /**
     * Basically its caps how close you can get swiping to the vertical borders of the device
     *
     * @param driver The AppiumDriver used to swipe.
     * @param y the actual vertical position.
     * @param offset how much you want to swipe from the vertical position.
     * @return the end coordinate of the swiping action.
     */
    private static int getVerticalOffset(AppiumDriver<?> driver, int y, int offset) {
        Preconditions.checkNotNull(driver);

        if (y + offset < VERTICAL_OFFSET) {
            return VERTICAL_OFFSET;
        } else if (y + offset > driver.manage().window().getSize().getHeight() - VERTICAL_OFFSET) {
            return driver.manage().window().getSize().getHeight() - VERTICAL_OFFSET;
        } else {
            return y + offset;
        }
    }
}
