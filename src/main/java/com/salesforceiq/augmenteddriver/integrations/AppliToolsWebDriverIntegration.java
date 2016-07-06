package com.salesforceiq.augmenteddriver.integrations;

import com.applitools.eyes.Eyes;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.web.AugmentedWebDriver;

public class AppliToolsWebDriverIntegration implements Integration {

    private final boolean enabled;
    private final String applitoolsKey;
    private final String applitoolsAppName;
    private final Provider<AugmentedWebDriver> driverProvider;
    private Eyes eyes;

    @Inject
    public AppliToolsWebDriverIntegration(
            @Named(PropertiesModule.APPLITOOLS_INTEGRATION) String applitoolsIntegration,
            @Named(PropertiesModule.APPLITOOLS_APP_NAME) String applitoolsAppName,
            @Named(PropertiesModule.APPLITOOLS_KEY) String applitoolsKey,
            Provider<AugmentedWebDriver> driverProvider) {
        this.enabled = Boolean.valueOf(Preconditions.checkNotNull(applitoolsIntegration));
        this.applitoolsKey = Preconditions.checkNotNull(applitoolsKey);
        this.applitoolsAppName = Preconditions.checkNotNull(applitoolsAppName);
        this.driverProvider = Preconditions.checkNotNull(driverProvider);

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Eyes getEyes() {
        return Preconditions.checkNotNull(eyes, "Applitools should be initialized first");
    }

    public Eyes initialize(String testName) {
        if (isEnabled()) {
            if (eyes == null) {
                eyes = new Eyes();
                eyes.setApiKey(applitoolsKey);
                eyes.open(driverProvider.get(), applitoolsAppName, testName);
            }
            return eyes;
        } else {
            throw new IllegalArgumentException("Applitools Integration is not enabled");
        }
    }
}
