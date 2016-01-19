package com.salesforceiq.augmenteddriver.util.saucelabs;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;
import com.google.common.base.Preconditions;

import java.nio.file.Path;

/**
 * Command line arguments for uploading files to SauceLabs.
 */
public class SauceCommandLineArguments {

    public static SauceCommandLineArguments ARGUMENTS;

    /**
     * Initializes the configuration.
     *
     * @param args the Command line arguments of the main method.
     * @return the configuration for the run.
     */
    public static SauceCommandLineArguments initialize(String[] args) {
        SauceCommandLineArguments result = new SauceCommandLineArguments();
        JCommander jCommander = new JCommander();
        jCommander.setAcceptUnknownOptions(true);
        jCommander.addObject(result);
        jCommander.parse(args);
        ARGUMENTS = result;
        return ARGUMENTS;
    }

    /**
     * @return file to upload to SauceLabs.
     */
    public Path file() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#initialize first");
        return ARGUMENTS.fileToUpload;
    }

    /**
     * @return whether to overwrite or not a file if it exists already.
     */
    public boolean overwrite() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#initialize first");
        return ARGUMENTS.overwrite;
    }

    /**
     * @return path to the properties file with the configuration.
     */
    public String conf() {
        Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#initialize first");
        return ARGUMENTS.conf;
    }

    @Parameter(names = "-file", description = "Path to file to upload", converter = PathConverter.class)
    private Path fileToUpload;

    @Parameter(names = "-overwrite", description = "Whether to overwrite or not the file in case it exists in SauceLabs")
    private boolean overwrite = true;

    @Parameter(names = "-conf", description = "Path to the properties file, conf/augmented.properties by default")
    private String conf = "conf/augmented.properties";

}
