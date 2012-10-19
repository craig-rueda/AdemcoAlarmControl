package com.craigrueda.webkeypad.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.craigrueda.webkeypad.security.UserHolder;
import com.craigrueda.webkeypad.service.LoginService;
import com.craigrueda.webkeypad.util.ParamUtils;

@Controller
public class LoginController extends BaseController {
	public static final String REMEMBER_ME_COOKIE_NAME = "user";
	public static final int REMEMBER_ME_COOKIE_AGE = (int) (DateUtils.MILLIS_PER_DAY * 180 / 1000);
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView doGet(Boolean error, String userName, HttpServletRequest request) {
		userName = StringUtils.isEmpty(userName) ?
				getRememberMeVal(request) : userName;
		
		return new ModelAndView(formView, 
				ParamUtils.createMapForParams(
						"error", error,
						"userName", userName));
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView doPost(@RequestParam String userName, 
			@RequestParam String password,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if (loginService.validateCreds(userName, password))
			doLogin(userName, response);
		else
			return doGet(true, userName, request);
		
		return new ModelAndView(successView);
	}
	
	private String getRememberMeVal(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		
		if (cookies == null || cookies.length == 0) return null;
		
		for (Cookie c : cookies) {
			if (REMEMBER_ME_COOKIE_NAME.equalsIgnoreCase(c.getName()))
				return c.getValue();
		}
		
		return null;
	}
	
	private void doLogin(String userName, HttpServletResponse response) {
		Cookie newCook = new Cookie(REMEMBER_ME_COOKIE_NAME, userName);

		newCook.setPath("/");
		newCook.setMaxAge(REMEMBER_ME_COOKIE_AGE);
		
		response.addCookie(newCook);
		
		UserHolder newHolder = new UserHolder(userName);
		
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(
						newHolder,
						"",
						newHolder.getAuthorities()
				)
		);
	}
}
