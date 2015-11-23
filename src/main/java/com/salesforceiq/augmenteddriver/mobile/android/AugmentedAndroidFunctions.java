package com.salesforceiq.augmenteddriver.mobile.android;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.mobile.AugmentedMobileFunctions;
import com.salesforceiq.augmenteddriver.util.AugmentedFunctions;
import com.salesforceiq.augmenteddriver.util.MobileUtil;
import com.salesforceiq.augmenteddriver.util.WebDriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class AugmentedAndroidFunctions implements AugmentedFunctions<AugmentedAndroidElement>,
        AugmentedMobileFunctions<AugmentedAndroidElement>,
        AugmentedAndroidOnlyFunctions {

    private final int waitTimeInSeconds;
    private final AugmentedMobileFunctions<AugmentedAndroidElement> mobileFunctions;
    private final AugmentedAndroidElementFactory augmentedAndroidElementFactory;
    private final SearchContext searchContext;
    private final AugmentedAndroidDriverProvider augmentedAndroidDriveProvider;

    @Inject
    public AugmentedAndroidFunctions(@Assisted SearchContext searchContext,
                                     @Named("WAIT_TIME_IN_SECONDS") String waitTimeInSeconds,
                                     AugmentedMobileFunctions<AugmentedAndroidElement> mobileFunctions,
                                     AugmentedAndroidElementFactory augmentedAndroidElementFactory,
                                     AugmentedAndroidDriverProvider augmentedAndroidDriverProvider) {
        this.searchContext = Preconditions.checkNotNull(searchContext);
        this.augmentedAndroidDriveProvider = Preconditions.checkNotNull(augmentedAndroidDriverProvider);
        this.waitTimeInSeconds= Integer.valueOf(Preconditions.checkNotNull(waitTimeInSeconds));
        this.mobileFunctions = Preconditions.checkNotNull(mobileFunctions);
        this.augmentedAndroidElementFactory = Preconditions.checkNotNull(augmentedAndroidElementFactory);
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
    public AugmentedAndroidElement findElementPresent(By by) {
        Preconditions.checkNotNull(by);
        return findElementPresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement findElementPresentAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedAndroidElementFactory.create(WebDriverUtil.findElementPresentAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedAndroidElement findElementVisible(By by) {
        Preconditions.checkNotNull(by);
        return findElementVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement findElementVisibleAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedAndroidElementFactory.create(WebDriverUtil.findElementVisibleAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedAndroidElement findElementClickable(By by) {
        Preconditions.checkNotNull(by);
        return findElementClickableAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement findElementClickableAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedAndroidElementFactory.create(WebDriverUtil.findElementClickableAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedAndroidElement findElementNotMoving(By by) {
        Preconditions.checkNotNull(by);
        return findElementNotMovingAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement findElementNotMovingAfter(By by, int waitSeconds) {
        Preconditions.checkNotNull(by);
        return augmentedAndroidElementFactory.create(WebDriverUtil.findElementNotMovingAfter(searchContext, by, waitSeconds));
    }

    @Override
    public AugmentedAndroidElement findElementContain(By by, String text) {
        return findElementContainAfter(by, text, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement findElementContainAfter(By by, String text, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));
        return augmentedAndroidElementFactory.create(WebDriverUtil.findElementContainAfter(searchContext, by, text, waitInSeconds));
    }

    @Override
    public List<AugmentedAndroidElement> findElementsVisible(By by) {
        Preconditions.checkNotNull(by);
        return findElementsVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public List<AugmentedAndroidElement> findElementsVisibleAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        return WebDriverUtil.findElementsVisibleAfter(searchContext, by, waitInSeconds)
                .stream()
                .map(webElement -> augmentedAndroidElementFactory.create(webElement))
                .collect(Collectors.toList());
    }

    @Override
    public List<AugmentedAndroidElement> findElementsPresent(By by) {
        Preconditions.checkNotNull(by);
        return findElementsPresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public List<AugmentedAndroidElement> findElementsPresentAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        return WebDriverUtil.findElementsVisibleAfter(searchContext, by, waitInSeconds)
                .stream()
                .map(webElement -> augmentedAndroidElementFactory.create(webElement))
                .collect(Collectors.toList());
    }

    @Override
    public List<AugmentedAndroidElement> findElementsClickable(By by) {
        Preconditions.checkNotNull(by);
        return findElementsClickableAfter(by, waitTimeInSeconds);
    }

    @Override
    public List<AugmentedAndroidElement> findElementsClickableAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        return WebDriverUtil.findElementsClickableAfter(searchContext, by, waitInSeconds)
                .stream()
                .map(webElement -> augmentedAndroidElementFactory.create(webElement))
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
    public AugmentedAndroidElement clickAndPresent(By click, By wait) {
        return clickAndPresentAfter(click, wait, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement clickAndPresentAfter(By click, By wait, int waitInSeconds) {
        Preconditions.checkNotNull(click);
        Preconditions.checkNotNull(wait);
        findElementClickableAfter(click, waitInSeconds).click();
        return findElementPresentAfter(wait, waitInSeconds);
    }

    @Override
    public void moveToAndClick(By moveTo, By click) {
        WebDriverUtil.moveToAndClick(augmentedAndroidDriveProvider.get(), moveTo, click, waitTimeInSeconds);
    }

    @Override
    public void moveToAndClickAfter(By moveTo, By click, int waitInSeconds) {
        WebDriverUtil.moveToAndClick(augmentedAndroidDriveProvider.get(), moveTo, click, waitInSeconds);
    }

    @Override
    public AugmentedAndroidElement moveTo(By moveTo) {
        return augmentedAndroidElementFactory.create(WebDriverUtil.moveTo(augmentedAndroidDriveProvider.get(), moveTo, waitTimeInSeconds));
    }

    @Override
    public AugmentedAndroidElement moveToAfter(By moveTo, int waitInSeconds) {
        return augmentedAndroidElementFactory.create(WebDriverUtil.moveTo(augmentedAndroidDriveProvider.get(), moveTo, waitInSeconds));
    }

    @Override
    public void clearAndSendKeys(By by, String text) {
        clearAndSendKeysAfter(by, text, waitTimeInSeconds);
    }

    @Override
    public void clearAndSendKeysAfter(By by, String text, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));
        AugmentedAndroidElement element = findElementClickableAfter(by, waitInSeconds);
        element.clear();
        element.sendKeys(text);
    }

    @Override
    public AugmentedAndroidElement tap(By by) {
        Preconditions.checkNotNull(by);
        return tapAfter(by, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement tapAfter(By by, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);
        WebElement element = MobileUtil.tap(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), by, waitTimeInSeconds);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public AugmentedAndroidElement tap(By by, int offsetX, int offsetY) {
        Preconditions.checkNotNull(by);
        return tapAfter(by, offsetX, offsetY, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement tapAfter(By by, int offsetX, int offsetY, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);
        WebElement element = MobileUtil.tap(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(),
                by, offsetX, offsetY, waitTimeInSeconds);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public void tap(WebElement element, int pressInMilliSeconds) {
        Preconditions.checkNotNull(element);
        MobileUtil.tap(augmentedAndroidDriveProvider.get(), element, pressInMilliSeconds);
    }

    @Override
    public void clickAndSendKeys(By by, String keys) {
        clickAndSendKeysAfter(by, keys, waitTimeInSeconds);
    }

    @Override
    public void clickAndSendKeysAfter(By by, String keys, int waitInSeconds) {
        findElementClickableAfter(by, waitInSeconds).click();
        findElementClickableAfter(by, waitInSeconds).sendKeys(keys);
    }

    @Override
    public AugmentedAndroidElement swipeUpWaitElementVisible(By swipeUpElement, By elementPresent) {
        WebElement element = MobileUtil.swipeUpWaitVisible(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), swipeUpElement, elementPresent);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public AugmentedAndroidElement swipeDownWaitElementVisible(By swipeUpElement, By elementPresent) {
        WebElement element = MobileUtil.swipeDownWaitVisible(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), swipeUpElement, elementPresent);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public AugmentedAndroidElement swipeVerticalWaitVisible(By swipeElement, By elementVisible, int offset, int quantity, int duration) {
        WebElement element = MobileUtil.swipeVerticalWaitVisible(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), swipeElement, elementVisible, offset, quantity, duration);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public void swipeUp(By swipeBy) {
        MobileUtil.swipeUp(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), swipeBy);
    }

    @Override
    public void swipeDown(By swipeBy) {
        MobileUtil.swipeDown(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), swipeBy);
    }

    @Override
    public void swipeVertical(By swipeBy, int offset, int duration) {
        MobileUtil.swipeVertical(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), swipeBy, offset, duration);
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
        MobileUtil.swipeFullRightAfter(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), element, waitInSeconds);
    }

    @Override
    public void swipeFullRightAfter(By by, int waitInSeconds) {
        MobileUtil.swipeFullRightAfter(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), by, waitInSeconds);
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
        MobileUtil.swipeFullLeftAfter(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), element, waitInSeconds);

    }

    @Override
    public void swipeFullLeftAfter(By by, int waitInSeconds) {
        MobileUtil.swipeFullLeftAfter(augmentedAndroidDriveProvider.get(), augmentedAndroidDriveProvider.get().augmented(), by, waitInSeconds);
    }
}
