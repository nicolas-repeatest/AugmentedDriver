package com.salesforceiq.augmenteddriver.mobile.ios;

import com.google.common.base.Preconditions;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidFunctions;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Driver used for automation against IOS.
 *
 * <p>
 *     IMPORTANT: Not using Guice, since we would need to do a Singleton, and that
 *     reduces the performance (when trying to run tests on parallel on SauceLabs, for
 *     some reason, it only creates one at a time, and sometimes Sauce takes 40 seconds
 *     to create a new one.
 * </p>
 */
public class AugmentedIOSDriver extends IOSDriver {
    private AugmentedIOSFunctions augmentedFunctions;

    public AugmentedIOSDriver(@Named(PropertiesModule.REMOTE_ADDRESS) String remoteAddress,
                              DesiredCapabilities capabilities,
                              AugmentedIOSFunctions augmentedFunctions) throws MalformedURLException {
        super(new URL(remoteAddress), capabilities);
        this.augmentedFunctions = Preconditions.checkNotNull(augmentedFunctions);
    }

    public AugmentedIOSFunctions augmented() {
        return augmentedFunctions;
    }

    public void setAugmentedFunctions(AugmentedIOSFunctions augmentedFunctions) {
        this.augmentedFunctions = augmentedFunctions;
    }
}
