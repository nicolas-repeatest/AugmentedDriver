package com.salesforceiq.augmenteddriver.server.slack;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Strings;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SlackUtil {
    public static Optional<String> receiver(SlackMessagePosted messagePosted) {
        String message = messagePosted.getMessageContent();
        String[] split = message.split("<");
        if (split.length > 1) {
            String firstUser =  split[1].split(">")[0];
            return Optional.of(firstUser.substring(firstUser.indexOf("@") + 1));
        }
        return Optional.empty();
    }

    public static Optional<String> command(SlackMessagePosted messagePosted) {
        String message = messagePosted.getMessageContent();
        String[] split = message.split(">:");
        if (split.length > 1) {
            String command = split[1].trim().split(" ")[0];
            if (!Strings.isNullOrEmpty(command)) {
                return Optional.of(command.trim());
            }
        }
        return Optional.empty();
    }

    public static Optional<List<String>> arguments(SlackMessagePosted messagePosted) {
        String message = messagePosted.getMessageContent();
        String[] split = message.split(">:");
        if (split.length > 1) {
            String[] together = split[1].trim().split(" ", 2);
            if (together.length > 1) {
                return Optional.of(Arrays.asList(together[1].trim().split(" "))
                        .stream()
                        .map(arg -> arg.trim())
                        .filter(arg -> !Strings.isNullOrEmpty(arg))
                        .collect(Collectors.toList()));
            } else {
                return Optional.of(Lists.newArrayList());
            }
        }
        return Optional.empty();
    }
}
