package com.salesforceiq.augmenteddriver.integrations;

import com.beust.jcommander.Parameter;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import org.junit.runner.notification.RunListener;
import ru.yandex.qatools.allure.junit.AllureRunListener;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

/**
 * Integration for Allure.
 */
public class AllureIntegration implements ReportIntegration {

    public static final String TOGGLE_PROPERTY = "ALLURE";

    @Parameter(names = "-allure", description = "Whether to run tests on SauceLabs or not")
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
    public RunListener getReporter(ByteArrayOutputStream outputStream, String nameAppender) {
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
