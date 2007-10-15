package com.aavu.server.web.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.server.service.UserService;

public abstract class BasicFormController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(BasicFormController.class);

	protected UserService userService;


	protected Map<String, Object> getDefaultModel(HttpServletRequest req) {
		return BasicController.getDefaultModel(req, userService);
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
