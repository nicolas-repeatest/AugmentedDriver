package augmenteddriver.mobile.android.pageobjects;

import augmenteddriver.mobile.android.AugmentedAndroidDriver;
import augmenteddriver.mobile.android.AugmentedAndroidElement;

/**
 * Generic interface for executing an action in a container.
 */
public interface ActionContainer {
    void execute(AugmentedAndroidDriver driver, AugmentedAndroidElement container);
}
