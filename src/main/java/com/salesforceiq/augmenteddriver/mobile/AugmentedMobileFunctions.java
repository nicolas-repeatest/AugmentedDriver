package com.salesforceiq.augmenteddriver.mobile;

import org.openqa.selenium.By;

/**
 * Augmented functionality for Mobile (Both IOS and Android).
 */
public interface AugmentedMobileFunctions<T> {

    /**
     * Taps on an element that is not on top (so it is not visible or displayed but it is present).
     *
     * @param by the element to be tapped.
     * @return the element that was tapped. (but might not be visible or displayed, but present)
     */
    T tap(By by);

    /**
     * Taps on an element that is not on top (so it is not visible or displayed but it is present).
     * Waits up to waitTimeInSeconds for the element to be present.
     *
     * @param by the element to be tapped.
     * @param waitTimeInSeconds time to wait for the element to show up.
     * @return the element that was tapped. (but might not be visible or displayed, but present)
     */
    T tapAfter(By by, int waitTimeInSeconds);

    /**
     * Taps on the offset of an element.
     *
     * <p>
     *     I.e if we select (by, 5, 5) it will tap 5 pixels to the right and 5 pixels to the bottom
     * </p>
     *
     * @param by the element to be tapped.
     * @param offsetX horizontal offset.
     * @param offsetY vertical offset.
     * @return the element that was tapped. (but might not be visible or displayed, but present)
     */
    T tapOffset(By by, int offsetX, int offsetY);

    /**
     * Taps on the offset of an element. Waits up to waitTimeInSeconds for the element to be present.
     *
     * <p>
     *     I.e if we select (by, 5, 5) it will tap 5 pixels to the right and 5 pixels to the bottom
     * </p>

     * @param by the element to be tapped.
     * @param offsetX horizontal offset.
     * @param offsetY vertical offset.
     * @param waitTimeInSeconds time to wait for the element to show up.
     * @return the element that was tapped. (but might not be visible or displayed, but present)
     */
    T tapOffsetAfter(By by, int offsetX, int offsetY, int waitTimeInSeconds);

    /**
     * Taps on an element for the given milli seconds
     *
     * @param by the element to be tapped.
     * @param pressInMilliSeconds for how long the element is going to be tapped.
     * @return the element that was tapped. (but might not be visible or displayed, but present)
     */
    T tapLong(By by, int pressInMilliSeconds);

    /**
     * Taps on an element for the given milli seconds
     *
     * @param by the element to be tapped.
     * @param pressInMilliSeconds for how long the element is going to be tapped.
     * @param waitTimeInSeconds time to wait for the element to show up.
     * @return the element that was tapped. (but might not be visible or displayed, but present)
     */
    T tapLongAfter(By by, int pressInMilliSeconds, int waitTimeInSeconds);

    /**
     * Taps on an element with an amount of fingers.
     *
     * @param by the element to be tapped.
     * @param fingers the amount of fingers.
     * @return the element that was tapped. (but might not be visible or displayed, but present)
     */
    T tapFingers(By by, int fingers);

    /**
     * Taps on an element with an amount of fingers.
     *
     * @param by the element to be tapped.
     * @param fingers the amount of fingers.
     * @param waitTimeInSeconds time to wait for the element to show up.
     * @return the element that was tapped. (but might not be visible or displayed, but present)
     */
    T tapFingersAfter(By by, int fingers, int waitTimeInSeconds);

    /**
     * Taps on an offset from an element, for a desired amount of time and with an amount of fingers.
     *
     * @param by the element to be tapped.
     * @param fingers the amount of fingers.
     * @param waitTimeInSeconds time to wait for the element to show up.
     * @param offsetX horizontal offset.
     * @param offsetY vertical offset.
     * @param pressInMilliSeconds for how long the element is going to be tapped.
     * @return the element that was tapped. (but might not be visible or displayed, but present)
     */
    T tapCustomAfter(By by, int offsetX, int offsetY, int pressInMilliSeconds, int fingers, int waitTimeInSeconds);

