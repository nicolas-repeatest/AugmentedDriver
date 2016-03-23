package com.salesforceiq.augmenteddriver.guice;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.List;

public class GuiceTestRunner extends BlockJUnit4ClassRunner {

    private final transient Injector injector;

    /**
     * Constructor.
     *
     * @param clazz The in test.
     * @throws InitializationError If something goes wrong.
     */
    public GuiceTestRunner(final Class<?> clazz) throws InitializationError {
        super(clazz);

        List<Class<? extends AbstractModule>> modules = getGuiceModulesFor(clazz);
        modules.addAll(getExtraModulesFor(clazz));
        this.injector = this.createInjectorFor(modules);
    }

    @Override
    public final Object createTest() throws Exception {
        final Object obj = super.createTest();
        this.injector.injectMembers(obj);
        return obj;
    }

    /**
     * Create a Guice Injector for the class under test.
     *
     * @param classes Guice Modules
     * @return A Guice Injector instance.
     * @throws InitializationError If couldn't instantiate a module.
     */
    private Injector createInjectorFor(final List<Class<? extends AbstractModule>> classes) throws InitializationError {
        List<AbstractModule> modules = Lists.newArrayList();

        for(Class<? extends AbstractModule> clazz : classes) {
            try {
                modules.add(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }

        return Guice.createInjector(modules);
    }

    private List<Class<? extends AbstractModule>> getGuiceModulesFor(final Class<?> clazz) throws InitializationError {
        final GuiceModules annotation = clazz.getAnnotation(GuiceModules.class);

        if (annotation == null) {
            final String message = String.format("Missing @GuiceModules annotation for unit test '%s'", clazz.getName());
            throw new InitializationError(message);
        }

        return Lists.newArrayList(annotation.value());
    }

    private List<Class<? extends AbstractModule>> getExtraModulesFor(final Class<?> clazz) throws InitializationError {
        ExtraModules annotation = clazz.getAnnotation(ExtraModules.class);
        return annotation == null ? Lists.newArrayList() : Lists.newArrayList(annotation.value());
    }
}