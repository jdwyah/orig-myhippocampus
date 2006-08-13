package com.aavu.server.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


public class LoginController extends AbstractController{
	private static Logger log = Logger.getLogger(LoginController.class);

	private String view;
	
	
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}


	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse arg1) throws Exception {
		if(req.getParameter("login_error") != null){
			String message = ((AuthenticationException) req.getSession().getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage();
			return new ModelAndView(getView(),"login_error",message);
		}
		return new ModelAndView(getView());
	}

//	private UserDAO userDAO;

	
	
//
//	@Override
//	protected void onBindAndValidate(HttpServletRequest arg0, Object command, BindException error) throws Exception {		
//		super.onBindAndValidate(arg0, command, error);
//
//		LoginCommand cmd = (LoginCommand) command;
//		User employee;
//
//		if(userDAO.isGodPassword(cmd.getPassword())){
//			error.rejectValue("password", "invalid.password", "Must enter your real password. You cannot access this page using the GOD password.");					
//		}
//		else{
//			employee = userDAO.getUser(cmd.getUsername(), cmd.getPassword());
//
//			
//			
//			if(employee == null || !userDAO.isUserValid(employee.getId())){
//				log.debug("employee: "+employee);
//				
//				if(employee != null){
//					log.debug("valid: "+userDAO.isUserValid(employee.getId()));
//				}
//				
//				error.rejectValue("password", "invalid.password", "Invalid Login.");
//			}
//
//			//TODO check authorizations!!
//		}
//	}




}
