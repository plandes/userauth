package com.zensols.sys.userauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnixUserManager {
    private static final Logger log = LoggerFactory.getLogger(UnixUserManager.class);

    private String pwauthPath;

    public UnixUserManager(String pwauthPath) {
	this.pwauthPath = pwauthPath;
    }

    public String getPwauthPath() {
	return this.pwauthPath;
    }

    public UnixUser createUser(String userName) {
	return new UnixUser(this, userName);
    }

    public static void main(String[] args) throws Exception {
	if (log.isInfoEnabled()) {
	    log.info("starting main...");
	}
	System.out.println("hello world!");
    }
}
