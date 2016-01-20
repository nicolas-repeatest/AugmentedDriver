package com.salesforceiq.augmenteddriver.util.bys;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.openqa.selenium.By;

/**
 * Helper static class with handy methods for creating Android Bys.
 */
public class AndroidBys {

    public static final By elementWithContentDesc(String element, String contentDescription) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(element));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(contentDescription));

        return By.xpath(String.format(".//%s[@content-desc='%s']", element, contentDescription));
    }

    public static final By editTextWithContentDesc(String contentDescription) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(contentDescription));

        return elementWithContentDesc("android.widget.EditText", contentDescription);
    }

    public static final By elementContainsText(String element, String name) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(element));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name));

        return By.xpath(String.format(".//%s[contains(@text, '%s')]", element, name));
    }

    public static final By elementWithText(String element, String name) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(element));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name));

        return By.xpath(String.format(".//%s[@text='%s']", element, name));
    }

    public static final By textViewWithText(String text) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));

        return elementWithText("android.widget.TextView", text);
    }

    public static final By textViewWithContentDesc(String contentDesc) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(contentDesc));

        return elementWithContentDesc("android.widget.TextView", contentDesc);
    }

    public static final By imageButtonWithContentDesc(String contentDesc) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(contentDesc));

        return elementWithContentDesc("android.widget.ImageButton", contentDesc);
    }

    public static final By textViewContainsText(String text) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));

        return elementContainsText("android.widget.TextView", text);
    }
}
