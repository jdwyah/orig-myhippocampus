package com.aavu.client.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gwm.client.GInternalFrame;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.GUIManager;
import com.aavu.client.gui.SearchResultsWindow;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.gadgets.GadgetManager;
import com.aavu.client.gui.ocean.MainMap;
import com.aavu.client.help.HelpWindow;
import com.aavu.client.images.Images;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTExternalServiceAsync;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.strings.Consts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractManager implements Manager {

	private Topic currentTopic;
	private GadgetManager gadgetManager;

	private HippoCache hippoCache;

	private MainMap map;
	protected Set selectedTopics = new HashSet();

	public AbstractManager(HippoCache hippoCache) {
		this.hippoCache = hippoCache;
		gadgetManager = new GadgetManager(this);

		initConstants();

	}

	public void addDeliciousTags(String username, String pass, AsyncCallback callback) {
		getHippoCache().getSubjectService().addDeliciousTags(username, pass, callback);
	}

	public void bringUpChart(long id) {
		getHippoCache().getTopicCache().getTopicByIdA(id,
				new StdAsyncCallback(ConstHolder.myConstants.topic_lookupAsync()) {
					public void onSuccess(Object result) {
						super.onSuccess(result);
						bringUpChart((Topic) result);
					}
				});
	}

	public abstract void bringUpChart(Topic topic);

	public void bringUpChart(TopicIdentifier ident) {
		System.out.println("bring up chart Ident " + ident);
		// TODO
		// map.clearForLoading();

		getHippoCache().getTopicCache().getTopic(ident,
				new StdAsyncCallback(ConstHolder.myConstants.topic_lookupAsync()) {
					public void onSuccess(Object result) {
						super.onSuccess(result);
						bringUpChart((Topic) result);
					}
				});

	}

	public void changeState(Topic topic, boolean b, StdAsyncCallback callback) {
		// TODO Auto-generated method stub

	}

	public void delete(Topic topic, AsyncCallback callback) {
		// TODO Auto-generated method stub

	}

	public PopupWindow displayInfo(String gadget_not_available) {
		// TODO Auto-generated method stub
		return null;
	}

	public void doSearch(String text) {
		System.out.println("Search " + text);
		getHippoCache().getTopicCache().search(text,
				new StdAsyncCallback(ConstHolder.myConstants.searchCallback()) {
					public void onSuccess(Object result) {
						super.onSuccess(result);
						System.out.println("ssss");
						List searchRes = (List) result;

						SearchResultsWindow tw = new SearchResultsWindow(AbstractManager.this,
								searchRes);

					}
				});
	}

	public void editMetas(AsyncCallback callback, Meta type) {
		// TODO Auto-generated method stub

	}

	public Topic getCurrentTopic() {
		return currentTopic;
	}

	public GadgetManager getGadgetManager() {
		return gadgetManager;
	}

	public GUIManager getGui() {
		return getMap();
	}

	public HippoCache getHippoCache() {
		return hippoCache;
	}

	public MainMap getMap() {
		return map;
	}

	/**
	 * Called by to replace the load screen with the map. Called before we've even checked if a user
	 * exists.
	 * 
	 * @return
	 */
	public Widget getRootWidget() {
		return getMap();
	}


	public Set getSelectedTopics() {
		return selectedTopics;
	}



	public GWTExternalServiceAsync getSubjectService() {
		return getHippoCache().getSubjectService();
	}

	public TopicCache getTopicCache() {
		return getHippoCache().getTopicCache();
	}

	public User getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * we can goto a topic linked by either Name, or ID. Parse the history token, ie
	 * HippoTest.html#453 or HippoTest.html#MyTopicName
	 * 
	 * @param historyToken
	 */
	public void gotoTopic(String historyToken) {

		long parsedID = -2;// Will be -2 if we're loading by name
		try {
			parsedID = Long.parseLong(historyToken);
		} catch (NumberFormatException e) {

		}
		System.out.println("|" + historyToken + "|" + parsedID);
		if (parsedID == -2 && historyToken != null && historyToken.length() > 0) {
			getHippoCache().getTopicCache().getTopicForNameA(historyToken,
					new StdAsyncCallback("GotoTopicStr " + parsedID) {
						public void onSuccess(Object result) {
							super.onSuccess(result);
							Topic t = (Topic) result;
							bringUpChart(t);
						}
					});
		} else if (parsedID != -1) {// == HippoTest.EMPTY

			// don't load if we're already loaded
			if (getCurrentTopic() != null && getCurrentTopic().getId() != parsedID) {
				getHippoCache().getTopicCache().getTopicByIdA(parsedID,
						new StdAsyncCallback("GotoTopicID " + parsedID) {
							public void onSuccess(Object result) {
								super.onSuccess(result);
								Topic t = (Topic) result;
								bringUpChart(t);
							}
						});
			}
		}
	}

	private void initConstants() {
		ConstHolder.myConstants = (Consts) GWT.create(Consts.class);
		ConstHolder.images = (Images) GWT.create(Images.class);
	}

	public GInternalFrame newFrame() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCurrentTopic(Topic currentTopic) {
		this.currentTopic = currentTopic;
	}

	public void setMap(MainMap map) {
		this.map = map;
	}

	public void showHelp() {
		HelpWindow hw = new HelpWindow(this, newFrame());
	}
}
