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

public class Command {
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

    protected CommandOutput doExecute(Integer expectReturn, List<String> args, List<String> input)
	throws SystemException, IOException {

	ProcessBuilder pb = new ProcessBuilder(args);
	Process proc = pb.start();
	BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	BufferedReader stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
	String line = null;
	List<String> stdoutLines = new java.util.LinkedList();
	int returnCode = -1;
	CommandOutput output = null;

	if (input != null) {
	    PrintWriter stdin = new PrintWriter(proc.getOutputStream());
	    for (String inputLine: input) {
		stdin.println(inputLine);
	    }
	    stdin.flush();
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

	while ((line = stdout.readLine()) != null) {
	    line = line.trim();
	    log.info(String.format("%s: output: %s", this.name, line));
	    stdoutLines.add(line);
	}

	output = new CommandOutput(returnCode, stdoutLines);

	if (log.isDebugEnabled()) {
	    log.debug(output.toString());
	}

	if ((expectReturn != null) && (returnCode != expectReturn)) {
	    String msg = String.format("expected return code %d but got %d",
				       expectReturn, returnCode);
	    throw new SystemException(msg);
	}

	return output;
    }

    public CommandOutput execute(Integer expectReturn, String args, List<String> input) throws SystemException {
	List<String> argList = new java.util.ArrayList();

	if (args != null) {
	    argList.addAll(Arrays.asList(args.split(" ")));
	}

	argList.add(0, find().getPath());

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
		CommandOutput output = this.which.execute(null, this.name, null);
		if (output.returnCode == 1) {
		    String msg = "could not find executable program: " + this.name;
		    throw new SystemException(msg);
		} else {
		    String foundPath = output.stdout.get(0);
		    if (foundPath == null) {
			this.exists = Exists.no;
		    } else {
			this.exists = Exists.yes;
			file = new File(foundPath);
		    }
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
