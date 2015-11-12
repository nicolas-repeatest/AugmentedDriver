package com.salesforceiq.augmenteddriver.util;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Suites {
    String[] value();
}
