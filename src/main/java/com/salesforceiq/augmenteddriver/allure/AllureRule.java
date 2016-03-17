package com.salesforceiq.augmenteddriver.allure;

import com.google.common.base.Preconditions;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.integrations.IntegrationFactory;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.ClearStepStorageEvent;
import ru.yandex.qatools.allure.events.ClearTestStorageEvent;

@Singleton
public class AllureRule implements TestRule {

    private final Integer maxRetries;
    private final ConcurrentHashMultiset<String> countTests;
    private final IntegrationFactory integrationFactory;

    @Inject
    public AllureRule(@Named(PropertiesModule.MAX_RETRIES) String maxRetries,
                      IntegrationFactory integrationFactory) {
        this.maxRetries = Integer.valueOf(Preconditions.checkNotNull(maxRetries));
        this.countTests = ConcurrentHashMultiset.create();
        this.integrationFactory = Preconditions.checkNotNull(integrationFactory);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                countTests.add(getId(description));
                try {
                    base.evaluate();
                } catch (Throwable e) {
                    if (integrationFactory.allure().isEnabled() && countTests.count(getId(description)) < maxRetries) {
                        System.out.println("FAILED, RETRYING ATTEMPT " + countTests.count(getId(description)));
//                        Allure.LIFECYCLE.fire(new ClearStepStorageEvent());
//                        Allure.LIFECYCLE.fire(new ClearTestStorageEvent());
                    } else {
                        System.out.println("FAILED, NO MORE RETRIES");
                    }
                    throw e;
                }
            }
        };
    }

    private String getId(Description description) {
        return String.format("%s#%s", description.getClassName(), description.getMethodName());
    }
}
