package com.aavu.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.UserPageBean;

public class TopicServiceImpl implements TopicService {
	private static final Logger log = Logger.getLogger(TopicServiceImpl.class);

	private TopicDAO topicDAO;
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}


	public Topic getForName(String string) {
		return topicDAO.getForName(userService.getCurrentUser(),string);
	}

	public List<String> getTopicsStarting(String match) {
		return topicDAO.getTopicsStarting(userService.getCurrentUser(),match);
	}

	/**
	 * TODO I don't really like setting the user here, but it's tricky 
	 * to give the Topic & Entry objects something that knows about users.
	 * 
	 */
	public Topic save(Topic topic) throws HippoBusinessException {
		System.out.println("Topic Save Setting User "+userService.getCurrentUser());
		topic.setLastUpdated(new Date());
		topic.setUser(userService.getCurrentUser());
		Set<Occurrence> occs = topic.getOccurences();
		for(Occurrence o : occs){
			o.setUser(userService.getCurrentUser());
		}
		return topicDAO.save(topic);
	}
	public List<TopicIdentifier> getTopicIdsWithTag(Tag tag) {
		return topicDAO.getTopicIdsWithTag(tag,userService.getCurrentUser());
	}
	public List<TopicIdentifier> getAllTopicIdentifiers() {
		return topicDAO.getAllTopicIdentifiers(userService.getCurrentUser());
	}
	public Topic getForID(long topicID) {
		return topicDAO.getForID(userService.getCurrentUser(),topicID);
	}
	public List<TimeLineObj> getTimelineObjs() {
		return topicDAO.getTimeline(userService.getCurrentUser());
	}
	public List<Topic> save(Topic[] topics) throws HippoBusinessException {
		List<Topic> rtn = new ArrayList<Topic>();
		for (Topic topic : topics) {
			rtn.add(save(topic));
		}
		return rtn;
	}
	public Occurrence save(Occurrence link) {
		return topicDAO.save(link);
	}
	public List<TopicIdentifier> getLinksTo(Topic topic) {
		return topicDAO.getLinksTo(topic, userService.getCurrentUser());
	}
	public void addLinkToTags(WebLink link, String[] tags) throws HippoBusinessException {

		for (String string : tags) {			
			log.debug("str: "+string);			

			Topic t = getForName(string);			
			if(null == t){
				log.debug("was null, creating as Tag ");
				t = new Topic();
				t.setTitle(string);				
				t.setUser(userService.getCurrentUser());							
			}			
			t.getOccurences().add(link);
			Topic st = save(t);
		}
	}
	public MindTree getTree(MindTreeOcc occ) {
		return topicDAO.getTree(occ);
	}
	public MindTree saveTree(MindTree tree) {
		return topicDAO.save(tree);
	}
	public UserPageBean getUserPageBean(User su) {
		UserPageBean rtn = new UserPageBean();
		rtn.setUser(su);
		topicDAO.populateUsageStats(rtn);
		return rtn;
	}
	
	/*
	 * TODO maybe AOP secure this? 
	 * 
	 * (non-Javadoc)
	 * @see com.aavu.server.service.TopicService#delete(com.aavu.client.domain.Topic)
	 */
	public void delete(Topic topic) throws HippoBusinessException {
		if(userService.getCurrentUser().equals(topic.getUser())){
			topicDAO.delete(topic);

			//TODO delete S3Files 
			//TODO delete Weblinks that were only referenced by us
			//TODO_ delete our Entries - done
			//TODO_ delete our MindTrees - done
			
		}else{
			throw new HippoBusinessException("User "+userService.getCurrentUser().getUsername()+" can't delete this topic");
		}
	}




}
