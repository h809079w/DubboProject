package com.w.common.redis;

public class UserKey extends BasePrefix {

	public static final int TOKEN_EXPIRE = 3600*24 * 2;
	private UserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static UserKey tokenKey = new UserKey(TOKEN_EXPIRE, "tokenKey");
	public static UserKey getUserByIdKey = new UserKey(0, "UserByIdKey");
}
