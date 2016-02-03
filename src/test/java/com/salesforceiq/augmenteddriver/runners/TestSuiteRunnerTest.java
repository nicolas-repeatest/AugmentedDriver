package com.salesforceiq.augmenteddriver.runners;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.salesforceiq.augmenteddriver.integrations.IntegrationManager;
import com.salesforceiq.augmenteddriver.util.Quarantine;
import com.salesforceiq.augmenteddriver.util.TestRunnerConfig;

public class TestSuiteRunnerTest {

    private TestSuiteRunner runner;

    @Before
    public void setUp() {
    	runner = buildTestRunner(mock(TestRunnerConfig.class));
    }

    @Test
    public void methodWithoutTestAnnotationShouldNotBeValid() throws Exception {
        assertFalse(isValidTest("method1"));
    }

    @Test
    public void methodWithIgnoreAnnotationShouldNotBeValid() throws Exception {
        assertFalse(isValidTest("method2"));
        assertFalse(isValidTest("method3"));
    }

    @Test
    public void methodWithQuarantineShouldNotBeValid() throws Exception {
        assertFalse(isValidTest("method4"));
    }

    @Test
    public void methodWithTestAnnotationWithoutIgnoreAndQuarantineShouldBeValid() throws Exception {
        assertTrue(isValidTest("method5"));
    }
    
    @Test
    public void methodWithQuarantineShouldBeValidIfQuarantineIsEnabled() throws Exception {
    	TestRunnerConfig testRunnerConfig = mock(TestRunnerConfig.class);
    	when(testRunnerConfig.quarantine()).thenReturn(true);
    	runner = buildTestRunner(testRunnerConfig);
    	
    	assertTrue(isValidTest("method4"));
    }
    
    private TestSuiteRunner buildTestRunner(TestRunnerConfig testRunnerConfig) {
        return new TestSuiteRunner(
                testRunnerConfig,
                mock(TestRunnerFactory.class),
                "2",
                mock(IntegrationManager.class)
        );
    }

    private boolean isValidTest(String method) throws NoSuchMethodException {
        return runner.validTest().test(TestStub.class.getMethod(method));
    }

    class TestStub {
        public void method1() { }

        @Test @Ignore public void method2() {}

        @Test @Ignore @Quarantine public void method3() {}

        @Test @Quarantine public void method4() { }

        @Test public void method5() { }
    }

}
