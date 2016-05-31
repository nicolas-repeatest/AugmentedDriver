package com.salesforceiq.augmenteddriver.util.saucelabs;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;

import java.util.Properties;

public class SauceConfigurator {

    private final String sauceKey;
    private final String sauceUser;

    public SauceConfigurator(
            @Named(PropertiesModule.SAUCE_USER) String sauceUser,
            @Named(PropertiesModule.SAUCE_KEY) String sauceKey) {
        this.sauceKey = Preconditions.checkNotNull(sauceKey);
        this.sauceUser = Preconditions.checkNotNull(sauceUser);
    }

    public void configure(TestRunnerConfig arguments) {
        if (Strings.isNullOrEmpty(sauceKey)) {
            throw new IllegalArgumentException("To run on Sauce Labs, define SAUCE_KEY in the properties file");
        }
        if (Strings.isNullOrEmpty(sauceUser)) {
            throw new IllegalArgumentException("To run on Sauce Labs, define SAUCE_USER in the properties file");
        }

        // To override the app in the yaml.
        if (!Strings.isNullOrEmpty(arguments.app())) {
            arguments.capabilities().setCapability("app", "sauce-storage:" + arguments.app());
            arguments.capabilities().setCapability("username", sauceUser);
            arguments.capabilities().setCapability("access-key", sauceKey);
        }
    }
}
