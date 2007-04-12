package com.aavu.server.web.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.aavu.client.domain.User;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.MailingListCommand;

public class ManifestoController extends BasicController {
	private static final Logger log = Logger.getLogger(ManifestoController.class);


	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse arg1) throws Exception {


		Map model = getDefaultModel();		
		model.put("command",new MailingListCommand());
		
		return new ModelAndView(getView(),model);
		
	}


}
