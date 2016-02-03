package com.salesforceiq.augmenteddriver.modules;


import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.salesforceiq.augmenteddriver.integrations.SauceLabsIntegration;
import com.saucelabs.saucerest.SauceREST;

public class SaucelabsModule extends IntegrationModule {

    @Override
    protected void configureActions() {
        String sauceKey = readFromEnvironmentOrPropertiesFile(SauceLabsIntegration.SAUCE_KEY);
        String sauceUser = readFromEnvironmentOrPropertiesFile(SauceLabsIntegration.SAUCE_USER);

        Preconditions.checkArgument(!Strings.isNullOrEmpty(sauceKey), String.format("Set %s in the properties file", SauceLabsIntegration.SAUCE_KEY));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sauceUser), String.format("Set %s in the properties file", SauceLabsIntegration.SAUCE_USER));

        bind(SauceREST.class).toInstance(new SauceREST(sauceUser, sauceKey));
        bindIntegration().to(SauceLabsIntegration.class);
    }

}
