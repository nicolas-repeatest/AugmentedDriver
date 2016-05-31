package com.salesforceiq.augmenteddriver.server;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.modules.TestRunnerModule;
import com.salesforceiq.augmenteddriver.runners.TestRunner;
import com.salesforceiq.augmenteddriver.server.modules.AugmentedDriverServerModule;
import com.salesforceiq.augmenteddriver.server.resources.HealthResource;
import com.salesforceiq.augmenteddriver.server.slack.SlackServerIntegration;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AugmentedDriverServerApplication extends Application<AugmentedDriverServerConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(AugmentedDriverServerApplication.class);

    private SlackServerIntegration slackServerIntegration;
    private Injector injector;

    public static void main(String[] args) throws Exception {
        new AugmentedDriverServerApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<AugmentedDriverServerConfiguration> bootstrap) {
        List<Module> modules = Lists.newArrayList(
                new AugmentedDriverServerModule(),
                new PropertiesModule(),
                new TestRunnerModule());
        injector = Guice.createInjector(modules);
    }

    @Override
    public String getName() {
        return "AugmentedDriver Application";
    }

    @Override
    public void run(AugmentedDriverServerConfiguration augmentedDriverServerConfiguration, Environment environment) throws Exception {
        slackServerIntegration = injector.getInstance(SlackServerIntegration.class);
        if (slackServerIntegration != null && slackServerIntegration.enabled()) {
            slackServerIntegration.initialize();
            slackServerIntegration.startBotListener();
        }
        environment.jersey().register(new HealthResource());
        // Not so nice way to shut down the Slack Connection.
        // Could not figure it ouw a cleaner way.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOG.info("Shuting down server");
                if (slackServerIntegration != null && slackServerIntegration.enabled()) {
                    try {
                        LOG.info("Shuting down Slack Connection");
                        slackServerIntegration.close();
                    } catch (Exception e) {
                        LOG.error("Failed to shut down Slack Connection", e);
                    }
                }
            }
        });
    }
}