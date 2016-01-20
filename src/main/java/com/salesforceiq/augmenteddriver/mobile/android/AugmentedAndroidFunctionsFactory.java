package com.salesforceiq.augmenteddriver.mobile.android;

import org.openqa.selenium.SearchContext;

/**
 * Guice Factory.
 */
public interface AugmentedAndroidFunctionsFactory {
    /**
     * Creates an AugmentedAndroidFunctions based on a context (Driver or WebElement).
     *
     * @param context the context to use.
     * @return the AugmentedAndroidFunctions.
     */
    AugmentedAndroidFunctions create(SearchContext context);
}
