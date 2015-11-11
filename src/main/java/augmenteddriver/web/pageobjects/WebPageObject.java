package augmenteddriver.web.pageobjects;

import com.google.inject.Inject;
import com.google.inject.Provider;
import augmenteddriver.util.PageObjectAssertionsInterface;
import augmenteddriver.web.AugmentedWebDriver;
import augmenteddriver.web.AugmentedWebElement;
import augmenteddriver.web.AugmentedWebFunctions;
import augmenteddriver.util.PageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for WebPages with a container.
 */
public abstract class WebPageObject implements WebPageObjectActionsInterface, PageObjectAssertionsInterface, PageObject {
    private static final Logger LOG = LoggerFactory.getLogger(WebPageObject.class);

    /**
     * Important we use a Provider, since we need the driver to be initialized when the first test starts to run
     * not at creation time, like Guice wants.
     */
    @Inject
    private Provider<AugmentedWebDriver> driverProvider;

    @Inject
    private WebPageObjectActions webPageObjectActions;

    @Override
    public <T extends WebPageObject> T get(Class<T> clazz) {
        return webPageObjectActions.get(clazz);
    }

    @Override
    public <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container) {
        return webPageObjectActions.get(clazz, container);
    }

    @Override
    public <T extends WebPageObject> T action(Action action, Class<T> landingPageObject) {
        return webPageObjectActions.action(action, landingPageObject);
    }

    @Override
    public <T extends WebPageContainerObject> T action(ActionContainer action, AugmentedWebElement container, Class<T> landingPageObject) {
        return webPageObjectActions.action(action, container, landingPageObject);
    }

    @Override
    public void assertPresent() {
        if (visibleBy().isPresent()) {
            augmented().findElementsVisible(visibleBy().get());
        }
    }

    @Override
    public AugmentedWebDriver driver() {
        return driverProvider.get();
    }

    @Override
    public AugmentedWebFunctions augmented() {
        return driverProvider.get().augmented();
    }

}
