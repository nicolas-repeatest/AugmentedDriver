package com.salesforceiq.augmenteddriver.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

public class TestRunnerConfigTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
}
