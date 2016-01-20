package com.salesforceiq.augmenteddriver.integrations;

import com.google.common.base.Strings;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.reporters.TeamCityReporter;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.junit.runner.notification.RunListener;
import org.openqa.selenium.remote.SessionId;

import java.io.ByteArrayOutputStream;

/**
 * Integration for TeamCity, used to write the output so Team City understands.
 */
@Singleton
public class TeamCityIntegration implements Integration {

    private final boolean teamCityIntegration;

    @Inject
    public TeamCityIntegration(@Named(PropertiesModule.TEAM_CITY_INTEGRATION) String teamCityIntegration) {
        this.teamCityIntegration = Preconditions.checkNotNull(Boolean.valueOf(teamCityIntegration));
    }

    @Override
    public boolean isEnabled() {
        return teamCityIntegration;
    }

    /**
     * Prints the Session Id the way SauceLabs likes it, for the SauceLabs plugin.
     *
     * @param jobName the name of the job running.
     * @param sessionId the session id of the driver.
     */
    public void printSessionId(String jobName, String sessionId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sessionId));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(jobName));

        String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", sessionId, jobName);
        System.out.println(message);
    }

    /**
     * Creates a reporter that understands how to print information for TeamCity.
     *
     * @param outputStream where to print the log.
     * @param nameAppender string to append to the name of the test.
     * @return the Reporter.
     */
    public RunListener getReporter(ByteArrayOutputStream outputStream, String nameAppender) {
        Preconditions.checkNotNull(outputStream);
        Preconditions.checkNotNull(nameAppender);

        return new TeamCityReporter(outputStream, nameAppender);
    }
}
