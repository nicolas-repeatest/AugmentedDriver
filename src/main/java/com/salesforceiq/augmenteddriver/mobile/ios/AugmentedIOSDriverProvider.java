package com.salesforceiq.augmenteddriver.mobile.ios;

import com.google.common.base.Preconditions;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.salesforceiq.augmenteddriver.util.AugmentedProvider;

/**
 * Ugly hack
 *
 * Reasoning:
 * <p>
 *     We cannot define the driver as Singleton for performance reasons. One driver is created synchronously after the other.
 *     When we run in parallel, all the tests queue up, and for example, an android takes 50 seconds to be created on SLabs.
 * </p>
 * <p>
 *     We create the first Driver at the beginning of the set up and we assign it with the setter here. Since this class
 *     is Singleton, all the other classes gets this provider, and since the very first thing the test does on the setup
 *     is to set the driver her, all the rests providers will get this one.
 * </p>
 */
@Singleton
public class AugmentedIOSDriverProvider implements AugmentedProvider<AugmentedIOSDriver> {

    private AugmentedIOSDriver driver;

    @Override
    public AugmentedIOSDriver get() {
        return Preconditions.checkNotNull(driver, "AugmentedIOSDriver not initialized, call initialize first");
    }

    @Override
    public void initialize(AugmentedIOSDriver driver) {
        this.driver = driver;
    }

    @Override
    public boolean isInitialized() {
        return driver != null;
    }
}