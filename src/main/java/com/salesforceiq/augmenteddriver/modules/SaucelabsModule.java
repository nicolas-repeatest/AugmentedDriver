package com.salesforceiq.augmenteddriver.modules;


import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.salesforceiq.augmenteddriver.integrations.SauceLabsIntegration;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import com.saucelabs.saucerest.SauceREST;

public class SaucelabsModule extends IntegrationModule {

    @Override
    protected void configureActions() {
        String sauceAddress = readFromEnvironmentOrPropertiesFile(SauceLabsIntegration.SAUCE_ADDRESS);

        if (Strings.isNullOrEmpty(sauceAddress)) {
        	TestRunnerConfig.ARGUMENTS.setRemoteAddress(SauceLabsIntegration.SAUCE_DEFAULT_ADDRESS);
        } else {
        	TestRunnerConfig.ARGUMENTS.setRemoteAddress(sauceAddress);
        }

        String sauceKey = readFromEnvironmentOrPropertiesFile(SauceLabsIntegration.SAUCE_KEY);
        String sauceUser = readFromEnvironmentOrPropertiesFile(SauceLabsIntegration.SAUCE_USER);

        Preconditions.checkArgument(!Strings.isNullOrEmpty(sauceKey), String.format("Set %s in the properties file", SauceLabsIntegration.SAUCE_KEY));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sauceUser), String.format("Set %s in the properties file", SauceLabsIntegration.SAUCE_USER));

        bind(SauceREST.class).toInstance(new SauceREST(sauceUser, sauceKey));
        bindIntegration().to(SauceLabsIntegration.class);
    }

}
