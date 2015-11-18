package com.salesforceiq.augmenteddriver.mobile.ios.pageobjects;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.salesforceiq.augmenteddriver.util.PageObjectAssertionsInterface;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSDriver;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSElement;
import com.salesforceiq.augmenteddriver.util.PageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for WebPages with a container.
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
    private IOSPageObjectActions IOSPageObjectActions;

    private AugmentedIOSElement container;

    @Override
    public <T extends IOSPageObject> T get(Class<T> clazz) {
        return IOSPageObjectActions.get(clazz);
    }

    @Override
    public <T extends IOSPageContainerObject> T get(Class<T> clazz, AugmentedIOSElement container) {
        return IOSPageObjectActions.get(clazz, container);
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

    /**
     * DO NOT USE
     */
    void setContainer(AugmentedIOSElement container) {
        this.container = container;
    }

    public AugmentedIOSElement container() {
        return container;
    }
}
