package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.exception.HippoBusinessException;

public interface TopicService {

	List<TopicIdentifier> getAllTopicIdentifiers();

	Topic save(Topic topic) throws HippoBusinessException;

	List<String> getTopicsStarting(String match);

	Topic getForName(String string);

	List<TopicIdentifier> getTopicIdsWithTag(Tag tag);

	Topic getForID(long topicID);

	List<TimeLineObj> getTimelineObjs();

	List<Topic> save(Topic[] topics) throws HippoBusinessException;

	Occurrence save(Occurrence link);
	
	List<TopicIdentifier> getLinksTo(Topic topic);

	/**
	 * Create the link and save it for each of the tags enumerated, creating
	 * and saving those if necessary. 
	 * 
	 * @param link
	 * @param tags
	 * @throws HippoBusinessException 
	 */
	void addLinkToTags(WebLink link, String[] tags) throws HippoBusinessException;
	
	List<Topic> search(String searchString);
}
