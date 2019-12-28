package com.zensols.sys.userauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnixUserManager {
    private static final Logger log = LoggerFactory.getLogger(UnixUserManager.class);

    private String pwauthPath;

    public UnixUserManager() {
	this("/usr/bin/pwauth");
    }

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
	UnixUser usr = null;
	String password = null;
	if (args.length == 2) {
	    UnixUserManager mng = new UnixUserManager();
	    usr = mng.createUser(args[0]);
	    password = args[1];
	} else if (args.length == 3) {
	    UnixUserManager mng = new UnixUserManager(args[2]);
	    usr = mng.createUser(args[0]);
	    password = args[1];
	} else {
	    System.err.println("usage: java ... <user> <password> [pwauth path]");
	    System.exit(1);
	}
	if (log.isDebugEnabled()) {
	    log.debug("user: " + usr);
	}
	System.out.println(usr.getStatusForPassword(password));
    }
}
