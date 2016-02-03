package com.salesforceiq.augmenteddriver.integrations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import com.saucelabs.saucerest.SauceREST;

/**
 * In charge of interacting with SauceLabs
 */
@Singleton
public class SauceLabsIntegration implements Integration {

    private final SauceREST sauceRest;

    @Parameter(names = "-sauce", description = "Whether to run tests on SauceLabs or not")
    private boolean enabled = false;

    @Parameter(names = "-file", description = "Path to file to upload", converter = PathConverter.class)
    private Path fileToUpload;

    @Parameter(names = "-overwrite", description = "Whether to overwrite or not the file in case it exists in SauceLabs")
    private boolean overwrite = true;

    @Parameter(names = "-conf", description = "Path to the properties file, conf/augmented.properties by default")
    private String conf = "conf/augmented.properties";

    public static final String SAUCE_USER = "SAUCE_USER";
    public static final String SAUCE_KEY = "SAUCE_KEY";
    public static final String SAUCE_ADDRESS = "SAUCE_ADDRESS";
    public static final String SAUCE_DEFAULT_ADDRESS = "http://ondemand.saucelabs.com:80/wd/hub";

    public static final String TOGGLE_PROPERTY = "SAUCE";
    public static final String OVERRIDE_PROPERTY = "saucelabs.overwrite";
    public static final String CONF_PROPERTY = "saucelabs.conf";

    @Inject
    public SauceLabsIntegration(SauceREST sauceREST) {
        this.sauceRest = Preconditions.checkNotNull(sauceREST);
    }

    public void initialize(Properties properties) {
        if (properties.get(TOGGLE_PROPERTY) != null) {
            this.enabled = Boolean.valueOf(properties.getProperty(TOGGLE_PROPERTY));
        }

        if (properties.get(OVERRIDE_PROPERTY) != null) {
            this.overwrite = Boolean.valueOf(properties.getProperty(OVERRIDE_PROPERTY));
        }

        if (properties.get(CONF_PROPERTY) != null) {
            this.conf = properties.getProperty(CONF_PROPERTY);
        }

        if (!enabled || TestRunnerConfig.ARGUMENTS == null) {
        	return;
        }
        
        TestRunnerConfig.ARGUMENTS.capabilities().setCapability("app", "sauce-storage:" + TestRunnerConfig.ARGUMENTS.app());
        TestRunnerConfig.ARGUMENTS.capabilities().setCapability("username", properties.getProperty(SAUCE_USER));
        TestRunnerConfig.ARGUMENTS.capabilities().setCapability("access-key", properties.getProperty(SAUCE_KEY));
        
        if (Strings.isNullOrEmpty(properties.getProperty(SAUCE_KEY))) {
        	TestRunnerConfig.ARGUMENTS.setRemoteAddress(SauceLabsIntegration.SAUCE_DEFAULT_ADDRESS);
        } else {
        	TestRunnerConfig.ARGUMENTS.setRemoteAddress(properties.getProperty(SAUCE_KEY));
        }
    }

    /**
     * Sets the job name
     *
     * @param jobName the name to set in SauceLabs.
     * @param sessionId the session id of the test.
     */
    public void jobName(String jobName, String sessionId) {
        update("name", jobName, sessionId);
    }

    /**
     * Sets whether the test passed or not.
     *
     * @param testPassed if the test passed or not.
     * @param sessionId the session id of the test.
     */
    public void testPassed(boolean testPassed, String sessionId) {
        update("passed", testPassed, sessionId);
    }

    /**
     * Sets the build name
     *
     * @param buildName the name to assign to the entire build.
     * @param sessionId the session id of the test.
     */
    public void buildName(String buildName, String sessionId) {
        update("build", buildName, sessionId);
    }

    /**
     * Uploads a file to SauceLabs. Usually for an app or an apk.
     *
     * @param fileToUpload which file to upload to sauce.
     * @param destinationFileName the name of the file for SauceLabs.
     * @throws IOException if the file couldnt be uploaded.
     */
    public void uploadFile(Path fileToUpload, String destinationFileName) throws IOException {
        Preconditions.checkNotNull(fileToUpload);
        Preconditions.checkArgument(Files.exists(fileToUpload));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(destinationFileName));

        uploadFile(fileToUpload, destinationFileName, true);
    }

    /**
     * Uploads a file to SauceLabs. Usually for an app or an apk.
     *
     * @param overwrite if set to true, it overwrites the file if it exists in SauceLabs.
     * @param fileToUpload which file to upload to sauce.
     * @param destinationFileName the name of the file for SauceLabs.
     * @throws IOException if the file couldnt be uploaded.
     */
    public void uploadFile(Path fileToUpload, String destinationFileName, boolean overwrite) throws IOException {
        Preconditions.checkNotNull(fileToUpload);
        Preconditions.checkArgument(Files.exists(fileToUpload));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(destinationFileName));

        sauceRest.uploadFile(fileToUpload.toFile(), destinationFileName, overwrite);
    }

    private void update(String key, Object value, String sessionId) {
        if (isEnabled()) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(sessionId));
            Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
            Preconditions.checkNotNull(value);

            Map<String, Object> updates = Maps.newHashMap();
            updates.put(key, value);
            sauceRest.updateJobInfo(sessionId, updates);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String name() {
        return "SauceLabsIntegration";
    }

}
