package com.salesforceiq.augmenteddriver.util.bys;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.openqa.selenium.By;

public class IOSBys {

    public static final By elementWithName(String element, String name) {
        return elementWithAttribute(element, "name", name);
    }

    public static final By elementWithValue(String element, String value) {
        return elementWithAttribute(element, "value", value);
    }

    public static final By elementWithAttribute(String element, String attributeName, String attributeValue) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(element));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(attributeName));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(attributeValue));
        return By.xpath(String.format(".//%s[@%s='%s']", element, attributeName, attributeValue));
    }

    public static final By tableCellWithName(String name) {
        return elementWithName("UIATableCell", name);
    }

    public static final By buttonWithName(String name) {
        return elementWithName("UIAButton", name);
    }

    public static final By textFieldWithValue(String value) {
        return elementWithValue("UIATextField", value);
    }
}
