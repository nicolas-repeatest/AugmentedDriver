package com.salesforceiq.augmenteddriver.mobile.ios.pageobjects;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSDriver;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSElement;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSFunctions;
import com.salesforceiq.augmenteddriver.util.PageObject;
import com.salesforceiq.augmenteddriver.util.PageObjectAssertionsInterface;
import com.salesforceiq.augmenteddriver.util.PageObjectWaiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for IOSPages with a container.
 *
 * <p>
 *     Basically it is a helper so it is more convenient to follow the Page Object Pattern.
 *
 *     The getters initializes the Page Object using Guice for dependency injection.
 * </p>
 */
public abstract class IOSPageContainerObject implements IOSPageObjectActionsInterface, PageObjectAssertionsInterface, PageObject {
    private static final Logger LOG = LoggerFactory.getLogger(IOSPageContainerObject.class);

    /**
     * Important we use a Provider, since we need the driver to be initialized when the first test starts to run
     * not at creation time, like Guice wants.
     */
    @Inject
    private Provider<AugmentedIOSDriver> driverProvider;

    @Inject
    private IOSPageObjectActions iosPageObjectActions;

    private AugmentedIOSElement container;

    @Override
    public <T extends IOSPageObject> T get(Class<T> clazz) {
        return iosPageObjectActions.get(Preconditions.checkNotNull(clazz));
    }

    @Override
    public <T extends IOSPageObject> T get(Class<T> clazz, Predicate<T> waitUntil) {
        return iosPageObjectActions.get(Preconditions.checkNotNull(clazz), Preconditions.checkNotNull(waitUntil));
    }

    @Override
    public <T extends IOSPageContainerObject> T get(Class<T> clazz, AugmentedIOSElement container) {
        return iosPageObjectActions.get(Preconditions.checkNotNull(clazz),
                Preconditions.checkNotNull(container));
    }

    @Override
    public <T extends IOSPageContainerObject> T get(Class<T> clazz, AugmentedIOSElement container, Predicate<T> waitUntil) {
        return iosPageObjectActions.get(Preconditions.checkNotNull(clazz),
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
    public AugmentedIOSDriver driver() {
        return driverProvider.get();
    }

    @Override
    public AugmentedIOSFunctions augmented() {
        return driverProvider.get().augmented();
    }

    /**
     * DO NOT USE
     *
     * @param container the container to set.
     */
    void setContainer(AugmentedIOSElement container) {
        this.container = Preconditions.checkNotNull(container);
    }

    /**
     * @return the container used by the Page Object.
     */
    public AugmentedIOSElement container() {
        return Preconditions.checkNotNull(container);
    }

    @Override
    public PageObjectWaiter waiter() {
        return Preconditions.checkNotNull(iosPageObjectActions.waiter());
    }
}
