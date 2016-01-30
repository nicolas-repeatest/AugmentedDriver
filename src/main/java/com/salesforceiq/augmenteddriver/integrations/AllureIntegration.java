package com.salesforceiq.augmenteddriver.integrations;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import org.junit.runner.notification.RunListener;

import ru.yandex.qatools.allure.junit.AllureRunListener;

import com.beust.jcommander.Parameter;

/**
 * Integration for Allure.
 */
public class AllureIntegration implements ReportIntegration {

    public static final String TOGGLE_PROPERTY = "ALLURE";

    @Parameter(names = "-allure")
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

    @Override
    public RunListener getReporter(ByteArrayOutputStream outputStream) {
        return new AllureRunListener();
    }

    @Override
    public void print(String testName, String sessionId, IntegrationManager integrationManager) {
        //
    }

    @Override
    public String name() {
        return "AllureIntegration";
    }

}
