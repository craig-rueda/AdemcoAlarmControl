package com.craigrueda.webkeypad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.craigrueda.webkeypad.util.ParamUtils;

@Controller
public class HomeStatusController extends BaseController {
	private String statusInclude;
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index() {
		return new ModelAndView(formView, 
				ParamUtils.createMapForParams(
						"status", panelCommService.getCurrentStatus()));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView currentStatus() {
		return new ModelAndView(successView, 
				ParamUtils.createMapForParams(
						"status", panelCommService.getCurrentStatus()));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView currentStatusInclude() {
		return new ModelAndView(statusInclude, 
				ParamUtils.createMapForParams(
						"status", panelCommService.getCurrentStatus()));
	}

	public void setStatusInclude(String statusInclude) {
		this.statusInclude = statusInclude;
	}
}
