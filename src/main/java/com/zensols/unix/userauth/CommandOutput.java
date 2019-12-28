package com.zensols.unix.userauth;

import java.util.List;

public class CommandOutput {
    public int returnCode;
    public List<String> stdout;

    public CommandOutput(int returnCode, List<String> stdout) {
	this.returnCode = returnCode;
	this.stdout = stdout;
    }

    public String toString() {
	return String.format("return code: %d, stdout: <%s>", this.returnCode, this.stdout);
    }
}
