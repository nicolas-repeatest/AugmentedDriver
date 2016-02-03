package com.salesforceiq.augmenteddriver.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import com.salesforceiq.augmenteddriver.util.Util;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Guice Module that loads all the properties file.
 */
public class PropertiesModule extends AbstractModule {

    public static final String REPORTING = "REPORTING";
    public static final String UNIQUE_ID = "UNIQUE_ID";
    public static final String WAIT_IN_SECONDS = "WAIT_TIME_IN_SECONDS";
    public static final String PRESS_TIME_IN_MILLISECONDS = "PRESS_TIME_IN_MILLISECONDS";
    public static final String SWIPE_QUANTITY = "SWIPE_QUANTITY";
    public static final String TAP_FINGERS = "TAP_FINGERS";
    public static final String MAX_RETRIES = "MAX_RETRIES";
    public static final String CAPABILITIES = "CAPABILITIES";
    public static final String DEFAULT_CONFIG = "conf/augmented.properties";

    private static final String ID = Util.getRandomAsString();

    /**
     * For now all the default properties are defined here.
     */
    private static final Map<String, String> defaultProperties = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
            put(WAIT_IN_SECONDS, "30");
            put(PRESS_TIME_IN_MILLISECONDS, "1000");
            put(SWIPE_QUANTITY, "5");
            put(REPORTING, "false");
            put(TAP_FINGERS, "1");
            put(MAX_RETRIES, "2");
        }
    };

    @Override
    protected void configure() {
        Properties properties = loadDefaultProperties(new Properties());

        String path = TestRunnerConfig.ARGUMENTS == null ? DEFAULT_CONFIG : TestRunnerConfig.ARGUMENTS.conf();
        Path propertiesPath = Paths.get(path);

        // Loads the properties set in the properties file.
        if (Files.exists(propertiesPath)) {
            try {
                properties.load(new FileInputStream(propertiesPath.toFile()));
            } catch (IOException e) {
                throw new IllegalStateException("Failed to load properties file " + propertiesPath, e);
            }
        } else {
            throw new IllegalArgumentException("Properties file does not exist " + propertiesPath);
        }

        // To load the capabilities from properties file.
        if (TestRunnerConfig.ARGUMENTS == null && properties.get(CAPABILITIES) != null) {
            TestRunnerConfig.initialize(properties);
        }

        if (TestRunnerConfig.ARGUMENTS == null) {
            throw new IllegalStateException("Capabilities were not loaded. Please set on properties file or command line args.");
        }

        // This will override the properties set in the property file, with the properties sent in the extra parameters.
        if (TestRunnerConfig.ARGUMENTS.extra() != null && !TestRunnerConfig.ARGUMENTS.extra().isEmpty()) {
            properties.putAll(TestRunnerConfig.ARGUMENTS.extra());
        }

        Names.bindProperties(binder(), properties);
        
        bind(Properties.class).toInstance(properties);
        bind(DesiredCapabilities.class).toInstance(TestRunnerConfig.ARGUMENTS.capabilities());
        bind(String.class).annotatedWith(Names.named(PropertiesModule.UNIQUE_ID)).toInstance(ID);
    }
    
    private Properties loadDefaultProperties(Properties properties) {
        defaultProperties.entrySet()
                .stream()
                .forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue()));

        return properties;
    }

}
