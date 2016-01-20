package com.salesforceiq.augmenteddriver.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Augmented functionality on top of the WebDriver.
 */
public interface AugmentedFunctions<T extends WebElement> {

    /**
     * Checks if an element is present. Will wait up to the default time to see if it shows up.
     *
     * @param by the By representing the WebElement.
     * @return true if the element is present, false otherwise.
     */
    boolean isElementPresent(By by);

    /**
     * Checks if an element present and waits up to waitSeconds.
     *
     * @param by the By representing the WebElement.
     * @param waitSeconds Time in seconds to wait for the WebElement
     * @return true if the element is present, false if it didn't show up after waitSeconds
     */
    boolean isElementPresentAfter(By by, int waitSeconds);

    /**
     * Checks if an element is present immediately (will wait up to 1 second).
     *
     * @param by the By representing the WebElement.
     * @return true if the element is present, false otherwise
     */
    boolean isElementPresentImmediate(By by);

    /**
     * Checks if an element is visible. Will wait up to the default time to see if it is visible.
     *
     * @param by the By representing the WebElement.
     * @return true if the element is visible. false otherwise.
     */
    boolean isElementVisible(By by);

    /**
     * Checks if an element is visible, up to waitSeconds.
     *
     * @param by the By representing the WebElement.
     * @param waitSeconds Time in seconds to wait for the element to be present and visible.
     * @return true if the element is visible, false if it didn't become visible after waitSeconds.
     */
    boolean isElementVisibleAfter(By by, int waitSeconds);

    /**
     * Checks if an element is visible immediately (waits up to 1 second).
     *
     * @param by the By representing the WebElement.
     * @return true if the element is visible, false otherwise.
     */
    boolean isElementVisibleImmediate(By by);

    /**
     * Checks if an element is clickable. Waits up to the default time to see if it is clickable.
     *
     * @param by the By representing the WebElement.
     * @return true if the element is clickable, false otherwise.
     */
    boolean isElementClickable(By by);

    /**
     * Checks if an element is clickable, waiting up to waitSeconds time.
     *
     * @param by the By representing the WebElement.
     * @param waitSeconds Time in seconds to wait until the element is clickable.
     * @return true if the element is clickable, false otherwise.
     */
    boolean isElementClickableAfter(By by, int waitSeconds);

    /**
     * Checks if an elemebt is clickable immediately (waits up to 1 second).
     *
     * @param by The by representing the WebElement.
     * @return true if the element is clickable. Will return almost immediately (1 second).
     */
    boolean isElementClickableImmediate(By by);

    /**
     * Waits up to the default time until the element is present and returns it.
     *
     * @param by The by representing the WebElement.
     * @return the element found.
     */
    T findElementPresent(By by);

    /**
     * Waits up to waitSeconds until the element is present and returns it
     *
     * @param by The by representing the WebElement.
     * @param waitSeconds Time in seconds to wait until the element is present.
     * @return the element found.
     */
    T findElementPresentAfter(By by, int waitSeconds);

    /**
     * Waits up to the default time until the element is visible.
     *
     * @param by The by representing the WebElement.
     * @return the element found.
     */
    T findElementVisible(By by);

    /**
     * Waits up to waitSeconds until the element is visible.
     *
     * @param by The by representing the WebElement.
     * @param waitSeconds Time in seconds to wait until the element is visible.
     * @return the element found.
     */
    T findElementVisibleAfter(By by, int waitSeconds);

    /**
     * Waits up to the default time until the element is clickable.
     *
     * @param by The by representing the WebElement.
     * @return the element found.
     */
    T findElementClickable(By by);

    /**
     * Waits up to waitSeconds until the element is clickable.
     *
     * @param by The by representing the WebElement.
     * @param waitSeconds Time in seconds to wait until the element is clickable.
     * @return the element found.
     */
    T findElementClickableAfter(By by, int waitSeconds);

    /**
     * Waits up to the default time until the element stops moving.
     *
     * @param by The by representing the WebElement.
     * @return the element found.
     */
    T findElementNotMoving(By by);

    /**
     * Waits up to waitSeconds until the element stops moving.
     *
     * @param by The by representing the WebElement.
     * @param waitSeconds Time in seconds to wait until the element stops moving.
     * @return the element found.
     */
    T findElementNotMovingAfter(By by, int waitSeconds);

    /**
     * Waits up to the default time until the element is contains a text.
     *
     * @param by The by representing the WebElement.
     * @param text The text that the element has to contain.
     * @return the element found.
     */
    T findElementContain(By by, String text);

    /**
     * Waits up to waitSeconds until the element contains a specific text.
     *
     * @param by The by representing the WebElement.
     * @param text The text that the element has to contain.
     * @param waitInSeconds Time in seconds to wait until the element is visible.
     * @return the element found.
     */
    T findElementContainAfter(By by, String text, int waitInSeconds);

