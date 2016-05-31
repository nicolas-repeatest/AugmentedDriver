package com.salesforceiq.augmenteddriver.integrations;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.runners.AugmentedResult;
import com.salesforceiq.augmenteddriver.slack.SlackCommon;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In charge of broadcasting via SLACK.
 */
@Singleton
public class SlackIntegration implements Integration, AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(SlackIntegration.class);

    private final boolean enabled;
    private final String slackVerboseChannel;
    private final String slackDigestChannel;
    private final String slackBotToken;

    @Inject
    public SlackIntegration(@Named(PropertiesModule.SLACK_INTEGRATION) String slackIntegration,
                            @Named(PropertiesModule.SLACK_BOT_TOKEN) String slackBotToken,
                            @Named(PropertiesModule.SLACK_DIGEST_CHANNEL) String slackDigestChannel,
                            @Named(PropertiesModule.SLACK_VERBOSE_CHANNEL) String slackVerboseChannel) {
        this.enabled = Boolean.valueOf(Preconditions.checkNotNull(slackIntegration));
        this.slackVerboseChannel = Preconditions.checkNotNull(slackVerboseChannel);
        this.slackDigestChannel = Preconditions.checkNotNull(slackDigestChannel);
        this.slackBotToken = Preconditions.checkNotNull(slackBotToken);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * It will send 2 attachments and one message:
     *
     * <ul>
     *     <li> The header, with the test name.</li>
     *     <li> The reason with the error message.</li>
     *     <li> A ``` code with the stacktrace.</li>
     * </ul>
     *
     * @param description the test description from JUnit.
     * @param error reason of the failure.
     * @param sessionId WebDriver session Id. (to link to saucelabs).
     */
    public void failed(Description description, Throwable error, String sessionId) {
        if (enabled && verboseEnabled()) {
            slackSession()
                    .sendMessage(verboseChannel(),
                            "",
                            createHeaderAttachment(description, false));

            String title = "Reason";
            String text = String.format(error.getMessage());
            SlackAttachment reason = new SlackAttachment(title, "", text, null);
            reason
                    .setColor("warning");
            slackSession()
                    .sendMessage(verboseChannel(),
                            "",
                            reason);

            StringBuilder exception = new StringBuilder();
            exception.append("```");
            Arrays.asList(error.getStackTrace())
                    .stream()
                    .forEach(stackTraceElement -> {
                        exception
                                .append(stackTraceElement.toString() + "\n");
                    });
            exception.append("```");
            slackSession()
                    .sendMessage(verboseChannel(),
                            exception.toString());
        }
    }

    /**
     * It will send one attachment:
     *
     * <ul>
     *     <li> The test that passed.</li>
     * </ul>
     *
     * @param description the test description from JUnit.
     * @param sessionId WebDriver session Id. (to link to saucelabs).
     */
    public void passed(Description description, String sessionId) {
        if (enabled && verboseEnabled()) {
            slackSession()
                    .sendMessage(verboseChannel(),
                            "",
                            createHeaderAttachment(description, true));
        }
    }

    /**
     * It will send on attachment with the title
     *
     * @param title the title
     */
    public void startDigest(String title) {
        if (enabled && digestEnabled()) {
            String attachmentTitle = "STARTED";

            SlackAttachment slackAttachment = new SlackAttachment(attachmentTitle, "", title, null);
            slackAttachment
                    .setColor("good");
            slackSession()
                    .sendMessage(digestChannel(),
                            "",
                            slackAttachment);
        }
    }

    /**
     * It will send one attachment with the summary and one attachment per test failure:
     *
     * <ul>
     *     <li> Summary will contain the title and how many tests total/passed/failed</li>
     *     <li> For each failure it will send and attachment with the test name</li>
     * </ul>
     *
     * @param title Title of the message.
     * @param results all the test results.
     */
    public void finishDigest(String title, List<AugmentedResult> results) {
        if (enabled && digestEnabled()) {
            List<AugmentedResult> failed = failedTests(results);

            String slackTitle = String.format("%s %s", title, failed.isEmpty() ? " SUCCEEDED" : " FAILED");
            String slackText = String.format("TOTAL: %s SUCCEEDED: %s FAILED %s",
                    results.size(),
                    results.size() - failed.size(),
                    failed.size());

            SlackAttachment slackAttachment = new SlackAttachment(slackTitle, "", slackText, null);
            slackAttachment.setColor(failed.isEmpty() ? "good" : "danger");
            slackSession()
                    .sendMessage(digestChannel(),
                            "",
                            slackAttachment);

            failed
                    .stream()
                    .forEach(failedTest -> {
                        String failedTestTitle = String.format("%s", failedTest.getTestName());
                        String failedTestText = failedTest.getResult().getFailures().get(0).getMessage();
                        SlackAttachment failedTestAttachment = new SlackAttachment(failedTestTitle, "", failedTestText, null);
                        failedTestAttachment
                                .setColor("warning");
                        slackSession()
                                .sendMessage(digestChannel(),
                                        "",
                                        failedTestAttachment);
                    });
        }
    }

    private SlackAttachment createHeaderAttachment(Description description, boolean succeeded) {
        String text = String.format("%s#%s", description.getClassName(), description.getMethodName());
        String title = succeeded? "SUCCEEDED" : "FAILED";

        SlackAttachment slackAttachment = new SlackAttachment(title, "", text, null);
        slackAttachment
                .setColor(succeeded? "good" : "danger");
        return slackAttachment;
    }

    @Override
    public void close() throws Exception {
        SlackCommon.close(false);
    }

    public void initialize() {
        SlackCommon.initialize(slackBotToken, slackVerboseChannel, slackDigestChannel, false);
    }

    private List<AugmentedResult> failedTests(List<AugmentedResult> results) {
        return results.stream()
                .filter(result -> !result.getResult().wasSuccessful())
                .collect(Collectors.toList());
    }

    private SlackSession slackSession() {
        return SlackCommon.slackSession();
    }

    private boolean verboseEnabled() {
        return SlackCommon.verboseEnabled();
    }

    private boolean digestEnabled() {
        return SlackCommon.digestEnabled();
    }

    private SlackChannel digestChannel() {
        return SlackCommon.digestChannel();
    }

    private SlackChannel verboseChannel() {
        return SlackCommon.verboseChannel();
    }
}
