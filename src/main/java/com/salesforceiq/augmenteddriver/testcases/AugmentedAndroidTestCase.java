package com.salesforceiq.augmenteddriver.testcases;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.asserts.AugmentedAssert;
import com.salesforceiq.augmenteddriver.guice.GuiceModules;
import com.salesforceiq.augmenteddriver.integrations.IntegrationFactory;
import com.salesforceiq.augmenteddriver.mobile.android.*;
import com.salesforceiq.augmenteddriver.mobile.android.pageobjects.AndroidPageContainerObject;
import com.salesforceiq.augmenteddriver.mobile.android.pageobjects.AndroidPageObject;
import com.salesforceiq.augmenteddriver.mobile.android.pageobjects.AndroidPageObjectActions;
import com.salesforceiq.augmenteddriver.mobile.android.pageobjects.AndroidPageObjectActionsInterface;
import com.salesforceiq.augmenteddriver.mobile.ios.AugmentedIOSDriverProvider;
import com.salesforceiq.augmenteddriver.modules.AugmentedAndroidDriverModule;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

/**
 * Base test class for all Android tests.
 */
@GuiceModules({PropertiesModule.class, AugmentedAndroidDriverModule.class})
public class AugmentedAndroidTestCase extends AugmentedBaseTestCase implements AndroidPageObjectActionsInterface {
    private static final Logger LOG = LoggerFactory.getLogger(AugmentedAndroidTestCase.class);

    private AugmentedAndroidDriver driver;

    @Inject
    private AugmentedAndroidDriverProvider augmentedAndroidDriverProvider;

    @Inject
    private AugmentedAndroidFunctionsFactory augmentedAndroidFunctionsFactory;

    private AugmentedAndroidFunctions augmentedAndroidFunctions;

    @Named(PropertiesModule.REMOTE_ADDRESS)
    @Inject
    private String remoteAddress;

    @Rule
    @Inject
    public AugmentedTestWatcher<AugmentedAndroidDriverProvider> augmentedTestWatcher;

    @Inject
    private DesiredCapabilities capabilities;

    @Inject
    private AndroidPageObjectActions androidPageObjectActions;

    @Inject
    private IntegrationFactory integrations;

    @Inject
    private TestRunnerConfig arguments;

    /**
     * <p>
     *     IMPORTANT, the session of the driver is set after the driver is initialized.
     * </p>
     */
    @Before
    public void setUp() {
        Preconditions.checkNotNull(augmentedAndroidDriverProvider);
        Preconditions.checkNotNull(integrations);
        Preconditions.checkNotNull(arguments);
        Preconditions.checkNotNull(androidPageObjectActions);
        Preconditions.checkNotNull(remoteAddress);
        Preconditions.checkNotNull(capabilities);

        // If left to Guice, it creates each driver serially, queueing all tests
        // (Android SauceLabs takes up to 40 seconds to create one)
        // This is a hack that creates a driver manually and sets it in the
        // AugmentedWebDriverProvider and AugmentedWebFunctionsFactory.
        //
        // NOT IDEAL.
        long start = System.currentTimeMillis();
        LOG.info("Creating AugmentedAndroidDriver");
        try {
            driver = new AugmentedAndroidDriver(remoteAddress, capabilities, augmentedAndroidFunctions);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Check your addresses on the properties file", e);
        }
        augmentedAndroidFunctions= augmentedAndroidFunctionsFactory.create(driver);
        driver.setAugmentedFunctions(augmentedAndroidFunctions);

        augmentedAndroidDriverProvider.initialize(driver);
        LOG.info("AugmentedAndroidDriver created in " + Util.TO_PRETTY_FORMAT.apply(System.currentTimeMillis() - start));

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
    public AugmentedAndroidDriver driver() {
        return Preconditions.checkNotNull(driver);
    }

    @Override
    public AugmentedAndroidFunctions augmented() {
        return Preconditions.checkNotNull(augmentedAndroidFunctions);
    }

    @Override
    public PageObjectWaiter waiter() {
        return Preconditions.checkNotNull(androidPageObjectActions.waiter());
    }

    @Override
    public <T extends AndroidPageObject> T get(Class<T> clazz) {
        return androidPageObjectActions.get(Preconditions.checkNotNull(clazz));
    }

    @Override
    public <T extends AndroidPageObject> T get(Class<T> clazz, Predicate<T> waitUntil) {
        return androidPageObjectActions.get(Preconditions.checkNotNull(clazz), Preconditions.checkNotNull(waitUntil));
    }

    @Override
    public <T extends AndroidPageContainerObject> T get(Class<T> clazz, AugmentedAndroidElement container) {
        return androidPageObjectActions.get(Preconditions.checkNotNull(clazz), Preconditions.checkNotNull(container));
    }

    @Override
    public <T extends AndroidPageContainerObject> T get(Class<T> clazz, AugmentedAndroidElement container, Predicate<T> waitUntil) {
        return androidPageObjectActions.get(Preconditions.checkNotNull(clazz),
                Preconditions.checkNotNull(container), Preconditions.checkNotNull(waitUntil));
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

