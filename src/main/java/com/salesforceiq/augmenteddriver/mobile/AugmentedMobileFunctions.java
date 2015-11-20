package com.salesforceiq.augmenteddriver.mobile;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Augmented functionality for Mobile.
 */
public interface AugmentedMobileFunctions<T> {

    /**
     * Taps on an element that is not on top (so it is not visible or displayed but it is present).
     *
     * @param by the element to be tapped.
     * @return the element to be tapped (but might not be visible or displayed, but present)
     */
    T tap(By by);

    /**
     * Taps on an element that is not on top (so it is not visible or displayed but it is present).
     * Waits up to waitTimeInSeconds for the element to be visible.
     */
    T tapAfter(By by, int waitTimeInSeconds);


    /**
     * Taps on the offset of an element.
     *
     * <p>
     *     I.e if we select (by, 5, 5) it will tap 5 pixels to the right and 5 pixels to the bottom
     * </p>
     */
    T tap(By by, int offsetX, int offsetY);

    /**
     * Taps on the offset of an element.
     *
     * <p>
     *     I.e if we select (by, 5, 5) it will tap 5 pixels to the right and 5 pixels to the bottom
     * </p>
     */
    T tapAfter(By by, int offsetX, int offsetY, int waitTimeInSeconds);

    /**
     * Taps on an element for the given milli seconds
     */
    void tap(WebElement element, int pressInMilliSeconds);

    /**
     * Clicks on an element (waiting default time to show up), types and then hides the keyboard.
     */
    void clickAndSendKeys(By by, String keys);

    /**
     * Clicks on an element (waiting waitInSeconds time), types and then hides the keyboard.
     */
    void clickAndSendKeysAfter(By by, String keys, int waitInSeconds);

    /**
     * Swipes up on an element until the other is visible.
     */
    T swipeUpWaitElementVisible(By swipeUpElement, By elementPresent);

    /**
     * Swipes down on an element until the other is visible.
     */
    T swipeDownWaitElementVisible(By swipeUpElement, By elementPresent);

    /**
     * Swipes vertical on an element (negative offset up, positive offset down) until the
     * other element is visible.
     *
     * It will swipe quantity times, with as specific offset (pixels up or down) with a duration
     */
    T swipeVerticalWaitVisible(By swipeElement,
                       By elementVisible,
                       int offset,
                       int quantity,
                       int duration);

    /**
     * Swipes up on an element (negative offset up, positive offset down).
     */
    void swipeUp(By swipeBy);

    /**
     * Swipes down on an element (negative offset up, positive offset down).
     */
    void swipeDown(By swipeBy);

    /**
     * Swipes vertical on an element (negative offset up, positive offset down).
     */
    void swipeVertical(By swipeBy, int offset, int duration);
}
