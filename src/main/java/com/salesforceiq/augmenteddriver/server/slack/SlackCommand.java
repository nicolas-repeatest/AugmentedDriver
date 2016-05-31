package com.salesforceiq.augmenteddriver.server.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.util.function.BiPredicate;

public interface SlackCommand extends BiPredicate<SlackMessagePosted, SlackSession> {
}
