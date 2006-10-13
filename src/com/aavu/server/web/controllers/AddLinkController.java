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
import com.aavu.server.web.domain.AddLinkCommand;

public class AddLinkController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(AddLinkController.class);

	
	private UserService userService;	

	public AddLinkController(){
		setCommandClass(AddLinkCommand.class);				
	}	
	

	@Override
	protected Object formBackingObject(HttpServletRequest req) throws Exception {
		AddLinkCommand command = new AddLinkCommand();
		
		command.setUrl(req.getParameter("url"));
		
		command.setDescription(req.getParameter("description"));
				
		return command;
		
	}


	@Override
	protected void doSubmitAction(Object command) throws Exception {
		// TODO Auto-generated method stub
		super.doSubmitAction(command);
		log.warn("Unimplemented Form submit");
	}



	@Override
	protected Map referenceData(HttpServletRequest arg0) throws Exception {		
		
		Map reference = new HashMap<String, Object>();
				
		try{			
			User su = userService.getCurrentUser();
			reference.put("user", su);			
		}catch(UsernameNotFoundException e){
			log.debug("No user logged in.");
		}
		
		return reference;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
