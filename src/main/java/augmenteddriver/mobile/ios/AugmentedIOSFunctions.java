package augmenteddriver.mobile.ios;

import augmenteddriver.mobile.AugmentedMobileFunctions;
import augmenteddriver.util.AugmentedFunctions;
import augmenteddriver.util.MobileUtil;
import augmenteddriver.util.WebDriverUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import io.appium.java_client.remote.HideKeyboardStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class AugmentedIOSFunctions implements AugmentedFunctions<AugmentedIOSElement>,
                                             AugmentedMobileFunctions<AugmentedIOSElement>,
        AugmentedIOSOnlyFunctions {

    /**
     * Important we use a Provider, since we need the driver to be initialized when the first test starts to run
     * not at creation time, like Guice wants.
     */
    private final Provider<AugmentedIOSDriver> driverProvider;
    private final int waitTimeInSeconds;
    private final AugmentedMobileFunctions<AugmentedIOSElement> mobileFunctions;
    private final AugmentedIOSElementFactory augmentedIOSElementFactory;

    @Inject
    public AugmentedIOSFunctions(Provider<AugmentedIOSDriver> driverProvider,
                                 @Named("WAIT_TIME_IN_SECONDS") String waitTimeInSeconds,
                                 AugmentedMobileFunctions<AugmentedIOSElement> mobileFunctions,
                                 AugmentedIOSElementFactory augmentedIOSElementFactory) {
        this.driverProvider = Preconditions.checkNotNull(driverProvider);
        this.waitTimeInSeconds= Integer.valueOf(Preconditions.checkNotNull(waitTimeInSeconds));
        this.mobileFunctions = Preconditions.checkNotNull(mobileFunctions);
        this.augmentedIOSElementFactory = Preconditions.checkNotNull(augmentedIOSElementFactory);
    }

    @Override
    public boolean isElementPresent(By by) {
        Preconditions.checkNotNull(by);
        return isElementPresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public boolean isElementPresentAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        try {
            findElementPresentAfter(by, waitSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean isElementPresentImmediate(By by) {
        Preconditions.checkNotNull(by);
        return isElementPresentAfter(by, 0);
    }

    @Override
    public boolean isElementVisible(By by) {
        Preconditions.checkNotNull(by);
        return isElementVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public boolean isElementVisibleAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        try {
            findElementsVisibleAfter(by, waitSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean isElementVisibleImmediate(By by) {
        Preconditions.checkNotNull(by);
        return isElementVisibleAfter(by, 0);
    }

    @Override
    public boolean isElementClickable(By by) {
        Preconditions.checkNotNull(by);
        return isElementClickableAfter(by, waitTimeInSeconds);
    }

    @Override
    public boolean isElementClickableAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        try {
            findElementClickableAfter(by, waitSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean isElementClickableImmediate(By by) {
        Preconditions.checkNotNull(by);
        return isElementClickableAfter(by, 0);
    }

    @Override
    public AugmentedIOSElement findElementPresent(By by) {
        Preconditions.checkNotNull(by);
        return findElementPresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement findElementPresentAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedIOSElementFactory.create(WebDriverUtil.findElementPresentAfter(driverProvider.get(), by, waitSeconds));
    }

    @Override
    public AugmentedIOSElement findElementVisible(By by) {
        Preconditions.checkNotNull(by);
        return findElementVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement findElementVisibleAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedIOSElementFactory.create(WebDriverUtil.findElementVisibleAfter(driverProvider.get(), by, waitSeconds));
    }

    @Override
    public AugmentedIOSElement findElementClickable(By by) {
        Preconditions.checkNotNull(by);
        return findElementClickableAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement findElementClickableAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedIOSElementFactory.create(WebDriverUtil.findElementClickableAfter(driverProvider.get(), by, waitSeconds));
    }

    @Override
    public AugmentedIOSElement findElementNotMoving(By by) {
        Preconditions.checkNotNull(by);
        return findElementNotMovingAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement findElementNotMovingAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedIOSElementFactory.create(WebDriverUtil.findElementNotMovingAfter(driverProvider.get(), by, waitSeconds));
    }

    @Override
    public AugmentedIOSElement findElementContain(By by, String text) {
        return findElementContainAfter(by, text, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement findElementContainAfter(By by, String text, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));
        return augmentedIOSElementFactory.create(WebDriverUtil.findElementContainAfter(driverProvider.get(), by, text, waitInSeconds));
    }

    @Override
    public List<AugmentedIOSElement> findElementsVisible(By by) {
        Preconditions.checkNotNull(by);
        return findElementsVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public List<AugmentedIOSElement> findElementsVisibleAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        return WebDriverUtil.findElementsVisibleAfter(driverProvider.get(), by, waitInSeconds)
                .stream()
                .map(webElement -> augmentedIOSElementFactory.create(webElement))
                .collect(Collectors.toList());
    }

    @Override
    public List<AugmentedIOSElement> findElementsPresent(By by) {
        Preconditions.checkNotNull(by);
        return findElementsPresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public List<AugmentedIOSElement> findElementsPresentAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        return WebDriverUtil.findElementsPresentAfter(driverProvider.get(), by, waitInSeconds)
                .stream()
                .map(webElement -> augmentedIOSElementFactory.create(webElement))
                .collect(Collectors.toList());
    }

    @Override
    public List<AugmentedIOSElement> findElementsClickable(By by) {
        Preconditions.checkNotNull(by);
        return findElementsClickableAfter(by, waitTimeInSeconds);
    }

    @Override
    public List<AugmentedIOSElement> findElementsClickableAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        return WebDriverUtil.findElementsClickableAfter(driverProvider.get(), by, waitInSeconds)
                .stream()
                .map(webElement -> augmentedIOSElementFactory.create(webElement))
                .collect(Collectors.toList());
    }

    @Override
    public void waitElementToNotBePresent(By by) {
        waitElementToNotBePresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public void waitElementToNotBePresentAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        WebDriverUtil.waitElementToNotBePresent(driverProvider.get(), by, waitInSeconds);
    }

    @Override
    public void waitElementToNotBeVisible(By by) {
        waitElementToNotBeVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public void waitElementToNotBeVisibleAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        WebDriverUtil.waitElementToNotBeVisible(driverProvider.get(), by, waitInSeconds);
    }

    @Override
    public AugmentedIOSElement clickAndPresent(By click, By wait) {
        return clickAndPresentAfter(click, wait, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement clickAndPresentAfter(By click, By wait, int waitInSeconds) {
        Preconditions.checkNotNull(click);
        Preconditions.checkNotNull(wait);
        findElementClickableAfter(click, waitInSeconds).click();
        return findElementPresentAfter(wait, waitInSeconds);
    }

    @Override
    public void clickAnSendKeys(By by, String keys) {
        clickAnSendKeysAfter(by, keys, waitTimeInSeconds);
    }

    @Override
    public void clickAnSendKeysAfter(By by, String keys, int waitInSeconds) {
        findElementClickableAfter(by, waitInSeconds).click();
        findElementClickableAfter(by, waitInSeconds).sendKeys(keys);
        driverProvider.get().hideKeyboard(HideKeyboardStrategy.PRESS_KEY, "Done");
    }

    @Override
    public AugmentedIOSElement swipeUpWaitElementVisible(By swipeUpElement, By elementPresent) {
        WebElement element = MobileUtil.swipeUpWaitVisible(driverProvider.get(), driverProvider.get().augmented(), swipeUpElement, elementPresent);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement swipeDownWaitElementVisible(By swipeUpElement, By elementPresent) {
        WebElement element = MobileUtil.swipeDownWaitVisible(driverProvider.get(), driverProvider.get().augmented(), swipeUpElement, elementPresent);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement swipeVerticalWaitVisible(By swipeElement, By elementVisible, int offset, int quantity, int duration) {
        WebElement element = MobileUtil.swipeVerticalWaitVisible(driverProvider.get(), driverProvider.get().augmented(), swipeElement, elementVisible, offset, quantity, duration);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public void swipeUp(By swipeBy) {
        MobileUtil.swipeUp(driverProvider.get(), driverProvider.get().augmented(), swipeBy);
    }

    @Override
    public void swipeDown(By swipeBy) {
        MobileUtil.swipeDown(driverProvider.get(), driverProvider.get().augmented(), swipeBy);
    }

    @Override
    public void swipeVertical(By swipeBy, int offset, int duration) {
        MobileUtil.swipeVertical(driverProvider.get(), driverProvider.get().augmented(), swipeBy, offset, duration);
    }
}
