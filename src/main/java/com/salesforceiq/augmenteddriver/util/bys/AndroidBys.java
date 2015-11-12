package com.salesforceiq.augmenteddriver.util.bys;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.openqa.selenium.By;

public class AndroidBys {

    public static final By elementWithContentDescription(String element, String contentDescription) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(element));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(contentDescription));
        return By.xpath(String.format(".//%s[@content-desc='%s']", element, contentDescription));
    }

    public static final By editTextWithContentDescription(String contentDescription) {
        return elementWithContentDescription("android.widget.EditText", contentDescription);
    }
}
