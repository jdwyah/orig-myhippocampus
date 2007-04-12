package com.aavu.server.web.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.client.domain.User;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.MailingListCommand;

public abstract class BasicFormController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(BasicFormController.class);

	protected UserService userService;	

	
	protected Map<String,Object> getDefaultModel(){
		Map<String,Object> model = new HashMap<String, Object>();
		
		User su = null;
		try{
			su = userService.getCurrentUser();	
			model.put("user",su);			
		}catch(UsernameNotFoundException e){
			log.debug("No user logged in.");
		}
				
		return model;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
