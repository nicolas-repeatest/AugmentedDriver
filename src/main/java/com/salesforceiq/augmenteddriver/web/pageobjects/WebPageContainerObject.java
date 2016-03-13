package com.salesforceiq.augmenteddriver.web.pageobjects;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.salesforceiq.augmenteddriver.util.PageObject;
import com.salesforceiq.augmenteddriver.util.PageObjectAssertionsInterface;
import com.salesforceiq.augmenteddriver.web.AugmentedWebDriver;
import com.salesforceiq.augmenteddriver.web.AugmentedWebElement;
import com.salesforceiq.augmenteddriver.web.AugmentedWebFunctions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for WebPages with a container.
 *
 * <p>
 *     Basically it is a helper so it is more convenient to follow the Page Object Pattern.
 *
 *     The getters initializes the Page Object using Guice for dependency injection.
 * </p>
 */
public abstract class WebPageContainerObject implements  WebPageObjectActionsInterface, PageObjectAssertionsInterface, PageObject {
    private static final Logger LOG = LoggerFactory.getLogger(WebPageObject.class);

    /**
     * Important we use a Provider, since we need the driver to be initialized when the first test starts to run
     * not at creation time, like Guice wants.
     */
    @Inject
    private Provider<AugmentedWebDriver> driverProvider;

    @Inject
    private WebPageObjectActions webPageObjectActions;

    private AugmentedWebElement container;

    @Override
    public <T extends WebPageObject> T get(Class<T> clazz) {
        return webPageObjectActions.get(clazz);
    }

    @Override
    public <T extends WebPageObject> T get(Class<T> clazz, Predicate<T> waitUntil) {
        return webPageObjectActions.get(clazz, waitUntil);
    }

    @Override
    public <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container) {
        return webPageObjectActions.get(clazz, container);
    }

    @Override
    public <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container, Predicate<T> waitUntil) {
        return webPageObjectActions.get(clazz, container, waitUntil);
    }

    @Override
    public void assertPresent() {
        if (visibleBy().isPresent()) {
            container().augmented().findElementsVisible(visibleBy().get());
        }
    }

    @Override
    public AugmentedWebDriver driver() {
        return driverProvider.get();
    }

    @Override
    public AugmentedWebFunctions augmented() {
        return driverProvider.get().augmented();
    }

    /**
     * DO NOT USE
     *
     * @param container the container to set.
     */
    void setContainer(AugmentedWebElement container) {
        this.container = Preconditions.checkNotNull(container);
    }

    /**
     * @return the container used by the Web Page Object.
     */
    public AugmentedWebElement container() {
        return Preconditions.checkNotNull(container);
    }
}
