package com.craigrueda.webkeypad.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.craigrueda.webkeypad.util.PasswordUtils;

public class LoginServiceImpl implements LoginService {
	private Map<String, String> userMap;

	@Override
	public Authentication authenticate(Authentication arg0)
			throws AuthenticationException {
		return null;
	}

	@Override
	public boolean supports(Class<? extends Object> arg0) {
		return true;
	}
	
	@Override
	public boolean validateCreds(String userName, String clearPwd) {
		if (StringUtils.isEmpty(clearPwd)) return false;
		
		String hashedPwd = PasswordUtils.hashPassword(clearPwd);
		String targetPwd = userMap.get(userName);
		
		return hashedPwd.equals(targetPwd);
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}
}
