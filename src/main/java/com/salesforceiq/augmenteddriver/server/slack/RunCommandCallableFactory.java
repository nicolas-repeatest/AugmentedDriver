package com.salesforceiq.augmenteddriver.server.slack;

import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;

public interface RunCommandCallableFactory {
    RunCommandCallable create(TestRunnerConfig config);
}
