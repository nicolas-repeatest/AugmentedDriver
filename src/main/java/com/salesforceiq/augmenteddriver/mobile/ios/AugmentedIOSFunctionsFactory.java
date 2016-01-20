package com.salesforceiq.augmenteddriver.mobile.ios;

import org.openqa.selenium.SearchContext;

/**
 * Guice Factory.
 */
public interface AugmentedIOSFunctionsFactory {

    /**
     * Creates an AugmentedIOSFunctions based on a context (Driver or WebElement).
     *
     * @param context the context to use.
     * @return the AugmentedIOSFunctions.
     */
    AugmentedIOSFunctions create(SearchContext context);
}
