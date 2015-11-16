package com.salesforceiq.augmenteddriver.util;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.common.base.Preconditions;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.zip.CRC32;

public class YamlCapabilitiesConverter {

    private static final String CAPABILITIES = "capabilities";
    private static final String CHROME_EXTENSION = "chromeExtension";

    @SuppressWarnings("unchecked")
    public static DesiredCapabilities convert(Path yamlFile) throws YamlException {
        Preconditions.checkNotNull(yamlFile);
        Preconditions.checkArgument(Files.exists(yamlFile));
        try {
            YamlReader yamlReader = new YamlReader(new FileReader(yamlFile.toFile()));
            Map<String, String> properties = (Map<String, String>) yamlReader.read();
            if (!properties.containsKey(CAPABILITIES)) {
                throw new IllegalArgumentException(String.format("File %s should have property capabilities, got %s", yamlFile, properties));
            }
            DesiredCapabilities capabilities = Capabilities.valueOf(properties.remove(CAPABILITIES).toUpperCase()).getCapabilities();
            properties.entrySet()
                    .stream()
                    .forEach(pair -> capabilities.setCapability(pair.getKey(), pair.getValue()));

            if (properties.containsKey(CHROME_EXTENSION)) {
                ChromeOptions options = new ChromeOptions();
                options.addExtensions(new File(properties.get(CHROME_EXTENSION)));
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            }
            return capabilities;
        } catch (FileNotFoundException e) {
            // Thsi should never happen
            throw new IllegalStateException(e);
        }
    }

    private enum Capabilities {
        ANDROID(DesiredCapabilities.android()),
        CHROME(DesiredCapabilities.chrome()),
        FIREFOX(DesiredCapabilities.firefox()),
        EDGE(DesiredCapabilities.edge()),
        HTMLUNIT(DesiredCapabilities.htmlUnit()),
        HTMLUNITWITHJS(DesiredCapabilities.htmlUnitWithJs()),
        INTERNETEXPLORE(DesiredCapabilities.internetExplorer()),
        IPAD(DesiredCapabilities.ipad()),
        IPHONE(DesiredCapabilities.iphone()),
        OPERABLINK(DesiredCapabilities.operaBlink()),
        PHANTOMJS(DesiredCapabilities.phantomjs()),
        SAFARI(DesiredCapabilities.safari()),
        ;

        private final DesiredCapabilities capabilities;

        Capabilities(DesiredCapabilities capabilities) {
            this.capabilities = capabilities;
        }

        public DesiredCapabilities getCapabilities() {
            return capabilities;
        }
    }
}
