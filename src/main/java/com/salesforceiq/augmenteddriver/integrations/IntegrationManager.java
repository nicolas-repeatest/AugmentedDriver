package com.salesforceiq.augmenteddriver.integrations;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class IntegrationManager {

    @Inject
    private final List<Integration> integrations = new LinkedList<>();

    @Inject
    private final List<ReportIntegration> reportIntegrations = new LinkedList<>();

    public List<Integration> enabledIntegrations() {
        return integrations
                .stream()
                .filter(integration -> integration.isEnabled())
                .collect(Collectors.toList());
    }

    public List<ReportIntegration> enabledReportIntegrations() {
        return reportIntegrations
                .stream()
                .filter(integration -> integration.isEnabled())
                .collect(Collectors.toList());
    }

    public boolean containsIntegration(String name) {
        if (name == null) return false;

        return enabledIntegrations()
                .stream()
                .anyMatch(integration -> name.equalsIgnoreCase(integration.name()));
    }

    public boolean containsReportIntegration(String name) {
        if (name == null) return false;

        return enabledReportIntegrations()
                .stream()
                .anyMatch(integration -> name.equalsIgnoreCase(integration.name()));
    }

}
