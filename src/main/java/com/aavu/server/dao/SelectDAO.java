package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.server.web.domain.UserPageBean;

public interface SelectDAO {

	
	
	List<DatedTopicIdentifier> getAllTopicIdentifiers(User user,int start, int max, String startStr);
	Topic getForID(User currentUser, long topicID);
	Topic getForName(User user,String string);
	List<TimeLineObj> getTimeline(long tagID, User user);
	List<TimeLineObj> getTimeline(User user);
	MetaSeeAlso getSeeAlsoSingleton();
	List<TopicTypeConnector> getTopicIdsWithTag(long tagid,User user);	
	MindTree getTree(MindTreeOcc occ);
	List<TopicIdentifier> getLinksTo(Topic topic,User user);	
	List<LocationDTO> getLocations(long tagID, User user);

	
	List<TopicIdentifier> getTagsStarting(User user,String match);

	List<TagStat> getTagStats(User user);
	
	
	Root getRoot(User user, User currentUser);
	
	Topic get(long topicID);
	
	List<Meta> getAllMetas(User currentUser);

	List<DatedTopicIdentifier> getAllPublicTopicIdentifiers(User user,int start, int max, String startStr);
	
	List<DatedTopicIdentifier> getAllTopicIdentifiers(User user,boolean all);

	
	List<Topic> getAllTopics(User u);


	List<LocationDTO> getLocations(User user);
	
	Occurrence getOccurrrence(long id);

	Topic getPublicForName(String username, String string);




	int getTopicCount(final User user);
	
	List<TopicIdentifier> getTopicForOccurrence(long id);

	List<TopicTypeConnector> getTopicIdsWithTag(long id);

	List<TopicIdentifier> getTopicsStarting(User user,String match);



	UserPageBean getUsageStats(final User user);
	WebLink getWebLinkForURI(String url, User currentUser);

	
	void tester();
	List<TopicTypeConnector> getRootTopics(User forUser, User currentUser);

	

	

}