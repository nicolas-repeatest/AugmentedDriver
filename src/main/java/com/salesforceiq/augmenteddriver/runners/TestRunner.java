package com.salesforceiq.augmenteddriver.runners;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.salesforceiq.augmenteddriver.integrations.IntegrationManager;
import com.salesforceiq.augmenteddriver.util.Util;

/**
 * Knows how to run one test.
 */
public class TestRunner implements Callable<AugmentedResult> {
    private static final Log logger = LogFactory.getLog(TestRunner.class);

    private final Method test;
    private final ByteArrayOutputStream output;
    private final IntegrationManager integrationManager;

    @Inject
    public TestRunner(@Assisted Method test, ByteArrayOutputStream outputStream, IntegrationManager integrationManager) {
        this.test = Preconditions.checkNotNull(test);
        this.output = Preconditions.checkNotNull(outputStream);
        this.integrationManager = Preconditions.checkNotNull(integrationManager);
    }

    @Override
    public AugmentedResult call() throws Exception {
        integrationManager.initIntegrations();

        JUnitCore jUnitCore = getJUnitCore();
        String testName = String.format("%s#%s", test.getDeclaringClass().getCanonicalName(), test.getName());
        long start = System.currentTimeMillis();

        try {
            logger.info(String.format("STARTING Test %s", testName));
            Result result = jUnitCore.run(Request.method(test.getDeclaringClass(), test.getName()));
            logger.info(String.format("FINISHED Test %s in %s", testName, Util.TO_PRETTY_FORMAT.apply(System.currentTimeMillis() - start)));
            return new AugmentedResult(result, output);
        } finally {
            output.close();
        }
    }

    private JUnitCore getJUnitCore() {
        JUnitCore jUnitCore = new JUnitCore();

        integrationManager
                .enabledReportIntegrations()
                .forEach(integration -> jUnitCore.addListener(integration.getReporter(output)));

        return jUnitCore;
    }
    
}
