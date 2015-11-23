package com.salesforceiq.augmenteddriver.mobile.android;

import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSFunctions;
import org.openqa.selenium.SearchContext;

public interface AugmentedAndroidFunctionsFactory {
    AugmentedAndroidFunctions create(SearchContext context);
}
