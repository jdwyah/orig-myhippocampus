package com.aavu.server.web.domain.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public class CreateUserRequestValidator implements Validator{

	private UserService userService;
	
	public boolean supports(Class clazz) {
		return clazz.equals(CreateUserRequestCommand.class);
	}

	
	
	/**
	 * lookup messages from resource bundle 
	 */
	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username","required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password2","required");

		CreateUserRequestCommand comm = (CreateUserRequestCommand) command;
		
		//can't have the same password
		if(!comm.getPassword().equals(comm.getPassword2())){
			errors.rejectValue("password2","invalid.password2");
		}
		
		if(!userService.isUnique(comm)){
			errors.rejectValue("username","invalid.username");
		}
		
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
