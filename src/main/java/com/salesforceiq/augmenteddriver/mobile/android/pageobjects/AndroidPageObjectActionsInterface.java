package com.salesforceiq.augmenteddriver.mobile.android.pageobjects;

import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidDriver;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidElement;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidFunctions;

/**
 * Common functionality to all AndroidPageObjects
 */
public interface AndroidPageObjectActionsInterface {
    /**
     * returns the PageObject represented by the input
     */
    <T extends AndroidPageObject> T get(Class<T> clazz);

    /**
     * returns the PageContainerObject represented by the input
     */
    <T extends AndroidPageContainerObject> T get(Class<T> clazz, AugmentedAndroidElement container);

    /**
     * @return the AugmentedAndroidDriver to use.
     */
    AugmentedAndroidDriver driver();

    /**
     * @return the Augmented Android Functions to use.
     */
    AugmentedAndroidFunctions augmented();
}
