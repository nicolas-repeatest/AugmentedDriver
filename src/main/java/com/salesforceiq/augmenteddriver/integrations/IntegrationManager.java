package com.salesforceiq.augmenteddriver.integrations;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IntegrationManager {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationManager.class);

    public static String[] args;

    @Inject
    private final List<Integration> integrations = new LinkedList<>();

    @Inject
    private final List<ReportIntegration> reportIntegrations = new LinkedList<>();
    
    @Inject
    private Properties properties;

    public void initIntegrations() {
        logger.info("Initializing Integrations");

        if (IntegrationManager.args == null) {
            throw new IllegalArgumentException("IntegrationManager args was not defined");
        }

        enabledIntegrations()
                .forEach(integration -> initWithCommandLineArgs(integration));

        enabledReportIntegrations()
                .forEach(report -> initWithCommandLineArgs(report));
        
        enabledIntegrations()
        		.forEach(integration -> integration.initialize(properties));
        
        enabledReportIntegrations()
				.forEach(report -> report.initialize(properties));
    }
    
    private void initWithCommandLineArgs(Object integration) {
        JCommander jCommander = new JCommander();
        jCommander.setAcceptUnknownOptions(true);
        jCommander.addObject(integration);
        jCommander.parse(args);
    }

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
        if (name == null) {        	
        	return false;
        }

        boolean match = enabledIntegrations()
                .stream()
                .anyMatch(integration -> name.equalsIgnoreCase(integration.name()));

        return match || enabledReportIntegrations()
                .stream()
                .anyMatch(integration -> name.equalsIgnoreCase(integration.name()));
    }

}
