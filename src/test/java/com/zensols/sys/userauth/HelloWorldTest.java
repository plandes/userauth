package com.zensols.sys.userauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class HelloWorldTest {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldTest.class);

    @BeforeClass
    public static void setup() {
	if (log.isDebugEnabled()) {
	    log.debug("setting up...");
	}
    }

    @AfterClass
    public static void tearDown() {
	if (log.isDebugEnabled()) {
	    log.debug("tearing down...");
	}
    }

    @Test
    public void testHelloWorld() throws Exception {
	if (log.isDebugEnabled()) {
	    log.debug("testing hello world");
	}
	
        Assert.assertEquals(1, 1);
    }
}
