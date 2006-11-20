package com.aavu.server.web.controllers;

import java.net.HttpURLConnection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoInfrastructureException;
import com.aavu.server.s3.com.amazon.s3.AWSAuthConnection;
import com.aavu.server.s3.com.amazon.s3.ListBucketResponse;
import com.aavu.server.s3.com.amazon.s3.ListEntry;
import com.aavu.server.s3.com.amazon.s3.Response;
import com.aavu.server.s3.com.amazon.s3.S3Object;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.FileUploadBean;

public class FileUploadController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(FileUploadController.class);
	private static final Integer MAX_RES = 10;
	
	private UserService userService;
	private AWSAuthConnection awsConnection; 
	
	
	public void setAwsConnection(AWSAuthConnection awsConnection) {
		this.awsConnection = awsConnection;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

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
        }else{
        	log.debug("Uploaded ok");
	
        	String filename = bean.getFile().getOriginalFilename();
        	
            User user = userService.getCurrentUser();                
    		String key = user.getUsername()+"/"+topicID+"/"+filename;
        	
    		S3Object s3object = new S3Object(file,null);
    		
    		log.debug("ready to put bucket |"+awsConnection.getDefaultBucket()+"|");
    		log.debug("ready to put at "+key);
    		
    		Response awsResp = awsConnection.put(awsConnection.getDefaultBucket(), key, s3object, null);
    		if(awsResp.connection.getResponseCode() != HttpURLConnection.HTTP_OK){
    			log.error("File Upload failure "+awsResp);
    			throw new HippoInfrastructureException("File Upload failure "+awsResp);
    		}
    		
    		//TODO remove this check
    		ListBucketResponse listresp = awsConnection.listBucket(awsConnection.getDefaultBucket(), null, null, null, null);
    		log.debug("Found: "+listresp.entries.size());
    		for (Iterator iter = listresp.entries.iterator(); iter.hasNext();) {
    			ListEntry element = (ListEntry) iter.next();
				log.debug("f: "+element.key+" "+element.size);
			}
    		
        }

		return new ModelAndView(getSuccessView(),"string","Success");
		
	}


}
