package com.aavu.server.dao;

import java.io.Serializable;
import java.util.List;

import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.web.domain.UserPageBean;

public interface TopicDAO {

	void changeState(Topic t, boolean toIsland);
	
	void delete(Topic topic);

	void deleteOccurrence(Occurrence o);
	void evict(Serializable obj);

	Topic get(long topicID);
	List getAllMetas(User currentUser);
	
	List<DatedTopicIdentifier> getAllTopicIdentifiers(User user);

	List<DatedTopicIdentifier> getAllTopicIdentifiers(User user,boolean all);
	
	List<Topic> getAllTopics(User u);

	Topic getForID(User currentUser, long topicID);

	Topic getForName(User user,String string);

	List<TopicIdentifier> getLinksTo(Topic topic,User user);
	List<LocationDTO> getLocations(long tagID, User user);
	

	List<LocationDTO> getLocations(User user);

	Occurrence getOccurrrence(long id);

	MetaSeeAlso getSeeAlsoSingleton();
	List<TimeLineObj> getTimeline(long tagID, User user);

	List<TimeLineObj> getTimeline(User user);

	int getTopicCount(final User user);
	
	List<TopicIdentifier> getTopicForOccurrence(long id);

	List<TopicTypeConnector> getTopicIdsWithTag(long tagid,User user);
	List<TopicIdentifier> getTopicsStarting(User user,String match);

	MindTree getTree(MindTreeOcc occ);

	UserPageBean getUsageStats(final User user);
	WebLink getWebLinkForURI(String url, User currentUser);

	Topic load(long topicID);

	MindTree save(MindTree tree);

	Occurrence save(Occurrence link);

	Topic save(Topic t) throws HippoBusinessException;

	Long saveSimple(Topic t);

	void saveTopicsLocation(long tagID, long topicID, double longitude, double latitude);

	void tester();
	

}