package com.salesforceiq.augmenteddriver.examples;

import com.applitools.eyes.Eyes;
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
    @Title("Successful Test")
    public void testSucceed() {
        driver().get("https://www.wikipedia.org/");
        driver()
                .eyes()
                .instance()
                .checkWindow("Main Page");
        
        driver()
                .augmented()
                .findElementClickable(By.id("searchInput"))
                .sendKeys("WebDriver");
        driver()
                .eyes()
                .instance()
                .checkWindow("Search");
        driver()
                .augmented()
                .findElementsVisible(By.className("suggestion-link"))
                .get(0)
                .click();
        driver()
                .augmented()
                .findElementVisible(By.id("firstHeading"));
        driver()
                .eyes()
                .instance()
                .checkWindow("WebDriver Wiki");
    }

    @Test
    @Title("Failed Test")
    public void testFail() {
        count++;
        driver().get("https://www.wikipedia.org/");

        stepOne();
        if (count != 4433) {
            Assert.fail("THEREASON");
        }
    }

    @Step("STEP ONE")
    public void stepOne() {

    }

}
