package augmenteddriver.testcases;

import augmenteddriver.asserts.AugmentedAssert;
import augmenteddriver.guice.GuiceModules;
import augmenteddriver.integrations.IntegrationFactory;
import augmenteddriver.modules.AugmentedWebDriverModule;
import augmenteddriver.modules.PropertiesModule;
import augmenteddriver.util.CommandLineArguments;
import augmenteddriver.util.Util;
import augmenteddriver.web.AugmentedWebDriver;
import augmenteddriver.web.AugmentedWebDriverProvider;
import augmenteddriver.web.AugmentedWebElement;
import augmenteddriver.web.AugmentedWebFunctions;
import augmenteddriver.web.pageobjects.*;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
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
    private AugmentedWebFunctions augmentedWebFunctions;

    @Inject
    private WebPageObjectActions webPageObjectActions;

    @Inject
    private IntegrationFactory integrations;

    @Inject
    private CommandLineArguments arguments;

    @Named(PropertiesModule.REMOTE_ADDRESS)
    @Inject
    private String remoteAddress;

    @Inject
    private DesiredCapabilities capabilities;

    @Override
    public AugmentedWebDriver driver() {
        return Preconditions.checkNotNull(driver);
    }

    @Override
    public AugmentedWebFunctions augmented() {
        return Preconditions.checkNotNull(augmentedWebFunctions);
    }

    @Override
    public <T extends WebPageObject> T action(Action action, Class<T> landingPageObject) {
        return webPageObjectActions.action(action, landingPageObject);
    }

    @Override
    public <T extends WebPageContainerObject> T action(ActionContainer action, AugmentedWebElement container, Class<T> landingPageObject) {
        return webPageObjectActions.action(action, container, landingPageObject);
    }

    @Override
    public <T extends WebPageObject> T get(Class<T> clazz) {
        return webPageObjectActions.get(clazz);
    }

    @Override
    public <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container) {
        return webPageObjectActions.get(clazz, container);
    }

    /**
     * <p>
     *     IMPORTANT, the session of the driver is set after the driver is initialized.
     * </p>
     */
    @Before
    public void setUp() {
        Preconditions.checkNotNull(augmentedWebDriverProvider);
        Preconditions.checkNotNull(augmentedWebFunctions);
        Preconditions.checkNotNull(integrations);
        Preconditions.checkNotNull(arguments);
        Preconditions.checkNotNull(webPageObjectActions);
        Preconditions.checkNotNull(remoteAddress);
        Preconditions.checkNotNull(capabilities);

        long start = System.currentTimeMillis();
        LOG.info("Creating AugmentedWebDriver");
        try {
            driver = new AugmentedWebDriver(remoteAddress, capabilities, augmentedWebFunctions);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Check your addresses on the properties file", e);
        }

        augmentedWebDriverProvider.set(driver);
        LOG.info("AugmentedWebDriver created in " + Util.TO_PRETTY_FORNAT.apply(System.currentTimeMillis() - start));


        sessionId = driver.getSessionId().toString();
        if (integrations.sauceLabs().isEnabled()) {
            integrations.sauceLabs().jobName(getFullTestName(), sessionId);
            integrations.sauceLabs().buildName(getUniqueId(), sessionId);
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Override
    public void assertElementIsPresentAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsPresentAfter(augmentedWebFunctions, by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsPresent(By by) {
        AugmentedAssert.assertElementIsPresentAfter(augmentedWebFunctions, by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsVisibleAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsVisibleAfter(augmentedWebFunctions, by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsVisible(By by) {
        AugmentedAssert.assertElementIsVisibleAfter(augmentedWebFunctions, by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsClickableAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsClickableAfter(augmentedWebFunctions, by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsClickable(By by) {
        AugmentedAssert.assertElementIsClickableAfter(augmentedWebFunctions, by, waitTimeInSeconds());
    }

    @Override
    public void assertElementContainsAfter(By by, String text, int timeoutInSeconds) {
        AugmentedAssert.assertElementContainsAfter(augmentedWebFunctions, by, text, timeoutInSeconds);
    }

    @Override
    public void assertElementContains(By by, String text) {
        AugmentedAssert.assertElementContainsAfter(augmentedWebFunctions, by, text, waitTimeInSeconds());
    }
}
