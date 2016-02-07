package com.salesforceiq.augmenteddriver.integrations;


import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;

public class JenkinsIntegration implements Integration {

    private final boolean allureIntegration;

    @Inject
    public JenkinsIntegration(@Named(PropertiesModule.JENKINS_INTEGRATION) String jenkinsIntegration) {
        this.allureIntegration = Preconditions.checkNotNull(Boolean.valueOf(jenkinsIntegration));
    }

    @Override
    public boolean isEnabled() {
        return allureIntegration;
    }
}
