package com.zensols.sys.userauth;

public class SystemException extends RuntimeException {
    public SystemException(String message) {
	super(message);
    }

    public SystemException(String message, Throwable reason) {
	super(message, reason);
    }
}
