package com.salesforceiq.augmenteddriver.asserts;

import com.google.common.base.Predicate;
import com.salesforceiq.augmenteddriver.util.PageObject;
import org.openqa.selenium.By;

/**
 * Defines some assertions handy on the different tests.
 */
public interface AugmentedAssertInterface {

    /**
     * Asserts that an element is present. Fail if the element is not present during the timeout.
     *
     * @param by the element that has to be present.
     * @param timeoutInSeconds how much time to wait until it shows up.
     */
    void assertElementIsPresentAfter(By by, int timeoutInSeconds);

    /**
     * Asserts that an element is present. Fail if the element is not present during the default timeout.
     *
     * @param by the element that has to be present.
     */
    void assertElementIsPresent(By by);

    /**
     * Asserts that an element is visible. Fail if the element is not visible during the timeout.
     *
     * @param by the element that has to be visible.
     * @param timeoutInSeconds how much time to wait until it shows up.
     */
    void assertElementIsVisibleAfter(By by, int timeoutInSeconds);

    /**
     * Asserts that an element is vibible. Fail if the element is not visible during the default timeout.
     *
     * @param by the element that has to be visible.
     */
    void assertElementIsVisible(By by);

    /**
     * Asserts that an element is clickable. Fail if the element is not clickable during the timeout.
     *
     * @param by the element that has to be clickable.
     * @param timeoutInSeconds how much time to wait until it shows up.
     */
    void assertElementIsClickableAfter(By by, int timeoutInSeconds);

    /**
     * Asserts that an element is clickable. Fail if the element is not clickable during the default timeout.
     *
     * @param by the element that has to be clickable.
     */
    void assertElementIsClickable(By by);

    /**
     * Asserts that an element contains a text. Fail if the element does not contain the text during the timeout.
     *
     * @param by the element that has to be present.
     * @param text text that has to be contained in the element.
     * @param timeoutInSeconds how much time to wait until it shows up.
     */
    void assertElementContainsAfter(By by, String text, int timeoutInSeconds);

    /**
     * Asserts that an element contains a text. Fail if the element does not contain the text during the default timeout.
     *
     * @param by the element that has to be present.
     * @param text text that has to be contained in the element.
     */
    void assertElementContains(By by, String text);

    /**
     * Asserts that an element is not clickable. Fail if the element is clickable during the timeout.
     *
     * @param by the element that has to not be clickable.
     * @param timeoutInSeconds how much time to wait until it shows up.
     */
    void assertElementIsNotClickableAfter(By by, int timeoutInSeconds);

    /**
     * Asserts that an element is not clickable. Fail if the element is clickable during the default timeout.
     *
     * @param by the element that has to not be clickable.
     */
    void assertElementIsNotClickable(By by);

    /**
     * Asserts that an element is not visible. Fail if the element is visible during the timeout.
     *
     * @param by the element that has to not be visible.
     * @param timeoutInSeconds how much time to wait until it shows up.
     */
    void assertElementIsNotVisibleAfter(By by, int timeoutInSeconds);

    /**
     * Asserts that an element is not visibile. Fail if the element is visible during the default timeout.
     *
     * @param by the element that has to not be visible.
     */
    void assertElementIsNotVisible(By by);

    /**
     * Asserts that an element is not present . Fail if the element is present during the timeout.
     *
     * @param by the element that has to not be present.
     * @param timeoutInSeconds how much time to wait until it shows up.
     */
    void assertElementIsNotPresentAfter(By by, int timeoutInSeconds);

    /**
     * Asserts that an element is not present. Fail if the element is present during the default timeout.
     *
     * @param by the element that has to not be present.
     */
    void assertElementIsNotPresent(By by);

    /**
     * Asserts that a predicate is fulfilled in a PageObject.
     *
     * @param entity the entity where the predicate will be fulfilled.
     * @param assertThat which predicate to fulfill.
     * @param <T> the type of the page object.
     */
    <T extends PageObject> void assertThat(T entity, Predicate<T> assertThat);

    /**
     * Asserts that a predicate is fulfilled in a PageObject. If not fulfilled will throw an assertion error with
     * errorMessage.
     *
     * @param entity the entity where the predicate will be fulfilled.
     * @param assertThat which predicate to fulfill.
     * @param errorMessage message of the exception thrown if not fulfilled.
     * @param <T> the type of the page object.
     */
    <T extends PageObject> void assertThat(T entity, Predicate<T> assertThat, String errorMessage);

    /**
     * Asserts that a predicate is fulfilled in a PageObject. Wait until waitTimeInSeconds.
     *
     * @param entity the entity where the predicate will be fulfilled.
     * @param assertThat which predicate to fulfill.
     * @param waitTimeInSeconds How much time to wait until the predicate is fulfilled.
     * @param <T> the type of the page object.
     */
    <T extends PageObject> void assertThatAfter(T entity, Predicate<T> assertThat, int waitTimeInSeconds);

    /**
     * Asserts that a predicate is fulfilled in a PageObject. If not fulfilled will throw an assertion error with
     * errorMessage. Wait until waitTimeInSeconds.
     *
     * @param entity the entity where the predicate will be fulfilled.
     * @param assertThat which predicate to fulfill.
     * @param waitTimeInSeconds How much time to wait until the predicate is fulfilled.
     * @param errorMessage message of the exception thrown if not fulfilled.
     * @param <T> the type of the page object.
     */
    <T extends PageObject> void assertThatAfter(T entity, Predicate<T> assertThat, String errorMessage, int waitTimeInSeconds);
}
