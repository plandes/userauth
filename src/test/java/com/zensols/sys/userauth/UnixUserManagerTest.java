package com.zensols.sys.userauth;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Assert;
import org.junit.Test;

public class UnixUserManagerTest {
    private static final Logger log = LoggerFactory.getLogger(UnixUserManagerTest.class);
    private static UnixUserManager mng;

    public UnixUserManagerTest() {
	this.mng = new UnixUserManager();
	this.mng.overridePath("pwauth", "src/test/python/pwauth.py");
    }

    @Test
    public void testFindExec() throws Exception {
	Command bin = this.mng.getCommand("ls");
	log.debug("found: " + bin);
	File path = bin.find();
	Assert.assertNotNull(path);
	Assert.assertTrue(path.getPath().endsWith("ls"));
    }

    //@Test
    public void testStatus() throws Exception {
	if (log.isDebugEnabled()) {
	    log.debug("testing hello world");
	}
	
	UnixUser usr = mng.createUser("bob");
	if (log.isDebugEnabled()) {
	    log.debug("user: " + usr);
	}

	UserAuthStatus status = usr.getStatusForPassword("wrongpasswd");
	Assert.assertEquals(UserAuthStatus.INVALID, status);

	status = usr.getStatusForPassword("pass123");
	Assert.assertEquals(UserAuthStatus.OK, status);

	usr = mng.createUser("nosuchuser");
	status = usr.getStatusForPassword("pass123");
	Assert.assertEquals(UserAuthStatus.UNKNOWN, status);
    }

    //@Test
    public void testAuth() throws Exception {
	if (log.isDebugEnabled()) {
	    log.debug("testing hello world");
	}

	UnixUser usr = mng.createUser("bob");
	if (log.isDebugEnabled()) {
	    log.debug("user: " + usr);
	}

	UserAuthStatus status = usr.getStatusForPassword("wrongpasswd");
	Assert.assertFalse(usr.isAuthorized("wrongpasswd"));

	Assert.assertTrue(usr.isAuthorized("pass123"));

	usr = mng.createUser("jane");
	Assert.assertFalse(usr.isAuthorized("pass123"));
	Assert.assertTrue(usr.isAuthorized("changeit"));

	usr = mng.createUser("goofy");
	Assert.assertFalse(usr.isAuthorized("pass123"));
    }
}
