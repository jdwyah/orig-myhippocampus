package com.aavu.server.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aavu.client.domain.S3File;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.client.exception.HippoInfrastructureException;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.domain.PersistedFile;
import com.aavu.server.s3.com.amazon.s3.AWSAuthConnection;
import com.aavu.server.s3.com.amazon.s3.GetResponse;
import com.aavu.server.s3.com.amazon.s3.ListBucketResponse;
import com.aavu.server.s3.com.amazon.s3.ListEntry;
import com.aavu.server.s3.com.amazon.s3.Response;
import com.aavu.server.s3.com.amazon.s3.S3Object;
import com.aavu.server.service.FilePersistanceService;
import com.aavu.server.web.controllers.FileUploadController;

public class FilePersistanceServiceImpl implements FilePersistanceService {

	private static final Logger log = Logger.getLogger(FilePersistanceServiceImpl.class);

	private AWSAuthConnection awsConnection;

	private TopicDAO topicDAO; 	

	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}
	public void setAwsConnection(AWSAuthConnection awsConnection) {
		this.awsConnection = awsConnection;
	}

	/*
	 * Save to S3
	 * 
	 * NOTE: Caller is responsible for saving the S3File.class
	 * 
	 * (non-Javadoc)
	 * @see com.aavu.server.service.FilePersistanceService#saveFile(java.lang.String, int, byte[], java.lang.String, com.aavu.client.domain.User)
	 */
	public String saveFile(String contentType, long topicID, byte[] file, String filename, User user) throws HippoInfrastructureException {

		String key = user.getUsername()+"/"+topicID+"/"+filename;

		S3Object s3object = new S3Object(file,null);

		log.debug("ready to put bucket |"+awsConnection.getDefaultBucket()+"|");
		log.debug("ready to put at "+key);
		log.debug("putting content-type: "+contentType);

		Map<String, List<String>> metaMap = new HashMap<String, List<String>>();
		List<String> type = new ArrayList<String>();
		type.add(contentType);
		metaMap.put("content-type", type);
		s3object.metadata = metaMap;

		log.debug("Put with type: "+contentType);

		Response awsResp;
		try {
			awsResp = awsConnection.put(awsConnection.getDefaultBucket(), key, s3object, null);

			if(awsResp.connection.getResponseCode() != HttpURLConnection.HTTP_OK){
				log.error("File Upload failure "+awsResp);
				throw new HippoInfrastructureException(FileUploadController.ERR_STR+" File Upload failure "+awsResp);
			}

			//TODO remove this check
			ListBucketResponse listresp = awsConnection.listBucket(awsConnection.getDefaultBucket(), null, null, null, null);
			log.debug("Found: "+listresp.entries.size());
			for (Iterator iter = listresp.entries.iterator(); iter.hasNext();) {
				ListEntry element = (ListEntry) iter.next();
				log.debug("f: "+element.key+" Size: "+element.size+" ");
			}

			return key;

		} catch (MalformedURLException e) {
			throw new HippoInfrastructureException(e);				
		} catch (IOException e) {
			throw new HippoInfrastructureException(e);
		}
	}

	/**
	 * 
	 * NOTE: unlike the other method, this method will save the S3File.class to the given topic 
	 * 
	 * (non-Javadoc)
	 * @throws HippoBusinessException 
	 * @see com.aavu.server.service.FilePersistanceService#saveFileToTopic(java.lang.String, int, java.io.InputStream, java.lang.String, com.aavu.client.domain.User)
	 */	
	public void saveFileToTopic(PersistedFile file, Topic topic, User user) throws HippoException {

		if(topic.getId() == 0){
			throw new HippoBusinessException("Can't save File to unsaved Topic.");
		}

		String key = saveFile(file.getContentType(), topic.getId(), file.getContent(), file.getFilename(), user);

		S3File fileObj = new S3File(user,file.getFilename(),key);
		topic.getOccurences().add(fileObj);

		topicDAO.save(topic);

	}
	

	/**
	 * Get the file. Check to make sure the key starts with username. 
	 * TODO is that secure enough?
	 * 
	 */
	public PersistedFile getFile(User u, String key) throws HippoException {
		
		
		if(!key.startsWith(u.getUsername())){
			throw new HippoBusinessException("User "+u.getUsername()+" cannot access "+key);
		}
		
		GetResponse awsResponse;
		try {
			awsResponse = awsConnection.get(awsConnection.getDefaultBucket(), key, null);
		} catch (Exception e1) {
			throw new HippoInfrastructureException(e1);
		}
		
		S3Object file = awsResponse.object;
		
		String contentType = "text/plain";
		try{
			List l = (List) awsResponse.object.metadata.get("content-type");			
			contentType = (String) l.get(0);
								
		}catch(Exception e){
			log.warn("Fail "+e+" trying with text/plain");
		}
		
		log.debug("Returning contentType "+contentType);
		
		byte[] content = file.data;
		
		//TODO 
		return new PersistedFile(content, contentType, null);
	}

	/**
	 * Delete the file. Check to make sure the key starts with username. 
	 * TODO is that secure enough?
	 * 
	 */
	public void deleteFile(User u, String key) throws HippoException {
		
		
		if(!key.startsWith(u.getUsername())){
			throw new HippoBusinessException("User "+u.getUsername()+" cannot access "+key);
		}
		
		Response awsResponse;
		try {
			awsResponse = awsConnection.delete(awsConnection.getDefaultBucket(), key, null);
		} catch (Exception e1) {
			throw new HippoInfrastructureException(e1);
		}
		
		
	}
	
	
}
