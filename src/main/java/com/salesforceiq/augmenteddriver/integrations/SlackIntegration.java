package com.salesforceiq.augmenteddriver.integrations;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * In charge of broadcasting via SLACK.
 */
@Singleton
public class SlackIntegration implements Integration {
    private static final Logger LOG = LoggerFactory.getLogger(SlackIntegration.class);

    private final boolean enabled;
    private final String slackVerboseChannel;
    private final String slackDigestChannel;
    private final String slackBotToken;
    private SlackChannel digestChannel;
    private SlackSession slackSession;
    private SlackChannel verboseChannel;

    @Inject
    public SlackIntegration(@Named(PropertiesModule.SLACK_INTEGRATION) String slackIntegration,
                            @Named(PropertiesModule.SLACK_BOT_TOKEN) String slackBotToken,
                            @Named(PropertiesModule.SLACK_DIGEST_CHANNEL) String slackDigestChannel,
                            @Named(PropertiesModule.SLACK_VERBOSE_CHANNEL) String slackVerboseChannel) {
        this.enabled = Boolean.valueOf(Preconditions.checkNotNull(slackIntegration));
        this.slackVerboseChannel = Preconditions.checkNotNull(slackVerboseChannel);
        this.slackDigestChannel = Preconditions.checkNotNull(slackDigestChannel);
        this.slackBotToken = Preconditions.checkNotNull(slackBotToken);
        if (enabled) {
            initializeSlackConnection();
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void failed(Description description, Throwable error, String sessionId) {
        if (verboseEnabled()) {
            slackSession
                    .sendMessage(verboseChannel, String.format("%s#%s FAILED", description.getClassName(), description.getMethodName()));
        }
    }

    public void passed(Description description, String sessionId) {
        if (verboseEnabled()) {
            slackSession
                    .sendMessage(verboseChannel, String.format("%s#%s SUCCEEDED", description.getClassName(), description.getMethodName()));
        }
    }

    /**
     * Initialization of slack connection and slack channels
     */
    private void initializeSlackConnection() {
        if (Strings.isNullOrEmpty(slackBotToken)) {
            LOG.warn("No Slack Bot Token, Slack Integration will not broadcast at all");
        } else {
            try {
                slackSession = SlackSessionFactory
                        .createWebSocketSlackSession(slackBotToken);
                slackSession.connect();
                if (Strings.isNullOrEmpty(slackVerboseChannel)) {
                    LOG.warn("No Slack Verbose Channel, Slack Integration will not broadcast success/failures");
                    verboseChannel = slackSession.findChannelByName(slackVerboseChannel);
                    if (verboseChannel == null) {
                        LOG.warn(String.format("Verbose Channel %s not found, Slack Integration will not broadcast success/failures"), slackVerboseChannel);
                    }
                }
                if (Strings.isNullOrEmpty(slackDigestChannel)) {
                    LOG.warn("No Slack Digest Channel, Slack Integration will not broadcast summaries");
                    digestChannel = slackSession.findChannelByName(slackDigestChannel);
                    if (digestChannel == null) {
                        LOG.warn(String.format("Digest Channel %s not found, Slack Integration will not broadcast summaries"), slackDigestChannel);
                    }
                }
            } catch (IOException e) {
                LOG.warn(String.format("Could not create session with token %s, Slack Integration will not broadcast at all", slackBotToken), e);
            }
        }
    }

    private boolean verboseEnabled() {
        return enabled && slackSession != null && verboseChannel != null;
    }

    private boolean digestEnabled() {
        return enabled && slackSession != null && digestChannel != null;
    }

}