    /**
     * Taps on an offset from an element, for a desired amount of time and with an amount of fingers.
     *
     * @param by the element to be tapped.
     * @param fingers the amount of fingers.
     * @param offsetX horizontal offset.
     * @param offsetY vertical offset.
     * @param pressInMilliSeconds for how long the element is going to be tapped.
     * @return the element that was tapped. (but might not be visible or displayed, but present)
     */
    T tapCustom(By by, int offsetX, int offsetY, int pressInMilliSeconds, int fingers);

    /**
     * Clicks on an element (waiting default time to show up), then types.
     *
     * @param by element to click and type.
     * @param keys what to type.
     */
    void clickAndSendKeys(By by, String keys);

    /**
     * Clicks on an element (waiting waitInSeconds time), then types.
     *
     * @param by element to click and type.
     * @param keys what to type.
     * @param waitInSeconds how much time to wait until the element is clickable.
     */
    void clickAndSendKeysAfter(By by, String keys, int waitInSeconds);

    /**
     * Swipes up on an element until the other is visible.
     *
     * @param elementPresent what element should show up.
     * @param swipeUpElement what element to swipe up
     * @return the element that showed up.
     */
    T swipeUpWaitElementVisible(By swipeUpElement, By elementPresent);

    /**
     * Swipes down on an element until the other is visible.
     *
     * @param elementPresent what element should show up.
     * @param swipeDownElement what element to swipe down.
     * @return the element that showed up.
     */
    T swipeDownWaitElementVisible(By swipeDownElement, By elementPresent);

    /**
     * Swipes vertical on an element (negative offset up, positive offset down) until the
     * other element is visible.
     *
     * It will swipe quantity times, with as specific offset (pixels up or down) with a duration of
     * how long to press the element before swiping.
     *
     * @param durationInMilliSeconds how much time to press the element before swiping.
     * @param quantity how many times the swipe will happen before giving up.
     * @param elementVisible the element that should show up.
     * @param swipeElement whiche element to swipe.
     * @param offset up to which point the swipe will happen.
     * @return the element that showed up.
     */
    T swipeVerticalWaitVisible(By swipeElement,
                       By elementVisible,
                       int offset,
                       int quantity,
                       int durationInMilliSeconds);

    /**
     * Swipes up on an element (negative offset up, positive offset down).
     *
     * @param swipeBy the element to swipe up
     */
    void swipeUp(By swipeBy);

    /**
     * Swipes down on an element (negative offset up, positive offset down).
     *
     * @param swipeBy the element to swipe down
     */
    void swipeDown(By swipeBy);

    /**
     * Swipes vertical on an element (negative offset up, positive offset down).
     *
     * @param swipeBy the element to swipe
     * @param offset the destination of the swipe (in pixels)
     * @param durationInMilliSeconds how much time the element will be pressed before the swipe.
     */
    void swipeVertical(By swipeBy, int offset, int durationInMilliSeconds);

    /**
     * Swipes right from the left of the screen to the right of the screen on the vertical alignment of an
     * element.
     *
     * @param by the element to find the vertical coordinates
     */
    void swipeFullRight(By by);

    /**
     * Swipes right from the left of the screen to the right of the screen on the vertical alignment of an
     * element.
     *
     * @param by the element to find the vertical coordinates
     * @param waitInSeconds how much time to wait until the element becomes present.
     */
    void swipeFullRightAfter(By by, int waitInSeconds);

    /**
     * Swipes left from the right of the screen to the left of the screen on the vertical alignment of an
     * element.
     *
     * @param by the element to find the vertical coordinates
     */
    void swipeFullLeft(By by);

    /**
     * Swipes left from the right of the screen to the left of the screen on the vertical alignment of an
     * element.
     *
     * @param by the element to find the vertical coordinates
     * @param waitInSeconds how much time to wait until the element becomes present.
     */
    void swipeFullLeftAfter(By by, int waitInSeconds);
}
