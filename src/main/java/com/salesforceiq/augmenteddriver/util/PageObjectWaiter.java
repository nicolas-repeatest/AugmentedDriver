package com.salesforceiq.augmenteddriver.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Waits until a condition on a PageObject is true, or times out.
 */
public class PageObjectWaiter {
    private static final Logger LOG = LoggerFactory.getLogger(PageObjectWaiter.class);

    private int waitInSeconds;

    private int waitBetweenIterationsInMilliseconds;


    @Inject
    public PageObjectWaiter(@Named(PropertiesModule.WAIT_IN_SECONDS) String waitInSeconds,
                            @Named(PropertiesModule.WAIT_BETWEEN_ITERATIONS_IN_MILLISECONDS) String waitBetweenIterationsInMilliseconds) {
        this.waitInSeconds = Integer.valueOf(Preconditions.checkNotNull(waitInSeconds));
        this.waitBetweenIterationsInMilliseconds = Integer.valueOf(Preconditions.checkNotNull(waitBetweenIterationsInMilliseconds));
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

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, waitInSeconds);
        long limit = calendar.getTimeInMillis();
        while (System.currentTimeMillis() < limit) {
            if (waitUntil.apply(entity)) {
                LOG.info("Condition applied");
                return;
            } else {
                LOG.info("Condition did not apply");
                Util.pause(waitBetweenIterationsInMilliseconds);
            }
        }
        throw new AssertionError(errorMessage);
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

        waitUntil(entity, waitUntil, String.format("Waited %s seconds and condition did not meet", waitInSeconds));
    }
}
