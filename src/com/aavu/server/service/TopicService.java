package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoPermissionException;
import com.aavu.server.web.domain.UserPageBean;

public interface TopicService {

	/**
	 * Create the link and save it for each of the tags enumerated, creating
	 * and saving those if necessary. 
	 * 
	 * @param link
	 * @param tags
	 * @throws HippoBusinessException 
	 */
	void addLinkToTags(WebLink link, String[] tags) throws HippoBusinessException;
	void changeState(long topicID, boolean toIsland) throws HippoPermissionException;

	void delete(Topic topic) throws HippoBusinessException;

	void deleteOccurrence(long id) throws HippoPermissionException;

	void executeAndSaveCommand(AbstractCommand command) throws HippoBusinessException;

	List getAllMetas();

	List<TopicIdentifier> getAllTopicIdentifiers();

	List<TopicIdentifier> getAllTopicIdentifiers(boolean all);
	Topic getForID(long topicID);

	Topic getForName(String string);
	
	List<TopicIdentifier> getLinksTo(Topic topic);

	List<TimeLineObj> getTimelineObjs(long tagID);

	List<List<TimeLineObj>>  getTimelineWithTags(List<TopicIdentifier> shoppingList);

	List<FullTopicIdentifier> getTopicIdsWithTag(long id);

	List<List<FullTopicIdentifier>> getTopicIdsWithTags(List<TopicIdentifier> shoppingList);

	List<String> getTopicsStarting(String match);

	MindTree getTree(MindTreeOcc occ);

	UserPageBean getUserPageBean(User su);

	WebLink getWebLinkForURL(String url);
	
	Occurrence save(Occurrence link);

	Topic save(Topic topic) throws HippoBusinessException;

	void saveTopicLocation(long tagId, long topicId, double xpct, double ypct);

	MindTree saveTree(MindTree tree);
}
