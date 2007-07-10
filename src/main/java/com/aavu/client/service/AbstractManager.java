package com.aavu.client.service;

import java.util.ArrayList;
import java.util.List;

import org.gwm.client.GInternalFrame;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.SearchResultsWindow;
import com.aavu.client.gui.gadgets.GadgetManager;
import com.aavu.client.images.Images;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTSubjectServiceAsync;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.strings.Consts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractManager implements Manager {

	private GadgetManager gadgetManager;
	private HippoCache hippoCache;
	protected List currentObjs = new ArrayList();

	public AbstractManager(HippoCache hippoCache) {
		this.hippoCache = hippoCache;
		gadgetManager = new GadgetManager(this);

		initConstants();

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

	public void displayInfo(String gadget_not_available) {
		// TODO Auto-generated method stub

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

	public List getCurrentObjs() {
		return currentObjs;
	}

	public GadgetManager getGadgetManager() {
		return gadgetManager;
	}

	public HippoCache getHippoCache() {
		return hippoCache;
	}

	public GWTSubjectServiceAsync getSubjectService() {
		return getHippoCache().getSubjectService();
	}

	public TopicCache getTopicCache() {
		return getHippoCache().getTopicCache();
	}

	public User getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	private void initConstants() {
		ConstHolder.myConstants = (Consts) GWT.create(Consts.class);
		ConstHolder.images = (Images) GWT.create(Images.class);
	}

	public GInternalFrame newFrame() {
		// TODO Auto-generated method stub
		return null;
	}

}
