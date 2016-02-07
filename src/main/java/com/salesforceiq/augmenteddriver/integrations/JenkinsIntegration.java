package com.salesforceiq.augmenteddriver.integrations;


import barrypitman.junitXmlFormatter.AntXmlRunListener;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.Util;
import org.junit.runner.notification.RunListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

/**
 * Integration for Jenkins, writes results to an XML format that Jenkins reads.
 */
public class JenkinsIntegration implements Integration {

    private final boolean jenkinsIntegration;
    private final File jenkinsXMLDir;

    @Inject
    public JenkinsIntegration(@Named(PropertiesModule.JENKINS_INTEGRATION) String jenkinsIntegration,
                              @Named(PropertiesModule.JENKINS_XML_DIR) String jenkinsXMLDir) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(jenkinsIntegration));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(jenkinsXMLDir));

        this.jenkinsIntegration = Boolean.valueOf(jenkinsIntegration);
        this.jenkinsXMLDir  = new File(jenkinsXMLDir);
        if (this.jenkinsIntegration && this.jenkinsXMLDir.exists()) {
            this.jenkinsXMLDir.mkdir();
        }
    }

    /**
     * Creates a reporter that saves the results in an XML that Jenkins will parse later.
     *
     * @return the Reporter.
     * @param test Test that is running
     * @param nameAppender Name appender, if it has.
     */
    public RunListener getReporter(Method test, String nameAppender) throws FileNotFoundException {
        Preconditions.checkNotNull(test);
        Preconditions.checkNotNull(nameAppender);

        File resultFile = new File(jenkinsXMLDir, String.format("%s:%s%s.xml", Util.shortenClass(test.getDeclaringClass()), test.getName(),
                                   Strings.isNullOrEmpty(nameAppender)? "" : "-" + nameAppender));
        AntXmlRunListener jenkinsRunListener = new AntXmlRunListener();

        jenkinsRunListener.setOutputStream(new FileOutputStream(resultFile));
        return jenkinsRunListener;
    }


    @Override
    public boolean isEnabled() {
        return jenkinsIntegration;
    }
}
