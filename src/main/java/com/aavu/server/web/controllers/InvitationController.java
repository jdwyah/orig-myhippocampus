package com.aavu.server.web.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.aavu.client.domain.User;
import com.aavu.server.service.InvitationService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.MailingListCommand;

/**
 * TODO Cheating! Lazy programmer didn't feel like doing a SimpleFormController, 
 * so just reading request.
 * 
 * @author Jeff Dwyer
 *
 */
public class InvitationController extends BasicController {
	private static final Logger log = Logger.getLogger(InvitationController.class);

	private InvitationService invitationService;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse arg1) throws Exception {


		String email = req.getParameter("email");
		
		Map model = getDefaultModel(req);				
		
		User u = (User) model.get("user");		
		invitationService.createAndSendInvitation(email, u);
		
		//userService.getCurrentUser()
		
		model.put("message", "We've sent "+email+" an invitation.<br> Thanks for helping to spread the word.");
		
		return new ModelAndView(getView(),model);
		
	}

	public void setInvitationService(InvitationService invitationService) {
		this.invitationService = invitationService;
	}


}
