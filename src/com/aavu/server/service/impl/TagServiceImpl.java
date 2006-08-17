package com.aavu.server.service.impl;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.service.UserService;

public class TagServiceImpl implements com.aavu.server.service.TagService {

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




	public Tag getTag(String tagName) {
		return tagDAO.getTag(userService.getCurrentUser(),tagName);
	}




	public List<Tag> getTagsStarting(String match) {
		return tagDAO.getTagsStarting(userService.getCurrentUser(),match);
	}




	public void removeTag(String itemText) {
		tagDAO.removeTag(userService.getCurrentUser(),itemText);
	}




	public void save(Tag selectedTag) {
		tagDAO.save(userService.getCurrentUser(),selectedTag);
	}


}
