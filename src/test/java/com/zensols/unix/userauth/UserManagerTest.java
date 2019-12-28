package com.zensols.unix.userauth;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Assert;
import org.junit.Test;

public class UserManagerTest {
    private static final Logger log = LoggerFactory.getLogger(UserManagerTest.class);
    private static UserManager mng;

    public UserManagerTest() {
	this.mng = new UserManager();
	this.mng.overridePath("pwauth", "src/test/python/pwauth.py");
	this.mng.overridePath("getent", "src/test/python/getent.py");
    }

    @Test
    public void testFindExec() throws Exception {
	Command bin = this.mng.getCommand("ls");
	log.debug("found: " + bin);
	File path = bin.find();
	Assert.assertNotNull(path);
	Assert.assertTrue(path.getPath().endsWith("ls"));
    }

    @Test(expected = SystemException.class)
    public void testNotFindExec() throws Exception {
	Command bin = this.mng.getCommand("NOSUCHCMD");
	log.debug("found: " + bin);
	File path = bin.find();
    }

    @Test
    public void testStatus() throws Exception {
	User usr = mng.createUser("bob");
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

    @Test
    public void testAuth() throws Exception {
	User usr = mng.createUser("bob");
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

    @Test
    public void testUserInfo() throws Exception {
	User usr = mng.createUser("bob");
	Assert.assertEquals(701, usr.getUserId());
	Assert.assertEquals("Bob Copymeister", usr.getFullName());

	usr = mng.createUser("jane");
	Assert.assertEquals(702, usr.getUserId());
	Assert.assertEquals("Jane Smith", usr.getFullName());
    }
}
