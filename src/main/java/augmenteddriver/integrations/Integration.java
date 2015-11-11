package augmenteddriver.integrations;

/**
 * Markup interface for all integrations.
 */
public interface Integration {
    /**
     * Determines whether the integration is enabled or not.
     */
    boolean isEnabled();
}
