package com.salesforceiq.augmenteddriver.util;

import com.google.common.base.Preconditions;
import org.openqa.selenium.By;

import java.util.Optional;

/**
 * Common assertions for al Page Objects (Web, Android, IOS).
 */
public interface PageObjectAssertionsInterface {
    /**
     * Asserts that the PageObject is present.
     *
     * If this method is not overriden, it will simple check the the element defined in visibleBy is visible
     */
    void assertPresent();

    /**
     * Should return which element has to be visible to assert the Page Object is present.
     *
     * If the optional is empty, it will not be asserted that the page is present.
     *
     * <p>
     *     All Page Objects should implement it, so the framework can check that the page is in  the
     *     correct state.
     * </p>
     *
     * @return  the optional containing the by that should show up.
     */
    Optional<By> visibleBy();
}
