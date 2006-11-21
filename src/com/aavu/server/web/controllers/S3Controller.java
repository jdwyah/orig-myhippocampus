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
import com.aavu.server.s3.com.amazon.s3.AWSAuthConnection;
import com.aavu.server.s3.com.amazon.s3.GetResponse;
import com.aavu.server.s3.com.amazon.s3.S3Object;
import com.aavu.server.service.UserService;

public class S3Controller extends AbstractController {
	private static final Logger log = Logger.getLogger(S3Controller.class);

	private UserService userService;
	private AWSAuthConnection awsConnection; 
		
	public void setAwsConnection(AWSAuthConnection awsConnection) {
		this.awsConnection = awsConnection;
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
		
		User u = userService.getCurrentUser();
		
		if(!key.startsWith(u.getUsername())){
			throw new HippoBusinessException("User "+u.getUsername()+" cannot access "+key);
		}
		
		GetResponse awsResponse = awsConnection.get(awsConnection.getDefaultBucket(), key, null);
		
		S3Object file = awsResponse.object;
		
		String contentType = "text/plain";
		try{
			List l = (List) awsResponse.object.metadata.get("content-type");
			contentType = (String) l.get(0);
		}catch(Exception e){
			log.debug("Fail "+e+" trying with text/plain");
		}
		
		byte[] content = file.data;
				
		//String mimetype = this.getServletContext().getMimeType(filename);
		
		response.setContentType(contentType);
		response.setContentLength(content.length);
		FileCopyUtils.copy(content , response.getOutputStream());
		return null;
	}



}
