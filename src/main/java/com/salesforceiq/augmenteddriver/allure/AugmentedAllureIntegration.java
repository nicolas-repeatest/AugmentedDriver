package com.salesforceiq.augmenteddriver.allure;

import com.google.common.base.Preconditions;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import ru.yandex.qatools.allure.junit.AllureRunListener;

@Singleton
public class AugmentedAllureIntegration extends RunListener {

    private final int maxRetries;

    private final Multiset<String> countTests;
    private final AllureRunListener allureListener;

    @Inject
    public AugmentedAllureIntegration(@Named(PropertiesModule.MAX_RETRIES) String maxRetries) {
        this.maxRetries = Integer.valueOf(Preconditions.checkNotNull(maxRetries));
        this.countTests = ConcurrentHashMultiset.create();
        this.allureListener = new AllureRunListener();
    }

    @Override
    public void testStarted(Description description) {
        countTests.add(getId(description));
        allureListener.testStarted(description);
    }

    @Override
    public void testFailure(Failure failure) {
        if (countTests.count(failure.getDescription()) < maxRetries) {
            allureListener.testIgnored(failure.getDescription());
        } else {
            allureListener.testFailure(failure);
        }
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        allureListener.testAssumptionFailure(failure);
    }

    @Override
    public void testIgnored(Description description) {
        allureListener.testIgnored(description);
    }

    @Override
    public void testFinished(Description description) {
        allureListener.testFinished(description);
    }

    @Override
    public void testRunFinished(Result result) {
        allureListener.testRunFinished(result);
    }

    private String getId(Description description) {
        Preconditions.checkNotNull(description);

        return String.format("%s#%s", description.getClassName(), description.getMethodName());
    }
}
