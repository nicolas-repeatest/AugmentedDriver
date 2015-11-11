package augmenteddriver.util;

import com.google.common.base.Preconditions;
import org.openqa.selenium.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utilities around WebDriver.
 */
public class WebDriverUtil {
    public static String getText(WebElement element) {
        if ("input".equals(element.getTagName()) || "textarea".equals(element.getTagName())) {
            return element.getAttribute("value");
        } else {
            return element.getText();
        }
    }

    public static WebElement findElementVisibleAfter(SearchContext parent, By by, int timeoutInSeconds) {
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

    public static WebElement findElementClickableAfter(SearchContext parent, By by, int timeoutInSeconds) {
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

    public static WebElement findElementNotMovingAfter(SearchContext parent, By by, int timeoutInSeconds) {
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

    public static WebElement findElementContainAfter(SearchContext parent, By by, String text, int timeoutInSeconds) {
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

    public static List<WebElement> findElementsVisibleAfter(SearchContext parent, By by, int waitInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);
        findElementVisibleAfter(parent, by, waitInSeconds);
        return parent.findElements(by)
                     .stream()
                     .filter(child -> isElementVisible(child))
                     .collect(Collectors.toList());
    }

    public static List<WebElement> findElementsPresentAfter(SearchContext parent, By by, int waitInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);
        findElementPresentAfter(parent, by, waitInSeconds);
        return parent.findElements(by);
    }

    public static List<WebElement> findElementsClickableAfter(SearchContext parent, By by, int waitInSeconds) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(by);
        findElementClickableAfter(parent, by, waitInSeconds);
        return parent.findElements(by)
                     .stream()
                     .filter(child -> isElementClickable(child))
                     .collect(Collectors.toList());
    }

    public static void waitElementToNotBePresent(SearchContext parent, By by, int waitInSeconds) {
        try {
            WebElementWait wait = new WebElementWait(parent, waitInSeconds);
            wait.until((SearchContext element) -> {
                List<WebElement> children = element.findElements(by);
                if (children.isEmpty()) {
                    return null;
                }
                throw new NotFoundException(String.format("Element %s still displayed yet", by));
            });
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("Element %s is still present after %s seconds", by, waitInSeconds), e);
        }
    }

    private static boolean isElementVisible(WebElement element) {
        Preconditions.checkNotNull(element);
        return element.isDisplayed();
    }

    private static boolean isElementClickable(WebElement element) {
        Preconditions.checkNotNull(element);
        return element.isDisplayed() && element.isEnabled();
    }
}
