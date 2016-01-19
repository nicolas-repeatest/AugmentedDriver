package com.salesforceiq.augmenteddriver.util.bys;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.openqa.selenium.By;

/**
 * Helper static class with handy methods for creating IOS Bys.
 */
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

    public static final By buttonWithName(String name) {
        return elementWithName("UIAButton", name);
    }

    public static final By staticTextWithName(String name) {
        return elementWithName("UIAStaticText", name);
    }

    public static final By textFieldWithName(String name) { return elementWithName("UIATextField", name);  }

    public static final By textFieldWithValue(String name) {
        return elementWithValue("UIATextField", name);
    }

    public static final By collectionCellWithName(String name) {
        return elementWithName("UIACollectionCell", name);
    }

    public static final By imageWithName(String name) {
        return elementWithName("UIAImage", name);
    }

    public static final By secureTextFieldWithName(String name) {
        return elementWithName("UIASecureTextField", name);
    }

    public static final By elementWithName(String name) {
        return elementWithName("UIAElement", name);
    }

    public static final By tableCellWithName(String name) {
        return elementWithName("UIATableCell", name);
    }

    public static final By tableGroupWithName(String name) {
        return elementWithName("UIATableGroup", name);
    }

    public static final By tableViewWithName(String name) {
        return elementWithName("UIATableView", name);
    }

    public static final By navigationBarWithName(String name) {
        return elementWithName("UIANavigationBar", name);
    }

    public static final By searchBarWithName(String name) {
        return elementWithName("UIASearchBar", name);
    }

    public static final By searchBarWithValue(String name) {
        return elementWithValue("UIASearchBar", name);
    }
}
