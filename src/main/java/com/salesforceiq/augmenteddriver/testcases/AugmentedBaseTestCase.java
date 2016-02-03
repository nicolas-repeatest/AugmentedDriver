package com.salesforceiq.augmenteddriver.testcases;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.asserts.AugmentedAssertInterface;
import com.salesforceiq.augmenteddriver.guice.GuiceTestRunner;
import com.salesforceiq.augmenteddriver.integrations.IntegrationManager;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.Util;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Base Test Case for all tests.
 *
 * <p>
 *     No one should extend from this, AugmentedWebTestCase or the like should be used.
 * </p>
 */
@RunWith(GuiceTestRunner.class)
public abstract class AugmentedBaseTestCase implements AugmentedAssertInterface {

    @Rule
    public TestName testName = new TestName();

    @Inject
    @Named(PropertiesModule.WAIT_IN_SECONDS)
    private String waitTimeInSeconds;

    @Inject
    @Named(PropertiesModule.UNIQUE_ID)
    private String uniqueId;

    @Inject
    private IntegrationManager integrationManager;

    @Inject
    protected DesiredCapabilities capabilities;

    /**
     * Hack, but there is no way to get the session Id in other way.
     */
    protected String sessionId;

    /**
     * @return the wait time in seconds defined in the com.salesforceiq.augmenteddriver.properties (or 30 by default)
     */
    protected int waitTimeInSeconds() {
        return Integer.valueOf(Preconditions.checkNotNull(waitTimeInSeconds));
    }

    /**
     * @return Unique 10 digit Id for the run (tests will share it in the suite, or if a same test is running
     *         repeated times.
     */
    protected String getUniqueId() {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(uniqueId));

        return uniqueId;
    }

    /**
     * @return Unique name of the test, including the unique id, the class and the test name
     */
    protected String getFullTestName() {
        return String.format("%s:%s:%s", getUniqueId(), Util.shortenClass(this.getClass()), testName.getMethodName());
    }

    /**
     * Rule for executing code after the test finished, whether it failed or not.
     *
     * <p>
     *     IMPORTANT. It's implemented here since many if the data is generated at runtime.
     * </p>
     */
    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            if (Strings.isNullOrEmpty(sessionId)) return;

            integrationManager
                    .enabledIntegrations()
                    .stream()
                    .forEach(integration -> integration.testPassed(false, sessionId));
        }

        @Override
        protected void succeeded(Description description) {
            if (Strings.isNullOrEmpty(sessionId)) return;

            integrationManager
                    .enabledIntegrations()
                    .stream()
                    .forEach(integration -> integration.testPassed(true, sessionId));
        }
    };

    protected void triggerIntegrations() {
        integrationManager
                .enabledIntegrations()
                .stream()
                .forEach(integration -> {
                    integration.jobName(getFullTestName(), sessionId);
                    integration.buildName(getUniqueId(), sessionId);
                });

        integrationManager
                .enabledReportIntegrations()
                .stream()
                .forEach(integration -> integration.print(getFullTestName(), sessionId, integrationManager));
    }

}
