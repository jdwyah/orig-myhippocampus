package com.aavu.server.web.domain.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.CreateUserRequestCommand;
import com.aavu.server.web.domain.MailingListCommand;

public class MailingListCommandValidator implements Validator{

	public boolean supports(Class clazz) {
		return clazz.equals(MailingListCommand.class);
	}

	
	
	/**
	 * lookup messages from resource bundle 
	 */
	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email","required");				
	}

}
