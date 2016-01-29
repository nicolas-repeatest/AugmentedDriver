package com.salesforceiq.augmenteddriver.modules;


import com.salesforceiq.augmenteddriver.integrations.AllureIntegration;

public class AllureModule extends IntegrationModule {

    @Override
    protected void configureActions() {
        bindReportIntegration().to(AllureIntegration.class);
    }

}

