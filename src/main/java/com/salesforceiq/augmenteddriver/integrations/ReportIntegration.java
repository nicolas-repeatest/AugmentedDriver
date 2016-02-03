package com.salesforceiq.augmenteddriver.integrations;


import org.junit.runner.notification.RunListener;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public interface ReportIntegration {

    boolean isEnabled();

    RunListener getReporter(ByteArrayOutputStream outputStream);

    void print(String testName, String sessionId, IntegrationManager integrationManager);

    void initialize(Properties properties);

    String name();

}
