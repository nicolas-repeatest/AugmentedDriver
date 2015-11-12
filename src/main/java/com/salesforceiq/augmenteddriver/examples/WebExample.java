package com.salesforceiq.augmenteddriver.examples;

import com.salesforceiq.augmenteddriver.testcases.AugmentedWebTestCase;
import org.junit.Test;

public class WebExample extends AugmentedWebTestCase {

    @Test
    public void testOne() {
        driver().get("htpps://www.google.com");
    }
}
