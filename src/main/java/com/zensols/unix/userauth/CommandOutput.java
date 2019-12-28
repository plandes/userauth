package com.zensols.unix.userauth;

public class CommandOutput {
    public int returnCode;
    public String stdout;

    public CommandOutput(int returnCode, String stdout) {
	this.returnCode = returnCode;
	this.stdout = stdout;
    }

    public String toString() {
	return String.format("return code: %d, stdout: <%s>", this.returnCode, this.stdout);
    }
}
