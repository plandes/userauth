package com.zensols.sys.userauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
    private static final Logger log = LoggerFactory.getLogger(HelloWorld.class);

    public static void main(String[] args) throws Exception {
	if (log.isInfoEnabled()) {
	    log.info("starting main...");
	}
	System.out.println("hello world!");
    }
}
