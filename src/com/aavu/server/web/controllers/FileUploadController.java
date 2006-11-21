package com.aavu.server.web.controllers;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	/**
	 * This string will be parsed in UploadWidget.java to see if there's been an error,
	 * otherwise it will assume the string is the filename so we need to make sure to begin
	 * the return with Error if there's been a problem.
	 * TODO make sure we always do
	 */
	private static final String ERR_STR = "Error:";
	
	private UserService userService;
	private AWSAuthConnection awsConnection; 
	
	
	public void setAwsConnection(AWSAuthConnection awsConnection) {
		this.awsConnection = awsConnection;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Return is a simple string with the key that we'll then save as the URI of 
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
    		String key = user.getUsername()+"/"+topicID+"/"+filename;
        	
    		S3Object s3object = new S3Object(file,null);
    		
    		log.debug("ready to put bucket |"+awsConnection.getDefaultBucket()+"|");
    		log.debug("ready to put at "+key);
    		log.debug("putting content-type: "+bean.getFile().getContentType());
    		    		    		    		
    		Map<String, List<String>> metaMap = new HashMap<String, List<String>>();
    		List<String> type = new ArrayList<String>();
    		type.add(bean.getFile().getContentType());
    		metaMap.put("content-type", type);
    		s3object.metadata = metaMap;
    		
    		Response awsResp = awsConnection.put(awsConnection.getDefaultBucket(), key, s3object, null);
    		if(awsResp.connection.getResponseCode() != HttpURLConnection.HTTP_OK){
    			log.error("File Upload failure "+awsResp);
    			throw new HippoInfrastructureException(ERR_STR+" File Upload failure "+awsResp);
    		}
    		
    		//TODO remove this check
    		ListBucketResponse listresp = awsConnection.listBucket(awsConnection.getDefaultBucket(), null, null, null, null);
    		log.debug("Found: "+listresp.entries.size());
    		for (Iterator iter = listresp.entries.iterator(); iter.hasNext();) {
    			ListEntry element = (ListEntry) iter.next();
				log.debug("f: "+element.key+" "+element.size);
			}

    		return new ModelAndView(getSuccessView(),"string",key);    		
        }

	}


}
