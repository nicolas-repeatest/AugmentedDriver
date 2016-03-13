package com.salesforceiq.augmenteddriver.modules;

import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import com.salesforceiq.augmenteddriver.util.Util;
import com.saucelabs.saucerest.SauceREST;
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

    public static final String TEAM_CITY_INTEGRATION = "TEAM_CITY_INTEGRATION";
    public static final String ALLURE_INTEGRATION = "ALLURE_INTEGRATION";
    public static final String JENKINS_INTEGRATION = "JENKINS_INTEGRATION";
    public static final String JENKINS_XML_DIR = "JENKINS_XML_DIR";
    public static final String REPORTING = "REPORTING";
    public static final String REMOTE_ADDRESS = "REMOTE_ADDRESS";
    public static final String UNIQUE_ID = "UNIQUE_ID";
    public static final String WAIT_IN_SECONDS = "WAIT_TIME_IN_SECONDS";
    public static final String WAIT_BETWEEN_ITERATIONS_IN_MILLISECONDS = "WAIT_BETWEEN_ITERATIONS_IN_MILLISECOND";
    public static final String PRESS_TIME_IN_MILLISECONDS = "PRESS_TIME_IN_MILLISECONDS";
    public static final String SWIPE_QUANTITY = "SWIPE_QUANTITY";
    public static final String TAP_FINGERS = "TAP_FINGERS";
    public static final String MAX_RETRIES = "MAX_RETRIES";
    public static final String SAUCE_USER = "SAUCE_USER";
    public static final String SAUCE_KEY = "SAUCE_KEY";
    public static final String LOCAL_ADDRESS = "LOCAL_ADDRESS";
    public static final String SAUCE_ADDRESS = "SAUCE_ADDRESS";
    public static final String CAPABILITIES = "CAPABILITIES";
    public static final String SAUCE = "SAUCE";
    public static final String DEFAULT_CONFIG = "conf/augmented.properties";

    private static final String ID = Util.getRandomAsString();

    /**
     * For now all the default properties are defined here.
     */
    private static final Map<String, String> defaultProperties = new HashMap<String, String>() {
        {
            put(LOCAL_ADDRESS, "http://127.0.0.1:7777/wd/hub");
            put(SAUCE_ADDRESS, "http://ondemand.saucelabs.com:80/wd/hub");
            put(WAIT_IN_SECONDS, "30");
            put(WAIT_BETWEEN_ITERATIONS_IN_MILLISECONDS, "500");
            put(PRESS_TIME_IN_MILLISECONDS, "1000");
            put(SWIPE_QUANTITY, "5");
            put(TEAM_CITY_INTEGRATION, "false");
            put(ALLURE_INTEGRATION, "false");
            put(JENKINS_INTEGRATION, "false");
            put(JENKINS_XML_DIR, "results");
            put(REPORTING, "false");
            put(SAUCE_KEY, "");
            put(SAUCE_USER, "");
            put(TAP_FINGERS, "1");
            put(MAX_RETRIES, "2");
        }
    };

    @Override
    protected void configure() {
        // Loads the default properties.
        Properties properties = new Properties();
        defaultProperties.entrySet()
                .stream()
                .forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue()));

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

        if (TestRunnerConfig.ARGUMENTS.sauce()) {
            setSauceProperties(properties);
        } else {
            properties.setProperty(PropertiesModule.REMOTE_ADDRESS, properties.getProperty(PropertiesModule.LOCAL_ADDRESS));
        }

        // This will override the properties set in the property file, with the properties sent in the extra parameters.
        if (TestRunnerConfig.ARGUMENTS.extra() != null
                && !TestRunnerConfig.ARGUMENTS.extra().isEmpty()) {
            properties.putAll(TestRunnerConfig.ARGUMENTS.extra());
        }
        Names.bindProperties(binder(), properties);

        bind(DesiredCapabilities.class).toInstance(TestRunnerConfig.ARGUMENTS.capabilities());
        bind(String.class)
                .annotatedWith(Names.named(PropertiesModule.UNIQUE_ID))
                .toInstance(ID);

        // Always set SauceRest, even with empty user key, so Guice does not complain.
        bind(SauceREST.class).toInstance(new SauceREST(properties.getProperty(PropertiesModule.SAUCE_USER), properties.getProperty(PropertiesModule.SAUCE_KEY)));
    }

    /**
     * Hack to set the sauce key and sauce user into the capabilities.
     */
    private void setSauceProperties(Properties properties) {
        properties.setProperty(PropertiesModule.REMOTE_ADDRESS, properties.getProperty(PropertiesModule.SAUCE_ADDRESS));
        if (Strings.isNullOrEmpty(properties.getProperty(PropertiesModule.SAUCE_KEY))) {
            throw new IllegalArgumentException("To run on Sauce Labs, define SAUCE_KEY in the properties file");
        }
        if (Strings.isNullOrEmpty(properties.getProperty(PropertiesModule.SAUCE_USER))) {
            throw new IllegalArgumentException("To run on Sauce Labs, define SAUCE_USER in the properties file");
        }

        // To override the app in the yaml.
        if (!Strings.isNullOrEmpty(TestRunnerConfig.ARGUMENTS.app())) {
            TestRunnerConfig.ARGUMENTS.capabilities().setCapability("app", "sauce-storage:" + TestRunnerConfig.ARGUMENTS.app());
        }
        TestRunnerConfig.ARGUMENTS.capabilities().setCapability("username", properties.getProperty(PropertiesModule.SAUCE_USER));
        TestRunnerConfig.ARGUMENTS.capabilities().setCapability("access-key", properties.getProperty(PropertiesModule.SAUCE_KEY));
    }
}
