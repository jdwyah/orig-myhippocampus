package com.aavu.server.web.controllers.secure.extreme;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.aavu.client.domain.User;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.service.UserService;

public class UserManagerController extends MultiActionController {
	private static final Logger log = Logger.getLogger(UserManagerController.class);
	
	private UserService userService;
	private String viewUserList;
	
	
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response){
		
		log.debug("Servername: "+request.getServerName());
		
		List<User> users = userService.getAllUsers();		
		return new ModelAndView(viewUserList,"users",users);
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws PermissionDeniedException{
		

		String idStr = request.getParameter("user");		
		Integer id = Integer.parseInt(idStr);
		
		userService.delete(id);
		
		List<User> users = userService.getAllUsers();		
		return new ModelAndView(viewUserList,"users",users); 
	}
	public ModelAndView enable(HttpServletRequest request, HttpServletResponse response) throws PermissionDeniedException{
		
		
		String idStr = request.getParameter("user");		
		Integer id = Integer.parseInt(idStr);
		
		userService.toggleEnabled(id);
		
		List<User> users = userService.getAllUsers();		
		return new ModelAndView(viewUserList,"users",users); 
	}
	
	public ModelAndView supervisor(HttpServletRequest request, HttpServletResponse response) throws PermissionDeniedException{

		String idStr = request.getParameter("user");		
		Integer id = Integer.parseInt(idStr);

		log.debug("supervisor change for id: "+id+" str:"+idStr);
		
		userService.toggleSupervisor(id);
		
		List<User> users = userService.getAllUsers();		
		return new ModelAndView(viewUserList,"users",users); 
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setViewUserList(String viewUserList) {
		this.viewUserList = viewUserList;
	}

}
