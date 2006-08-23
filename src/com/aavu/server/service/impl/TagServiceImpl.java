package com.aavu.server.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Tag;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.service.UserService;

public class TagServiceImpl implements com.aavu.server.service.TagService {
	private static final Logger log = Logger.getLogger(TagServiceImpl.class);
	
	private TagDAO tagDAO;
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}




	public List<Tag> getAllTags() {
		return tagDAO.getAllTags(userService.getCurrentUser());
	}




	public Tag getTagAddIfNew(String tagName) {
		log.debug("load tag named: "+tagName);
		
		Tag rt = tagDAO.getTag(userService.getCurrentUser(),tagName);
		if(null == rt){
			log.debug("was null, creating ");
			Tag t = new Tag();
			t.setName(tagName);
			t.setPublicVisible(false);
			t.setUser(userService.getCurrentUser());
			tagDAO.save(t);
			
			log.debug("created: "+t.getId());
			return t;
		}else{
			log.debug("existed "+rt.getId()+"\n"+rt.toPrettyString());
			
			return rt;
		}
	}




	public List<Tag> getTagsStarting(String match) {
		return tagDAO.getTagsStarting(userService.getCurrentUser(),match);
	}




	public void removeTag(Tag selectedTag) {
		tagDAO.removeTag(userService.getCurrentUser(),selectedTag);
	}




	public void save(Tag selectedTag) {
		
		log.debug("Servic tag.save() setting user");
		
		selectedTag.setUser(userService.getCurrentUser());
		
		tagDAO.save(selectedTag);
	}


}
