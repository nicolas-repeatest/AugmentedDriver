package com.salesforceiq.augmenteddriver.testcases;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.salesforceiq.augmenteddriver.asserts.AugmentedAssert;
import com.salesforceiq.augmenteddriver.guice.GuiceModules;
import com.salesforceiq.augmenteddriver.modules.AugmentedWebDriverModule;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import com.salesforceiq.augmenteddriver.util.Util;
import com.salesforceiq.augmenteddriver.web.*;
import com.salesforceiq.augmenteddriver.web.pageobjects.WebPageContainerObject;
import com.salesforceiq.augmenteddriver.web.pageobjects.WebPageObject;
import com.salesforceiq.augmenteddriver.web.pageobjects.WebPageObjectActions;
import com.salesforceiq.augmenteddriver.web.pageobjects.WebPageObjectActionsInterface;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

/**
 * Base test class for all Web tests.
 */
@GuiceModules({PropertiesModule.class, AugmentedWebDriverModule.class})
public class AugmentedWebTestCase extends AugmentedBaseTestCase implements WebPageObjectActionsInterface {
    private static final Logger LOG = LoggerFactory.getLogger(AugmentedWebTestCase.class);

    private AugmentedWebDriver driver;

    @Inject
    private AugmentedWebDriverProvider augmentedWebDriverProvider;

    @Inject
    private AugmentedWebFunctionsFactory augmentedWebFunctionsFactory;

    private AugmentedWebFunctions augmentedWebFunctions;

    @Inject
    private WebPageObjectActions webPageObjectActions;

    @Inject
    private TestRunnerConfig arguments;

    @Override
    public AugmentedWebDriver driver() {
        return Preconditions.checkNotNull(driver);
    }

    @Override
    public AugmentedWebFunctions augmented() {
        return Preconditions.checkNotNull(augmentedWebFunctions);
    }

    @Override
    public <T extends WebPageObject> T get(Class<T> clazz) {
        return webPageObjectActions.get(Preconditions.checkNotNull(clazz));
    }

    @Override
    public <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container) {
        return webPageObjectActions.get(Preconditions.checkNotNull(clazz),
                                    Preconditions.checkNotNull(container));
    }

    /**
     * <p>
     *     IMPORTANT, the session of the driver is set after the driver is initialized.
     * </p>
     */
    @Before
    public void setUp() {
        Preconditions.checkNotNull(augmentedWebDriverProvider);
        Preconditions.checkNotNull(augmentedWebFunctionsFactory);
        Preconditions.checkNotNull(arguments);
        Preconditions.checkNotNull(webPageObjectActions);
        Preconditions.checkNotNull(capabilities);

        // If left to Guice, it creates each driver serially, queueing all tests
        // (Android SauceLabs takes up to 40 seconds to create one)
        // This is a hack that creates a driver manually and sets it in the
        // AugmentedWebDriverProvider and AugmentedWebFunctionsFactory.
        //
        // NOT IDEAL.
        long start = System.currentTimeMillis();
        LOG.info("Creating AugmentedWebDriver");
        try {
            driver = new AugmentedWebDriver(arguments.remoteAddress(), capabilities);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Check your addresses on the properties file", e);
        }
        augmentedWebFunctions = augmentedWebFunctionsFactory.create(driver);
        driver.setAugmentedFunctions(augmentedWebFunctions);
        augmentedWebDriverProvider.set(driver);
        LOG.info("AugmentedWebDriver created in " + Util.TO_PRETTY_FORMAT.apply(System.currentTimeMillis() - start));

        sessionId = driver.getSessionId().toString();
        triggerIntegrations();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
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

}
