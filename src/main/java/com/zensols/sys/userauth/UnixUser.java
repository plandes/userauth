package com.zensols.sys.userauth;

import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnixUser {
    private static final Logger log = LoggerFactory.getLogger(UnixUser.class);

    private UnixUserManager owner;
    private String userName;

    public UnixUser(UnixUserManager owner, String userName) {
	this.owner = owner;
	this.userName = userName;
    }

    public UserAuthStatus getStatusForPassword(String password) {
	String path = this.owner.getPwauthPath();

	if (log.isDebugEnabled()) {
	    log.debug(String.format("executing pwauth at %s", path));
	}
	
	//ProcessBuilder pbld = ProcessBuilder();
	// Process p = Runtime.process();
	// InputStream stdout = p.getInputStream();
	// InputStream stderr = p.getErrorStream();
	// OutputStream stdin = p.getOutputStream();
	return UserAuthStatus.INVALID;
    }

    public String toString() {
	return this.userName;
    }
}
