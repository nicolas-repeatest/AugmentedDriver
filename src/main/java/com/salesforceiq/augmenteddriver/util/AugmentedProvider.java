package com.salesforceiq.augmenteddriver.util;

import com.google.inject.Provider;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Provider extension that also allows initializing it
 *
 * (since we cannot create it at Guice init time, since the driver needs to be created,
 * and that is at setUp time.
 */
public interface AugmentedProvider<T extends RemoteWebDriver> extends Provider<T> {
    /**
     * Sets the driver that will be used for this test.
     *
     * <p>
     *     SHOULD NOT BE CALLED OUTSIDE THE SETUP FOR THE BASE TESTCASES.
     * </p>
     * @param driver the driver to set.
     */
    void initialize(T driver);

    /**
     * @return Whether the provider has been initialized or not.
     */
    boolean isInitialized();
}
