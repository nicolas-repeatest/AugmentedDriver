package com.salesforceiq.augmenteddriver.server.slack;

import com.beust.jcommander.internal.Maps;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.BiPredicate;

@Singleton
public class SlackCommandListener {
    private static final Logger LOG = LoggerFactory.getLogger(SlackCommandListener.class);

    private final Map<String, SlackCommand> commands;

    @Inject
    public SlackCommandListener() {
        this.commands = Maps.newHashMap();
    }

    public void register(String command, SlackCommand predicate) {
        Preconditions.checkNotNull(command);
        Preconditions.checkNotNull(predicate);

        commands.put(command.toLowerCase(), predicate);
    }

    public void execute(String command, SlackMessagePosted message, SlackSession session) {
        Preconditions.checkNotNull(command);
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(session);

        BiPredicate<SlackMessagePosted, SlackSession> predicate = commands.get(command.toLowerCase());
        if (predicate != null) {
            predicate.test(message, session);
        } else {
            LOG.warn("Could not find command for " + command);
        }
    }
}
