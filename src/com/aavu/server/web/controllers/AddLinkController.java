package com.aavu.server.web.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.AddLinkCommand;

public class AddLinkController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(AddLinkController.class);

	
	private UserService userService;	
	private TopicService topicService;

	public AddLinkController(){
		setCommandClass(AddLinkCommand.class);				
	}	
	

	@Override
	protected Object formBackingObject(HttpServletRequest req) throws Exception {
		log.debug("FormBackingObject");
		AddLinkCommand command = new AddLinkCommand();
		
		command.setCommand_url(req.getParameter("url"));
		
		command.setCommand_description(req.getParameter("description"));
		
		command.setCommand_notes(req.getParameter("notes"));
		
		return command;
		
	}


	@Override
	protected void doSubmitAction(Object command) throws Exception {		
		log.debug("command: "+command);
		AddLinkCommand addLink = (AddLinkCommand) command;
		
		
		log.debug("addLinkCommand: "+addLink);
		
		WebLink link = new WebLink(userService.getCurrentUser(),addLink.getCommand_description(),addLink.getCommand_url(),addLink.getCommand_notes());
		
		link = (WebLink) topicService.save(link);
		
		String[] tags = addLink.getCommand_tags().split(";");
		
		log.debug("tags: "+Arrays.toString(tags));
		
		
		if(tags[0].equals("")){
			log.debug("blank tags, setting topic to; "+addLink.getCommand_description());		
			tags[0] = addLink.getCommand_description();
		}
		
		topicService.addLinkToTags(link, tags);
		
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


	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

}
