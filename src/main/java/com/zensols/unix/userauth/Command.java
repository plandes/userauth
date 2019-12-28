package com.zensols.unix.userauth;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringWriter;
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.IOUtils;

class Command {
    private static final Logger log = LoggerFactory.getLogger(Command.class);

    public static final String WHICH_NAME = "which";

    private enum Exists {maybe, yes, no};

    private String name;
    private String path;
    private Command which;
    private File absolute;
    private Exists exists;

    public Command(String name, String path, Command which) {
	this.name = name;
	this.path = path;
	this.which = which;
	exists = Exists.maybe;
    }

    protected String doExecute(int expectReturn, List<String> args, List<String> input)
	throws SystemException, IOException {

	ProcessBuilder pb = new ProcessBuilder(args);
	Process proc = pb.start();
	StringWriter stdout = new StringWriter();
	BufferedReader stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
	String line = null;
	int returnCode = -1;
	String output = null;

	if (input != null) {
	    PrintWriter stdin = new PrintWriter(proc.getOutputStream());
	    for (String inputLine: input) {
		stdin.println(inputLine);
	    }
	}

	if (log.isDebugEnabled()) {
	    log.debug("waiting for process to finish");
	}
	try {
	    proc.waitFor();
	} catch (InterruptedException e) {
	    throw new RuntimeException("interrupted", e);
	}

	returnCode = proc.exitValue();

	while ((line = stderr.readLine()) != null) {
	    log.error(line);
	}
	IOUtils.copy(proc.getInputStream(), stdout, "UTF-8");
	output = stdout.toString().trim();

	if (log.isDebugEnabled()) {
	    log.debug(String.format("return code: %d, output: <%s>", returnCode, output));
	}

	if (returnCode != expectReturn) {
	    throw new SystemException(String.format("expected return code %d but got %d"));
	}

	return output;
    }

    public String execute(int expectReturn, String args, List<String> input) throws SystemException {
	List<String> argList = new java.util.ArrayList(Arrays.asList(args.split(" ")));

	argList.add(0, this.path);

	if (log.isDebugEnabled()) {
	    log.debug(String.format("%s: executing %s expecting exit %d", this.name, argList, expectReturn));
	}

	try {
	    return doExecute(expectReturn, argList, input);
	} catch (IOException e) {
	    throw new SystemException("IO error while executing " + this.path, e);
	}
    }

    public File find() {
	File file = null;

	if (this.exists == Exists.maybe) {
	    file = this.path != null ? new File(this.path) : null;

	    if ((file == null) || !file.exists()) {
		if (WHICH_NAME.equals(this.name)) {
		    throw new SystemException("which executable not found at: " + file);
		}
		String foundPath = this.which.execute(0, this.name, null);
		if (foundPath == null) {
		    this.exists = Exists.no;
		} else {
		    this.exists = Exists.yes;
		    file = new File(foundPath);
		}
	    }

	    file = file.getAbsoluteFile();
	}

	return file;
    }

    public boolean exists() {
	find();
	return this.exists == Exists.yes;
    }

    public String toString() {
	return String.format("name=%s, path=%s", this.name, this.path);
    }
}
