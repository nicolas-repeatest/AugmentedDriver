package com.salesforceiq.augmenteddriver.web;

import org.openqa.selenium.SearchContext;

public interface AugmentedWebFunctionsFactory {
    AugmentedWebFunctions create(SearchContext context);
}
