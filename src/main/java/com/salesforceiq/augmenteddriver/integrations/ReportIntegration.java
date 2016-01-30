package com.salesforceiq.augmenteddriver.integrations;


import org.junit.runner.notification.RunListener;

import java.io.ByteArrayOutputStream;

public interface ReportIntegration {

    boolean isEnabled();

    RunListener getReporter(ByteArrayOutputStream outputStream);

    void print(String testName, String sessionId, IntegrationManager integrationManager);

    String name();

}
