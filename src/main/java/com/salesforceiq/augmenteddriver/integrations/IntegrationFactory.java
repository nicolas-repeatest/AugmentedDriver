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
    private final SlackIntegration slackIntegration;

    @Inject
    public IntegrationFactory(SauceLabsIntegration sauceLabsIntegration,
                              TeamCityIntegration teamCityIntegration,
                              AllureIntegration allureIntegration,
                              JenkinsIntegration jenkinsIntegration,
                              SlackIntegration slackIntegration) {
        this.sauceLabsIntegration = Preconditions.checkNotNull(sauceLabsIntegration);;
        this.teamCityIntegration = Preconditions.checkNotNull(teamCityIntegration);
        this.allureIntegration = Preconditions.checkNotNull(allureIntegration);
        this.jenkinsIntegration = Preconditions.checkNotNull(jenkinsIntegration);
        this.slackIntegration = Preconditions.checkNotNull(slackIntegration);
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

    /**
     * Slack Integration
     *
     * @return the Slack Integration.
     */
    public SlackIntegration slack() {
        return slackIntegration;
    }
}
