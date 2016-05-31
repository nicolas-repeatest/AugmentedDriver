package com.salesforceiq.augmenteddriver.slack;

import com.google.common.base.Strings;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SlackCommon {
    private static final Logger LOG = LoggerFactory.getLogger(SlackCommon.class);

    /**
     *  <P> IMPORTANT: WHY THESE ARE STATIC??</P>
     *
     *  The reason is that there is one Injector for the Main Test Runner/Suite Runner (where these fields
     *  are initialized) but each TEST also has its own Injector, meaning the @Singleton annotation is only
     *  local to each Injector.
     *  I do not want to create a connection for each test (I could create this on each setup and destroy
     *  on each tear down). This way is a little bit cleaner, even though I am sharing these configuration
     *  via static.
     */
    private static SlackChannel digestChannel;
    private static SlackSession slackSession;
    private static SlackChannel verboseChannel;
    private static boolean initializedServer = false;

    /**
     * Initialization of slack connection and slack channels
     *
     * @param slackBotToken auth token for the bot
     * @param slackVerboseChannel name of the verbose channel.
     * @param slackDigestChannel name of the digest channel.
     * @param isServer whether the one that is initalizaing is the Augmented Server or not.
     */
    public static void initialize(String slackBotToken,
                                  String slackVerboseChannel,
                                  String slackDigestChannel,
                                  boolean isServer) {
        if (initializedServer) {
            return;
        }
        if (Strings.isNullOrEmpty(slackBotToken)) {
            LOG.warn("No Slack Bot Token, Slack Integration will not broadcast at all");
        } else {
            try {
                slackSession = SlackSessionFactory
                        .createWebSocketSlackSession(slackBotToken);
                slackSession.connect();
                if (Strings.isNullOrEmpty(slackVerboseChannel)) {
                    LOG.warn("No Slack Verbose Channel, Slack Integration will not broadcast success/failures");
                } else {
                    verboseChannel = slackSession.findChannelByName(slackVerboseChannel);
                    if (verboseChannel == null) {
                        LOG.warn(String.format("Verbose Channel %s not found, Slack Integration will not broadcast success/failures", slackVerboseChannel));
                    } else {
                        slackSession
                                .joinChannel(slackVerboseChannel);
                    }
                }
                if (Strings.isNullOrEmpty(slackDigestChannel)) {
                    LOG.warn("No Slack Digest Channel, Slack Integration will not broadcast summaries");
                } else {
                    digestChannel = slackSession.findChannelByName(slackDigestChannel);
                    if (digestChannel == null) {
                        LOG.warn(String.format("Digest Channel %s not found, Slack Integration will not broadcast summaries", slackDigestChannel));
                    } else {
                        slackSession
                                .joinChannel(slackDigestChannel);
                    }
                }
            } catch (IOException e) {
                LOG.warn(String.format("Could not create session with token %s, Slack Integration will not broadcast at all", slackBotToken), e);
            }
        }
        if (isServer) {
            initializedServer = true;
        }
    }

    public static boolean verboseEnabled() {
        return slackSession != null && verboseChannel != null;
    }

    public static boolean digestEnabled() {
        return slackSession != null && digestChannel != null;
    }

    public static SlackSession slackSession() {
        return slackSession;
    }

    public static SlackChannel digestChannel() {
        return digestChannel;
    }

    public static SlackChannel verboseChannel() {
        return verboseChannel;
    }

    public static String botId() {
        if (slackSession != null) {
            return slackSession.sessionPersona().getId();
        } else {
            return "";
        }
    }

    public static void close(boolean isServer) throws IOException {
        if (!isServer && initializedServer) {
            return;
        }
        if (slackSession != null) {
            slackSession.disconnect();
        }
    }
}
