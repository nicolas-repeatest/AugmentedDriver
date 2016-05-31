package com.salesforceiq.augmenteddriver.util;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.esotericsoftware.yamlbeans.YamlException;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Where all the configuration parameters of the test reside (from command line arguments and Properties file)
 */
public class TestRunnerConfig {

    private static Class<?> hackClass;
    public static TestRunnerConfig ARGUMENTS;

    /**
     * Initializes the configuration with the command line arguments.
     *
     * @param commandLineArgs the command line arguments that come from the java main method.
     * @return the Test Configuration.
     */
    public static TestRunnerConfig initialize(String[] commandLineArgs) {
        Preconditions.checkNotNull(commandLineArgs);

        TestRunnerConfig result = new TestRunnerConfig();
        JCommander jCommander = new JCommander();
        jCommander.setAcceptUnknownOptions(true);
        jCommander.addObject(result);
        jCommander.parse(commandLineArgs);
        ARGUMENTS = result;
        return ARGUMENTS;
    }

    /**
     * Initializes the configuration with a properties file.
     *
     * @param properties the properties file to use.
     * @return the Test Configuration.
     */
    public static TestRunnerConfig initialize(Properties properties) {
        Preconditions.checkNotNull(properties);

        TestRunnerConfig result = new TestRunnerConfig();

        if (properties.get(PropertiesModule.CAPABILITIES) != null) {
            result.capabilities = new CapabilitiesConverter().convert((String) properties.get(PropertiesModule.CAPABILITIES));
        }

        if (properties.get(PropertiesModule.SAUCE) != null) {
            result.sauce = Boolean.valueOf((String) properties.get(PropertiesModule.SAUCE));
        }

        if (properties.get(PropertiesModule.PARALLEL) != null) {
            result.parallel = Integer.valueOf((String) properties.get(PropertiesModule.PARALLEL));
        }

        if (properties.get(PropertiesModule.SUITES) != null) {
            result.suites = (String) properties.get(PropertiesModule.SUITES);
        }

        if (properties.get(PropertiesModule.SUITES_PACKAGE) != null) {
            result.suitesPackage = (String) properties.get(PropertiesModule.SUITES_PACKAGE);
        }

        ARGUMENTS = result;
        return ARGUMENTS;
    }


