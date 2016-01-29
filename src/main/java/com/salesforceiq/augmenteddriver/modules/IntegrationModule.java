package com.salesforceiq.augmenteddriver.modules;

import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.salesforceiq.augmenteddriver.integrations.Integration;
import com.salesforceiq.augmenteddriver.integrations.ReportIntegration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class IntegrationModule extends AbstractModule {

    private final Properties augmented = new Properties();

    private Multibinder<Integration> integrationsBinder;
    private Multibinder<ReportIntegration> reportersBinder;

    @Override
    protected void configure() {
        loadAugmented();
        integrationsBinder = Multibinder.newSetBinder(binder(), Integration.class);
        reportersBinder = Multibinder.newSetBinder(binder(), ReportIntegration.class);
        configureActions();
    }

    protected abstract void configureActions();

    protected final LinkedBindingBuilder<Integration> bindIntegration() {
        return integrationsBinder.addBinding();
    }

    protected final LinkedBindingBuilder<ReportIntegration> bindReportIntegration() {
        return reportersBinder.addBinding();
    }

    protected String readFromEnvironmentOrPropertiesFile(String propertyName) {
        String value = System.getenv().get(propertyName);

        if (Strings.isNullOrEmpty(propertyName)) {
            return augmented.getProperty(propertyName);
        }

        return value;
    }

    protected void loadAugmented() {
        try {
            InputStream in = getClass().getResourceAsStream(PropertiesModule.DEFAULT_CONFIG);
            augmented.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Error loading augmented.properties");
        }
    }

}
