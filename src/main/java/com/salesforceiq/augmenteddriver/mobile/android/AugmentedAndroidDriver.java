package com.salesforceiq.augmenteddriver.mobile.android;

import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.web.AugmentedWebFunctions;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Driver used for automation against Android.
 *
 * <p>
 *     IMPORTANT: Not using Guice, since we would need to do a Singleton, and that
 *     reduces the performance (when trying to run tests on parallel on SauceLabs, for
 *     some reason, it only creates one at a time, and sometimes Sauce takes 40 seconds
 *     to create a new one.
 * </p>
 */
public class AugmentedAndroidDriver extends AndroidDriver {
    private AugmentedAndroidFunctions augmentedFunctions;

    public AugmentedAndroidDriver(@Named(PropertiesModule.REMOTE_ADDRESS) String remoteAddress,
                                  DesiredCapabilities capabilities,
                                  AugmentedAndroidFunctions augmentedFunctions) throws MalformedURLException {
        super(new URL(remoteAddress), capabilities);
        this.augmentedFunctions = augmentedFunctions;
    }

    public AugmentedAndroidFunctions augmented() {
        return augmentedFunctions;
    }

    public void setAugmentedFunctions(AugmentedAndroidFunctions augmentedFunctions) {
        this.augmentedFunctions = augmentedFunctions;
    }
}
