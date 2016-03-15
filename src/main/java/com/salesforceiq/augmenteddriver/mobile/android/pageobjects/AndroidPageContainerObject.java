package com.salesforceiq.augmenteddriver.mobile.android.pageobjects;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidDriver;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidElement;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidFunctions;
import com.salesforceiq.augmenteddriver.util.PageObject;
import com.salesforceiq.augmenteddriver.util.PageObjectAssertionsInterface;
import com.salesforceiq.augmenteddriver.util.PageObjectWaiter;

/**
 * Page Object for AndroidPages with a container.
 *
 * <p>
 *     Basically it is a helper so it is more convenient to follow the Page Object Pattern.
 *
 *     The getters initializes the Page Object using Guice for dependency injection.
 * </p>
 */
public abstract class AndroidPageContainerObject implements AndroidPageObjectActionsInterface, PageObjectAssertionsInterface, PageObject {

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
        return androidPageObjectActions.get(Preconditions.checkNotNull(clazz));
    }

    @Override
    public <T extends AndroidPageObject> T get(Class<T> clazz, Predicate<T> waitUntil) {
        return androidPageObjectActions.get(Preconditions.checkNotNull(clazz), Preconditions.checkNotNull(waitUntil));
    }

    @Override
    public <T extends AndroidPageContainerObject> T get(Class<T> clazz, AugmentedAndroidElement container) {
        return androidPageObjectActions.get(Preconditions.checkNotNull(clazz), Preconditions.checkNotNull(container));
    }

    @Override
    public <T extends AndroidPageContainerObject> T get(Class<T> clazz, AugmentedAndroidElement container, Predicate<T> waitUntil) {
        return androidPageObjectActions.get(Preconditions.checkNotNull(clazz),
                Preconditions.checkNotNull(container),
                Preconditions.checkNotNull(waitUntil));
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

    @Override
    public AugmentedAndroidFunctions augmented() {
        return driverProvider.get().augmented();
    }

    /**
     * DO NOT USE
     *
     * @param container the container to set.
     */
    void setContainer(AugmentedAndroidElement container) {
        this.container = container;
    }

    /**
     * @return the container used by the Page Object.
     */
    public AugmentedAndroidElement container() {
        return container;
    }

    @Override
    public PageObjectWaiter waiter() {
        return Preconditions.checkNotNull(androidPageObjectActions.waiter());
    }
}
