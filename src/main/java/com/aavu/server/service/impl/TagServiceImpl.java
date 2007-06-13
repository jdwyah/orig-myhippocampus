package com.aavu.server.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.EditDAO;
import com.aavu.server.dao.SelectDAO;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.service.UserService;

public class TagServiceImpl implements com.aavu.server.service.TagService {
	private static final Logger log = Logger.getLogger(TagServiceImpl.class);

	private TagDAO tagDAO;
	private EditDAO editDAO;
	private SelectDAO selectDAO;
	private UserService userService;
	
	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Required
	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}
	@Required
	public void setSelectDAO(SelectDAO selectDAO) {
		this.selectDAO = selectDAO;
	}
	@Required
	public void setEditDAO(EditDAO editDAO) {
		this.editDAO = editDAO;
	}
	public List<Tag> getAllTags() {
		return tagDAO.getAllTags(userService.getCurrentUser());
	}


	/**
	 * NOTE: remember we're applying "read-only" transactions to all "service.get*()"
	 * 
	 */
	public Tag createTagIfNonExistent(String tagName) throws HippoBusinessException {
		log.debug("load tag named: "+tagName);

		Tag rt = tagDAO.getTag(userService.getCurrentUser(),tagName);
		if(null == rt){
			log.debug("was null, creating ");
			Tag t = new Tag();
			t.setName(tagName);
			t.setPublicVisible(false);
			t.setUser(userService.getCurrentUser());
			editDAO.save(t);

			log.debug("created: "+t.getId());
			return t;
		}else{
			log.debug("existed "+rt.getId()+"\n"+rt);

			return rt;
		}
	}




	public List<TopicIdentifier> getTagsStarting(String match) {
		return tagDAO.getTagsStarting(userService.getCurrentUser(),match);
	}






	/**
	 * TODO who sets possibly unsaved meta Users? Client?
	 * @throws HippoBusinessException 
	 *  
	 */
	public Tag save(Tag selectedTag) throws HippoBusinessException {

		log.debug("Servic tag.save() setting user");

		selectedTag.setUser(userService.getCurrentUser());

		
		return (Tag) editDAO.save(selectedTag);
	}
	
	public Tag getTagForName(String completeText) {
		return tagDAO.getTag(userService.getCurrentUser(),completeText);
	}
	public List<TagStat> getTagStats() {
		log.info("getting tag stats "+userService.getCurrentUser().getUsername());
		return tagDAO.getTagStats(userService.getCurrentUser());
	}


}
