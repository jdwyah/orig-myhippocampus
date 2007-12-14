package com.aavu.client.service.remote;

import java.util.Date;
import java.util.List;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.LinkAndUser;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.SearchResult;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTTopicService extends RemoteService {

	void changeState(long topicID, boolean toIsland) throws HippoException;

	TopicIdentifier createNew(String title, Topic prototype, Topic parent, int[] lnglat,
			Date dateCreated) throws HippoBusinessException;

	TopicIdentifier createNewIfNonExistent(String title) throws HippoBusinessException;

	TopicIdentifier createNewIfNonExistent(String title, Topic prototype, Topic parent,
			int[] lnglat, Date dateCreated) throws HippoBusinessException;

	void delete(long id) throws HippoException;


	void editVisibility(List<TopicIdentifier> topics, boolean visible) throws HippoException;


	List<LocationDTO> getAllLocations() throws HippoException;


	List<Meta> getAllMetas() throws HippoException;


	List<DatedTopicIdentifier> getAllTopicIdentifiers(int start, int max, String startStr)
			throws HippoException;


	List<TopicIdentifier> getDeleteList(long id) throws HippoException;


	List<TopicIdentifier> getLinksTo(Topic topic) throws HippoException;




	List<List<LocationDTO>> getLocationsForTags(List<TopicIdentifier> shoppingList)
			throws HippoException;


	List<TopicIdentifier> getMakePublicList(long id) throws HippoException;

	/**
	 * 
	 */
	Root getRootTopic(User forUser) throws HippoException;


	TagStat[] getTagStats() throws HippoException;

	List<TimeLineObj> getTimeline() throws HippoException;

	List<List<TimeLineObj>> getTimelineWithTags(List<TopicIdentifier> shoppingList)
			throws HippoException;


	// void save(Topic topic, String[] seeAlsos);
	Topic getTopicByID(long topicID) throws HippoException;

	Topic getTopicForName(String topicName);

	List<FullTopicIdentifier> getTopicIdsWithTag(long id) throws HippoException;


	List<List<FullTopicIdentifier>> getTopicsWithTags(List<TopicIdentifier> shoppingList)
			throws HippoException;

	MindTree getTree(MindTreeOcc occ) throws HippoException;

	LinkAndUser getWebLinkForURLAndUser(String url) throws HippoException;

	List<TopicIdentifier> match(String match);// List<String>

	void saveCommand(AbstractCommand command) throws HippoBusinessException, HippoException;

	void saveOccurrenceLocation(long topicID, long occurrenceID, int lat, int lng)
			throws HippoException;

	void saveTopicLocation(long tagId, long topicId, int lat, int lng) throws HippoException;

	MindTree saveTree(MindTree tree) throws HippoException;


	// C test(C a);


	List<SearchResult> search(String searchString) throws HippoException;
}
