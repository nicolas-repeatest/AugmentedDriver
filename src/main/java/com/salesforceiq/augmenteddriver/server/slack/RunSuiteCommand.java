package com.salesforceiq.augmenteddriver.server.slack;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

@Singleton
public class RunSuiteCommand implements SlackCommand {
    private static final Logger LOG = LoggerFactory.getLogger(SlackCommandListener.class);
    private final ListeningExecutorService executor;
    private final RunCommandCallableFactory runCommandCallableFactory;
    private String suites;
    private String suitesPackage;
    private int parallel;
    private String capabilities;
    private boolean sauce;

    @Inject
    public RunSuiteCommand(
            @Named(PropertiesModule.SUITES) String defaultSuites,
            @Named(PropertiesModule.SUITES_PACKAGE) String defaultSuitesPackage,
            @Named(PropertiesModule.PARALLEL) String defaultParallel,
            @Named(PropertiesModule.CAPABILITIES) String defaultCapabilities,
            @Named(PropertiesModule.SAUCE) String sauce,
            RunCommandCallableFactory runCommandCallableFactory) {
        this.executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
        this.runCommandCallableFactory = Preconditions.checkNotNull(runCommandCallableFactory);
        this.suites = Preconditions.checkNotNull(defaultSuites);
        this.suitesPackage = Preconditions.checkNotNull(defaultSuitesPackage);
        this.parallel = Integer.valueOf(defaultParallel);
        this.capabilities = Preconditions.checkNotNull(defaultCapabilities);
        this.sauce = Boolean.valueOf(Preconditions.checkNotNull(sauce));
    }

    @Override
    public boolean test(SlackMessagePosted messagePosted, SlackSession session) {
        Optional<List<String>> arguments = SlackUtil.arguments(messagePosted);
        if (!arguments.isPresent()) {
            List<String> strings = arguments.get();
            suites = strings.get(0);
            strings.remove(0);
            strings
                    .stream()
                    .forEach(parameter -> initializeParameter(parameter));
        }
//        TestRunnerConfig config = TestRunnerConfig.initialize(suites, suitesPackage, parallel, capabilities, sauce);
//        executor.submit(runCommandCallableFactory.create(config));
        return false;
    }

    private void initializeParameter(String parameter) {
        if (isInt(parameter)) {
            parallel = Integer.valueOf(parameter);
        } else if (isBoolean(parameter)) {
            sauce = Boolean.valueOf(parameter);
        } else if (Files.exists(Paths.get(parameter))) {
            capabilities = parameter;
        } else {
            suitesPackage = parameter;
        }
    }

    private boolean isInt(String input) {
        try {
            Integer.valueOf(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isBoolean(String input) {
        return (!Strings.isNullOrEmpty(input)) && ("false".equalsIgnoreCase(input) || ("true".equalsIgnoreCase(input)));
    }
}
