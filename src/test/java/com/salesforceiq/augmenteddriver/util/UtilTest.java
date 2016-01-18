package com.salesforceiq.augmenteddriver.util;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testShortenClass() {
        String shortened = Util.shortenClass(TestOne.class);
        Assert.assertEquals("c.s.a.u.TestOne", shortened);
    }

    @Test
    public void testPrettyFormat() {
        String pretty = Util.TO_PRETTY_FORMAT.apply(3661000L);
        Assert.assertEquals("01:01:01", pretty);
    }
}
