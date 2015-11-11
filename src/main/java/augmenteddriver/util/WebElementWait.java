package augmenteddriver.util;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.SystemClock;

import java.util.concurrent.TimeUnit;

class WebElementWait extends FluentWait<SearchContext> {

    public WebElementWait(SearchContext element, long timeoutInSeconds) {
        super(element, new SystemClock(), Sleeper.SYSTEM_SLEEPER);
        withTimeout(timeoutInSeconds, TimeUnit.SECONDS);
        pollingEvery(500, TimeUnit.MILLISECONDS);
        ignoring(WebDriverException.class);
    }
}
