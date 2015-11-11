package augmenteddriver.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Augmented functionality on top of the WebDriver.
 */
public interface AugmentedFunctions<T extends WebElement> {

    /**
     * @return true if the element is present. Will wait up to the default time to see if it shows up.
     */
    boolean isElementPresent(By by);

    /**
     * @return true if the element is present. Will wait up to waitSeconds to see if it shows up
     */
    boolean isElementPresentAfter(By by, int waitSeconds);

    /**
     * @return true if the element is present. Will return almost immediately (1 second).
     */
    boolean isElementPresentImmediate(By by);

    /**
     * @return true if the element is visible. Will wait up to the default time to see if it is visible.
     */
    boolean isElementVisible(By by);

    /**
     * @return true if the element is visible. Will wait up to waitSeconds to see if it is visible
     */
    boolean isElementVisibleAfter(By by, int waitSeconds);

    /**
     * @return true if the element is visible. Will return almost immediately (1 second).
     */
    boolean isElementVisibleImmediate(By by);

    /**
     * @return true if the element is clickable. Will wait up to the default time to see if it is clickable.
     */
    boolean isElementClickable(By by);

    /**
     * @return true if the element is clickable. Will wait up to waitSeconds to see if it is clickable
     */
    boolean isElementClickableAfter(By by, int waitSeconds);

    /**
     * @return true if the element is clickable. Will return almost immediately (1 second).
     */
    boolean isElementClickableImmediate(By by);

    /**
     * Waits up to the default time until the element is present.
     */
    T findElementPresent(By by);

    /**
     * Waits up to waitSeconds until the element is present.
     */
    T findElementPresentAfter(By by, int waitSeconds);

    /**
     * Waits up to the default time until the element is visible.
     */
    T findElementVisible(By by);

    /**
     * Waits up to waitSeconds until the element is visible.
     */
    T findElementVisibleAfter(By by, int waitSeconds);

    /**
     * Waits up to the default time until the element is clickable.
     */
    T findElementClickable(By by);

    /**
     * Waits up to waitSeconds until the element is clickable.
     */
    T findElementClickableAfter(By by, int waitSeconds);

    /**
     * Waits up to the default time until the element stops moving.
     */
    T findElementNotMoving(By by);

    /**
     * Waits up to waitSeconds until the element stops moving.
     */
    T findElementNotMovingAfter(By by, int waitSeconds);

    /**
     * Waits up to the default time until the element is contains a text.
     */
    T findElementContain(By by, String text);

    /**
     * Waits up to waitSeconds until the element contains a specific text.
     */
    T findElementContainAfter(By by, String text, int waitInSeconds);

    /**
     * Waits up to the default time until one element is visible. Then returns all the elements that are
     * visible and identified by the by.
     */
    List<T> findElementsVisible(By by);

    /**
     * Waits up to waitInSeconds until one element is visible. Then returns all the elements that are
     * visible and identified by the by.
     */
    List<T> findElementsVisibleAfter(By by, int waitInSeconds);

    /**
     * Waits up to the default time until one element is present. Then returns all the elements that are
     * present and identified by the by.
     */
    List<T> findElementsPresent(By by);

    /**
     * Waits up to waitInSeconds until one element is present. Then returns all the elements that are
     * present and identified by the by.
     */
    List<T> findElementsPresentAfter(By by, int waitInSeconds);

    /**
     * Waits up to the default time until one element is clickable. Then returns all the elements that are
     * clickable and identified by the by.
     */
    List<T> findElementsClickable(By by);

    /**
     * Waits up to waitInSeconds until one element is clickable. Then returns all the elements that are
     * clickable and identified by the by.
     */
    List<T> findElementsClickableAfter(By by, int waitInSeconds);

    /**
     * Waits up to the default time until no element identified by by is present
     */
    void waitElementToNotBePresent(By by);

    /**
     * Waits up to waitInSeconds time until no element identified by by is present
     */
    void waitElementToNotBePresentAfter(By by, int waitInSeconds);

    /**
     * Waits up to the default time until click is clickable, then clicks it and finally waits up to
     * the default time until the element wait is present.
     */
    T clickAndPresent(By click, By wait);

    /**
     * Waits up to waitInSeconds until click is clickable, then clicks it and finally waits up to
     * waitInSeconds until the element wait is present.
     */
    T clickAndPresentAfter(By click, By wait, int waitInSeconds);
}
