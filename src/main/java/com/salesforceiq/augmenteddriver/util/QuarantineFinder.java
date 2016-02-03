package com.salesforceiq.augmenteddriver.util;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;
import com.google.common.base.Preconditions;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for finding all the tests that are quarantined.
 */
public class QuarantineFinder {

    private static class QuarantineCommandLineArguments {
        private static QuarantineCommandLineArguments ARGUMENTS;

        private static QuarantineCommandLineArguments initialize(String[] args) {
            QuarantineCommandLineArguments result = new QuarantineCommandLineArguments();
            JCommander jCommander = new JCommander();
            jCommander.setAcceptUnknownOptions(true);
            jCommander.addObject(result);
            jCommander.parse(args);
            ARGUMENTS = result;
            return ARGUMENTS;
        }

        private List<String> suites() {
            Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
            return Arrays.asList(ARGUMENTS.suites.split(","));
        }

        private String suitesPackage() {
            Preconditions.checkNotNull(ARGUMENTS, "Call TestRunnerConfig#intialize first");
            return ARGUMENTS.suitesPackage;
        }

        @Parameter(names = "-suites", description = "Comma delimited suites to find quarantined tests")
        private String suites;

        @Parameter(names = "-suitesPackage", description = "Base package to grab the tests")
        private String suitesPackage;
    }

    /**
     * Given a set of suites and the starting root package, it will print all the tests that are quarantined.
     *
     * @param args command line arguments
     * @throws Exception if something went wrong.
     */
    public static void main(String[] args) throws Exception {
        QuarantineCommandLineArguments arguments = QuarantineCommandLineArguments.initialize(args);
        checkArguments(arguments);
        List<Class<?>> classes = TestsFinder.getTestClassesOfPackage(arguments.suites(), arguments.suitesPackage());
        System.out.println(String.format("Quarantined tests for suites %s in package %s",
                                                                                arguments.suites(),
                                                                                arguments.suitesPackage()));
        classes.stream()
                .forEach(test -> Lists.newArrayList(test.getMethods())
                        .stream()
                        .filter(method -> method.isAnnotationPresent(Test.class)
                                && !method.isAnnotationPresent(Ignore.class)
                                && method.isAnnotationPresent(Quarantine.class) == true)
                        .forEach(quarantinedTest-> {
                            System.out.println(String.format("%s:%s", quarantinedTest.getDeclaringClass().getCanonicalName(),
                                                                      quarantinedTest.getName()));
                        }));
    }

    private static void checkArguments(QuarantineCommandLineArguments arguments) {
        Preconditions.checkNotNull(arguments.suites(), "You should specify the suites to run");
        Preconditions.checkNotNull(arguments.suitesPackage(), "You should specify a the package to find the tests");
    }
}
