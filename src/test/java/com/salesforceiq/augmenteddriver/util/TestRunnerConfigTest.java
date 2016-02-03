package com.salesforceiq.augmenteddriver.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.openqa.selenium.Platform;

import com.salesforceiq.augmenteddriver.integrations.SauceLabsIntegration;
import com.saucelabs.saucerest.SauceREST;

public class TestRunnerConfigTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testEmptyArgsIsNotAccepted() {
        TestRunnerConfig.ExtraArgumentsConverter converter = new TestRunnerConfig.ExtraArgumentsConverter();
        Map<String, String> convert = converter.convert("");
        Assert.assertEquals(0, convert.size());
    }

    @Test
    public void testEmptyExtraArgumentIsNotAccepted() {
        thrown.expect(IllegalArgumentException.class);
        TestRunnerConfig.ExtraArgumentsConverter converter = new TestRunnerConfig.ExtraArgumentsConverter();
        converter.convert("TEST=2,TEST2=");
    }

    @Test
    public void testSingleArgument() {
        TestRunnerConfig.ExtraArgumentsConverter converter = new TestRunnerConfig.ExtraArgumentsConverter();
        Map<String, String> convert = converter.convert("TEST=2");
        Assert.assertTrue(convert.containsKey("TEST"));
        Assert.assertEquals("2", convert.get("TEST"));
        convert = converter.convert("\"TEST\"=\"2\"");
        Assert.assertTrue(convert.containsKey("TEST"));
        Assert.assertEquals("2", convert.get("TEST"));
    }

    @Test
    public void testThreeArguments() {
        TestRunnerConfig.ExtraArgumentsConverter converter = new TestRunnerConfig.ExtraArgumentsConverter();
        Map<String, String> convert = converter.convert("TEST=FirstArgument,TEST2=\"Second argument\",TEST3=\" Third , argument\"");
        Assert.assertTrue(convert.containsKey("TEST"));
        Assert.assertEquals("FirstArgument", convert.get("TEST"));
        Assert.assertTrue(convert.containsKey("TEST2"));
        Assert.assertEquals("Second argument", convert.get("TEST2"));
        Assert.assertTrue(convert.containsKey("TEST3"));
        Assert.assertEquals(" Third , argument", convert.get("TEST3"));
        Assert.assertEquals(3, convert.size());
    }

    @Test
    public void testInitializeSauceFromProperties() {
        Properties properties = new Properties();
        
        SauceLabsIntegration integration = new SauceLabsIntegration(mock(SauceREST.class));
        assertFalse(integration.isEnabled());
        
        properties.setProperty("SAUCE", "true");
        integration.initialize(properties);
        assertTrue(integration.isEnabled());
        
        properties.setProperty("SAUCE", "false");
        integration.initialize(properties);
        assertFalse(integration.isEnabled());
    }

    @Test
    public void testInitializeCapabilitiesFromProperties() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File origin = new File(classLoader.getResource("converttest.yaml").getFile());
        File dest = folder.newFile();
        FileUtils.copyFile(origin, dest);

        Properties example = new Properties();
        example.setProperty("CAPABILITIES", dest.getCanonicalPath());
        TestRunnerConfig config = TestRunnerConfig.initialize(example);
        Assert.assertEquals("chrome", config.capabilities().getBrowserName());
        Assert.assertEquals("1280x1024", config.capabilities().getCapability("screenResolution"));
        Assert.assertEquals("47.0", config.capabilities().getVersion());
        Assert.assertEquals(Platform.fromString("OS X 10.10"), config.capabilities().getPlatform());
    }
    
}
