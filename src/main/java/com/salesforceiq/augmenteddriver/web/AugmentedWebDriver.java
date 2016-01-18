package com.salesforceiq.augmenteddriver.web;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Driver used for automation against web.
 *
 * <p>
 *     IMPORTANT: Not using Guice, since we would need to do a Singleton, and that
 *     reduces the performance (when trying to run tests on parallel on SauceLabs, for
 *     some reason, it only creates one at a time, and sometimes Sauce takes 40 seconds
 *     to create a new one.
 * </p>
 */
public class AugmentedWebDriver extends RemoteWebDriver {

    private AugmentedWebFunctions augmentedFunctions;

    /**
     * Extensive Constructor.
     *
     * @param remoteAddress Where the Selenium is running.
     * @param capabilities The capabilities to use.
     * @throws MalformedURLException In case the remoteAddress is now well formed.
     */
    public AugmentedWebDriver(String remoteAddress,
                              DesiredCapabilities capabilities) throws MalformedURLException {
        super(new URL(remoteAddress), capabilities);
    }

    /**
     * The augmented functions for the whole driver.
     *
     * @return the AugmentedWebFunctions.
     */
    public AugmentedWebFunctions augmented() {
        return augmentedFunctions;
    }

    /**
     * Sets the AugmentedFunction for the session.
     *
     * <p>
     *     SHOULD NOT BE USED OUTSIDE THE SETUP FOR THE BASE WEB TESTCASE.
     * </p>
     * @param augmentedFunctions the functions to use.
     */
    public void setAugmentedFunctions(AugmentedWebFunctions augmentedFunctions) {
        this.augmentedFunctions = augmentedFunctions;
    }
}
