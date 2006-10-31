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
		AddLinkCommand command = new AddLinkCommand();
		
		command.setUrl(req.getParameter("url"));
		
		command.setDescription(req.getParameter("description"));
		
		command.setNotes(req.getParameter("notes"));
		
		return command;
		
	}


	@Override
	protected void doSubmitAction(Object command) throws Exception {		
		log.debug("command: "+command);
		AddLinkCommand addLink = (AddLinkCommand) command;
		
		Occurrence occ = new Occurrence(userService.getCurrentUser(),addLink.getDescription(),addLink.getUrl(),addLink.getNotes());
		
		occ = (Occurrence) topicService.save(occ);
		
		String[] tags = addLink.getTags().split(";");
		
		log.debug("tags: "+Arrays.toString(tags));
		for (String string : tags) {			
			log.debug("str: "+string);			
			Topic t = topicService.getForName(string);			
			if(null == t){
				log.debug("was null, creating as Tag ");
				t = new Tag();
				t.setTitle(string);				
				t.setUser(userService.getCurrentUser());							
			}			
			t.getOccurences().add(occ);
			topicService.save(t);	
		}
		
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
