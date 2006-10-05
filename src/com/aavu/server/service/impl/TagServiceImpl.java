package com.aavu.server.service.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagStat;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.service.UserService;

public class TagServiceImpl implements com.aavu.server.service.TagService {
	private static final Logger log = Logger.getLogger(TagServiceImpl.class);

	private TagDAO tagDAO;
	private TopicDAO topicDAO;
	private UserService userService;
	

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}
	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
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
			topicDAO.save(t);

			log.debug("created: "+t.getId());
			return t;
		}else{
			log.debug("existed "+rt.getId()+"\n"+rt.toPrettyString());

			return rt;
		}
	}




	public List<String> getTagsStarting(String match) {
		return tagDAO.getTagsStarting(userService.getCurrentUser(),match);
	}




	public void removeTag(Tag selectedTag) throws PermissionDeniedException {
		if(userService.getCurrentUser().equals(selectedTag.getUser())){
			tagDAO.removeTag(userService.getCurrentUser(),selectedTag);
		}else{
			log.warn("User: "+userService.getCurrentUser().getUsername()+" tried to delete tag of "+selectedTag.getUser().getUsername());
			throw new PermissionDeniedException("No permission to delete tag");
		}
	}



	/**
	 * TODO who sets possibly unsaved meta Users? Client?
	 *  
	 */
	public void save(Tag selectedTag) {

		log.debug("Servic tag.save() setting user");

		selectedTag.setUser(userService.getCurrentUser());

		
		topicDAO.save(selectedTag);
	}
	
	public Tag getTagForName(String completeText) {
		return tagDAO.getTag(userService.getCurrentUser(),completeText);
	}
	public List<TagStat> getTagStats() {
		return tagDAO.getTagStats(userService.getCurrentUser());
	}


}
