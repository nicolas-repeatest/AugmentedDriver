package com.salesforceiq.augmenteddriver.integrations;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import org.junit.runner.notification.RunListener;

import com.beust.jcommander.Parameter;
import com.google.inject.Singleton;
import com.salesforceiq.augmenteddriver.reporters.TeamCityReporter;

/**
 * Integration for TeamCity, used to write the output so Team City understands.
 */
@Singleton
public class TeamCityIntegration implements ReportIntegration {

    public static final String TOGGLE_PROPERTY = "TEAM_CITY_INTEGRATION";

    @Parameter(names = "-teamcity")
    private boolean enabled = false;

    public void initialize(Properties properties) {
        if (properties.get(TOGGLE_PROPERTY) != null) {
            this.enabled = Boolean.valueOf(properties.getProperty(TOGGLE_PROPERTY));
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Prints the Session Id the way SauceLabs likes it, for the SauceLabs plugin.
     *
     * @param jobName the name of the job running.
     * @param sessionId the session id of the driver.
     */
    @Override
    public void print(String jobName, String sessionId, IntegrationManager integrationManager) {
        if (integrationManager.containsIntegration("SauceLabsIntegration")) {
            System.out.println(String.format("SauceOnDemandSessionID=%s job-name=%s", jobName, sessionId));
        }
    }

    /**
     * Creates a reporter that understands how to print information for TeamCity.
     *
     * @param outputStream where to print the log.
     * @param nameAppender string to append to the name of the test.
     * @return the Reporter.
     */
    @Override
    public RunListener getReporter(ByteArrayOutputStream outputStream) {
        return new TeamCityReporter(outputStream);
    }

    @Override
    public String name() {
        return "TeamCityIntegration";
    }

}
