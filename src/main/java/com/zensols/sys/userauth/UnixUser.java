package com.zensols.sys.userauth;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a UNIX user that can test the equality of the password and get
 * basic information using system command line utilities.
 *
 * @author Paul Landes
 */
public class UnixUser {
    private static final Logger log = LoggerFactory.getLogger(UnixUser.class);

    private UnixUserManager owner;
    private String userName;

    /**
     * @param owner manager that created it
     * @param userName the name string ID of the user
     */
    public UnixUser(UnixUserManager owner, String userName) {
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
	ProcessBuilder pb = new ProcessBuilder(pwauthPath);
	Process proc = pb.start();
	PrintWriter stdin = new PrintWriter(proc.getOutputStream());
	BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	BufferedReader stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
	String line = null;
	int returnCode = -1;

	stdin.println(this.userName);
	stdin.println(password);
	stdin.flush();

	if (log.isDebugEnabled()) {
	    log.debug("waiting for process to finish");
	}
	try {
	    proc.waitFor();
	} catch (InterruptedException e) {
	    throw new RuntimeException("interrupted", e);
	}

	while ((line = stdout.readLine()) != null) {
	    if (log.isInfoEnabled()) {
		log.info(String.format("out: %s", line));
	    }
	}
	while ((line = stderr.readLine()) != null) {
	    if (log.isInfoEnabled()) {
		log.info(String.format("error: %s", line));
	    }
	}

	returnCode = proc.exitValue();
	if (log.isDebugEnabled()) {
	    log.debug(String.format("return code: %d", returnCode));
	}

	return UserAuthStatus.getByCode(returnCode);
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
