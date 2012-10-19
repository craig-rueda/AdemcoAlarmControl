package com.craigrueda.webkeypad.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.craigrueda.webkeypad.service.PanelCommService;

public class BaseController {
	protected String formView;
	protected String successView;
	@Autowired
	protected PanelCommService panelCommService;
	
	public void setFormView(String formView) {
		this.formView = formView;
	}
	
	public void setSuccessView(String successView) {
		this.successView = successView;
	}
}