    /**
     * @return Test Class to run (for running one test).
     */
    public Class<?> clazz() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.clazz;
    }

    /**
     * @return Method name to run (for running one test).
     */
    public Method test() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.test;
    }

    /**
     * @return Suites to run (for running suites).
     */
    public List<String> suites() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return Arrays.asList(ARGUMENTS.suites.split(","));
    }

    /**
     * @return root package.
     */
    public String suitesPackage() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.suitesPackage;
    }

    /**
     * @return How many times to run a test (for running one test).
     */
    public int quantity() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.quantity;
    }

    /**
     * @return How many tests in parallel can be run.
     */
    public int parallel() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.parallel;
    }

    /**
     * @return whether is running on sauce or not.
     */
    public boolean sauce() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.sauce;
    }

    /**
     * @return whether to run quarantined tests or not.
     */
    public boolean quarantine() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.quarantine;
    }

    /**
     * @return Capabilities used to run the test/suite.
     */
    public DesiredCapabilities capabilities() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.capabilities;
    }

    /**
     * @return Pointing to the properties file with the configuration.
     */
    public String conf() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.conf;
    }

    /**
     * @return Path to the apk or the zip with the android/ios app to run.
     */
    public String app() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.app;
    }

    /**
     * @return extra parameters.
     */
    public Map<String, String> extra() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
        return ARGUMENTS.extra;
    }

    @Parameter(names = "-clazz", description = "Class to run", converter = ClassConverter.class)
    private Class<?> clazz;

    @Parameter(names = "-test", description = "Test to run", converter = MethodConverter.class)
    private Method test;

    @Parameter(names = "-suites", description = "Comma delimited suites to run")
    private String suites;

    @Parameter(names = "-suitesPackage", description = "Base package to grab the tests")
    private String suitesPackage;

    @Parameter(names = "-quantity", description = "How many times the test is going to run")
    private Integer quantity = 1;

    @Parameter(names = "-parallel", description = "How many tests in parallel are going to run")
    private Integer parallel = 1;

    @Parameter(names = "-sauce", description = "Whether to run tests on SauceLabs or not")
    private boolean sauce = false;

    @Parameter(names = "-quarantine", description = "Run quarantined tests")
    private boolean quarantine = false;

    @Parameter(names = "-capabilities", description = "Path to the YAML with the desired capabilities", converter = CapabilitiesConverter.class)
    private DesiredCapabilities capabilities;

    @Parameter(names = "-conf", description = "Path to the properties file, conf/augmented.properties by default")
    private String conf = PropertiesModule.DEFAULT_CONFIG;

    @Parameter(names = "-app", description = "Path to file to use as app (IOS) or apk (Android)")
    private String app = "";

    @Parameter(names = "-extra", description = "Extra parameters that are going to be injected (comma delimited)", converter = ExtraArgumentsConverter.class)
    private Map<String, String> extra = Maps.newHashMap();

    public static class ClassConverter implements IStringConverter<Class<?>> {
        @Override
        public Class<?> convert(String value) {
            try {
                Class<?> theClass = Class.forName(value);
                TestRunnerConfig.hackClass = theClass;
                return theClass;
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(String.format("Parameter -clazz should be a class, got : %s", value));
            }
        }
    }

    public static class CapabilitiesConverter implements IStringConverter<DesiredCapabilities> {
        @Override
        public DesiredCapabilities convert(String path) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "-capabilities should contain an argument");
            Path yamlPath = Paths.get(path);
            if (!Files.exists(yamlPath)) {
                throw new IllegalArgumentException(String.format("File %s does not exist", yamlPath));
            }
            try {
                return YamlCapabilitiesConverter.convert(yamlPath);
            } catch (YamlException e) {
                throw new IllegalArgumentException(String.format("File %s cannot be parsed as YAML file", yamlPath), e);
            }
        }
    }

    public static class MethodConverter implements IStringConverter<Method> {
        @Override
        public Method convert(String value) {
            try {
                Preconditions.checkArgument(hackClass != null, "Need to declare -clazz argument first");
                return hackClass.getMethod(value);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(String.format("Parameter -test should be a method of %s, got %s", hackClass.getCanonicalName(), value));
            }
        }
    }

    public static class ExtraArgumentsConverter implements  IStringConverter<Map<String, String>> {
        @Override
        public Map<String, String> convert(String extraArguments) {
            Map<String, String> result = Maps.newHashMap();
            if (!Strings.isNullOrEmpty(extraArguments)) {
                Iterable<String> arguments = Splitter
                        // Split by comma, but ignoring commas that come between quotes.
                        // http://stackoverflow.com/questions/1757065/java-splitting-a-comma-separated-string-but-ignoring-commas-in-quotes
                        .on(Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"))
                        .split(extraArguments);
                arguments.forEach(argument -> {
                    // Extra checks just to make sure there is key value.
                    String[] splitted = argument.split("=", 2);
                    if (splitted.length != 2) {
                        throw new IllegalArgumentException(String.format("Argument %s is not well formatted", argument));
                    }
                    if (Strings.isNullOrEmpty(splitted[0])) {
                        throw new IllegalArgumentException(String.format("Key of argument %s is empty", argument));
                    }
                    if (Strings.isNullOrEmpty(splitted[1])) {
                        throw new IllegalArgumentException(String.format("Value of argument %s is empty", argument));
                    }
                    //Remove the quote at the beginning and the end if there is one.
                    String key = splitted[0].replaceAll("^\"|\"$", "");
                    String value = splitted[1].replaceAll("^\"|\"$", "");
                    result.put(key, value);
                });
            }
            return result;
        }
    }
}
