package com.zensols.sys.userauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnixUserManagerTest {
    private static final Logger log = LoggerFactory.getLogger(UnixUserManagerTest.class);

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
    public void testUnixUserManager() throws Exception {
	if (log.isDebugEnabled()) {
	    log.debug("testing hello world");
	}
	
	UnixUserManager mng = new UnixUserManager("src/test/python/pwauth.py");
	UnixUser usr = mng.createUser("bob");
	if (log.isDebugEnabled()) {
	    log.debug("user: " + usr);
	}

	UserAuthStatus status = usr.getStatusForPassword("wrongpasswd");
	Assert.assertEquals(UserAuthStatus.INVALID, status);
    }
}
