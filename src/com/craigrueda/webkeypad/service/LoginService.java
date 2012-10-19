package com.craigrueda.webkeypad.service;

import org.springframework.security.authentication.AuthenticationProvider;

public interface LoginService extends AuthenticationProvider {
	public boolean validateCreds(String userName, String clearPwd);
}
