package com.aavu.server.web.domain.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.AddLinkCommand;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public class AddLinkCommandValidator implements Validator{

	
	public boolean supports(Class clazz) {
		return clazz.equals(AddLinkCommand.class);
	}

	
	/**
	 * lookup messages from resource bundle 
	 */
	public void validate(Object command, Errors errors) {

		AddLinkCommand comm = (AddLinkCommand) command;
		
		//can't have the same password
		if(comm.getCommand_url() == null || comm.getCommand_url().equals("")){
			errors.rejectValue("command_url","required");
		}
		if(comm.getCommand_description() == null || comm.getCommand_description().equals("")){
			errors.rejectValue("command_description","required");
		}
		
	}

}
