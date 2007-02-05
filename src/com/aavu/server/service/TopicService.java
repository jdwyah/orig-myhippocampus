package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AbstractSaveCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.web.domain.UserPageBean;

public interface TopicService {

	List<TopicIdentifier> getAllTopicIdentifiers();

	Topic save(Topic topic) throws HippoBusinessException;

	List<String> getTopicsStarting(String match);

	Topic getForName(String string);

	List<FullTopicIdentifier> getTopicIdsWithTag(long id);

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

	MindTree getTree(MindTreeOcc occ);

	MindTree saveTree(MindTree tree);

	UserPageBean getUserPageBean(User su);

	void delete(Topic topic) throws HippoBusinessException;

	void saveTopicLocation(long tagId, long topicId, double xpct, double ypct);

	void executeAndSaveCommand(AbstractSaveCommand command) throws HippoBusinessException;
	
}
