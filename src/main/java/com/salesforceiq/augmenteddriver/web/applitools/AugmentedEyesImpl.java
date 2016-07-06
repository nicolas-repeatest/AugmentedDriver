package com.salesforceiq.augmenteddriver.web.applitools;

import com.applitools.eyes.Eyes;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.salesforceiq.augmenteddriver.integrations.IntegrationFactory;

public class AugmentedEyesImpl implements AugmentedEyes {

    private final IntegrationFactory integrationFactory;
    private final String testName;
    private Eyes eyes;

    @Inject
    public AugmentedEyesImpl(
            @Assisted String testName,
            IntegrationFactory integrationFactory) {
        this.testName = Preconditions.checkNotNull(testName);
        this.integrationFactory = Preconditions.checkNotNull(integrationFactory);
    }

    @Override
    public Eyes instance() {
        if (integrationFactory.appliTools().isEnabled()) {
            if (eyes == null) {
                eyes = integrationFactory.appliTools().initialize(testName);
            }
            return eyes;
        } else {
            throw new IllegalStateException("Applitools Integration is not enabled");
        }
    }
}
