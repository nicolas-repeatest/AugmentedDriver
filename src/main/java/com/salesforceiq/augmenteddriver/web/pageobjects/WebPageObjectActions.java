package com.salesforceiq.augmenteddriver.web.pageobjects;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.core.ConditionTimeoutException;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.web.AugmentedWebDriver;
import com.salesforceiq.augmenteddriver.web.AugmentedWebElement;
import com.salesforceiq.augmenteddriver.web.AugmentedWebFunctions;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Implementation based on Guice of WebPageObjectActionsInterface.
 */
public class WebPageObjectActions implements WebPageObjectActionsInterface {
    private static final Logger LOG = LoggerFactory.getLogger(WebPageObjectActions.class);

    @Inject
    private Injector injector;

    @Inject
    @Named(PropertiesModule.WAIT_IN_SECONDS)
    private String waitInSeconds;

    @Inject
    @Named(PropertiesModule.WAIT_BETWEEN_ITERATIONS_IN_MILLISECONDS)
    private String waitBetweenIterationsInMilliseconds;

    /**
     * Important we use a Provider, since we need the driver to be initialized when the first test starts to run
     * not at creation time, like Guice wants.
     */
    @Inject
    private Provider<AugmentedWebDriver> driverProvider;

    @Override
    public <T extends WebPageObject> T get(Class<T> clazz) {
        Preconditions.checkNotNull(clazz);
        T instance = injector.getInstance(clazz);
        try {
            instance.assertPresent();
        } catch (TimeoutException | AssertionError e) {
            LOG.error(String.format("Page Object %s, not found, message: %s", clazz.getCanonicalName(), e.getMessage()));
            throw e;
        }
        return instance;
    }

    @Override
    public <T extends WebPageObject> T get(Class<T> clazz, Predicate<T> waitUntil) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotNull(waitUntil);

        T result = get(clazz);
        try {
            Awaitility.await()
                    .atMost(Integer.valueOf(waitInSeconds), TimeUnit.SECONDS)
                    .pollDelay(Integer.valueOf(waitBetweenIterationsInMilliseconds), TimeUnit.MILLISECONDS)
                    .until(() -> waitUntil.apply(result));
        } catch (ConditionTimeoutException e) {
            LOG.error(String.format("Condition not fulfilled for Page Object %s", clazz.getCanonicalName()));
        }
        return result;
    }

    @Override
    public <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotNull(container);

        T instance = injector.getInstance(clazz);
        instance.setContainer(container);
        try {
            instance.assertPresent();
        } catch (TimeoutException | AssertionError e) {
            LOG.error(String.format("Page Object Container %s, not found, message: %s", clazz.getCanonicalName(), e.getMessage()));
            throw e;
        }
        return instance;
    }


    @Override
    public <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container, Predicate<T> waitUntil) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotNull(container);
        Preconditions.checkNotNull(waitUntil);

        T result = get(clazz, container);
        try {
            Awaitility.await()
                    .atMost(Integer.valueOf(waitInSeconds), TimeUnit.SECONDS)
                    .pollDelay(Integer.valueOf(waitBetweenIterationsInMilliseconds), TimeUnit.MILLISECONDS)
                    .until(() -> waitUntil.apply(result));
        } catch (ConditionTimeoutException e) {
            LOG.error(String.format("Condition not fulfilled for Page Object Container %s", clazz.getCanonicalName()));
        }
        return result;
    }

    @Override
    public AugmentedWebDriver driver() {
        Preconditions.checkNotNull(driverProvider);

        return Preconditions.checkNotNull(driverProvider.get());
    }

    @Override
    public AugmentedWebFunctions augmented() {
        Preconditions.checkNotNull(driverProvider);
        Preconditions.checkNotNull(driverProvider.get());

        return Preconditions.checkNotNull(driverProvider.get().augmented());
    }
}
