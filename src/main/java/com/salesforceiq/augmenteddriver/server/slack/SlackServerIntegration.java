package com.salesforceiq.augmenteddriver.server.slack;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.integrations.SlackIntegration;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.slack.SlackCommon;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Created on 5/30/16.
 */
@Singleton
public class SlackServerIntegration implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(SlackServerIntegration.class);

    private final SlackCommandListener commandListener;
    private final Provider<RunSuiteCommand> runSuiteCommandProvider;
    private final Boolean enabled;
    private final String slackDigestChannel;
    private final String slackBotToken;
    private final String slackVerboseChannel;

    @Inject
    public SlackServerIntegration(
            @Named(PropertiesModule.SLACK_BOT_TOKEN) String slackBotToken,
            @Named(PropertiesModule.SLACK_INTEGRATION) String slackIntegration,
            SlackCommandListener commandListener,
            Provider<RunSuiteCommand> runSuiteCommandProvider,
            @Named(PropertiesModule.SLACK_VERBOSE_CHANNEL) String slackVerboseChannel,
            @Named(PropertiesModule.SLACK_DIGEST_CHANNEL) String slackDigestChannel) {
        this.enabled = Preconditions.checkNotNull(Boolean.valueOf(slackIntegration));
        this.slackBotToken = Preconditions.checkNotNull(slackBotToken);
        this.commandListener = Preconditions.checkNotNull(commandListener);
        this.runSuiteCommandProvider = Preconditions.checkNotNull(runSuiteCommandProvider);
        this.slackDigestChannel = Preconditions.checkNotNull(slackDigestChannel);
        this.slackVerboseChannel = Preconditions.checkNotNull(slackVerboseChannel);
    }

    public void registerCommand(String command, SlackCommand predicate) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(command));
        Preconditions.checkNotNull(predicate);
        if (digestEnabled()) {
            commandListener.register(command, predicate);
        } else {
            LOG.warn("Slack Integration does not seem to be configured properly, set it to enabled and create it");
        }
    }

    public void startBotListener() {
        registerCommand("runtests", runSuiteCommandProvider.get());
        SlackMessagePostedListener slackMessagePostedListener = (event, session) -> {
            if (slackDigestChannel.equals(event.getChannel().getName())) {
                SlackUtil.receiver(event)
                        .ifPresent(id -> {
                            if (botId().equals(id)) {
                                Optional<String> command = SlackUtil.command(event);
                                command
                                        .ifPresent(contain -> commandListener.execute(contain, event, session));
                            }
                        });
            }
        };
        slackSession()
                .addMessagePostedListener(slackMessagePostedListener);
    }

    private boolean verboseEnabled() {
        return enabled && SlackCommon.verboseEnabled();
    }

    public boolean enabled() {
        return enabled;
    }

    public void initialize() {
        SlackCommon.initialize(slackBotToken, slackVerboseChannel, slackDigestChannel, true);
    }

    @Inject
    public void close() throws IOException {
        SlackCommon.close(true);
    }

    private boolean digestEnabled() {
        return enabled && SlackCommon.digestEnabled();
    }

    private SlackChannel verboseChannel() {
        return SlackCommon.verboseChannel();
    }

    private SlackChannel digestChannel() {
        return SlackCommon.digestChannel();
    }

    private SlackSession slackSession() {
        return SlackCommon.slackSession();
    }

    private String botId() {
        return SlackCommon.botId();
    }
}
