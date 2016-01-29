package com.salesforceiq.augmenteddriver.modules;

import com.salesforceiq.augmenteddriver.integrations.TeamCityIntegration;


public class TeamCityModule extends IntegrationModule {

    @Override
    protected void configureActions() {
        bindReportIntegration().to(TeamCityIntegration.class);
    }

}