package com.craigrueda.webkeypad.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserHolder implements UserDetails {
	private static final long serialVersionUID = 1L;
	private String userName;
	
	public UserHolder(String userName) {
		this.userName = userName;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
