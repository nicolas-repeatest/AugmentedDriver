package com.salesforceiq.augmenteddriver.mobile.android.pageobjects;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.salesforceiq.augmenteddriver.util.PageObjectAssertionsInterface;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidDriver;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidElement;
import com.salesforceiq.augmenteddriver.util.PageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for WebPages with a container.
 */
public abstract class AndroidPageContainerObject implements AndroidPageObjectActionsInterface, PageObjectAssertionsInterface, PageObject {
    private static final Logger LOG = LoggerFactory.getLogger(AndroidPageContainerObject.class);

    /**
     * Important we use a Provider, since we need the driver to be initialized when the first test starts to run
     * not at creation time, like Guice wants.
     */
    @Inject
    private Provider<AugmentedAndroidDriver> driverProvider;

    @Inject
    private AndroidPageObjectActions androidPageObjectActions;

    private AugmentedAndroidElement container;

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
            container().augmented().findElementsVisible(visibleBy().get());
        }
    }

    @Override
    public AugmentedAndroidDriver driver() {
        return driverProvider.get();
    }

    /**
     * DO NOT USE
     */
    void setContainer(AugmentedAndroidElement container) {
        this.container = container;
    }

    public AugmentedAndroidElement container() {
        return container;
    }
}
