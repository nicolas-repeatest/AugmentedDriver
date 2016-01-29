package com.salesforceiq.augmenteddriver.modules;


import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.name.Names;
import com.salesforceiq.augmenteddriver.integrations.SauceLabsIntegration;
import com.saucelabs.saucerest.SauceREST;

public class SaucelabsModule extends IntegrationModule {

    @Override
    protected void configureActions() {
        String sauceAddress = readFromEnvironmentOrPropertiesFile(SauceLabsIntegration.SAUCE_ADDRESS);

        if (Strings.isNullOrEmpty(sauceAddress)) {
            sauceAddress = SauceLabsIntegration.SAUCE_DEFAULT_ADDRESS;
        }

        bind(String.class).annotatedWith(Names.named(PropertiesModule.REMOTE_ADDRESS)).toInstance(sauceAddress);

        String sauceKey = readFromEnvironmentOrPropertiesFile(SauceLabsIntegration.SAUCE_KEY);
        String sauceUser = readFromEnvironmentOrPropertiesFile(SauceLabsIntegration.SAUCE_USER);

        Preconditions.checkArgument(!Strings.isNullOrEmpty(sauceKey), String.format("Set %s in the properties file", SauceLabsIntegration.SAUCE_KEY));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sauceUser), String.format("Set %s in the properties file", SauceLabsIntegration.SAUCE_USER));

        bind(SauceREST.class).toInstance(new SauceREST(sauceUser, sauceKey));
        bindIntegration().to(SauceLabsIntegration.class);
    }

}
