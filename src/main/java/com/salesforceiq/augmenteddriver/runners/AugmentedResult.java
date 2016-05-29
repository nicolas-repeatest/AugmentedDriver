package com.salesforceiq.augmenteddriver.runners;

import org.junit.runner.Result;

import java.io.ByteArrayOutputStream;

/**
 * Wrapper o a result that also contains the output stream.
 */
public class AugmentedResult {

    private final Result result;
    private final ByteArrayOutputStream out;
    private final String testName;

    public AugmentedResult(String testName, Result result, ByteArrayOutputStream out) {
        this.result = result;
        this.testName = testName;
        this.out = out;
    }

    public Result getResult() {
        return result;
    }

    public String getTestName() {
        return testName;
    }

    public ByteArrayOutputStream getOut() {
        return out;
    }
}
