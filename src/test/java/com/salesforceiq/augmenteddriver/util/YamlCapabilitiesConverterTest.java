package com.salesforceiq.augmenteddriver.util;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;

public class YamlCapabilitiesConverterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testConvert() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File origin = new File(classLoader.getResource("converttest.yaml").getFile());
        File dest = folder.newFile();
        FileUtils.copyFile(origin, dest);
        DesiredCapabilities convert = YamlCapabilitiesConverter.convert(dest.toPath());
        Assert.assertEquals("chrome", convert.getBrowserName());
        Assert.assertEquals("1280x1024", convert.getCapability("screenResolution"));
        Assert.assertEquals("47.0", convert.getVersion());
        Assert.assertEquals(Platform.fromString("OS X 10.10"), convert.getPlatform());
    }
}
