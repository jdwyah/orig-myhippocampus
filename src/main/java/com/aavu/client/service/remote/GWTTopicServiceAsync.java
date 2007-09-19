package com.aavu.client.service.remote;

import java.util.Date;
import java.util.List;

import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.mapper.MindTree;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * remember, these MUST BE VOID! returns
 */
public interface GWTTopicServiceAsync {



	void changeState(long topicID, boolean toIsland, AsyncCallback callback);

	void createNew(String title, Topic prototype, Topic parent, int[] lnglat, Date dateCreated,
			AsyncCallback callback);

	void createNewIfNonExistent(String title, AsyncCallback callback);

	void createNewIfNonExistent(String title, Topic prototype, Topic parent, int[] lnglat,
			Date dateCreated, AsyncCallback callback);

	void delete(long id, AsyncCallback callback);

	void editVisibility(List topics, boolean visible, AsyncCallback callback);

	void getAllLocations(AsyncCallback callback);

	void getAllMetas(AsyncCallback callback);

	// List<DatedTopicIdentifiers>
	void getAllTopicIdentifiers(int start, int max, String startStr, AsyncCallback callback);

	// List TimeLineObj
	// void getTimelineObjs(long tag_id, AsyncCallback callback);

	void getDeleteList(long id, AsyncCallback callback);

	// List<TopicIdentifier>
	void getLinksTo(Topic topic, AsyncCallback callback);

	void getLocationsForTags(List shoppingList, AsyncCallback callback);

	void getMakePublicList(long id, AsyncCallback callback);

	// Root
	void getRootTopic(User forUser, AsyncCallback callback);

	void getTagStats(AsyncCallback callback);

	// List<TimeLineObj>
	void getTimeline(AsyncCallback callback);

	// List<List<TimeLineObj>>
	void getTimelineWithTags(List shoppingList, AsyncCallback callback);

	/*
	 * @Deprecated
	 */
	void getTopicByID(long topicID, AsyncCallback back);

	void getTopicForName(String topicName, AsyncCallback callback);

	void getTopicIdsWithTag(long id, AsyncCallback callback);

	// List<TopicIdentifier>
	void getTopicsWithTags(List shoppingList, AsyncCallback callback);

	// MindTree
	void getTree(MindTreeOcc occ, AsyncCallback callback);

	// LinkAndUser
	void getWebLinkForURLAndUser(String url, AsyncCallback callback);

	// void getTopicsStarting(String match, AsyncCallback callback);
	void match(String match, AsyncCallback callback);



	// void test(C c, AsyncCallback callback);

	void saveCommand(AbstractCommand command, AsyncCallback callback);

	void saveOccurrenceLocation(long topicID, long occurrenceID, int lat, int lng,
			AsyncCallback callback);

	void saveTopicLocation(long tagId, long topicId, int lat, int lng, AsyncCallback callback);

	// MindTree
	void saveTree(MindTree tree, AsyncCallback callback);

	// List<SearchResult>
	void search(String searchString, AsyncCallback callback);
}
