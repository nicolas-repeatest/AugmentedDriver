package com.salesforceiq.augmenteddriver.util;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.List;

/**
 * Provides functionality for finding the classes to run.
 */
public class TestsFinder {

    private static final ClassLoader CLASS_LOADER = TestsFinder.class.getClassLoader();

    /**
     * Finds the classes to test, given a list of suites and the root package.
     *
     * @param suites the Suites used to find classes (classes should be annotated with the Suites annotation)
     * @param suitesPackage the root package.
     * @return the List of classes that are annotated with any of the suites.
     * @throws IOException if failed to initialize the class loader.
     */
    @SuppressWarnings("unchecked")
    public static List<Class> getTestClassesOfPackage(List<String> suites, String suitesPackage) throws IOException {
        Preconditions.checkNotNull(suites);
        Preconditions.checkArgument(!suites.isEmpty());
        Preconditions.checkArgument(!Strings.isNullOrEmpty(suitesPackage));

        List<String> upperSuites = ImmutableList.copyOf(Lists.transform(suites, String::toUpperCase));
        return ImmutableList.copyOf(Iterables.filter(getAllClassesOfPackage(suitesPackage), clazz -> {
            if (clazz.isAnnotationPresent(Suites.class)) {
                Suites suitesAnnotation = (Suites) clazz.getAnnotation(Suites.class);
                return Lists.transform(
                        Lists.newArrayList(suitesAnnotation.value()), String::toUpperCase)
                        .stream()
                        .anyMatch(upperSuites::contains);
            }
            return false;
        }));
    }

    private static List<Class> getAllClassesOfPackage(String suitesPackage) throws IOException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(suitesPackage));

        ClassPath classPath = ClassPath.from(CLASS_LOADER);
        return ImmutableList.copyOf(Iterables.transform(classPath.getTopLevelClassesRecursive(suitesPackage), TO_CLASS));
    }

    private static final Function<ClassPath.ClassInfo, Class> TO_CLASS = classInfo -> {
        try {
            return CLASS_LOADER.loadClass(classInfo.getName());
        } catch (ClassNotFoundException e) {
            // Should never happen.... i think.
            throw new IllegalStateException(e);
        }
    };
}
