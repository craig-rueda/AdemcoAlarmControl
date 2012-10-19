package com.craigrueda.webkeypad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PanelControlController extends BaseController{
	@RequestMapping(method=RequestMethod.GET)
	public String doGet() {
		return formView;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String doPost(@RequestParam String action, @RequestParam String password) {
		if ("disarm".equalsIgnoreCase(action))
			panelCommService.disarm(password);
		else if ("stay".equalsIgnoreCase(action))
			panelCommService.armStay(password);
		else if ("night".equalsIgnoreCase(action))
			panelCommService.armNight(password);
		else if ("away".equalsIgnoreCase(action))
			panelCommService.armAway(password);
		
		return successView;
	}
}
