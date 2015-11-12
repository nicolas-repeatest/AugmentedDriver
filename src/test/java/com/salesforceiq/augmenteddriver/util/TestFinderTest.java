package com.salesforceiq.augmenteddriver.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class TestFinderTest {

    @Test
    public void findTests() throws IOException {

        Assert.assertEquals(3, TestsFinder.getTestClassesOfPackage(Arrays.asList(TestSuites.TEST, TestSuites.TEST2), TestOne.class.getPackage().getName()).size());
        Assert.assertEquals(2, TestsFinder.getTestClassesOfPackage(Arrays.asList(TestSuites.TEST2), TestOne.class.getPackage().getName()).size());
    }

}

