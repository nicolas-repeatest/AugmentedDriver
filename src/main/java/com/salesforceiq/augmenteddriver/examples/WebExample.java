package com.salesforceiq.augmenteddriver.examples;

import com.salesforceiq.augmenteddriver.testcases.AugmentedWebTestCase;
import com.salesforceiq.augmenteddriver.util.Suites;
import com.salesforceiq.augmenteddriver.util.Util;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;

@Suites("SMOKE")
public class WebExample extends AugmentedWebTestCase {


    public static int count = 0;
    @Test
    @Title("Succesfull Test")
    public void testSucceed() {
        driver().get("https://www.wikipedia.org/");
    }

    @Test
    @Title("Failed Test")
    public void testFail() {
        count++;
        driver().get("https://www.wikipedia.org/");
        if (count != 1) {
            Assert.fail("THEREASON");
        }
    }

}
