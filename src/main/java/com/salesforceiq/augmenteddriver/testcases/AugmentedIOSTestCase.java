package com.salesforceiq.augmenteddriver.testcases;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.asserts.AugmentedAssert;
import com.salesforceiq.augmenteddriver.guice.GuiceModules;
import com.salesforceiq.augmenteddriver.integrations.IntegrationFactory;
import com.salesforceiq.augmenteddriver.mobile.ios.*;
import com.salesforceiq.augmenteddriver.mobile.ios.pageobjects.IOSPageContainerObject;
import com.salesforceiq.augmenteddriver.mobile.ios.pageobjects.IOSPageObject;
import com.salesforceiq.augmenteddriver.mobile.ios.pageobjects.IOSPageObjectActions;
import com.salesforceiq.augmenteddriver.mobile.ios.pageobjects.IOSPageObjectActionsInterface;
import com.salesforceiq.augmenteddriver.modules.AugmentedIOSDriverModule;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.*;
import com.salesforceiq.augmenteddriver.web.AugmentedWebDriverProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.net.MalformedURLException;

/**
 * Base test class for all IOS tests.
 */
@GuiceModules({PropertiesModule.class, AugmentedIOSDriverModule.class})
public class AugmentedIOSTestCase extends AugmentedBaseTestCase implements IOSPageObjectActionsInterface {
    private static final Logger LOG = LoggerFactory.getLogger(AugmentedIOSTestCase.class);

    private AugmentedIOSDriver driver;

    @Inject
    private AugmentedIOSFunctionsFactory augmentedIOSFunctionsFactory;

    private AugmentedIOSFunctions augmentedIOSFunctions;

    @Inject
    private AugmentedIOSDriverProvider augmentedIOSDriverProvider;

    @Inject
    private IOSPageObjectActions iosPageObjectActions;

    @Rule
    @Inject
    public AugmentedTestWatcher<AugmentedIOSDriverProvider> augmentedTestWatcher;

    @Inject
    private IntegrationFactory integrations;

    @Inject
    private TestRunnerConfig arguments;

    @Named(PropertiesModule.REMOTE_ADDRESS)
    @Inject
    private String remoteAddress;

    @Inject
    private DesiredCapabilities capabilities;

    /**
     * <p>
     *     IMPORTANT, the session of the driver is set after the driver is initialized.
     * </p>
     */
    @Before
    public void setUp() {
        Preconditions.checkNotNull(augmentedIOSDriverProvider);
        Preconditions.checkNotNull(integrations);
        Preconditions.checkNotNull(arguments);
        Preconditions.checkNotNull(iosPageObjectActions);
        Preconditions.checkNotNull(remoteAddress);
        Preconditions.checkNotNull(capabilities);

        // If left to Guice, it creates each driver serially, queueing all tests
        // (Android SauceLabs takes up to 40 seconds to create one)
        // This is a hack that creates a driver manually and sets it in the
        // AugmentedWebDriverProvider and AugmentedWebFunctionsFactory.
        //
        // NOT IDEAL.
        long start = System.currentTimeMillis();
        LOG.info("Creating AugmentedIOSDriver");
        try {
            driver = new AugmentedIOSDriver(remoteAddress, capabilities, augmentedIOSFunctions);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Check your addresses on the properties file", e);
        }
        augmentedIOSFunctions = augmentedIOSFunctionsFactory.create(driver);
        driver.setAugmentedFunctions(augmentedIOSFunctions);
        augmentedIOSDriverProvider.initialize(driver);
        LOG.info("AugmentedIOSDriver created in " + Util.TO_PRETTY_FORMAT.apply(System.currentTimeMillis() - start));

        sessionId = driver.getSessionId().toString();
        if (integrations.sauceLabs().isEnabled()) {
            integrations.sauceLabs().jobName(getFullTestName(), sessionId);
            integrations.sauceLabs().buildName(getUniqueId(), sessionId);
        }
        if (integrations.teamCity().isEnabled() && integrations.sauceLabs().isEnabled()) {
            integrations.teamCity().printSessionId(getFullTestName(), sessionId);
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Override
    public AugmentedIOSDriver driver() {
        return Preconditions.checkNotNull(driver);
    }

    @Override
    public AugmentedIOSFunctions augmented() {
        return Preconditions.checkNotNull(augmentedIOSFunctions);
    }

    @Override
    public PageObjectWaiter waiter() {
        return Preconditions.checkNotNull(iosPageObjectActions.waiter());
    }

    @Override
    public <T extends IOSPageObject> T get(Class<T> clazz) {
        return iosPageObjectActions.get(Preconditions.checkNotNull(clazz));
    }

    @Override
    public <T extends IOSPageObject> T get(Class<T> clazz, Predicate<T> waitUntil) {
        return iosPageObjectActions.get(Preconditions.checkNotNull(clazz), Preconditions.checkNotNull(waitUntil));
    }

    @Override
    public <T extends IOSPageContainerObject> T get(Class<T> clazz, AugmentedIOSElement container) {
        return iosPageObjectActions.get(Preconditions.checkNotNull(clazz), Preconditions.checkNotNull(container));
    }

    @Override
    public <T extends IOSPageContainerObject> T get(Class<T> clazz, AugmentedIOSElement container, Predicate<T> waitUntil) {
        return iosPageObjectActions.get(Preconditions.checkNotNull(clazz), Preconditions.checkNotNull(container),
                Preconditions.checkNotNull(waitUntil));
    }

    @Override
    public void assertElementIsPresentAfter(By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsPresentAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsPresent(By by) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsPresentAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsVisibleAfter(By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsVisibleAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsVisible(By by) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsVisibleAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsClickableAfter(By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsClickableAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsClickable(By by) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsClickableAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public void assertElementContainsAfter(By by, String text, int timeoutInSeconds) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));

        AugmentedAssert.assertElementContainsAfter(augmented(), by, text, timeoutInSeconds);
    }

    @Override
    public void assertElementContains(By by, String text) {
        Preconditions.checkNotNull(by);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(text));

        AugmentedAssert.assertElementContainsAfter(augmented(), by, text, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsNotClickableAfter(By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsNotClickableAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsNotClickable(By by) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsNotClickableAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsNotVisibleAfter(By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsNotVisibleAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsNotVisible(By by) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsNotVisibleAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsNotPresentAfter(By by, int timeoutInSeconds) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsNotPresentAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsNotPresent(By by) {
        Preconditions.checkNotNull(by);

        AugmentedAssert.assertElementIsNotPresentAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public <T extends PageObject> void assertThat(T entity, Predicate<T> assertThat) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(assertThat);

        AugmentedAssert.assertThatAfter(waiter(), entity, assertThat, "Assert Failed", waitTimeInSeconds());
    }

    @Override
    public <T extends PageObject> void assertThat(T entity, Predicate<T> assertThat, String errorMessage) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(assertThat);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorMessage));

        AugmentedAssert.assertThatAfter(waiter(), entity, assertThat, errorMessage, waitTimeInSeconds());
    }

    @Override
    public <T extends PageObject> void assertThatAfter(T entity, Predicate<T> assertThat, int waitTimeInSeconds) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(assertThat);

        AugmentedAssert.assertThatAfter(waiter(), entity, assertThat, "Assert Failed", waitTimeInSeconds);
    }

    @Override
    public <T extends PageObject> void assertThatAfter(T entity, Predicate<T> assertThat, String errorMessage, int waitTimeInSeconds) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(assertThat);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(errorMessage));

        AugmentedAssert.assertThatAfter(waiter(), entity, assertThat, errorMessage, waitTimeInSeconds);
    }
}
