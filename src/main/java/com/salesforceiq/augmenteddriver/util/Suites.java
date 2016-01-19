package com.salesforceiq.augmenteddriver.util;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for defining Suites of Tests.
 *
 * TestSuiteRunner given a suite, will traverse all the tests and collect the ones that are annotated with
 * that suite.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Suites {
    String[] value();
}
