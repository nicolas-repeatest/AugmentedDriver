package com.salesforceiq.augmenteddriver.guice;

import com.google.inject.AbstractModule;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GuiceModules {

    /**
     * The Guice Modules classes needed by the class under test.
     *
     * @return all the Module needed by guice to initialize.
     */
    Class<? extends AbstractModule>[] value();
}