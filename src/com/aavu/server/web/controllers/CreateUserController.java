package com.aavu.server.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.server.exception.DuplicateUserException;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public class CreateUserController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(CreateUserController.class);
	
	private UserService userService;
	
	
	public CreateUserController(){
		setCommandClass(CreateUserRequestCommand.class);		
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		log.debug("OnBindAndValidate");
		log.debug("error count:"+errors.getAllErrors().size());

		for(Object b : errors.getAllErrors()){
			log.debug("err: "+b);
		}		

	}

	@Override
	protected ModelAndView onSubmit(Object arg0) throws Exception {
		CreateUserRequestCommand comm = (CreateUserRequestCommand) arg0;

		log.debug("SUBMIT");
		try {
			userService.createUser(comm);	
		} catch (DuplicateUserException e) {
			log.error("Fail Duplicate User");			
		}
				
				
		return super.onSubmit(arg0);
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	
}
