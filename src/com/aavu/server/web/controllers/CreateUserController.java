package com.aavu.server.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.server.web.domain.CreateUserRequestCommand;
import com.aavu.server.web.domain.validation.CreateUserRequestValidator;

public class CreateUserController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(CreateUserController.class);
	

	public CreateUserController(){
		setCommandClass(CreateUserRequestCommand.class);
		setValidator(new CreateUserRequestValidator());
		
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

		log.debug("SUBMIT");
		
		return super.onSubmit(arg0);
	}

}
