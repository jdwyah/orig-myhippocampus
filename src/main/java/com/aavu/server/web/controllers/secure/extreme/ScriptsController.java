package com.aavu.server.web.controllers.secure.extreme;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.dao.InitDAO;
import com.aavu.server.service.UserService;

public class ScriptsController extends MultiActionController {
	private static final Logger log = Logger.getLogger(ScriptsController.class);
	
	private UserService userService;	
	private InitDAO initDAO;
	
	private String view;
	
	
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response){
		
		log.debug("Servername: "+request.getServerName());
			
		return new ModelAndView(view);
	}

	public ModelAndView upgrade(HttpServletRequest request, HttpServletResponse response) throws PermissionDeniedException, HippoBusinessException{
		
		initDAO.upgradeRemoveTags();
		
		return new ModelAndView(view,"message","Upgrade Success"); 
	}
	
	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Required
	public void setView(String view) {
		this.view = view;
	}
	@Required
	public void setInitDAO(InitDAO initDAO) {
		this.initDAO = initDAO;
	}

}
