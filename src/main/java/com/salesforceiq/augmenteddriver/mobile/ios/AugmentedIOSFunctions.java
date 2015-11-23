package com.salesforceiq.augmenteddriver.mobile.ios;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.mobile.AugmentedMobileFunctions;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidDriverProvider;
import com.salesforceiq.augmenteddriver.util.AugmentedFunctions;
import com.salesforceiq.augmenteddriver.util.MobileUtil;
import com.salesforceiq.augmenteddriver.util.WebDriverUtil;
import io.appium.java_client.remote.HideKeyboardStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class AugmentedIOSFunctions implements AugmentedFunctions<AugmentedIOSElement>,
                                             AugmentedMobileFunctions<AugmentedIOSElement>,
        AugmentedIOSOnlyFunctions {

    private final int waitTimeInSeconds;
    private final AugmentedMobileFunctions<AugmentedIOSElement> mobileFunctions;
    private final AugmentedIOSElementFactory augmentedIOSElementFactory;
    private final AugmentedIOSDriverProvider augmentedIOSDriverProvider;
    private final SearchContext searchContext;

    @Inject
    public AugmentedIOSFunctions(@Assisted SearchContext searchContext,
                                 @Named("WAIT_TIME_IN_SECONDS") String waitTimeInSeconds,
                                 AugmentedMobileFunctions<AugmentedIOSElement> mobileFunctions,
                                 AugmentedIOSElementFactory augmentedIOSElementFactory,
                                 AugmentedIOSDriverProvider augmentedIOSDriverProvider) {
        this.searchContext = Preconditions.checkNotNull(searchContext);
        this.augmentedIOSDriverProvider = Preconditions.checkNotNull(augmentedIOSDriverProvider);
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
        return augmentedIOSElementFactory.create(WebDriverUtil.findElementPresentAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedIOSElement findElementVisible(By by) {
        Preconditions.checkNotNull(by);
        return findElementVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement findElementVisibleAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedIOSElementFactory.create(WebDriverUtil.findElementVisibleAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedIOSElement findElementClickable(By by) {
        Preconditions.checkNotNull(by);
        return findElementClickableAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement findElementClickableAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedIOSElementFactory.create(WebDriverUtil.findElementClickableAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedIOSElement findElementNotMoving(By by) {
        Preconditions.checkNotNull(by);
        return findElementNotMovingAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement findElementNotMovingAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedIOSElementFactory.create(WebDriverUtil.findElementNotMovingAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedIOSElement findElementContain(By by, String text) {
        return findElementContainAfter(by, text, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement findElementContainAfter(By by, String text, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));
        return augmentedIOSElementFactory.create(WebDriverUtil.findElementContainAfter(searchContext, by, text, waitInSeconds));
    }

    @Override
    public List<AugmentedIOSElement> findElementsVisible(By by) {
        Preconditions.checkNotNull(by);
        return findElementsVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public List<AugmentedIOSElement> findElementsVisibleAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        return WebDriverUtil.findElementsVisibleAfter(searchContext, by, waitInSeconds)
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
        return WebDriverUtil.findElementsPresentAfter(searchContext, by, waitInSeconds)
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
        return WebDriverUtil.findElementsClickableAfter(searchContext, by, waitInSeconds)
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
        WebDriverUtil.waitElementToNotBePresent(searchContext, by, waitInSeconds);
    }

    @Override
    public void waitElementToNotBeVisible(By by) {
        waitElementToNotBeVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public void waitElementToNotBeVisibleAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        WebDriverUtil.waitElementToNotBeVisible(searchContext, by, waitInSeconds);
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
    public void moveToAndClick(By moveTo, By click) {
        WebDriverUtil.moveToAndClick(augmentedIOSDriverProvider.get(), moveTo, click, waitTimeInSeconds);
    }

    @Override
    public void moveToAndClickAfter(By moveTo, By click, int waitInSeconds) {
        WebDriverUtil.moveToAndClick(augmentedIOSDriverProvider.get(), moveTo, click, waitInSeconds);
    }

    @Override
    public AugmentedIOSElement moveTo(By moveTo) {
        return augmentedIOSElementFactory.create(WebDriverUtil.moveTo(augmentedIOSDriverProvider.get(), moveTo, waitTimeInSeconds));
    }

    @Override
    public AugmentedIOSElement moveToAfter(By moveTo, int waitInSeconds) {
        return augmentedIOSElementFactory.create(WebDriverUtil.moveTo(augmentedIOSDriverProvider.get(), moveTo, waitInSeconds));
    }

    @Override
    public void clearAndSendKeys(By by, String text) {
        clearAndSendKeysAfter(by, text, waitTimeInSeconds);
    }

    @Override
    public void clearAndSendKeysAfter(By by, String text, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));
        AugmentedIOSElement element = findElementClickableAfter(by, waitInSeconds);
        element.clear();
        element.sendKeys(text);
    }

    @Override
    public AugmentedIOSElement tap(By by) {
        Preconditions.checkNotNull(by);
        return tapAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement tapAfter(By by, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);
        WebElement element = MobileUtil.tap(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), by, waitTimeInSeconds);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement tap(By by, int offsetX, int offsetY) {
        Preconditions.checkNotNull(by);
        return tapAfter(by, offsetX, offsetY, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement tapAfter(By by, int offsetX, int offsetY, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);
        WebElement element = MobileUtil.tap(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(),
                by, offsetX, offsetY, waitTimeInSeconds);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public void tap(WebElement element, int pressInMilliSeconds) {
        Preconditions.checkNotNull(element);
        MobileUtil.tap(augmentedIOSDriverProvider.get(), element, pressInMilliSeconds);
    }

    @Override
    public void clickAndSendKeys(By by, String keys) {
        clickAndSendKeysAfter(by, keys, waitTimeInSeconds);
    }

    @Override
    public void clickAndSendKeysAfter(By by, String keys, int waitInSeconds) {
        findElementClickableAfter(by, waitInSeconds).click();
        findElementClickableAfter(by, waitInSeconds).sendKeys(keys);
        augmentedIOSDriverProvider.get().hideKeyboard(HideKeyboardStrategy.PRESS_KEY, "Done");
    }

    @Override
    public AugmentedIOSElement swipeUpWaitElementVisible(By swipeUpElement, By elementPresent) {
        WebElement element = MobileUtil.swipeUpWaitVisible(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), swipeUpElement, elementPresent);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement swipeDownWaitElementVisible(By swipeUpElement, By elementPresent) {
        WebElement element = MobileUtil.swipeDownWaitVisible(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), swipeUpElement, elementPresent);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement swipeVerticalWaitVisible(By swipeElement, By elementVisible, int offset, int quantity, int duration) {
        WebElement element = MobileUtil.swipeVerticalWaitVisible(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), swipeElement, elementVisible, offset, quantity, duration);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public void swipeUp(By swipeBy) {
        MobileUtil.swipeUp(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), swipeBy);
    }

    @Override
    public void swipeDown(By swipeBy) {
        MobileUtil.swipeDown(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), swipeBy);
    }

    @Override
    public void swipeVertical(By swipeBy, int offset, int duration) {
        MobileUtil.swipeVertical(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), swipeBy, offset, duration);
    }

    @Override
    public void swipeFullRight(WebElement element) {
        swipeFullRightAfter(element, waitTimeInSeconds);
    }

    @Override
    public void swipeFullRight(By by) {
        swipeFullRightAfter(by, waitTimeInSeconds);
    }

    @Override
    public void swipeFullRightAfter(WebElement element, int waitInSeconds) {
        MobileUtil.swipeFullRightAfter(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), element, waitInSeconds);
    }

    @Override
    public void swipeFullRightAfter(By by, int waitInSeconds) {
        MobileUtil.swipeFullRightAfter(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), by, waitInSeconds);
    }

    @Override
    public void swipeFullLeft(WebElement element) {
        swipeFullLeftAfter(element, waitTimeInSeconds);

    }

    @Override
    public void swipeFullLeft(By by) {
        swipeFullLeftAfter(by, waitTimeInSeconds);
    }

    @Override
    public void swipeFullLeftAfter(WebElement element, int waitInSeconds) {
        MobileUtil.swipeFullLeftAfter(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), element, waitInSeconds);

    }

    @Override
    public void swipeFullLeftAfter(By by, int waitInSeconds) {
        MobileUtil.swipeFullLeftAfter(augmentedIOSDriverProvider.get(), augmentedIOSDriverProvider.get().augmented(), by, waitInSeconds);
    }
}
