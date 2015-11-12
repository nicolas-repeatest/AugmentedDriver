package com.salesforceiq.augmenteddriver.util;

import com.google.common.base.Preconditions;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MobileUtil {

    private static final int VERTICAL_OFFSET = 10;
    private static final int BIG_NUMBER = 9999999;
    private static final int DEFAULT_DURATION = 1000;

    public static WebElement swipeUpWaitVisible(AppiumDriver driver,
                                                AugmentedFunctions<?> augmentedFunctions,
                                                By swipeElement,
                                                By elementVisible) {
        return swipeVerticalWaitVisible(driver, augmentedFunctions, swipeElement,
                elementVisible, - BIG_NUMBER, 5, DEFAULT_DURATION);
    }

    public static WebElement swipeDownWaitVisible(AppiumDriver driver,
                                                AugmentedFunctions<?> augmentedFunctions,
                                                By swipeElement,
                                                By elementVisible) {
        return swipeVerticalWaitVisible(driver, augmentedFunctions, swipeElement,
                elementVisible, BIG_NUMBER, 5, DEFAULT_DURATION);
    }

    public static void swipeUp(AppiumDriver driver,
                               AugmentedFunctions<?> augmentedFunctions,
                               By swipeBy) {
        swipeVertical(driver, augmentedFunctions, swipeBy, - BIG_NUMBER, DEFAULT_DURATION);
    }

    public static void swipeDown(AppiumDriver driver,
                               AugmentedFunctions<?> augmentedFunctions,
                               By swipeBy) {
        swipeVertical(driver, augmentedFunctions, swipeBy, BIG_NUMBER, DEFAULT_DURATION);
    }


    public static void swipeVertical(AppiumDriver driver,
                                     AugmentedFunctions<?> augmentedFunctions,
                                     By swipeBy,
                                     int offset,
                                     int duration) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);
        Preconditions.checkNotNull(swipeBy);
        WebElement elementPresent = augmentedFunctions.findElementPresent(swipeBy);
        int x = elementPresent.getLocation().getX() + elementPresent.getSize().getWidth() / 2;
        int y = elementPresent.getLocation().getY() + elementPresent.getSize().getHeight() /  2;
        int swipe = getVerticalOffset(driver, y, offset);
        driver.swipe(x, y, x, swipe, duration);
    }

    public static WebElement swipeVerticalWaitVisible(AppiumDriver driver,
                                    AugmentedFunctions<?> augmentedFunctions,
                                    By swipeElement,
                                    By elementVisible,
                                    int offset,
                                    int quantity,
                                    int duration) {
        Preconditions.checkNotNull(driver);
        Preconditions.checkNotNull(augmentedFunctions);
        Preconditions.checkNotNull(swipeElement);
        WebElement elementPresent = augmentedFunctions.findElementPresent(swipeElement);
        int x = elementPresent.getLocation().getX() + elementPresent.getSize().getWidth() / 2;
        int y = elementPresent.getLocation().getY() + elementPresent.getSize().getHeight() /  2;

        int swipe = getVerticalOffset(driver, y, offset);

        for(int iteration = 0; iteration < quantity; iteration++) {
            driver.swipe(x, y, x, swipe, duration);
            if (augmentedFunctions.isElementVisibleAfter(elementVisible, 3)) {
                return augmentedFunctions.findElementVisible(elementVisible);
            }
        }
        throw new AssertionError(String.format("Swiped %s with an offest of %s times but element %s not found",
                        quantity, offset, elementVisible));
    }

    private static int getVerticalOffset(AppiumDriver driver, int y, int offset) {
        if (y + offset < VERTICAL_OFFSET) {
            return VERTICAL_OFFSET;
        } else if (y + offset > driver.manage().window().getSize().getHeight() - VERTICAL_OFFSET) {
            return driver.manage().window().getSize().getHeight() - VERTICAL_OFFSET;
        } else {
            return y + offset;
        }
    }
}
