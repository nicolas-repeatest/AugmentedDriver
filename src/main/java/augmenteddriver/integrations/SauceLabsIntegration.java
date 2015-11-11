package augmenteddriver.integrations;

import augmenteddriver.util.CommandLineArguments;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.saucelabs.saucerest.SauceREST;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * In charge of interacting with SauceLabs
 */
@Singleton
public class SauceLabsIntegration implements Integration {

    private final SauceREST sauceRest;
    private final CommandLineArguments arguments;

    @Inject
    public SauceLabsIntegration(SauceREST sauceREST, CommandLineArguments arguments) {
        this.sauceRest = Preconditions.checkNotNull(sauceREST);
        this.arguments = Preconditions.checkNotNull(arguments);
    }

    /**
     * Sets the job name
     */
    public void jobName(String jobName, String sessionId) {
        update("name", jobName, sessionId);
    }

    /**
     * Sets whether the test passed or not.
     */
    public void testPassed(boolean testPassed, String sessionId) {
        update("passed", testPassed, sessionId);
    }

    /**
     * Sets the build name
     */
    public void buildName(String buildName, String sessionId) {
        update("build", buildName, sessionId);
    }

    /**
     * Uploads a file to SauceLabs. Usually for an app or an apk.
     */
    public void uploadFile(Path fileToUpload) throws IOException {
        Preconditions.checkNotNull(fileToUpload);
        Preconditions.checkArgument(Files.exists(fileToUpload));
        uploadFile(fileToUpload, true);
    }

    /**
     * Uploads a file to SauceLabs. Usually for an app or an apk.
     *
     * @param overwrite if set to true, it overwrites the file if it exists in SauceLabs.
     */
    public void uploadFile(Path fileToUpload, boolean overwrite) throws IOException {
        Preconditions.checkNotNull(fileToUpload);
        Preconditions.checkArgument(Files.exists(fileToUpload));
        sauceRest.uploadFile(fileToUpload.toFile(), fileToUpload.getFileName().toString(), overwrite);
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
        return arguments.sauce();
    }
}
