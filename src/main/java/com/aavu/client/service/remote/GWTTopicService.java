package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.A;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.LinkAndUser;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTTopicService extends RemoteService {

	void changeState(long topicID, boolean toIsland) throws HippoException;

	TopicIdentifier createNew(String title, Topic prototype, Topic parent, int[] lnglat)
			throws HippoBusinessException;

	void delete(long id) throws HippoException;

	// List <LocationDTO>
	/**
	 * @gwt.typeArgs <com.aavu.client.domain.dto.LocationDTO>
	 */
	List getAllLocations() throws HippoException;

	/**
	 * @gwt.typeArgs <com.aavu.client.domain.Meta>
	 */
	List getAllMetas() throws HippoException;

	// List<DatedTopicIdentifier>
	/**
	 * @gwt.typeArgs <com.aavu.client.domain.dto.DatedTopicIdentifier>
	 */
	List getAllTopicIdentifiers(int start, int max, String startStr) throws HippoException;

	// List<TimeLineObj>
	// List getTimelineObjs(long tag_id) throws HippoException;

	// List<TopicIdentifier>
	/**
	 * @gwt.typeArgs <com.aavu.client.domain.dto.TopicIdentifier>
	 */
	List getLinksTo(Topic topic) throws HippoException;

	// List<List <LocationDTO>>
	/**
	 * @gwt.typeArgs shoppingList <com.aavu.client.domain.dto.TopicIdentifier>
	 * @gwt.typeArgs <java.util.List<com.aavu.client.domain.dto.LocationDTO>>
	 */
	List getLocationsForTags(List shoppingList) throws HippoException;

	// List<TimeLineObj>
	/**
	 * @gwt.typeArgs <com.aavu.client.domain.dto.TimeLineObj>
	 */
	List getTimeline() throws HippoException;

	// List<List<TimeLineObj>>
	/**
	 * @gwt.typeArgs shoppingList <com.aavu.client.domain.dto.TopicIdentifier>
	 * @gwt.typeArgs <java.util.List<com.aavu.client.domain.dto.TimeLineObj>>
	 */
	List getTimelineWithTags(List shoppingList) throws HippoException;

	// void save(Topic topic, String[] seeAlsos);
	Topic getTopicByID(long topicID) throws HippoException;

	Topic getTopicForName(String topicName);

	// List<FullTopicIdentifier>
	/**
	 * @gwt.typeArgs <com.aavu.client.domain.dto.FullTopicIdentifier>
	 */
	List getTopicIdsWithTag(long id) throws HippoException;

	TagStat[] getTagStats() throws HippoException;

	/**
	 * 
	 */
	Root getRootTopic(User forUser) throws HippoException;

	// List<TopicIdentifier>
	// List<List<FullTopicIdentifier>>
	/**
	 * @gwt.typeArgs shoppingList <com.aavu.client.domain.dto.TopicIdentifier>
	 * @gwt.typeArgs <java.util.List<com.aavu.client.domain.dto.TopicIdentifier>>
	 */
	List getTopicsWithTags(List shoppingList) throws HippoException;

	MindTree getTree(MindTreeOcc occ) throws HippoException;

	LinkAndUser getWebLinkForURLAndUser(String url) throws HippoException;

	// List<TopicIdentifier>
	/**
	 * @gwt.typeArgs <com.aavu.client.domain.dto.TopicIdentifier>
	 */
	List match(String match);// List<String>

	void saveCommand(AbstractCommand command) throws HippoBusinessException, HippoException;

	void saveTopicLocation(long tagId, long topicId, int lat, int lng) throws HippoException;

	void saveOccurrenceLocation(long topicID, long occurrenceID, int lat, int lng)
			throws HippoException;

	MindTree saveTree(MindTree tree) throws HippoException;

	A test(A a);

	// List<SearchResult>
	/**
	 * @gwt.typeArgs <com.aavu.client.domain.dto.SearchResult>
	 */
	List search(String searchString) throws HippoException;
}
