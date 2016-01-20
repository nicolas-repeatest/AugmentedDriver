package com.salesforceiq.augmenteddriver.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utilities around WebDriver.
 */
public class WebDriverUtil {

    /**
     * Convenient method since the text of inpus and textareas are in the value attribute,
     *
     * @param element the WebElement to get the text.
     * @return the text.
     */
    public static String getText(WebElement element) {
        Preconditions.checkNotNull(element);
        if ("input".equals(element.getTagName()) || "textarea".equals(element.getTagName())) {
            return element.getAttribute("value");
        } else {
            return element.getText();
        }
    }

    /**
     * Finds an element that is visible.
     *
     * @param parent The context where the element is going to be looked.
     * @param by The identifier of the element
     * @param timeoutInSeconds How much time to wait.
     * @return the first element that is visible.
     */
    public static WebElement findElementVisibleAfter(SearchContext parent, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);

        try {
            WebElementWait wait = new WebElementWait(parent, timeoutInSeconds);
            return wait.until((SearchContext element) -> {
                List<WebElement> children = element.findElements(by);
                if (children.isEmpty()) {
                    throw new NotFoundException("No elements found");
                }
                Optional<WebElement> displayed = children.stream()
                        .filter(child -> isElementVisible(child))
                        .findAny();
                if (displayed.isPresent()) {
                    return  displayed.get();
                } else {
                    throw new NotFoundException(String.format("Element %s not visible yet", by));
                }
            });
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("Element %s is not visible after %s seconds", by, timeoutInSeconds), e);
        }
    }

    /**
     * Finds an element that is clickable.
     *
     * @param parent The context where the element is going to be looked.
     * @param by The identifier of the element
     * @param timeoutInSeconds How much time to wait.
     * @return the first element that is clickable.
     */
    public static WebElement findElementClickableAfter(SearchContext parent, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);

        try {
            WebElementWait wait = new WebElementWait(parent, timeoutInSeconds);
            return wait.until((SearchContext element) -> {
                List<WebElement> children = element.findElements(by);
                if (children.isEmpty()) {
                    throw new NotFoundException("No elements found");
                }
                Optional<WebElement> displayed = children.stream()
                        .filter(child -> isElementClickable(child))
                        .findAny();
                if (displayed.isPresent()) {
                    return  displayed.get();
                } else {
                    throw new NotFoundException(String.format("Element %s not displayed yet", by));
                }
            });
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("Element %s is not clickable after %s seconds", by, timeoutInSeconds), e);
        }
    }

    /**
     * Finds an element that is not moving.
     *
     * <p>
     *     This is helpful for animations, sometimes you try to click and element but it moved already.
     * </p>
     *
     * @param parent The context where the element is going to be looked.
     * @param by The identifier of the element
     * @param timeoutInSeconds How much time to wait.
     * @return the first element that hasn't moved.
     */
    public static WebElement findElementNotMovingAfter(SearchContext parent, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);

        try {
            final WebElement[] previous = {null};
            WebElementWait wait = new WebElementWait(parent, timeoutInSeconds);
            return wait.until((SearchContext element) -> {
                List<WebElement> children = element.findElements(by);
                if (children.isEmpty()) {
                    throw new NotFoundException("No elements found");
                }
                WebElement current = children.get(0);
                if (previous[0] == null) {
                    previous[0] = current;
                    throw new NotFoundException(String.format("Element %s has not stop moving yet", by));
                } else {
                    if ((Math.abs(current.getLocation().getX() - previous[0].getLocation().getX()) < 5) &&
                        (Math.abs(current.getLocation().getY() - previous[0].getLocation().getY()) < 5)) {
                        return current;
                    } else {
                        previous[0] = current;
                        throw new NotFoundException(String.format("Element %s has not stop moving yet", by));
                    }
                }
            });
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("Element %s did not stop moving after %s seconds", by, timeoutInSeconds), e);
        }
    }

    /**
     * Finds an element that is contains a particular text.
     *
     * @param parent The context where the element is going to be looked.
     * @param by The identifier of the element
     * @param timeoutInSeconds How much time to wait.
     * @param text the string that has to be contained in the element.
     * @return the first element that contains the text.
     */
    public static WebElement findElementContainAfter(SearchContext parent, By by, String text, int timeoutInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));
        try {
            WebElementWait wait = new WebElementWait(parent, timeoutInSeconds);
            return wait.until((SearchContext element) -> {
                List<WebElement> children = element.findElements(by);
                if (children.isEmpty()) {
                    throw new NotFoundException("No elements found");
                }
                String value = WebDriverUtil.getText(children.get(0));
                if (value.contains(text)) {
                    return children.get(0);
                } else {
                    throw new NotFoundException(String.format("Element %s does not contain text %s, contains %s", by, text, value));
                }

            });
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("Element %s did not contain text %s after %s seconds", by, text, timeoutInSeconds), e);
        }
    }

    /**
     * Finds an element that is present.
     *
     * @param parent The context where the element is going to be looked.
     * @param by The identifier of the element
     * @param timeoutInSeconds How much time to wait.
     * @return the first element that is present.
     */
    public static WebElement findElementPresentAfter(SearchContext parent, By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);
        try {
            WebElementWait wait = new WebElementWait(parent, timeoutInSeconds);
            return wait.until((SearchContext element) -> {
                List<WebElement> children = element.findElements(by);
                if (children.isEmpty()) {
                    throw new NotFoundException("No elements found");
                } else {
                    return children.get(0);
                }
            });
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("Element %s is not present after %s seconds", by, timeoutInSeconds), e);
        }
    }

    /**
     * Will return all the elements that are visible identified by the by. It will wait until one is visible and then
     * return all the visible ones at that point.
     *
     * @param parent The context where the elements are going to be looked.
     * @param by The identifier of the elements.
     * @param waitInSeconds How much time to wait.
     * @return the elements that are visible.
     */
    public static List<WebElement> findElementsVisibleAfter(SearchContext parent, By by, int waitInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);

        findElementVisibleAfter(parent, by, waitInSeconds);
        return parent.findElements(by)
                     .stream()
                     .filter(child -> isElementVisible(child))
                     .collect(Collectors.toList());
    }

    /**
     * Will return all the elements that are present identified by the by. It will wait until one is present and then
     * return all the present ones at that point.
     *
     * @param parent The context where the elements are going to be looked.
     * @param by The identifier of the elements.
     * @param waitInSeconds How much time to wait.
     * @return the elements that are present.
     */
    public static List<WebElement> findElementsPresentAfter(SearchContext parent, By by, int waitInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);

        findElementPresentAfter(parent, by, waitInSeconds);
        return parent.findElements(by);
    }

    /**
     * Will return all the elements that are clickable identified by the by. It will wait until one is clickable and then
     * return all the clickable ones at that point.
     *
     * @param parent The context where the elements are going to be looked.
     * @param by The identifier of the elements.
     * @param waitInSeconds How much time to wait.
     * @return the elements that are clickable.
     */
    public static List<WebElement> findElementsClickableAfter(SearchContext parent, By by, int waitInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);

        findElementClickableAfter(parent, by, waitInSeconds);
        return parent.findElements(by)
                     .stream()
                     .filter(child -> isElementClickable(child))
                     .collect(Collectors.toList());
    }

    /**
     * Will wait until no element identified by the by is present.
     *
     * @param parent The context where the elements are going to be looked.
     * @param by The identifier of the element.
     * @param waitInSeconds How much time to wait.
     */
    public static void waitElementToNotBePresent(SearchContext parent, By by, int waitInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);

        try {
            WebElementWait wait = new WebElementWait(parent, waitInSeconds);
            wait.until((SearchContext element) -> {
                List<WebElement> children = element.findElements(by);
                if (children.isEmpty()) {
                    return children;
                }
                throw new NotFoundException(String.format("Element %s still present yet", by));
            });
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("Element %s is still present after %s seconds", by, waitInSeconds), e);
        }
    }

    /**
     * Will wait until no element identified by the by is visible.
     *
     * @param parent The context where the elements are going to be looked.
     * @param by The identifier of the element.
     * @param waitInSeconds How much time to wait.
     */
    public static void waitElementToNotBeVisible(SearchContext parent, By by, int waitInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);

        try {
            WebElementWait wait = new WebElementWait(parent, waitInSeconds);
            wait.until((SearchContext element) -> {
                List<WebElement> children = element.findElements(by);
                Optional<WebElement> isAny = children
                        .stream()
                        .filter(elementVisible -> isElementVisible(elementVisible))
                        .findAny();
                if (!isAny.isPresent()) {
                    return children;
                }
                throw new NotFoundException(String.format("Element %s still visbile yet", by));
            });
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("Element %s is still visible after %s seconds", by, waitInSeconds), e);
        }
    }

    /**
     * Moves to an element and then clicks another.
     *
     * @param parent The context where the elements are going to be looked.
     * @param moveTo The element to move to.
     * @param click The element to click.
     * @param waitInSeconds How much time to wait.
     */
    public static void moveToAndClick(RemoteWebDriver parent, By moveTo, By click, int waitInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(moveTo);
        Preconditions.checkNotNull(click);

        moveTo(parent, moveTo, waitInSeconds);
        findElementClickableAfter(parent, click, waitInSeconds).click();
    }

    /**
     * Moves to an element..
     *
     * @param parent The context where the elements are going to be looked.
     * @param moveTo The element to move to.
     * @param waitInSeconds How much time to wait.
     * @return the element that was moved to.
     */
    public static WebElement moveTo(RemoteWebDriver parent, By moveTo, int waitInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(moveTo);

        WebElement moveToElement = findElementVisibleAfter(parent, moveTo, waitInSeconds);
        new Actions(parent)
                .moveToElement(moveToElement)
                .perform();
        return moveToElement;
    }

    /**
     * Checks if an element is visible.
     *
     * @param element the Element to check.
     * @return true if it is visible.
     */
    private static boolean isElementVisible(WebElement element) {
        Preconditions.checkNotNull(element);

        return element.isDisplayed();
    }

    /**
     * Checks if an element is clickable.
     *
     * @param element the Element to check.
     * @return true if it is clickable.
     */
    private static boolean isElementClickable(WebElement element) {
        Preconditions.checkNotNull(element);

        return element.isDisplayed() && element.isEnabled();
    }

    /**
     * Checks if a WebDriver is running Chrome.
     *
     * @param driver the WebDriver to check.
     * @return true if it is chrome.
     */
    public static boolean isChrome(RemoteWebDriver driver) {
        Preconditions.checkNotNull(driver);

        Capabilities capabilities = driver.getCapabilities();
        return "CHROME".equals(capabilities.getBrowserName().toUpperCase());
    }

    /**
     * Checks if a WebDriver is running Firefox.
     *
     * @param driver the WebDriver to check.
     * @return true if it is Firefox.
     */
    public static boolean isFirefox(RemoteWebDriver driver) {
        Preconditions.checkNotNull(driver);

        Capabilities capabilities = driver.getCapabilities();
        return "FIREFOX".equals(capabilities.getBrowserName().toUpperCase());
    }

    /**
     * Checks if an Appium is running androd 4.x.
     *
     * @param driver the appium to check.
     * @return true if it is running 4.x
     */
    public static boolean isAndroid4(RemoteWebDriver driver) {
        Preconditions.checkNotNull(driver);

        String capabilities = driver.getCapabilities().getCapability("platformVersion").toString();
        return capabilities.contains("4");
    }
}
