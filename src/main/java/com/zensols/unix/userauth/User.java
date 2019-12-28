package com.zensols.unix.userauth;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a UNIX user that can test the equality of the password and get
 * basic information using system command line utilities.
 *
 * @author Paul Landes
 */
public class User {
    private static final Logger log = LoggerFactory.getLogger(User.class);

    private UserManager owner;
    private String userName;

    /**
     * @param owner manager that created it
     * @param userName the name string ID of the user
     */
    public User(UserManager owner, String userName) {
	this.owner = owner;
	this.userName = userName;
    }

    /**
     * Use the <code>pwauth</code> command to check the password.
     *
     * @param password the password to check with the system
     * @param pwauthPath the path to the <code>pwauth</code> command
     * @return the status of the command
     * @throws IOException when executing the binary fails
     */
    protected UserAuthStatus doPwauth(String password, String pwauthPath) throws IOException {
	Command cmd = owner.getCommand("pwauth");
	List<String> stdin = new java.util.LinkedList();
	CommandOutput output = null;

	stdin.add(this.userName);
	stdin.add(password);

	output = cmd.execute(null, null, stdin);

	return UserAuthStatus.getByCode(output.returnCode);
    }

    public UserAuthStatus getStatusForPassword(String password) throws IOException {
	String path = this.owner.getPath("pwauth");

	if (log.isDebugEnabled()) {
	    log.debug(String.format("executing pwauth at %s", path));
	}
	
	return this.doPwauth(password, path);
    }

    public boolean isAuthorized(String password) throws IOException {
	return this.getStatusForPassword(password) == UserAuthStatus.OK;
    }

    public String toString() {
	return this.userName;
    }
}
