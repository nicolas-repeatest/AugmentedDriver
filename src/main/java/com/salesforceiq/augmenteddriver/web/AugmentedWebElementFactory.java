package com.salesforceiq.augmenteddriver.web;

import org.openqa.selenium.WebElement;

public interface AugmentedWebElementFactory {
    AugmentedWebElement create(WebElement webElement);
}
