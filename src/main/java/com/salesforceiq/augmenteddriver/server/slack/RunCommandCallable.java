package com.salesforceiq.augmenteddriver.server.slack;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;

import java.util.concurrent.Callable;

public class RunCommandCallable implements Callable<Void> {

    private final TestRunnerConfig config;

    @Inject
    public RunCommandCallable(@Assisted TestRunnerConfig config) {
        this.config = Preconditions.checkNotNull(config);
    }
    @Override
    public Void call() throws Exception {
//        runner.call();
        return null;
    }
}
