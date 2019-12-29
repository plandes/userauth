package com.zensols.unix.userauth;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

public class UserManagerTest {
    private static final Logger log = LoggerFactory.getLogger(UserManagerTest.class);
    private static UserManager mng;

    @Before
    public void setUp() {
	this.mng = new UserManager();
	this.mng.overridePath("pwauth", "src/test/python/pwauth.py");
	this.mng.overridePath("getent", "src/test/python/getent.py");
    }

    @Test
    public void testFindExec() throws Exception {
	Command bin = this.mng.getCommand("ls");
	log.debug("found: " + bin);
	File path = bin.find();
	assertNotNull(path);
	assertTrue(path.getPath().endsWith("ls"));
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
	assertEquals(UserAuthStatus.INVALID, status);

	status = usr.getStatusForPassword("pass123");
	assertEquals(UserAuthStatus.OK, status);

	usr = mng.createUser("nosuchuser");
	status = usr.getStatusForPassword("pass123");
	assertEquals(UserAuthStatus.UNKNOWN, status);
    }

    @Test
    public void testAuth() throws Exception {
	User usr = mng.createUser("bob");
	if (log.isDebugEnabled()) {
	    log.debug("user: " + usr);
	}

	assertFalse("should be wrong pasword", usr.isAuthorized("wrongpasswd"));
	assertTrue("should match password", usr.isAuthorized("pass123"));

	usr = mng.createUser("jane");
	assertFalse("should not match password", usr.isAuthorized("pass123"));
	assertTrue("should match password", usr.isAuthorized("changeit"));

	usr = mng.createUser("goofy");
	assertFalse("should not match password", usr.isAuthorized("pass123"));
    }

    @Test
    public void testUserInfo() throws Exception {
	User usr = mng.createUser("bob");
	assertEquals(701, usr.getUserId());
	assertEquals("Bob Copymeister", usr.getFullName());

	usr = mng.createUser("jane");
	assertEquals(702, usr.getUserId());
	assertEquals("Jane Smith", usr.getFullName());
    }
}
