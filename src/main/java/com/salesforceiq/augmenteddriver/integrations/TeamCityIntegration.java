package com.salesforceiq.augmenteddriver.integrations;

import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.reporters.TeamCityReporter;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.junit.runner.notification.RunListener;

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

    public RunListener getReporter(ByteArrayOutputStream outputStream, String nameAppender) {
        Preconditions.checkNotNull(outputStream);
        Preconditions.checkNotNull(nameAppender);
        return new TeamCityReporter(outputStream, nameAppender);
    }
}
