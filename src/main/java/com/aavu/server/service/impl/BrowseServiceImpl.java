package com.aavu.server.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.WebLink;
import com.aavu.server.dao.BrowseDAO;
import com.aavu.server.service.BrowseService;
import com.aavu.server.service.UserService;

/**
 * 
 * 
 * @author Jeff Dwyer
 * 
 */
@Transactional(readOnly = true)
public class BrowseServiceImpl implements BrowseService {

	private static final Logger log = Logger.getLogger(BrowseServiceImpl.class);

	private BrowseDAO browseDAO;

	private UserService userService;


	@Required
	public void setBrowseDAO(BrowseDAO browseDAO) {
		this.browseDAO = browseDAO;
	}

	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<RealTopic> getTopTopics() {
		return browseDAO.getTopTopics();
	}

	public List<WebLink> getTopWeblinks() {
		return browseDAO.getTopWeblinks();
	}


}
