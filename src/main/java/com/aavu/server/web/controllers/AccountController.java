package com.aavu.server.web.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

public class AccountController extends BasicController {

	private static final Logger log = Logger.getLogger(AccountController.class);
	private String paypalEndpoint;
	private String paypalEmail;


	public void setPaypalEndpoint(String paypalEndpoint) {		
		this.paypalEndpoint = paypalEndpoint;
	}



	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse arg1) throws Exception {

		Map model = getDefaultModel(req);

		model.put("paypalEndpoint", paypalEndpoint);
		model.put("receiverEmail", paypalEmail);
		
		
		model.put("subscriptions", userService.getAllUpgradeSubscriptions());

		return new ModelAndView(getView(),model);

	}



	public void setPaypalEmail(String paypalEmail) {
		this.paypalEmail = paypalEmail;
	}


}
