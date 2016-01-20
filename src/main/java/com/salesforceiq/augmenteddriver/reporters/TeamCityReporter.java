package com.salesforceiq.augmenteddriver.reporters;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Listener that prints output that TeamCity can parse to determine the success/failure
 * of a test.
 */
public class TeamCityReporter extends RunListener {
    private final PrintStream out;
    private final String testNameAppender;
    private String currentTestClassName = null;
    private long startInMilliseconds;

    /**
     * Extensive Constructor.
     *
     * @param out where to print the output.
     * @param testNameAppender Can be empty but not null.
     */
    public TeamCityReporter(final OutputStream out, String testNameAppender) {
        Preconditions.checkNotNull(out);
        Preconditions.checkNotNull(testNameAppender);

        this.testNameAppender = testNameAppender;
        this.out = new PrintStream(out);
        this.currentTestClassName = null;
    }

    @Override
    public void testStarted(Description description) {
        final String testClassName = getTestClassName(description);
        final String testName = getTestName(description, testNameAppender);

        if (currentTestClassName == null || !currentTestClassName.equals(testClassName)) {
            if (currentTestClassName != null) {
                out.println(String.format("##teamcity[testSuiteFinished name='%s']", currentTestClassName));
            }
            this.out.println(String.format("##teamcity[testSuiteStarted name='%s']", testClassName));
            currentTestClassName = testClassName;
        }
        this.startInMilliseconds = System.currentTimeMillis();
        out.println(String.format("##teamcity[testStarted name='%s' captureStandardOutput='true']", testName));
    }

    @Override
    public void testFinished(Description description) {
        final String testName = getTestName(description, testNameAppender);

        out.println(String.format("##teamcity[testFinished name='%s' duration='%s']",
                testName, String.valueOf(System.currentTimeMillis() - startInMilliseconds)));
    }

    @Override
    public void testFailure(Failure failure) {
        if (failure.getTrace() != null && !failure.getTrace().isEmpty())
            out.print(failure.getTrace());
        out.println(String.format("##teamcity[testFailed name='%s' message='%s' details='%s']",
                getTestName(failure.getDescription(), testNameAppender),
                "failed",
                ""));
    }

    @Override
    public void testIgnored(Description description) {
        out.println(String.format("##teamcity[testIgnored name='%s' message='%s']",
                getTestName(description, testNameAppender),
                ""));
    }

    @Override
    public void testRunFinished(Result result) {
        if (currentTestClassName != null) {
            out.println(String.format("##teamcity[testSuiteFinished name='%s']", currentTestClassName));
        }
    }

    private String getTestClassName(final Description description) {
        return description.getTestClass().getName();
    }

    private String getTestName(final Description description, String testNameAppender) {
        return description.getMethodName() + ((Strings.isNullOrEmpty(testNameAppender)) ? "" : "-" + testNameAppender);
    }

}