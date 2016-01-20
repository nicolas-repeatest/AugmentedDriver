package com.salesforceiq.augmenteddriver.mobile.ios;

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
 * Implementation of the augmented mobile functionality for IOS.
 */
public class AugmentedIOSFunctions implements AugmentedFunctions<AugmentedIOSElement>,
                                              AugmentedMobileFunctions<AugmentedIOSElement>,
                                              AugmentedIOSOnlyFunctions {

    private final int waitTimeInSeconds;
    private final AugmentedIOSElementFactory augmentedIOSElementFactory;
    private final AugmentedIOSDriverProvider augmentedIOSDriverProvider;
    private final SearchContext searchContext;
    private final int pressTimeInMilliSeconds;
    private final int swipeQuantity;
    private final int tapFingers;

    @Inject
    public AugmentedIOSFunctions(@Assisted SearchContext searchContext,
                                 @Named(PropertiesModule.WAIT_IN_SECONDS) String waitTimeInSeconds,
                                 @Named(PropertiesModule.PRESS_TIME_IN_MILLISECONDS) String pressTimeInMilliSeconds,
                                 @Named(PropertiesModule.SWIPE_QUANTITY) String swipeQuantity,
                                 @Named(PropertiesModule.TAP_FINGERS) String tapFingers,
                                 AugmentedIOSElementFactory augmentedIOSElementFactory,
                                 AugmentedIOSDriverProvider augmentedIOSDriverProvider) {
        this.searchContext = Preconditions.checkNotNull(searchContext);
        this.augmentedIOSDriverProvider = Preconditions.checkNotNull(augmentedIOSDriverProvider);
        this.pressTimeInMilliSeconds = Integer.valueOf(Preconditions.checkNotNull(pressTimeInMilliSeconds));
        this.swipeQuantity = Integer.valueOf(Preconditions.checkNotNull(swipeQuantity));
        this.tapFingers = Integer.valueOf(Preconditions.checkNotNull(tapFingers));
        this.waitTimeInSeconds= Integer.valueOf(Preconditions.checkNotNull(waitTimeInSeconds));
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
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));

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
                .map(augmentedIOSElementFactory::create)
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
                .map(augmentedIOSElementFactory::create)
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
                .map(augmentedIOSElementFactory::create)
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
    public AugmentedIOSElement clickAndPresent(By click, By wait) {
        Preconditions.checkNotNull(click);
        Preconditions.checkNotNull(wait);

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
        Preconditions.checkNotNull(click);
        Preconditions.checkNotNull(moveTo);

        WebDriverUtil.moveToAndClick(augmentedIOSDriverProvider.get(), moveTo, click, waitTimeInSeconds);
    }

    @Override
    public void moveToAndClickAfter(By moveTo, By click, int waitInSeconds) {
        Preconditions.checkNotNull(click);
        Preconditions.checkNotNull(moveTo);

        WebDriverUtil.moveToAndClick(augmentedIOSDriverProvider.get(), moveTo, click, waitInSeconds);
    }

    @Override
    public AugmentedIOSElement moveTo(By moveTo) {
        Preconditions.checkNotNull(moveTo);

        return augmentedIOSElementFactory.create(WebDriverUtil.moveTo(augmentedIOSDriverProvider.get(), moveTo, waitTimeInSeconds));
    }

    @Override
    public AugmentedIOSElement moveToAfter(By moveTo, int waitInSeconds) {
        Preconditions.checkNotNull(moveTo);

        return augmentedIOSElementFactory.create(WebDriverUtil.moveTo(augmentedIOSDriverProvider.get(), moveTo, waitInSeconds));
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

        WebElement element = MobileUtil.tapAfter(
                augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                by,
                0,
                0,
                waitTimeInSeconds,
                pressTimeInMilliSeconds,
                tapFingers);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement tapOffset(By by, int offsetX, int offsetY) {
        Preconditions.checkNotNull(by);

        return tapOffsetAfter(by, offsetX, offsetY, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement tapOffsetAfter(By by, int offsetX, int offsetY, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);

        WebElement element = MobileUtil.tapAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                by,
                offsetX,
                offsetY,
                waitTimeInSeconds,
                pressTimeInMilliSeconds,
                tapFingers);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement tapLong(By by, int pressInMilliSeconds) {
        Preconditions.checkNotNull(by);

        return tapLongAfter(by, pressInMilliSeconds, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement tapLongAfter(By by, int pressInMilliSeconds, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);

        WebElement element = MobileUtil.tapAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                by,
                0,
                0,
                waitTimeInSeconds,
                pressInMilliSeconds,
                tapFingers);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement tapFingers(By by, int fingers) {
        Preconditions.checkNotNull(by);

        return tapFingersAfter(by, fingers, waitTimeInSeconds);
    }

    @Override
    public AugmentedIOSElement tapFingersAfter(By by, int fingers, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);

        WebElement element = MobileUtil.tapAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                by,
                0,
                0,
                waitTimeInSeconds,
                pressTimeInMilliSeconds,
                fingers);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement tapCustomAfter(By by, int offsetX, int offsetY, int pressInMilliSeconds,
                                              int fingers, int waitTimeInSeconds) {
        Preconditions.checkNotNull(by);

        WebElement element = MobileUtil.tapAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                by,
                offsetX,
                offsetY,
                waitTimeInSeconds,
                pressInMilliSeconds,
                fingers);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement tapCustom(By by, int offsetX, int offsetY, int pressInMilliSeconds, int fingers) {
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
    public AugmentedIOSElement swipeUpWaitElementVisible(By swipeUpElement, By elementPresent) {
        Preconditions.checkNotNull(swipeUpElement);
        Preconditions.checkNotNull(elementPresent);

        WebElement element = MobileUtil.swipeUpWaitVisibleAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                swipeUpElement,
                elementPresent,
                waitTimeInSeconds,
                swipeQuantity,
                pressTimeInMilliSeconds);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement swipeDownWaitElementVisible(By swipeDownElement, By elementPresent) {
        Preconditions.checkNotNull(swipeDownElement);
        Preconditions.checkNotNull(elementPresent);

        WebElement element = MobileUtil.swipeDownWaitVisible(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                swipeDownElement,
                elementPresent,
                waitTimeInSeconds,
                swipeQuantity,
                pressTimeInMilliSeconds);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public AugmentedIOSElement swipeVerticalWaitVisible(By swipeElement, By elementVisible,
                                                        int offset, int quantity, int durationInMilliSeconds) {
        Preconditions.checkNotNull(swipeElement);
        Preconditions.checkNotNull(elementVisible);

        WebElement element = MobileUtil.swipeVerticalWaitVisibleAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                swipeElement,
                elementVisible,
                waitTimeInSeconds,
                offset,
                quantity,
                durationInMilliSeconds);
        return augmentedIOSElementFactory.create(element);
    }

    @Override
    public void swipeUp(By swipeBy) {
        Preconditions.checkNotNull(swipeBy);

        MobileUtil.swipeUpAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                swipeBy,
                waitTimeInSeconds,
                pressTimeInMilliSeconds);
    }

    @Override
    public void swipeDown(By swipeBy) {
        Preconditions.checkNotNull(swipeBy);

        MobileUtil.swipeDownAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                swipeBy,
                waitTimeInSeconds,
                pressTimeInMilliSeconds);
    }

    @Override
    public void swipeVertical(By swipeBy, int offset, int durationInMilliSeconds) {
        Preconditions.checkNotNull(swipeBy);

        MobileUtil.swipeVerticalAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                swipeBy,
                waitTimeInSeconds,
                offset,
                durationInMilliSeconds);
    }


    @Override
    public void swipeFullRight(By by) {
        Preconditions.checkNotNull(by);

        swipeFullRightAfter(by, waitTimeInSeconds);
    }

    @Override
    public void swipeFullRightAfter(By by, int waitInSeconds) {
        Preconditions.checkNotNull(by);

        MobileUtil.swipeFullRightAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
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

        MobileUtil.swipeFullLeftAfter(augmentedIOSDriverProvider.get(),
                augmentedIOSDriverProvider.get().augmented(),
                by,
                waitInSeconds,
                pressTimeInMilliSeconds);
    }
}
