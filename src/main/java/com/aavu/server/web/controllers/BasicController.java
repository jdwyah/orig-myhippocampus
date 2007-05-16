package com.aavu.server.web.controllers;

import java.util.HashMap;
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

public class BasicController extends AbstractController {
	private static final Logger log = Logger.getLogger(BasicController.class);

	private String view;
	protected UserService userService;	

	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse arg1) throws Exception {

		//log.debug("SERVLET PATH: "+req.getServletPath()+" "+req.getPathInfo()+" "+req.getQueryString());

		return new ModelAndView(getView(),getDefaultModel(req));
		
	}
	
	protected Map<String,Object> getDefaultModel(HttpServletRequest req){
		Map<String,Object> model = new HashMap<String, Object>();
		
		User su = null;
		try{
			su = userService.getCurrentUser();	
			model.put("user",su);			
		}catch(UsernameNotFoundException e){
			//log.debug("No user logged in.");
		}
		
		//IE < 7 check 
		String userAgent = req.getHeader("User-Agent");		
		if(userAgent.contains("MSIE") 
				&& 
				!userAgent.contains("MSIE 7")) {
			model.put("iePre7", true);
		}
		
		return model;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
