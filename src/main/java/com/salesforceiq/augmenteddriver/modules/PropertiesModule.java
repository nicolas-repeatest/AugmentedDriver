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

    // false or true whether to print in the logs the information so TeamCity recognizes the tests
    // False by default
    public static final String TEAM_CITY_INTEGRATION = "TEAM_CITY_INTEGRATION";
    // false or true whether to generate the Allure reports.
    // False by default
    public static final String ALLURE_INTEGRATION = "ALLURE_INTEGRATION";
    // false or true whether to broadcast via Slack
    // False by default
    public static final String SLACK_INTEGRATION = "SLACK_INTEGRATION";
    // Token for the bot, get it here https://api.slack.com/bot-users
    // Default Empty
    public static final String SLACK_BOT_TOKEN = "SLACK_BOT_TOKEN";
    // Name of the channel where the bot will broadcast digested messages
    // A summary of the run will be broadcasted here.
    // Default Empty
    public static final String SLACK_DIGEST_CHANNEL = "SLACK_DIGEST_CHANNEL";
    // Name of the channel where the bot will broadcast verbose messages
    // Each failure/success will be broadcasted here.
    // Default Empty
    public static final String SLACK_VERBOSE_CHANNEL = "SLACK_VERBOSE_CHANNEL";
    // false or true whether to generate an xml file that can be parsed later by Jenkins to get the test results.
    // False by default
    public static final String JENKINS_INTEGRATION = "JENKINS_INTEGRATION";
    // Where the XML with the results will be stored.
    // results by default
    public static final String JENKINS_XML_DIR = "JENKINS_XML_DIR";
    // false or true whether to integrate with applitools.
    // default false
    public static final String APPLITOOLS_INTEGRATION = "APPLITOOLS_INTEGRATION";
    // Key for applitools
    // empty by default
    public static final String APPLITOOLS_KEY = "APPLITOOLS_KEY";
    // Name for the Eyes on applitools.
    // Default empty
    public static final String APPLITOOLS_APP_NAME = "APPLITOOLS_APP_NAME";
    // Address used by the framework to contact Selenium/Appium. It is set to LOCAL_ADDRESS or SAUCE_ADDRESS depending
    // whether the test is run on SauceLabs or locally.
    // SHOULD NOT BE SET EVER AT ALL
    public static final String REMOTE_ADDRESS = "REMOTE_ADDRESS";
    // Unique Id for the test/suite.
    // SHOULD NOT BE SET EVER AT ALL
    public static final String UNIQUE_ID = "UNIQUE_ID";
    // How much time in seconds all the commands (waitElementToBePresent, etc) will wait by default.
    // 30 seconds is the default.
    public static final String WAIT_IN_SECONDS = "WAIT_TIME_IN_SECONDS";
    // Max length of the test/suite in minutes.
    // 20 minutes by default.
    public static final String TIMEOUT_IN_MINUTES = "TIMEOUT_IN_MINUTES";
    // How much time in milliseconds will the PageObjectWaiter wait be default.
    // 500 milliseconds by default.
    public static final String WAIT_BETWEEN_ITERATIONS_IN_MILLISECONDS = "WAIT_BETWEEN_ITERATIONS_IN_MILLISECONDS";
    // How much time in milliseconds the Appium Swipe methods will press.
    // 1000 milliseconds by default.
    public static final String PRESS_TIME_IN_MILLISECONDS = "PRESS_TIME_IN_MILLISECONDS";
    // How many times the swipeUp/swipeDown are going to try to swipe until an element becomes visible.
    // 5 by default
    public static final String SWIPE_QUANTITY = "SWIPE_QUANTITY";
    // How many fingers will be used for the Appium Tap methods.
    // 1 by default
    public static final String TAP_FINGERS = "TAP_FINGERS";
    // How many total attempts a test will run
    // 2 by default.
    public static final String MAX_ATTEMPTS = "MAX_ATTEMPTS";
    // User for SauceLabs
    // Empty by default.
    public static final String SAUCE_USER = "SAUCE_USER";
    // Key for SauceLabs
    // Empty by default
    public static final String SAUCE_KEY = "SAUCE_KEY";
    // URL for linking to tests
    // https://saucelabs.com/beta/tests/%s/watch by default
    public static final String SAUCELABS_TEST_URL = "SAUCELABS_TEST_URL";
    // Local Address where Selenium/Appium is listening.
    // http://127.0.0.1:7777/wd/hub by default
    public static final String LOCAL_ADDRESS = "LOCAL_ADDRESS";
    // Sauce Address where Selenium/Appium is listening.
    // http://ondemand.saucelabs.com:80/wd/hub
    public static final String SAUCE_ADDRESS = "SAUCE_ADDRESS";
    // In case you want to load the capabilities from the properties file.
    public static final String SUITES = "SUITES";
    public static final String SUITES_PACKAGE = "SUITES_PACKAGE";
    public static final String PARALLEL = "PARALLEL";
    public static final String CAPABILITIES = "CAPABILITIES";
    // true or false whether to run the tests on saucelabs.
    public static final String SAUCE = "SAUCE";
    // Where the default properties file reside
    public static final String DEFAULT_CONFIG = "conf/augmented.properties";

    private static final String ID = Util.getRandomAsString();

    /**
     * For now all the default properties are defined here.
     */
    private static final Map<String, String> defaultProperties = new HashMap<String, String>() {
        {
            put(LOCAL_ADDRESS, "http://127.0.0.1:7777/wd/hub");
            put(SAUCE_ADDRESS, "http://ondemand.saucelabs.com:80/wd/hub");
            put(TIMEOUT_IN_MINUTES, "20");
            put(WAIT_IN_SECONDS, "30");
            put(WAIT_BETWEEN_ITERATIONS_IN_MILLISECONDS, "500");
            put(PRESS_TIME_IN_MILLISECONDS, "1000");
            put(SWIPE_QUANTITY, "5");
            put(TEAM_CITY_INTEGRATION, "false");
            put(ALLURE_INTEGRATION, "false");
            put(APPLITOOLS_INTEGRATION, "false");
            put(APPLITOOLS_KEY, "");
            put(APPLITOOLS_APP_NAME, "");
            put(SLACK_INTEGRATION, "false");
            put(SLACK_BOT_TOKEN, "");
            put(SLACK_DIGEST_CHANNEL, "");
            put(SLACK_VERBOSE_CHANNEL, "");
            put(JENKINS_INTEGRATION, "false");
            put(JENKINS_XML_DIR, "results");
            put(SAUCE_KEY, "");
            put(SAUCE_USER, "");
            put(SUITES, "");
            put(SUITES_PACKAGE, "");
            put(PARALLEL, "1");
            put(CAPABILITIES, "");
            put(SAUCE, "false");
            put(SAUCELABS_TEST_URL, "https://saucelabs.com/beta/tests/%s/watch");
            put(TAP_FINGERS, "1");
            put(MAX_ATTEMPTS, "2");
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
