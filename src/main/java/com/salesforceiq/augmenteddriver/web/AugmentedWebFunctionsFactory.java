package com.salesforceiq.augmenteddriver.web;

import org.openqa.selenium.SearchContext;

/**
 * Factory used by Guice to create AugmentedWebFunctions.
 */
public interface AugmentedWebFunctionsFactory {

    /**
     * Creates a AugmentedWebFunction based on a SearchContext.
     *
     * @param context the Selenium Context.
     * @return the AugmentedWebFunction.
     */
    AugmentedWebFunctions create(SearchContext context);
}
