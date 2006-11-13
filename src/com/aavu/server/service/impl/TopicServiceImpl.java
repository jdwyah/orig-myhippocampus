package com.aavu.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.compass.core.Compass;
import org.compass.core.CompassDetachedHits;
import org.compass.core.CompassHighlightedText;
import org.compass.core.CompassHitIterator;
import org.compass.core.CompassHits;
import org.compass.core.CompassTemplate;
import org.compass.core.impl.DefaultCompassHit;
import org.compass.gps.CompassGps;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.WebLink;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

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

	public Topic save(Topic topic) throws HippoBusinessException {
		System.out.println("Topic Save Setting User "+userService.getCurrentUser());
		topic.setLastUpdated(new Date());
		topic.setUser(userService.getCurrentUser());
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
			System.out.println("-----t-----"+t.toPrettyString());
			Topic st = save(t);
			System.out.println("-----st-----"+st.toPrettyString());
		}
	}


	

}
