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

    public static final int DEFAULT_USER_ID_FIELD = 2;
    public static final int DEFAULT_GROUP_ID_FIELD = 3;
    public static final int DEFAULT_FULL_NAME_FIELD = 4;

    private UserManager owner;
    private String userName;
    private String[] getentFields;

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
     * @throws SystemException when executing the binary fails
     */
    public UserAuthStatus getStatusForPassword(String password) throws SystemException {
	Command cmd = owner.getCommand("pwauth");
	List<String> stdin = new java.util.LinkedList();
	CommandOutput output = null;

	stdin.add(this.userName);
	stdin.add(password);
	output = cmd.execute(null, null, stdin);

	return UserAuthStatus.getByCode(output.returnCode);
    }

    public boolean isAuthorized(String password) throws SystemException {
	return this.getStatusForPassword(password) == UserAuthStatus.OK;
    }

    private String[] getGetentFields() throws SystemException {
	if (this.getentFields == null) {
	    CommandOutput output = owner.getCommand("getent").execute(0, "passwd " + this.userName, null);
	    String[] fields = null;

	    if (output.stdout.size() != 1) {
		throw new SystemException(String.format("expected single line output but got: <%s>", output.stdout));
	    }
	    this.getentFields = output.stdout.get(0).split(":");
	}
	return this.getentFields;
    }

    public int getUserId() throws SystemException {
	String[] fields = getGetentFields();
	String idStr = fields[DEFAULT_USER_ID_FIELD];
	return Integer.parseInt(idStr);
    }

    public int getGroupId() throws SystemException {
	String[] fields = getGetentFields();
	String idStr = fields[DEFAULT_GROUP_ID_FIELD];
	return Integer.parseInt(idStr);
    }

    public String getFullName() throws SystemException {
	String[] fields = getGetentFields();
	return fields[DEFAULT_FULL_NAME_FIELD];
    }

    public String toString() {
	return this.userName;
    }
}
