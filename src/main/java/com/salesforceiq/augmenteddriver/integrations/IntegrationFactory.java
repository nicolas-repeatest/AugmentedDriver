package com.salesforceiq.augmenteddriver.integrations;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Factory for all the different implementations.
 */
@Singleton
public class IntegrationFactory {

    private final SauceLabsIntegration sauceLabsIntegration;
    private final TeamCityIntegration teamCityIntegration;

    @Inject
    public IntegrationFactory(SauceLabsIntegration sauceLabsIntegration,
                              TeamCityIntegration teamCityIntegration) {
        this.sauceLabsIntegration = Preconditions.checkNotNull(sauceLabsIntegration);;
        this.teamCityIntegration = Preconditions.checkNotNull(teamCityIntegration);
    }

    /**
     * Saucelabs Integration.
     *
     * @return the SauceLabsIntegration.
     */
    public SauceLabsIntegration sauceLabs() {
        return sauceLabsIntegration;
    }

    /**
     * TeamCity Integration.
     *
     * @return  the TeamCityIntegration.
     */
    public TeamCityIntegration teamCity() {
        return teamCityIntegration;
    }
}
