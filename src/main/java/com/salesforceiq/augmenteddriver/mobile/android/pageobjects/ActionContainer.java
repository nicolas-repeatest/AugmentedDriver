package com.salesforceiq.augmenteddriver.mobile.android.pageobjects;

import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidDriver;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidElement;

/**
 * Generic interface for executing an action in a container.
 */
public interface ActionContainer {
    void execute(AugmentedAndroidDriver driver, AugmentedAndroidElement container);
}
