package com.salesforceiq.augmenteddriver.util;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import org.openqa.selenium.StaleElementReferenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

/**
 * Waits until a condition on a PageObject is true, or times out.
 */
public class PageObjectWaiter {
    private static final Logger LOG = LoggerFactory.getLogger(PageObjectWaiter.class);
    private final List<Class<?>> ignoreExceptions;

    private int waitInSeconds;

    private int waitBetweenIterationsInMilliseconds;


    @Inject
    public PageObjectWaiter(@Named(PropertiesModule.WAIT_IN_SECONDS) String waitInSeconds,
                            @Named(PropertiesModule.WAIT_BETWEEN_ITERATIONS_IN_MILLISECONDS) String waitBetweenIterationsInMilliseconds) {
        this.waitInSeconds = Integer.valueOf(Preconditions.checkNotNull(waitInSeconds));
        this.waitBetweenIterationsInMilliseconds = Integer.valueOf(Preconditions.checkNotNull(waitBetweenIterationsInMilliseconds));
        this.ignoreExceptions = Lists.newArrayList();
        this.ignoreExceptions.add(StaleElementReferenceException.class);
    }

    /**
     * Waits until the Predicate on the Page Object entity is true, times out in waitInSeconds.
     *
     * @param entity what Page object is going to be applied to the predicate.
     * @param waitUntil the predicate to check if it is fulfilled.
     * @param errorMessage what to print if it times out
     * @param <T> the type of the Page Object.
     */
    public <T extends PageObject> void waitUntil(T entity, Predicate<T> waitUntil, String errorMessage) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(waitUntil);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorMessage));

        waitUntilAfter(entity, waitUntil, errorMessage, waitInSeconds);
    }

    /**
     * Waits until the Predicate on the Page Object entity is true, times out in waitInSeconds.
     *
     * @param entity what Page object is going to be applied to the predicate.
     * @param waitUntil the predicate to check if it is fulfilled.
     * @param <T> the type of the Page Object.
     */
    public <T extends PageObject> void waitUntil(T entity, Predicate<T> waitUntil) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(waitUntil);

        waitUntilAfter(entity, waitUntil,
                String.format("Waited %s seconds and condition did not meet", waitInSeconds), waitInSeconds);
    }

    /**
     * Waits until the Predicate on the Page Object entity is true, times out in waitInSeconds.
     *
     * @param entity what Page object is going to be applied to the predicate.
     * @param waitTimeInSeconds How much time to wait
     * @param waitUntil the predicate to check if it is fulfilled.
     * @param <T> the type of the Page Object.
     */
    public <T extends PageObject> void waitUntilAfter(T entity, Predicate<T> waitUntil, int waitTimeInSeconds) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(waitUntil);

        waitUntilAfter(entity, waitUntil,
                String.format("Waited %s seconds and condition did not meet", waitTimeInSeconds), waitTimeInSeconds);
    }


    /**
     * Waits until the Predicate on the Page Object entity is true, times out in waitTimeInSeconds.
     *
     * @param entity what Page object is going to be applied to the predicate.
     * @param waitTimeInSeconds How much time to wait
     * @param waitUntil the predicate to check if it is fulfilled.
     * @param errorMessage what to print if it times out
     * @param <T> the type of the Page Object.
     */
    public <T extends PageObject> void waitUntilAfter(T entity, Predicate<T> waitUntil,
                                                            String errorMessage, int waitTimeInSeconds) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(waitUntil);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorMessage));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, waitTimeInSeconds);
        long limit = calendar.getTimeInMillis();
        while (System.currentTimeMillis() < limit) {
            try {
                if (waitUntil.apply(entity)) {
                    return;
                } else {
                    Util.pause(waitBetweenIterationsInMilliseconds);
                }
            } catch (Throwable e) {
                if (ignoreExceptions.contains(e.getClass())) {
                    Util.pause(waitBetweenIterationsInMilliseconds);
                } else {
                    throw e;
                }
            }
        }
        throw new AssertionError(errorMessage);
    }
}
