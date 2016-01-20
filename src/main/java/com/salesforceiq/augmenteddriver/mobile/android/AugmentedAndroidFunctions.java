package com.salesforceiq.augmenteddriver.mobile.android;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.mobile.AugmentedMobileFunctions;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.AugmentedFunctions;
import com.salesforceiq.augmenteddriver.util.MobileUtil;
import com.salesforceiq.augmenteddriver.util.WebDriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the augmented mobile functionality for Android.
 */
public class AugmentedAndroidFunctions implements AugmentedFunctions<AugmentedAndroidElement>,
        AugmentedMobileFunctions<AugmentedAndroidElement>,
        AugmentedAndroidOnlyFunctions {

    private final int waitTimeInSeconds;
    private final AugmentedAndroidElementFactory augmentedAndroidElementFactory;
    private final SearchContext searchContext;
    private final AugmentedAndroidDriverProvider augmentedAndroidDriveProvider;
    private final int pressTimeInMilliSeconds;
    private final int swipeQuantity;
    private final int tapFingers;

    @Inject
    public AugmentedAndroidFunctions(@Assisted SearchContext searchContext,
                                     @Named(PropertiesModule.WAIT_IN_SECONDS) String waitTimeInSeconds,
                                     @Named(PropertiesModule.PRESS_TIME_IN_MILLISECONDS) String pressTimeInMilliSeconds,
                                     @Named(PropertiesModule.SWIPE_QUANTITY) String swipeQuantity,
                                     @Named(PropertiesModule.TAP_FINGERS) String tapFingers,
                                     AugmentedAndroidElementFactory augmentedAndroidElementFactory,
                                     AugmentedAndroidDriverProvider augmentedAndroidDriverProvider) {
        this.searchContext = Preconditions.checkNotNull(searchContext);
        this.augmentedAndroidDriveProvider = Preconditions.checkNotNull(augmentedAndroidDriverProvider);
        this.pressTimeInMilliSeconds = Integer.valueOf(Preconditions.checkNotNull(pressTimeInMilliSeconds));
        this.swipeQuantity = Integer.valueOf(Preconditions.checkNotNull(swipeQuantity));
        this.tapFingers = Integer.valueOf(Preconditions.checkNotNull(tapFingers));
        this.waitTimeInSeconds= Integer.valueOf(Preconditions.checkNotNull(waitTimeInSeconds));
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

        return isElementPresentAfter(by, 1);
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

        return isElementVisibleAfter(by, 1);
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

        return isElementClickableAfter(by, 1);
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
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));

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
                .map(augmentedAndroidElementFactory::create)
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
                .map(augmentedAndroidElementFactory::create)
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
                .map(augmentedAndroidElementFactory::create)
                .collect(Collectors.toList());
    }

    @Override
    public void waitElementToNotBePresent(By by) {
        Preconditions.checkNotNull(by);

        waitElementToNotBePresentAfter(by, waitTimeInSeconds);
    }

    @Override
    public void waitElementToNotBePresentAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);

        WebDriverUtil.waitElementToNotBePresent(searchContext, by, waitInSeconds);
    }

    @Override
    public void waitElementToNotBeVisible(By by) {
        Preconditions.checkNotNull(by);

        waitElementToNotBeVisibleAfter(by, waitTimeInSeconds);
    }

    @Override
    public void waitElementToNotBeVisibleAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);

        WebDriverUtil.waitElementToNotBeVisible(searchContext, by, waitInSeconds);
    }

    @Override
    public AugmentedAndroidElement clickAndPresent(By click, By wait) {
        Preconditions.checkNotNull(click);
        Preconditions.checkNotNull(wait);

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
        Preconditions.checkNotNull(moveTo);
        Preconditions.checkNotNull(click);

        WebDriverUtil.moveToAndClick(augmentedAndroidDriveProvider.get(), moveTo, click, waitTimeInSeconds);
    }

    @Override
    public void moveToAndClickAfter(By moveTo, By click, int waitInSeconds) {
        Preconditions.checkNotNull(click);
        Preconditions.checkNotNull(moveTo);

        WebDriverUtil.moveToAndClick(augmentedAndroidDriveProvider.get(), moveTo, click, waitInSeconds);
    }

    @Override
    public AugmentedAndroidElement moveTo(By moveTo) {
        Preconditions.checkNotNull(moveTo);

        return augmentedAndroidElementFactory.create(WebDriverUtil.moveTo(augmentedAndroidDriveProvider.get(), moveTo, waitTimeInSeconds));
    }

    @Override
    public AugmentedAndroidElement moveToAfter(By moveTo, int waitInSeconds) {
        Preconditions.checkNotNull(moveTo);

        return augmentedAndroidElementFactory.create(WebDriverUtil.moveTo(augmentedAndroidDriveProvider.get(), moveTo, waitInSeconds));
    }

    @Override
    public void clearAndSendKeys(By by, String text) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));

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

        WebElement element = MobileUtil.tapAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                by,
                0,
                0,
                waitTimeInSeconds,
                pressTimeInMilliSeconds,
                tapFingers);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public AugmentedAndroidElement tapOffset(By by, int offsetX, int offsetY) {
        Preconditions.checkNotNull(by);

        return tapOffsetAfter(by, offsetX, offsetY, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement tapOffsetAfter(By by, int offsetX, int offsetY, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);

        WebElement element = MobileUtil.tapAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                by,
                offsetX,
                offsetY,
                waitTimeInSeconds,
                pressTimeInMilliSeconds,
                tapFingers);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public AugmentedAndroidElement tapLong(By by, int pressInMilliSeconds) {
        Preconditions.checkNotNull(by);

        return tapLongAfter(by, pressInMilliSeconds, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement tapLongAfter(By by, int pressInMilliSeconds, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);

        WebElement element = MobileUtil.tapAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                by,
                0,
                0,
                waitTimeInSeconds,
                pressInMilliSeconds,
                tapFingers);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public AugmentedAndroidElement tapFingers(By by, int fingers) {
        Preconditions.checkNotNull(by);

        return tapFingersAfter(by, fingers, waitTimeInSeconds);
    }

    @Override
    public AugmentedAndroidElement tapFingersAfter(By by, int fingers, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);

        WebElement element = MobileUtil.tapAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                by,
                0,
                0,
                waitTimeInSeconds,
                pressTimeInMilliSeconds,
                fingers);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public AugmentedAndroidElement tapCustomAfter(By by, int offsetX, int offsetY, int pressInMilliSeconds, int fingers, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);

        WebElement element = MobileUtil.tapAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                by,
                offsetX,
                offsetY,
                waitTimeInSeconds,
                pressInMilliSeconds,
                fingers);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public AugmentedAndroidElement tapCustom(By by, int offsetX, int offsetY, int pressInMilliSeconds, int fingers) {
        Preconditions.checkNotNull(by);

        return tapCustomAfter(by, offsetX, offsetY, pressInMilliSeconds, fingers, waitTimeInSeconds);
    }

    @Override
    public void clickAndSendKeys(By by, String keys) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(keys));


        clickAndSendKeysAfter(by, keys, waitTimeInSeconds);
    }

    @Override
    public void clickAndSendKeysAfter(By by, String keys, int waitInSeconds) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(keys));

        findElementClickableAfter(by, waitInSeconds).click();
        findElementClickableAfter(by, waitInSeconds).sendKeys(keys);
    }

    @Override
    public AugmentedAndroidElement swipeUpWaitElementVisible(By swipeUpElement, By elementPresent) {
        Preconditions.checkNotNull(swipeUpElement);
        Preconditions.checkNotNull(elementPresent);

        WebElement element = MobileUtil.swipeUpWaitVisibleAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                swipeUpElement,
                elementPresent,
                waitTimeInSeconds,
                swipeQuantity,
                pressTimeInMilliSeconds);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public AugmentedAndroidElement swipeDownWaitElementVisible(By swipeDownElement, By elementPresent) {
        Preconditions.checkNotNull(swipeDownElement);
        Preconditions.checkNotNull(elementPresent);

        WebElement element = MobileUtil.swipeDownWaitVisible(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                swipeDownElement,
                elementPresent,
                waitTimeInSeconds,
                swipeQuantity,
                pressTimeInMilliSeconds);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public AugmentedAndroidElement swipeVerticalWaitVisible(By swipeElement, By elementVisible, int offset, int quantity, int durationInMilliSeconds) {
        Preconditions.checkNotNull(swipeElement);
        Preconditions.checkNotNull(elementVisible);

        WebElement element = MobileUtil.swipeVerticalWaitVisibleAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                swipeElement,
                elementVisible,
                waitTimeInSeconds,
                offset,
                quantity,
                durationInMilliSeconds);
        return augmentedAndroidElementFactory.create(element);
    }

    @Override
    public void swipeUp(By swipeBy) {
        Preconditions.checkNotNull(swipeBy);

        MobileUtil.swipeUpAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                swipeBy,
                waitTimeInSeconds,
                pressTimeInMilliSeconds);
    }

    @Override
    public void swipeDown(By swipeBy) {
        Preconditions.checkNotNull(swipeBy);

        MobileUtil.swipeDownAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                swipeBy,
                waitTimeInSeconds,
                pressTimeInMilliSeconds);
    }

    @Override
    public void swipeVertical(By swipeBy, int offset, int durationinMilliSeconds) {
        Preconditions.checkNotNull(swipeBy);

        MobileUtil.swipeVerticalAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                swipeBy,
                waitTimeInSeconds,
                offset,
                durationinMilliSeconds);
    }

    @Override
    public void swipeFullRight(By by) {
        Preconditions.checkNotNull(by);

        swipeFullRightAfter(by, waitTimeInSeconds);
    }

    @Override
    public void swipeFullRightAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);

        MobileUtil.swipeFullRightAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                by,
                waitInSeconds,
                pressTimeInMilliSeconds);
    }

    @Override
    public void swipeFullLeft(By by) {
        Preconditions.checkNotNull(by);

        swipeFullLeftAfter(by, waitTimeInSeconds);
    }

    @Override
    public void swipeFullLeftAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);

        MobileUtil.swipeFullLeftAfter(augmentedAndroidDriveProvider.get(),
                augmentedAndroidDriveProvider.get().augmented(),
                by,
                waitInSeconds,
                pressTimeInMilliSeconds);
    }
}
