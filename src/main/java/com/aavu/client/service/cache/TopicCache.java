package com.aavu.client.service.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.client.gui.TopicSaveListener;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TopicCache {

	/**
	 * Call the save listeners on success, then pass the results back to the original caller.
	 * 
	 * @author Jeff Dwyer
	 * 
	 */
	private class SaveCallbackWrapper implements AsyncCallback<Void> {
		private AsyncCallback<Void> callback;
		private AbstractCommand command;
		private Topic topic;

		public SaveCallbackWrapper(Topic topic, AbstractCommand command,
				AsyncCallback<Void> callback) {
			this.topic = topic;
			this.command = command;
			this.callback = callback;
		}

		public void onFailure(Throwable caught) {
			Logger.log("caught " + caught);
			callback.onFailure(caught);
		}

		public void onSuccess(Void result) {
			Logger.log("Save callback rtn " + result);

			for (TopicSaveListener listener : saveListeners) {
				listener.topicSaved(topic, command);
			}

			callback.onSuccess(result);
		}
	}

	/**
	 * a callback that wraps the real callback, but caches the returned topic
	 * 
	 * you know, closures/first order functions could make this wayyy cooler.
	 * 
	 * @author Jeff Dwyer
	 * 
	 */
	private class TopicGetCallBack implements AsyncCallback {

		private AsyncCallback callback;
		private ReturnTypeConstant rtn;

		public TopicGetCallBack(ReturnTypeConstant rtn, AsyncCallback callback) {
			this.callback = callback;
			this.rtn = rtn;
		}

		public void onFailure(Throwable caught) {
			callback.onFailure(caught);
		}

		public void onSuccess(Object result) {

			// avoid NPE from debug sysouts
			if (result == null) {
				callback.onSuccess(result);
				return;
			}

			if (rtn == TOPIC) {

				System.out.println("res " + result);

				Topic t = (Topic) result;

				System.out.println("TC " + t.getId() + " size " + t.getOccurences().size());
				for (Iterator iterator = t.getOccurenceObjs().iterator(); iterator.hasNext();) {
					Occurrence link = (Occurrence) iterator.next();
					System.out.println("TC link " + link.getTopics().size());
					// assertEquals(1, link.getTopics().size());
				}

				System.out.println("rec " + t);
				if (t != null) {
					// Logger.debug("rec: "+t.toPrettyString());
					System.out.println("single adding to cache title:" + t.getTitle());
				}
				// topicByName.put(t.getTitle(), t);
				// topicByID.put(new Long(t.getId()), t);

			} else if (rtn == TOPIC_LIST) {
				Topic[] t = (Topic[]) result;
				// if(t.)
				for (int i = 0; i < t.length; i++) {
					Topic topic = t[i];
					System.out.println("list adding to cache " + topic.getTitle());
					// topicByName.put(topic.getTitle(), topic);
					// topicByID.put(new Long(topic.getId()), topic);

				}
			}

			callback.onSuccess(result);
		}

	}



	public static final ReturnTypeConstant TOPIC = new ReturnTypeConstant(1);
	public static final ReturnTypeConstant TOPIC_LIST = new ReturnTypeConstant(2);

	private List<TopicSaveListener> saveListeners = new ArrayList<TopicSaveListener>();

	private Map topicByID = new HashMap();
	private Map topicByName = new HashMap();

	private GWTTopicServiceAsync topicService;

	public TopicCache(GWTTopicServiceAsync topicService) {
		this.topicService = topicService;
	}

	public void addSaveListener(TopicSaveListener l) {
		saveListeners.add(l);
	}

	public void changeState(long id, boolean b, AsyncCallback callback) {
		topicService.changeState(id, b, callback);
	}

	public void createNew(String title, Topic prototype, Topic parent, int[] lnglat,
			Date dateCreated, AsyncCallback callback) {

		topicService.createNew(title, prototype, parent, lnglat, dateCreated, callback);

	}

	public void createNewIfNonExistent(String title, final AsyncCallback callback) {
		topicService.createNewIfNonExistent(title, callback);
	}



	/**
	 * returns a topicID to callback
	 * 
	 * 
	 * @param linkTo
	 * @param callback
	 * @throws HippoBusinessException
	 */
	public void createNewIfNonExistent(String title, Topic parent, int[] lnglat, Date dateCreated,
			final AsyncCallback callback) {

		// TopicLookupOrNewCallback ourCall = new TopicLookupOrNewCallback(callback, linkTo);
		// getTopicForNameA(linkTo, ourCall);

		topicService.createNewIfNonExistent(title, new RealTopic(), parent, lnglat, dateCreated,
				callback);

	}

	public void delete(Topic topic, AsyncCallback callback) {
		topicService.delete(topic.getId(), callback);
		// TODO update after delete
	}

	public void editVisibility(List<TopicIdentifier> topicIDs, boolean visibility,
			AsyncCallback<Void> callback) {
		topicService.editVisibility(topicIDs, visibility, callback);
	}

	/**
	 * Ok, here's how this works. We don't want to serialize the whole topic, send it to the server
	 * and then hope/pray/hack that things get saved right with respect to all of the lazy loading /
	 * persistent set / CGLIB etc munging that we did on the way to the client.
	 * 
	 * Instead we implement our logic in commands.
	 * 
	 * These nuggets have everything they need to affect the changes. We'll run them here on the
	 * client, to update our local state, but then we'll serialize them and send them over to the
	 * server where they will be hydrated, run and saved.
	 * 
	 * @param topic
	 * @param command
	 * @param callback
	 */
	public void executeCommand(Topic topic, AbstractCommand command, AsyncCallback<Void> callback) {

		try {
			command.executeCommand();
		} catch (HippoException e) {
			Logger.log("command execution problem: " + e);
		}

		topicService.saveCommand(command, new SaveCallbackWrapper(topic, command, callback));
	}

	public void getAllLocations(AsyncCallback callback) {
		topicService.getAllLocations(callback);
	}

	public void getAllMetasOfType(Meta type, AsyncCallback callback) {
		topicService.getAllMetas(callback);
	}

	public void getAllTimelineObjs(AsyncCallback callback) {
		topicService.getTimeline(callback);
	}

	/**
	 * return the identifier list or set it if 1st call
	 * 
	 * @param callback
	 */
	public void getAllTopicIdentifiers(int start, int max, final AsyncCallback callback) {
		getAllTopicIdentifiers(start, max, null, callback);
	}

	public void getAllTopicIdentifiers(int start, int max, String startStr,
			final AsyncCallback callback) {
		topicService.getAllTopicIdentifiers(start, max, startStr, callback);
	}

	public void getDeleteList(long id, AsyncCallback callback) {
		topicService.getDeleteList(id, callback);
	}

	public void getLinksTo(Topic topic2, StdAsyncCallback callback) {
		topicService.getLinksTo(topic2, callback);
	}

	public void getLocationsFor(List<TopicIdentifier> shoppingList,
			AsyncCallback<List<List<LocationDTO>>> callback) {
		topicService.getLocationsForTags(shoppingList, callback);
	}

	public void getMakePublicList(long id, EZCallback callback) {
		topicService.getMakePublicList(id, callback);
	}

	public void getRootTopic(User user, AsyncCallback callback) {
		topicService.getRootTopic(user, callback);
	}

	public void getTagStats(AsyncCallback stdAsyncCallback) {
		topicService.getTagStats(stdAsyncCallback);
	}

	public void getTimelineObjs(List shoppingList, AsyncCallback callback) {
		topicService.getTimelineWithTags(shoppingList, callback);
	}

	public void getTopic(TopicIdentifier ident, StdAsyncCallback callback) {
		Topic t = (Topic) topicByID.get(new Long(ident.getTopicID()));
		if (t != null) {
			System.out.println("ti - hit " + ident.getTopicTitle());
			callback.onSuccess(t);
		} else {
			System.out.println("ti - miss " + ident.getTopicTitle());
			topicService.getTopicByID(ident.getTopicID(), new TopicGetCallBack(TOPIC, callback));
		}
	}

	public Topic getTopicById(long id) {
		return (Topic) topicByID.get(new Long(id));
	}

	public void getTopicByIdA(long topicID, AsyncCallback callback) {

		Topic t = (Topic) topicByID.get(new Long(topicID));

		if (t != null) {
			System.out.println("hit " + topicID);
			callback.onSuccess(t);
		} else {
			System.out.println("miss " + topicID);
			topicService.getTopicByID(topicID, new TopicGetCallBack(TOPIC, callback));
		}

	}

	public void getTopicForNameA(String topicName, AsyncCallback callback) {

		Topic t = (Topic) topicByName.get(topicName);

		if (t != null) {
			System.out.println("hit " + topicName);
			callback.onSuccess(t);
		} else {
			System.out.println("miss " + topicName);
			topicService.getTopicForName(topicName, new TopicGetCallBack(TOPIC, callback));
		}

	}

	/**
	 * returns List<List<FullTopicIdentifier>>
	 * 
	 * @param shoppingList
	 * @param callback
	 */
	public void getTopicsWithTag(List shoppingList, StdAsyncCallback callback) {
		topicService.getTopicsWithTags(shoppingList, callback);
	}

	/**
	 * callback for List<TopicIdentifier>
	 * 
	 * @param id
	 * @param callback
	 */
	public void getTopicsWithTag(long id, StdAsyncCallback callback) {
		topicService.getTopicIdsWithTag(id, callback);
	}

	public void getTreeFor(MindTreeOcc treeOcc, StdAsyncCallback callback) {
		topicService.getTree(treeOcc, callback);
	}

	public void match(String match, AsyncCallback call) {
		topicService.match(match, call);
	}

	public void saveOccLocationA(long topicID, long occurrenceID, int lat, int lng,
			AsyncCallback callback) {
		topicService.saveOccurrenceLocation(topicID, occurrenceID, lat, lng, callback);
	}

	public void saveTopicLocationA(long tagId, long topicId, int lat, int lng,
			StdAsyncCallback callback) {
		topicService.saveTopicLocation(tagId, topicId, lat, lng, callback);
	}

	public void saveTree(MindTree mindTree, StdAsyncCallback callback) {
		topicService.saveTree(mindTree, callback);
	}

	public void search(String text, StdAsyncCallback callback) {
		topicService.search(text, callback);
	}

	//
	// public void test(StdAsyncCallback stdAsyncCallback) {
	// topicService.test(stdAsyncCallback);
	// }

}
