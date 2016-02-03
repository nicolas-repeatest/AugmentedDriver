package com.salesforceiq.augmenteddriver.mobile.ios;

import io.appium.java_client.ios.IOSDriver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Driver used for automation against IOS.
 *
 * <p>
 *     IMPORTANT: Not using Guice, since we would need to do a Singleton, and that
 *     reduces the performance (when trying to run tests on parallel on SauceLabs, for
 *     some reason, it only creates one at a time, and sometimes Sauce takes 40 seconds
 *     to create a new one.
 * </p>
 */
public class AugmentedIOSDriver extends IOSDriver<WebElement> {
    private AugmentedIOSFunctions augmentedFunctions;

    /**
     * Extensive constructor.
     *
     * @param remoteAddress Where the Selenium is running.
     * @param capabilities The capabilities to use.
     * @param augmentedFunctions the extra functionality for IOS.
     * @throws MalformedURLException In case the remoteAddress is now well formed.
     */
    public AugmentedIOSDriver(String remoteAddress,
                              DesiredCapabilities capabilities,
                              AugmentedIOSFunctions augmentedFunctions) throws MalformedURLException {
        super(new URL(remoteAddress), capabilities);
        this.augmentedFunctions = augmentedFunctions;
    }

    /**
     * @return the augmented functionality on top of the driver.
     */
    public AugmentedIOSFunctions augmented() {
        return augmentedFunctions;
    }

    /**
     * DO NOT USE.
     *
     * @param augmentedFunctions the functions to set on top of the driver.
     */
    public void setAugmentedFunctions(AugmentedIOSFunctions augmentedFunctions) {
        this.augmentedFunctions = augmentedFunctions;
    }
}
