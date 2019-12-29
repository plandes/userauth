package com.zensols.unix.userauth;

import java.util.Map;
import java.util.List;
import java.util.Properties;
import java.util.Arrays;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This creates instances of {@link User} instances.
 *
 * @author Paul Landes
 */
public class UserManager {
    private static final Logger log = LoggerFactory.getLogger(UserManager.class);
    private static final String PATHS_RES = "/paths.properties";
    private Map<String, String> paths;
    private Map<String, Command> commands;

    public UserManager() {
	commands = new java.util.HashMap();
    }

    private Map<String, String> getPaths() {
	if (this.paths == null) {
	    Properties props = new Properties();
	    InputStream in = getClass().getResourceAsStream(PATHS_RES);
	    if (in == null) {
		throw new RuntimeException("couldn't find resource: " + PATHS_RES);
	    }
	    try {
		props.load(in);
	    } catch (IOException e) {
		throw new RuntimeException("error accessing resource or parsing properties", e);
	    } finally {
		try {
		    in.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    this.paths = new java.util.HashMap(props);
	}
	return this.paths;
    }

    /**
     * Override a executableName path.  The default on debian systems is
     * <code>/usr/sbin/paths</code> or <code>/bin/paths</code> on CenotOS.
     *
     * @param executableName the name of the program (i.e. <code>pwauth</code>)
     * @param path the value of the path that clobbers
     */
    public void overridePath(String executableName, String path) {
	getPaths().put(executableName, path);
    }

    /**
     * Return an executable command.
     *
     * @param executableName the name of the program (i.e. <code>pwauth</code>)
     * @return the command if found, otherwise <code>null</code>
     */
    public synchronized Command getCommand(String executableName) {
	Command com = this.commands.get(executableName);
	if (com == null) {
	    Command which;
	    String path = getPaths().get(executableName);

	    if (executableName.equals(Command.WHICH_NAME)) {
		which = new Command(executableName, path, null);
	    } else {
		which = getCommand(Command.WHICH_NAME);
	    }

	    com = new Command(executableName, path, which);
	    this.commands.put(executableName, com);
	}
	return com;
    }

    /**
     * @param executableName the name of the program (i.e. <code>pwauth</code>)
     * @return the path to the executableName
     * @see #overridePath(String, String)
     */
    public String getPath(String executableName) {
	return this.paths.get(executableName);
    }

    /**
     * Create the user, which is associated with this manager.
     *
     * @param userName the string ID of the user
     * @return the new user
     */
    public User createUser(String userName) {
	return new User(this, userName);
    }

    private static void usage() {
	System.err.println("usage: java ... -auth <user> <password> [paths path]");
	System.err.println("usage: java ... -show <user>");
	System.exit(1);
    }

    public static void main(String[] argArr) throws Exception {
	String password = null;
	List<String> args = new java.util.ArrayList(Arrays.asList(argArr));
	boolean usage = true;

	if (args.size() > 1) {
	    if (args.get(0).equals("-auth")) {
		UserManager mng = new UserManager();
		User usr = null;

		args.remove(0);

		if (args.size() == 3) {
		    mng.overridePath("pwauth", args.get(2));
		}
		usr = mng.createUser(args.get(0));
		password = args.get(1);
		System.out.println(usr.getStatusForPassword(password));
		usage = false;
	    } else if (args.get(0).equals("-show")) {
		args.remove(0);
		UserManager mng = new UserManager();
		User usr = mng.createUser(args.get(0));
		if (args.size() == 2) {
		    mng.overridePath("getent", args.get(1));
		}
		System.out.println("user: " + usr);
		System.out.println("ID: " + usr.getUserId());
		System.out.println("name: " + usr.getFullName());
		usage = false;
	    }
	}
	if (usage) usage();
    }
}
