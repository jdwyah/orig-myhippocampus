package com.aavu.server.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.domain.PersistedFile;
import com.aavu.server.s3.com.amazon.s3.AWSAuthConnection;
import com.aavu.server.s3.com.amazon.s3.GetResponse;
import com.aavu.server.s3.com.amazon.s3.S3Object;
import com.aavu.server.service.FilePersistanceService;
import com.aavu.server.service.UserService;

public class S3Controller extends AbstractController {
	private static final Logger log = Logger.getLogger(S3Controller.class);

	private UserService userService;
	
	private FilePersistanceService fileService;
	
	public void setFileService(FilePersistanceService fileService) {
		this.fileService = fileService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * TODO check security
	 * 
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String key = request.getParameter("key");
		if(null == key ){ 
			throw new HippoBusinessException("No Key");
		}
		
		User u = userService.getCurrentUser();
		
		
		PersistedFile content = fileService.getFile(u,key);
				
		//String mimetype = this.getServletContext().getMimeType(filename);
		
		response.setContentType(content.getContentType());
		response.setContentLength(content.getContent().length);
		FileCopyUtils.copy(content.getContent() , response.getOutputStream());
		return null;
	}



}
