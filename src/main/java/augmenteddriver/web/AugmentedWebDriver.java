package augmenteddriver.web;

import augmenteddriver.modules.PropertiesModule;
import com.google.inject.name.Named;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Driver used for automation against web.
 *
 * <p>
 *     IMPORTANT: Not using Guice, since we would need to do a Singleton, and that
 *     reduces the performance (when trying to run tests on parallel on SauceLabs, for
 *     some reason, it only creates one at a time, and sometimes Sauce takes 40 seconds
 *     to create a new one.
 * </p>
 */
public class AugmentedWebDriver extends RemoteWebDriver {

    private final AugmentedWebFunctions augmentedFunctions;

    public AugmentedWebDriver(@Named(PropertiesModule.REMOTE_ADDRESS) String remoteAddress,
                              DesiredCapabilities capabilities,
                              AugmentedWebFunctions augmentedFunctions) throws MalformedURLException {
        super(new URL(remoteAddress), capabilities);
        this.augmentedFunctions = augmentedFunctions;
    }

    public AugmentedWebFunctions augmented() {
        return augmentedFunctions;
    }
}
