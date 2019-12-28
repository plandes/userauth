package com.zensols.sys.userauth;

/**
 * Status codes given by <code>pwauth</code>, their return codes, and descriptions.
 *
 * @see https://manpages.debian.org/stretch/pwauth/pwauth.8.en.html
 * @author Paul Landes
 */
public enum UserAuthStatus {
    OK(0, "Login OK"),
    UNKNOWN(1, "Nonexistant login or (for some configurations) incorrect password."),
    INVALID(2, "Incorrect password (for some configurations)."),
    BLOCKED(3, "Uid number is below MIN_UNIX_UID value configured in config.h."),
    EXPIRED(4, "Login ID has expired."),
    PW_EXPIRED(5, "Login's password has expired."),
    SNOLOGIN(6, "Logins to system have been turned off (usually by /etc/nologin file)."),
    MANYFAILES(7, "Limit on number of bad logins exceeded."),
    INT_USER(50, "pwauth was invoked by a uid not on the SERVER_UIDS list. If you get this error code, you probably have SERVER_UIDS set incorrectly in pwauth's config.h file."),
    INT_ARGS(51, "pwauth was not given a login & password to check. The means the passing of data from mod_auth_external to pwauth is messed up. Most likely one is trying to pass data via environment variables, while the other is trying to pass data via a pipe."),
    INT_ERR(52, "one of several rare and unlikely internal errors occurred. You'll have to read the source code to figure these out."),
    INT_NOROOT(53, "pwauth was not able to read the password database. Usually this means it is not running as root. (PAM and login.conf configurations will return 1 in this case.)"),
    NOT_MAPPED(-1, "Unknown and unmapped status code.");

    private int code;
    private String desc;

    private UserAuthStatus(int code, String desc) {
	this.code = code;
	this.desc = desc;
    }

    public static UserAuthStatus getByCode(int code) {
	for (UserAuthStatus stat: UserAuthStatus.values()) {
	    if (stat.code == code) {
		return stat;
	    }
	}
	return NOT_MAPPED;
    }
}
