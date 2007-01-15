package com.aavu.server.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.client.domain.User;
import com.aavu.server.service.FilePersistanceService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.FileUploadBean;

public class FileUploadController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(FileUploadController.class);

	/**
	 * This string will be parsed in UploadWidget.java to see if there's been an error,
	 * otherwise it will assume the string is the filename so we need to make sure to begin
	 * the return with Error if there's been a problem.
	 * TODO make sure we always do
	 */
	public static final String ERR_STR = "Error:";
	
	private UserService userService;

	private FilePersistanceService fileService;
	
	
	public void setFileService(FilePersistanceService fileService) {
		this.fileService = fileService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Return is a simple string with the key.
	 * 
	 * Caller is responsible for creating the S3File.class that we'll then save as the URI of 
	 * an S3File Occurence for the Topic in UploadWidget. 
	 * 
	 * //    		conn.put(BUCKET_NAME, filename, S3.S3Object(filedata),
	   //   	            {'x-amz-acl': 'public-read', 'Content-Type': content_type})
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		log.debug("onSubmit");
		
		// cast the bean
        FileUploadBean bean = (FileUploadBean) command;

        int topicID = bean.getTopicID();
        
    	log.debug("topic: "+topicID);

        
        //let's see if there's content there
        byte[] file = bean.getFile().getBytes();
        if (file == null) {
             // hmm, that's strange, the user did not upload anything
        	log.warn("Uploaded null");
        	return new ModelAndView(getSuccessView(),"string",ERR_STR+" Null Upload");
        }else{
        	log.debug("Uploaded ok");
	
        	String filename = bean.getFile().getOriginalFilename();
        	
            User user = userService.getCurrentUser();                
    		String key = fileService.saveFile(bean.getFile().getContentType(), topicID, file, filename, user);

    		return new ModelAndView(getSuccessView(),"string",key);    		
        }

	}

	
	


}
