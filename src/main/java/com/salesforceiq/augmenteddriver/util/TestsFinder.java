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


public class TestsFinder {

    private static final ClassLoader CLASS_LOADER = TestsFinder.class.getClassLoader();

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
