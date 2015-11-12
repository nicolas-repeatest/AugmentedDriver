package com.salesforceiq.augmenteddriver.mobile.ios.pageobjects;

import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSDriver;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSElement;

/**
 * Generic interface for executing an action in a container.
 */
public interface ActionContainer {
    void execute(AugmentedIOSDriver driver, AugmentedIOSElement container);
}
