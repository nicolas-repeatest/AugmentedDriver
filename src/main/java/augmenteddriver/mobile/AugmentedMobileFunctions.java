package augmenteddriver.mobile;

import org.openqa.selenium.By;

/**
 * Augmented functionality for Mobile.
 */
public interface AugmentedMobileFunctions<T> {

    /**
     * Clicks on an element (waiting default time to show up), types and then hides the keyboard.
     */
    void clickAnSendKeys(By by, String keys);

    /**
     * Clicks on an element (waiting waitInSeconds time), types and then hides the keyboard.
     */
    void clickAnSendKeysAfter(By by, String keys, int waitInSeconds);

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
