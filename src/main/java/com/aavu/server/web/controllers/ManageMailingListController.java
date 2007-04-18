package com.aavu.server.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoInfrastructureException;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.domain.MailingListEntry;
import com.aavu.server.service.InvitationService;
import com.aavu.server.service.UserService;

public class ManageMailingListController extends MultiActionController {
	private static final Logger log = Logger.getLogger(ManageMailingListController.class);
	
	private InvitationService invitationService;
	
	private String view;
	
	
	private Map<String,Object> getModel(){
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("list", invitationService.getMailingList());
		return model;
	}
	
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response){
		
		
		return new ModelAndView(view,getModel());
	}

	public ModelAndView resendInvite(HttpServletRequest request, HttpServletResponse response) throws PermissionDeniedException, HippoInfrastructureException{
		
		String idStr = request.getParameter("entry");		
		Long id = Long.parseLong(idStr);
		
		MailingListEntry entry = invitationService.getEntryById(id);
		
		invitationService.sendInvite(entry);
				
		Map model = getModel();
		model.put("message", "Success ReSending Invite");
				
		return new ModelAndView(view,getModel());
	}
	
	

	public void setView(String viewUserList) {
		this.view = viewUserList;
	}

	public void setInvitationService(InvitationService invitationService) {
		this.invitationService = invitationService;
	}

}
