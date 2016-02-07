package com.salesforceiq.augmenteddriver.integrations;


import barrypitman.junitXmlFormatter.AntXmlRunListener;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.reporters.TeamCityReporter;
import org.junit.runner.notification.RunListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Integration for Jenkins, writes results to an XML format that Jenkins reads.
 */
public class JenkinsIntegration implements Integration {

    private final boolean jenkinsIntegration;
    private final String jenkinsXML;

    @Inject
    public JenkinsIntegration(@Named(PropertiesModule.JENKINS_INTEGRATION) String jenkinsIntegration,
                              @Named(PropertiesModule.JENKINS_XML) String jenkinsXML) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(jenkinsIntegration));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(jenkinsXML));

        this.jenkinsIntegration = Boolean.valueOf(jenkinsIntegration);
        this.jenkinsXML = jenkinsXML

    }

    /**
     * Creates a reporter that saves the results in an XML that Jenkins will parse later.
     *
     * @return the Reporter.
     */
    public RunListener getReporter() throws FileNotFoundException {
        AntXmlRunListener jenkinsRunListener = new AntXmlRunListener();
        jenkinsRunListener.setOutputStream(new FileOutputStream(new File(jenkinsXML)));
        return jenkinsRunListener;
    }


    @Override
    public boolean isEnabled() {
        return jenkinsIntegration;
    }
}
