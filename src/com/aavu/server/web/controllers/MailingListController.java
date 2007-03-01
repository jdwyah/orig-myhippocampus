package com.aavu.server.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.server.dao.MailingListDAO;
import com.aavu.server.web.domain.MailingListCommand;
import com.aavu.server.web.domain.validation.MailingListCommandValidator;

public class MailingListController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(MailingListController.class);
	
	private MailingListDAO mailingDAO;
	
	
	public MailingListController(){
		setCommandClass(MailingListCommand.class);		
		setValidator(new MailingListCommandValidator());
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest req, HttpServletResponse arg1, Object obj, BindException arg3) throws Exception {
		
		MailingListCommand comm = (MailingListCommand) obj;
		
		comm.setUserAgent(req.getHeader("User-Agent"));
		comm.setReferer(req.getHeader("Referer"));
		comm.setHost(req.getHeader("Host")); 

		log.debug("email: "+comm.getEmail());
		log.debug("user-agent: "+comm.getUserAgent());
		
		mailingDAO.createEntry(comm);	
				
		String successStr = "Thanks "+comm.getEmail()+" we'll let you know when you can sign up for an account.";

		return new ModelAndView(getSuccessView(),"message",successStr);
		
	}

	public void setMailingDAO(MailingListDAO mailingDAO) {
		this.mailingDAO = mailingDAO;
	}

	

}
