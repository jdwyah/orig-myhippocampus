package com.aavu.server.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.aavu.client.domain.User;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.service.UserService;

public class UserManagerController extends MultiActionController {
	
	private UserService userService;
	private String viewUserList;
	
	
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response){
		
		
		
		List<User> users = userService.getAllUsers();		
		return new ModelAndView(viewUserList,"users",users);
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response){
		

		String idStr = request.getParameter("user");		
		Integer id = Integer.parseInt(idStr);
		
		userService.delete(id);
		
		List<User> users = userService.getAllUsers();		
		return new ModelAndView(viewUserList,"users",users); 
	}
	public ModelAndView enable(HttpServletRequest request, HttpServletResponse response){
		
		
		String idStr = request.getParameter("user");		
		Integer id = Integer.parseInt(idStr);
		
		userService.toggleEnabled(id);
		
		List<User> users = userService.getAllUsers();		
		return new ModelAndView(viewUserList,"users",users); 
	}
	
	public ModelAndView supervisor(HttpServletRequest request, HttpServletResponse response){

		String idStr = request.getParameter("user");		
		Integer id = Integer.parseInt(idStr);
		
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
