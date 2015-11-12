package augmenteddriver.examples;

import augmenteddriver.testcases.AugmentedWebTestCase;
import org.junit.Test;

public class WebExample extends AugmentedWebTestCase {

    @Test
    public void testOne() {
        driver().get("htpps://www.google.com");
    }
}
