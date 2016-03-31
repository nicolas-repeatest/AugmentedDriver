package com.salesforceiq.augmenteddriver.util;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.salesforceiq.augmenteddriver.integrations.IntegrationFactory;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.yandex.qatools.allure.annotations.Attachment;

/**
 * TestWatcher used primarily to take screenshots on failure.
 *
 * @param <T> the RemoteWebDriver provider.
 */
public class AugmentedTestWatcher<T extends AugmentedProvider<? extends RemoteWebDriver>> extends TestWatcher {

    private final T driverProvider;
    private final IntegrationFactory integrationFactory;

    @Inject
    public AugmentedTestWatcher(T driverProvider,
                                IntegrationFactory integrationFactory) {
        this.driverProvider = Preconditions.checkNotNull(driverProvider);
        this.integrationFactory = Preconditions.checkNotNull(integrationFactory);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        if (driverProvider.isInitialized()) {
            if (integrationFactory.allure().isEnabled() && driverProvider.get().getSessionId() != null) {
                takeScrenshoot();
            }
            driverProvider.get().quit();
        }
    }

    @Override
    protected void succeeded(Description description) {
        if (driverProvider.isInitialized()) {
            driverProvider.get().quit();
        }
    }

    @Attachment("Screenshot on Failure")
    public byte[] takeScrenshoot() {
        return driverProvider.get().getScreenshotAs(OutputType.BYTES);
    }
}
