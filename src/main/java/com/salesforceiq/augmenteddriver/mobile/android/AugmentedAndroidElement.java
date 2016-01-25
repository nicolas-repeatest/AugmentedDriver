package com.salesforceiq.augmenteddriver.mobile.android;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.salesforceiq.augmenteddriver.web.AugmentedWebFunctionsFactory;
import org.openqa.selenium.*;

import java.util.List;

/**
 * Wrapper of WebElement that also provides augmented functionality.
 */
public class AugmentedAndroidElement implements WebElement {
    private final WebElement webElement;
    private final AugmentedAndroidFunctions augmentedFunctions;

    @Inject
    public AugmentedAndroidElement(@Assisted WebElement webElement,
                                   AugmentedAndroidFunctionsFactory augmentedFunctions) {
        this.webElement = Preconditions.checkNotNull(webElement);
        this.augmentedFunctions = Preconditions.checkNotNull(augmentedFunctions.create(webElement));
    }

    /**
     * @return the augmented functionality on top of the web element.
     */
    public AugmentedAndroidFunctions augmented() {
        return augmentedFunctions;
    }

    /**
     * @return the wrapped web element.
     */
    public WebElement webElement() {
        return webElement;
    }

    @Override
    public void click() {
        webElement.click();
    }

    @Override
    public void submit() {
        webElement.submit();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        webElement.sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        webElement.clear();
    }

    @Override
    public String getTagName() {
        return webElement.getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return webElement.getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return webElement.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return webElement.isEnabled();
    }

    @Override
    public String getText() {
        return webElement.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return webElement.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return webElement.findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        return webElement.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return webElement.getLocation();
    }

    @Override
    public Dimension getSize() {
        return webElement.getSize();
    }

    @Override
    public Rectangle getRect() {
        return webElement.getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return webElement.getCssValue(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return webElement.getScreenshotAs(target);
    }
}
