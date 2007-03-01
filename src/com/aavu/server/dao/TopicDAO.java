package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.web.domain.UserPageBean;

public interface TopicDAO {

	void saveSimple(Topic t);
	
	Topic save(Topic t) throws HippoBusinessException;

	List<TopicIdentifier> getAllTopicIdentifiers(User user);

	List<String> getTopicsStarting(User user,String match);

	Topic getForName(User user,String string);

	List<TopicIdentifier> getLinksTo(Topic topic,User user);
	
	void tester();

	List<TopicTypeConnector> getTopicIdsWithTag(long tagid,User user);

	Topic getForID(User currentUser, long topicID);

	List<TimeLineObj> getTimeline(long meta_id, User user);

	List<Topic> getAllTopics();

	Occurrence save(Occurrence link);

	List<TopicIdentifier> getTopicForOccurrence(long id);

	MindTree getTree(MindTreeOcc occ);
	MindTree save(MindTree tree);

	void populateUsageStats(UserPageBean rtn);

	void delete(Topic topic);
	
	void saveTopicsLocation(long tagID, long topicID, double longitude, double latitude);

	Topic load(long topicID);
	Topic get(long topicID);

	MetaSeeAlso getSeeAlsoSingleton();

	WebLink getWebLinkForURI(String url, User currentUser);

	

}