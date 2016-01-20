package com.salesforceiq.augmenteddriver.mobile.android.pageobjects;

import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidDriver;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidElement;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidFunctions;

/**
 * Common functionality to all AndroidPageObjects
 */
public interface AndroidPageObjectActionsInterface {

    /**
     * Initializes a AndroidPageObject instance.
     *
     * @param clazz the AndroidPageObject class to initialize.
     * @param <T> The type of the AndroidPageObject to return.
     * @return the PageObject represented by the input
     */
    <T extends AndroidPageObject> T get(Class<T> clazz);

    /**
     * Initializes a AndroidPageContainerObject instance.
     *
     * @param clazz the AndroidPageContainerObject class to initialize.
     * @param container the container element that is used as a root.
     * @param <T> The type of the AndroidPageContainerObject to return.
     * @return the PageContainerObject represented by the input
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
