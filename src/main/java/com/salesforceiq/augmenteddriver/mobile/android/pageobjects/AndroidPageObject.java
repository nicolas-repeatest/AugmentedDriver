package com.salesforceiq.augmenteddriver.mobile.android.pageobjects;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidDriver;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidElement;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidFunctions;
import com.salesforceiq.augmenteddriver.util.PageObject;
import com.salesforceiq.augmenteddriver.util.PageObjectAssertionsInterface;

/**
 * Page Object for AndroidPages.
 *
 * <p>
 *     Basically it is a helper so it is more convenient to follow the Page Object Pattern.
 *
 *     The getters initializes the Page Object using Guice for dependency injection.
 * </p>
 */
public abstract class AndroidPageObject implements AndroidPageObjectActionsInterface, PageObjectAssertionsInterface, PageObject {

    /**
     * Important we use a Provider, since we need the driver to be initialized when the first test starts to run
     * not at creation time, like Guice wants.
     */
    @Inject
    private Provider<AugmentedAndroidDriver> driverProvider;

    @Inject
    private AndroidPageObjectActions androidPageObjectActions;

    @Override
    public <T extends AndroidPageObject> T get(Class<T> clazz) {
        return androidPageObjectActions.get(Preconditions.checkNotNull(clazz));
    }

    @Override
    public <T extends AndroidPageContainerObject> T get(Class<T> clazz, AugmentedAndroidElement container) {
        return androidPageObjectActions.get(Preconditions.checkNotNull(clazz), Preconditions.checkNotNull(container));
    }

    @Override
    public void assertPresent() {
        if (visibleBy().isPresent()) {
            augmented().findElementVisible(visibleBy().get());
        }
    }

    @Override
    public AugmentedAndroidDriver driver() {
        return driverProvider.get();
    }

    @Override
    public AugmentedAndroidFunctions augmented() {
        return driverProvider.get().augmented();
    }

}
