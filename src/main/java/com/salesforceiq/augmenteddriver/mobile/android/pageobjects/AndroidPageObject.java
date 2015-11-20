package com.salesforceiq.augmenteddriver.mobile.android.pageobjects;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidFunctions;
import com.salesforceiq.augmenteddriver.util.PageObjectAssertionsInterface;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidDriver;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidElement;
import com.salesforceiq.augmenteddriver.util.PageObject;
import com.salesforceiq.augmenteddriver.web.AugmentedWebFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for WebPages with a container.
 */
public abstract class AndroidPageObject implements AndroidPageObjectActionsInterface, PageObjectAssertionsInterface, PageObject {
    private static final Logger LOG = LoggerFactory.getLogger(AndroidPageObject.class);

    @Inject
    private Provider<AugmentedAndroidDriver> driverProvider;

    @Inject
    private AndroidPageObjectActions androidPageObjectActions;

    @Override
    public <T extends AndroidPageObject> T get(Class<T> clazz) {
        return androidPageObjectActions.get(clazz);
    }

    @Override
    public <T extends AndroidPageContainerObject> T get(Class<T> clazz, AugmentedAndroidElement container) {
        return androidPageObjectActions.get(clazz, container);
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
