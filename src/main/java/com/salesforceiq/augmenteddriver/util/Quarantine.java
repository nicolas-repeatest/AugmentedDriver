package com.salesforceiq.augmenteddriver.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Any test that is quarantined will not be run.
 *
 * It is another way of ignoring tests, but it has a quarantine finder to
 * list those.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Quarantine {}
