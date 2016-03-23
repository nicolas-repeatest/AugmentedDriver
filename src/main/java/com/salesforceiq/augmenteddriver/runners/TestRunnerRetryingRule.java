package com.salesforceiq.augmenteddriver.runners;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.integrations.IntegrationFactory;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.ClearStepStorageEvent;
import ru.yandex.qatools.allure.events.RemoveAttachmentsEvent;

/**
 * Rule for retrying failing tests.
 */
@Singleton
public class TestRunnerRetryingRule implements TestRule {
    private static final Logger LOG = LoggerFactory.getLogger(TestRunnerRetryingRule.class);

    private final Integer maxRetries;
    private final IntegrationFactory integrationFactory;
    private static boolean retry = true;

    @Inject
    public TestRunnerRetryingRule(@Named(PropertiesModule.MAX_RETRIES) String maxRetries,
                                  IntegrationFactory integrationFactory) {
        this.maxRetries = Integer.valueOf(Preconditions.checkNotNull(maxRetries));
        this.integrationFactory = Preconditions.checkNotNull(integrationFactory);
    }

    /**
     * Hack since we call JunitCore#run, and we don't have a way to send parameters.
     */
    public static void noRetry() {
        retry = false;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if (retry) {
                    Throwable e = null;
                    for (int attempt = 0; attempt < maxRetries; attempt++) {
                        try {
                            if (integrationFactory.allure().isEnabled()) {
                                /**
                                 * Remove all attachments from failed test before retry.
                                 */
                                Allure.LIFECYCLE.fire(new RemoveAttachmentsEvent(".*"));
                                /**
                                 * Remove all steps from failed test before retry.
                                 */
                                Allure.LIFECYCLE.fire(new ClearStepStorageEvent());
                            } 
                            base.evaluate();
                            return;
                        } catch (Throwable throwable) {
                            LOG.warn(String.format("Test %s#%s failed, attempt %s of %s",
                                    description.getClassName(), description.getMethodName(), attempt + 1, maxRetries + 1));
                            e = throwable;
                        }
                    }
                    LOG.error(String.format("Test %s#%s failed, all %s attempts",
                            description.getClassName(), description.getMethodName(), maxRetries + 1));
                    throw e;
                } else {
                    base.evaluate();
                }
            }
        };
    }
}
