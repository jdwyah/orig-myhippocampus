package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.web.domain.UserPageBean;

public interface TopicDAO {

	Topic save(Topic t) throws HippoBusinessException;

	List<TopicIdentifier> getAllTopicIdentifiers(User user);

	List<String> getTopicsStarting(User user,String match);

	Topic getForName(User user,String string);

	List<TopicIdentifier> getLinksTo(Topic topic,User user);
	
	void tester();

	List<TopicIdentifier> getTopicIdsWithTag(Tag tag,User user);

	Topic getForID(User currentUser, long topicID);

	List<TimeLineObj> getTimeline(User user);

	List<Topic> getAllTopics();

	void deleteAllTables();

	Occurrence save(Occurrence link);

	List<TopicIdentifier> getTopicForOccurrence(long id);

	MindTree getTree(MindTreeOcc occ);
	MindTree save(MindTree tree);

	void populateUsageStats(UserPageBean rtn);

}