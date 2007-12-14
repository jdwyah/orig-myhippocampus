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
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * remember, these MUST BE VOID! returns
 */
public interface GWTTopicServiceAsync {



	void changeState(long topicID, boolean toIsland, AsyncCallback<Void> callback);

	void createNew(String title, Topic prototype, Topic parent, int[] lnglat, Date dateCreated,
			AsyncCallback<TopicIdentifier> callback);

	void createNewIfNonExistent(String title, AsyncCallback<TopicIdentifier> callback);

	void createNewIfNonExistent(String title, Topic prototype, Topic parent, int[] lnglat,
			Date dateCreated, AsyncCallback<TopicIdentifier> callback);

	void delete(long id, AsyncCallback<Void> callback);

	void editVisibility(List<TopicIdentifier> topics, boolean visible, AsyncCallback<Void> callback);

	void getAllLocations(AsyncCallback<List<LocationDTO>> callback);

	void getAllMetas(AsyncCallback<List<Meta>> callback);

	// List<DatedTopicIdentifiers>
	void getAllTopicIdentifiers(int start, int max, String startStr,
			AsyncCallback<List<DatedTopicIdentifier>> callback);

	// List TimeLineObj
	// void getTimelineObjs(long tag_id, AsyncCallback callback);

	void getDeleteList(long id, AsyncCallback<List<TopicIdentifier>> callback);

	// List<TopicIdentifier>
	void getLinksTo(Topic topic, AsyncCallback<List<TopicIdentifier>> callback);

	void getLocationsForTags(List<TopicIdentifier> shoppingList,
			AsyncCallback<List<List<LocationDTO>>> callback);

	void getMakePublicList(long id, AsyncCallback<List<TopicIdentifier>> callback);

	// Root
	void getRootTopic(User forUser, AsyncCallback<Root> callback);

	void getTagStats(AsyncCallback<TagStat[]> callback);

	// List<TimeLineObj>
	void getTimeline(AsyncCallback<List<TimeLineObj>> callback);

	// List<List<TimeLineObj>>
	void getTimelineWithTags(List<TopicIdentifier> shoppingList,
			AsyncCallback<List<List<TimeLineObj>>> callback);

	/*
	 * @Deprecated
	 */
	void getTopicByID(long topicID, AsyncCallback<Topic> back);

	void getTopicForName(String topicName, AsyncCallback<Topic> callback);

	void getTopicIdsWithTag(long id, AsyncCallback<List<FullTopicIdentifier>> callback);


	void getTopicsWithTags(List<TopicIdentifier> shoppingList,
			AsyncCallback<List<List<FullTopicIdentifier>>> callback);

	// MindTree
	void getTree(MindTreeOcc occ, AsyncCallback<MindTree> callback);

	// LinkAndUser
	void getWebLinkForURLAndUser(String url, AsyncCallback<LinkAndUser> callback);

	// void getTopicsStarting(String match, AsyncCallback callback);
	void match(String match, AsyncCallback<List<TopicIdentifier>> callback);



	// void test(C c, AsyncCallback callback);

	void saveCommand(AbstractCommand command, AsyncCallback<Void> callback);

	void saveOccurrenceLocation(long topicID, long occurrenceID, int lat, int lng,
			AsyncCallback<Void> callback);

	void saveTopicLocation(long tagId, long topicId, int lat, int lng, AsyncCallback<Void> callback);

	// MindTree
	void saveTree(MindTree tree, AsyncCallback<MindTree> callback);

	// List<SearchResult>
	void search(String searchString, AsyncCallback<List<SearchResult>> callback);
}
