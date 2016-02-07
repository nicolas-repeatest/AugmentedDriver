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
    private final AllureIntegration allureIntegration;
    private final JenkinsIntegration jenkinsIntegration;

    @Inject
    public IntegrationFactory(SauceLabsIntegration sauceLabsIntegration,
                              TeamCityIntegration teamCityIntegration,
                              AllureIntegration allureIntegration,
                              JenkinsIntegration jenkinsIntegration) {
        this.sauceLabsIntegration = Preconditions.checkNotNull(sauceLabsIntegration);;
        this.teamCityIntegration = Preconditions.checkNotNull(teamCityIntegration);
        this.allureIntegration = Preconditions.checkNotNull(allureIntegration);
        this.jenkinsIntegration = jenkinsIntegration;
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

    /**
     * Allure Integration.
     *
     * @return  the AllureIntegration.
     */
    public AllureIntegration allure() {
        return allureIntegration;
    }

    /**
     * Jenkins Integration.
     *
     * @return  the JenkinsIntegration.
     */
    public JenkinsIntegration jenkins() {
        return jenkinsIntegration;
    }
}