    /**
     * Waits up to the default time until one element is visible. Then returns all the elements that are
     * visible and identified by the by.
     *
     * @param by The by representing the WebElements.
     * @return the elements found.
     */
    List<T> findElementsVisible(By by);

    /**
     * Waits up to waitInSeconds until one element is visible. Then returns all the elements that are
     * visible and identified by the by.
     *
     * @param by The by representing the WebElements.
     * @param waitInSeconds Time in seconds to wait until the first element becomes visible.
     * @return the elements found.
     */
    List<T> findElementsVisibleAfter(By by, int waitInSeconds);

    /**
     * Waits up to the default time until one element is present. Then returns all the elements that are
     * present and identified by the by.
     *
     * @param by The by representing the WebElements.
     * @return the elements found.
     */
    List<T> findElementsPresent(By by);

    /**
     * Waits up to waitInSeconds until one element is present. Then returns all the elements that are
     * present and identified by the by.
     *
     * @param by The by representing the WebElements.
     * @param waitInSeconds Time in seconds to wait until the first element becomes present.
     * @return the elements found.
     */
    List<T> findElementsPresentAfter(By by, int waitInSeconds);

    /**
     * Waits up to the default time until one element is clickable. Then returns all the elements that are
     * clickable and identified by the by.
     *
     * @param by The by representing the WebElements.
     * @return the elements found.
     */
    List<T> findElementsClickable(By by);

    /**
     * Waits up to waitInSeconds until one element is clickable. Then returns all the elements that are
     * clickable and identified by the by.
     *
     * @param by The by representing the WebElements.
     * @param waitInSeconds Time in seconds to wait until the first element becomes clickable.
     * @return the elements found.
     */
    List<T> findElementsClickableAfter(By by, int waitInSeconds);

    /**
     * Waits up to the default time until no element identified by by is present
     *
     * @param by The by representing the WebElement.
     */
    void waitElementToNotBePresent(By by);

    /**
     * Waits up to waitInSeconds time until no element identified by by is present
     *
     * @param by The by representing the WebElement.
     * @param waitInSeconds Time in seconds to wait until no element is not present.
     */
    void waitElementToNotBePresentAfter(By by, int waitInSeconds);

    /**
     * Waits up to the default time until no element identified by by is visible
     *
     * @param by The by representing the WebElement.
     */
    void waitElementToNotBeVisible(By by);

    /**
     * Waits up to waitInSeconds time until no element identified by by is visible
     *
     * @param by The by representing the WebElement.
     * @param waitInSeconds Time in seconds to wait until no element is not visible.
     */
    void waitElementToNotBeVisibleAfter(By by, int waitInSeconds);


    /**
     * Waits up to the default time until click is clickable, then clicks it and finally waits up to
     * the default time until the element wait is present.
     *
     * @param click the element that has to be clicked.
     * @param wait the element that has to show up after.
     * @return the element that is present.
     */
    T clickAndPresent(By click, By wait);

    /**
     * Waits up to waitInSeconds until click is clickable, then clicks it and finally waits up to
     * waitInSeconds until the element wait is present.
     *
     * @param click the element that has to be clicked.
     * @param wait the element that has to show up after.
     * @param waitInSeconds Time in seconds to wait until the first element can be clicked and the second is present.s.
     * @return the element that is present.
     */
    T clickAndPresentAfter(By click, By wait, int waitInSeconds);

    /**
     * Moves to one element and then clicks on the other.
     *
     * @param moveTo the element to move to.
     * @param click the element to click.
     */
    void  moveToAndClick(By moveTo, By click);

    /**
     * Moves to one element and then clicks on the other. Waits up to waitInSeconds until the first is present
     * and the second is clickable.
     *
     * @param moveTo the element to move to.
     * @param click the element to click.
     * @param waitInSeconds How much time in seconds to wait until the element is present.
     */
    void  moveToAndClickAfter(By moveTo, By click, int waitInSeconds);

    /**
     * Moves to an element. Waits up to the default time until the element is present.
     *
     * @param moveTo the element to move to.
     * @return the same element,
     */
    T moveTo(By moveTo);

    /**
     * Moves to an element. Waits up to waitInSeconds until the element is present.
     *
     * @param moveTo the element to move to.
     * @param waitInSeconds How much time in seconds to wait until the element is present.
     * @return the same element,
     */
    T moveToAfter(By moveTo, int waitInSeconds);

    /**
     * Clears an element and then types.
     *
     * @param by the element to be cleared and typed.
     * @param text Whats is going to be typed.
     */
    void clearAndSendKeys(By by, String text);

    /**
     * Clears an element and then types. Waits up to waitInSeconds for the element to be clickable.
     *
     * @param by the element to be cleared and typed.
     * @param waitInSeconds How much time in seconds to wait until the element is clickable..
     * @param text Whats is going to be typed.
     */
    void clearAndSendKeysAfter(By by, String text, int waitInSeconds);
}
