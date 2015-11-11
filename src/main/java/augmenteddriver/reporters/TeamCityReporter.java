package augmenteddriver.reporters;

import com.google.common.base.Strings;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.OutputStream;
import java.io.PrintStream;

import static com.google.common.base.Preconditions.checkArgument;

public class TeamCityReporter extends RunListener {
    private final PrintStream out;
    private final String testNameAppender;
    private long startInMilliseconds;

    public TeamCityReporter(OutputStream out, String testNameAppender) {
        checkArgument(out != null, "out must not be null");
        this.testNameAppender = testNameAppender;
        this.out = new PrintStream(out);
    }

    @Override
    public void testStarted(Description description) {
        final String testName = getTestName(description, testNameAppender);

        this.startInMilliseconds = System.currentTimeMillis();
        out.println(String.format("##teamcity[testStarted name='%s' captureStandardOutput='true']", testName));
    }

    @Override
    public void testFinished(Description description) {
        final String testName = getTestName(description, testNameAppender);

        out.println(String.format("##teamcity[testFinished name='%s' duration='%s']",
                testName, String.valueOf(System.currentTimeMillis() - startInMilliseconds)));
        out.close();
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
    }
    
    private String getTestName(final Description description, String testNameAppender) {
        return description.getMethodName() + ((Strings.isNullOrEmpty(testNameAppender)) ? "" : "-" + testNameAppender);
    }
}
