package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.LinkAndUser;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.client.exception.HippoPermissionException;
import com.aavu.server.web.domain.UserPageBean;

public interface TopicService {

	/**
	 * Create the link and save it for each of the tags enumerated, creating and saving those if
	 * necessary.
	 * 
	 * @param link
	 * @param tags
	 * @throws HippoBusinessException
	 */
	void addLinkToTags(WebLink link, String[] tags) throws HippoBusinessException;

	void addLinkToTags(WebLink link, String[] tags, Topic parent) throws HippoBusinessException;

	void changeState(long topicID, boolean toIsland) throws HippoPermissionException;

	<T> T createNew(String title, Class<T> type, Topic parent) throws HippoBusinessException;

	<T> T createNew(String title, Class<T> type, Topic parent, int[] lnglat)
			throws HippoBusinessException;

	Topic createNewIfNonExistent(String tagName) throws HippoBusinessException;

	Topic createNewIfNonExistent(String tagName, Topic parent) throws HippoBusinessException;


	<T extends Topic> T createNewIfNonExistent(String title, Class<? extends Topic> type,
			Topic parent) throws HippoBusinessException;

	<T extends Topic> T createNewIfNonExistent(String title, Class<? extends Topic> type,
			Topic parent, int[] lnglat) throws HippoBusinessException;


	void delete(long id) throws HippoBusinessException;

	void delete(Topic topic) throws HippoBusinessException;

	void executeAndSaveCommand(AbstractCommand command) throws HippoBusinessException,
			HippoException;

	List<LocationDTO> getAllLocations();


	List<Meta> getAllMetas();

	/**
	 * Filter out Dates/Locaitons/Metas.
	 * 
	 * @param startStr
	 * @param max
	 * @param start
	 */
	List<DatedTopicIdentifier> getAllPublicTopicIdentifiers(String username, int start, int max,
			String startStr);

	/**
	 * Filter out Dates/Locaitons/Metas.
	 */
	List<DatedTopicIdentifier> getAllTopicIdentifiers();

	/**
	 * Don't filter out Dates, Locations, Metas. Intended for debugging/testing use.
	 * 
	 * @param all
	 * @return
	 */
	List<DatedTopicIdentifier> getAllTopicIdentifiers(boolean all);

	/**
	 * Filter out Dates/Locaitons/Metas.
	 * 
	 * @param startStr
	 * @param max
	 * @param start
	 */
	List<DatedTopicIdentifier> getAllTopicIdentifiers(int start, int max, String startStr);

	Topic getForID(long topicID);

	Topic getForNameCaseInsensitive(String string);

	List<TopicIdentifier> getLinksTo(Topic topic);

	List<List<LocationDTO>> getLocationsForTags(List<TopicIdentifier> shoppingList);

	Topic getPublicTopic(String userString, String topicString) throws HippoBusinessException;

	List<FullTopicIdentifier> getPublicTopicIdsWithTag(long id);

	Root getRootTopic(User forUser);

	List<TopicIdentifier> getTagsStarting(String match);

	List<TagStat> getTagStats();

	List<TimeLineObj> getTimeline();

	List<List<TimeLineObj>> getTimelineWithTags(List<TopicIdentifier> shoppingList);

	List<FullTopicIdentifier> getTopicIdsWithTag(long id);

	List<List<FullTopicIdentifier>> getTopicIdsWithTags(List<TopicIdentifier> shoppingList);

	List<TopicIdentifier> getTopicsStarting(String match);

	MindTree getTree(MindTreeOcc occ);

	UserPageBean getUserPageBean(User su);

	LinkAndUser getWebLinkForURLAndUser(String url);

	Occurrence save(Occurrence link);

	Topic save(Topic topic) throws HippoBusinessException;

	void saveOccurrenceLocation(long topicID, long occurrenceID, int lat, int lng)
			throws HippoException;

	void saveTopicLocation(long tagId, long topicId, int lat, int lng) throws HippoException;

	MindTree saveTree(MindTree tree);



}
