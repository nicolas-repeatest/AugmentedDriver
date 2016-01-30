package com.salesforceiq.augmenteddriver.reporters;

import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Listener that prints output that TeamCity can parse to determine the success/failure
 * of a test.
 */
public class TeamCityReporter extends RunListener {
	
    private static final String TEMPLATE_SUITE_STARTED = "##teamcity[testSuiteStarted name='%s']";
    private static final String TEMPLATE_SUITE_FINISHED = "##teamcity[testSuiteFinished name='%s']";
	    
    private static final String TEMPLATE_TEST_STARTED = "##teamcity[testStarted name='%s' captureStandardOutput='true']";
    private static final String TEMPLATE_TEST_FINISHED = "##teamcity[testFinished name='%s' duration='%s']";
    private static final String TEMPLATE_TEST_FAILURE = "##teamcity[testFailed name='%s' message='%s' details='%s']";
    private static final String TEMPLATE_TEST_IGNORED = "##teamcity[testIgnored name='%s' message='%s']";
    
    private final PrintStream output;
    private String currentTestClassName;
    private long startInMilliseconds;

    /**
     * Extensive Constructor.
     *
     * @param out where to print the output.
     */
    public TeamCityReporter(final OutputStream out) {
    	this.output = new PrintStream(Preconditions.checkNotNull(out));
    }

    @Override
    public void testStarted(Description description) {
        final String testClassName = description.getClassName();
        
        if (!testClassName.equals(currentTestClassName)) {
        	currentTestClassName = testClassName;       	
        	output.println(String.format(TEMPLATE_SUITE_STARTED, currentTestClassName));
        }
        
        output.println(String.format(TEMPLATE_TEST_STARTED, description.getMethodName()));
        this.startInMilliseconds = System.currentTimeMillis();
    }

    @Override
    public void testFinished(Description description) {
        String totalTime = String.valueOf(System.currentTimeMillis() - startInMilliseconds);
        output.println(String.format(TEMPLATE_TEST_FINISHED, description.getMethodName(), totalTime));
    }

    @Override
    public void testFailure(Failure failure) {
        if (!Strings.isNullOrEmpty(failure.getTrace())) {        	
        	output.print(failure.getTrace());
        }
        
        output.println(String.format(TEMPLATE_TEST_FAILURE, failure.getDescription().getMethodName(), failure.getMessage(), failure.getTrace()));
    }

    @Override
    public void testIgnored(Description description) {
    	Ignore ignore = description.getAnnotation(Ignore.class);
    	String ignoreReason = ignore == null ? "" : ignore.value();
    	
        output.println(String.format(TEMPLATE_TEST_IGNORED, description.getMethodName(), ignoreReason));
    }

    @Override
    public void testRunFinished(Result result) {    	
    	output.println(String.format(TEMPLATE_SUITE_FINISHED, currentTestClassName));
    }

}
