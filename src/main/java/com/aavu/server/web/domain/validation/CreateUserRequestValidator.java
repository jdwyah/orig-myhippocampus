package com.aavu.server.web.domain.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.aavu.server.domain.MailingListEntry;
import com.aavu.server.service.InvitationService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public class CreateUserRequestValidator implements Validator{

	
	
	private static final int MIN_LENGTH = 3;
	private InvitationService invitationService;
	private UserService userService;
	
	public void setInvitationService(InvitationService invitationService) {
		this.invitationService = invitationService;
	}



	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	
	public boolean supports(Class clazz) {
		return clazz.equals(CreateUserRequestCommand.class);
	}


	/**
	 * lookup messages from resource bundle 
	 * 
	 * NOTE: topicService.createUser() .lowerCases() the username
	 */
	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username","required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password2","required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "randomkey","required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email","required");
		
		CreateUserRequestCommand comm = (CreateUserRequestCommand) command;
		
		
		
		//username must have no '.' for openid compatibility
		if(comm.getUsername().contains(".")){
			errors.rejectValue("username","invalid.username.nodots");
		}		
		
		//spaces would break email functionality
		if(comm.getUsername().contains(" ")){
			errors.rejectValue("username","invalid.username.nospaces");
		}	
		
		//generalemail compatibility
		if(!comm.getUsername().matches("([a-zA-Z0-9_\\.\\-])+")){
			errors.rejectValue("username","invalid.username");
		}
		if(comm.getUsername().length() < MIN_LENGTH){
			errors.rejectValue("username","invalid.username.length");
		}
		if(comm.getPassword().length() < MIN_LENGTH){
			errors.rejectValue("password","invalid.password.length");
		}
		
		//username != password
		if(comm.getPassword().equals(comm.getUsername())){
			errors.rejectValue("username","invalid.password.equalsuser");
		}
				
		//must have the same password
		if(!comm.getPassword().equals(comm.getPassword2())){
			errors.rejectValue("password2","invalid.password2");
		}
		
		if(!userService.isUnique(comm)){
			errors.rejectValue("username","invalid.username.exists");
		}
		
		
		if(!invitationService.isKeyValid(comm.getRandomkey())){
			errors.rejectValue("randomkey","invalid");
		}
		MailingListEntry entry = invitationService.getEntryForKey(comm.getRandomkey());
		if(entry != null && entry.getSignedUpUser() != null){
			errors.rejectValue("randomkey","invalid.randomkey.exists");
		}
		
	}

}
