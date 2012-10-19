package com.craigrueda.webkeypad.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.craigrueda.webkeypad.util.ParamUtils;

@Controller
public class ZoneStatusController extends BaseController{
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView doGet() {
		return new ModelAndView(formView, 
				ParamUtils.createMapForParams(
						"statusList", panelCommService.getCurrentStatusList()));
	}
}
