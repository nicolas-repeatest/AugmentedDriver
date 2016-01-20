package com.salesforceiq.augmenteddriver.integrations;

/**
 * Markup interface for all integrations.
 */
public interface Integration {

    /**
     * Determines whether the integration is enabled or not.
     *
     * @return whether the integration is enabled or not.
     */
    boolean isEnabled();

    void testPassed(boolean testPassed, String sessionId);

    void jobName(String testName, String sessionId);

    void buildName(String testName, String sessionId);

    String name();

}
