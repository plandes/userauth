package com.zensols.sys.userauth;

import java.util.Map;
import java.util.Properties;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This creates instances of {@link UnixUser} instances.
 *
 * @author Paul Landes
 */
public class UnixUserManager {
    private static final Logger log = LoggerFactory.getLogger(UnixUserManager.class);
    private static final String PATHS_RES = "/paths.properties";
    private Map<String, String> paths;
    private Map<String, Command> commands;

    public UnixUserManager() {
	commands = new java.util.HashMap();
    }

    public Map<String, String> getPaths() {
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
     * @param pathsPath the path to the executableName
     */
    public void overridePath(String executableName, String path) {
	getPaths().put(executableName, path);
    }

    public Command getCommand(String executableName) {
	Command com = this.commands.get(executableName);
	if (com == null) {
	    Command which;
	    String path = getPaths().get(executableName);

	    // if (path == null) {
	    // 	throw new SystemException("no path defined for: " + executableName);
	    // }

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
     * @see #overridePath(String, String)
     * @return the path to the executableName
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
	    UnixUserManager mng = new UnixUserManager();
	    //mng.setPathsPath(args[2]);
	    usr = mng.createUser(args[0]);
	    password = args[1];
	} else {
	    System.err.println("usage: java ... <user> <password> [paths path]");
	    System.exit(1);
	}
	if (log.isDebugEnabled()) {
	    log.debug("user: " + usr);
	}
	System.out.println(usr.getStatusForPassword(password));
    }
}
