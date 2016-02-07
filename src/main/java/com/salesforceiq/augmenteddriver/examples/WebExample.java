package com.salesforceiq.augmenteddriver.examples;

import com.salesforceiq.augmenteddriver.testcases.AugmentedWebTestCase;
import com.salesforceiq.augmenteddriver.util.Util;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;

public class WebExample extends AugmentedWebTestCase {

    @Test
    @Title("Succesfull Test")
    public void testOne() {

//        goToWikiPedia();
    }

    @Step("STEP {method} REASON {0}")
    private void failForReason(String reason) {
        Assert.fail(reason);
    }

    @Step("STEP {method}")
    private void checkThere() {
        assertElementIsClickable(By.id("searchInput"));
        failForReason("THE REASON");
    }

    @Step("STEP {method}")
    public void goToWikiPedia() {
        driver().get("https://www.wikipedia.org/");
        checkThere();
    }
}
