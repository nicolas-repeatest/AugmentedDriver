package com.salesforceiq.augmenteddriver.runners;

import java.lang.reflect.Method;

/**
 * Guice factory for creating TestRunners.
 */
public interface TestRunnerFactory {
    /**
     * Creates a TestRunner that knows how to run a test.
     *
     * @param test the test to run.
     * @param nameAppender String to append to the test name.
     * @return a Runner that can run a particular test.
     */
    TestRunner create(Method test, String nameAppender);
}