package com.salesforceiq.augmenteddriver.integrations;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;

public class AllureIntegration implements Integration {

    private final boolean allureIntegration;

    @Inject
    public AllureIntegration(@Named(PropertiesModule.ALLURE_INTEGRATION) String allureIntegration) {
        this.allureIntegration = Preconditions.checkNotNull(Boolean.valueOf(allureIntegration));
    }

    @Override
    public boolean isEnabled() {
        return allureIntegration;
    }
}
