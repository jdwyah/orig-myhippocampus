package com.aavu.client.service;

import java.util.Date;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.gui.LoadFinishedListener;
import com.aavu.client.gui.ocean.MainMap;
import com.aavu.client.service.cache.HippoCache;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BrowserManager extends AbstractManager implements LoadFinishedListener {

	private User user;


	public BrowserManager(HippoCache cache, User user) {
		super(cache);
		this.user = user;

		setMap(new MainMap(this));
	}


	public void setup(String caller) {


		getMap().onLoginComplete(this);


		// getTopicCache().getRootTopic(getUser(),
		// new StdAsyncCallback(ConstHolder.myConstants.getRoot_async()) {
		// // @Override
		// public void onSuccess(Object result) {
		// super.onSuccess(result);
		//
		// Root root = (Root) result;
		//
		// bringUpChart(root);
		// }
		// });

	}


	public void createNew(Topic prototype, int[] lnglat, Date dateCreated, boolean doNamePopup,
			boolean needsPopupForParent) {
		// TODO Auto-generated method stub

	}


	public void delete(Topic topic, AsyncCallback callback) {
		// TODO Auto-generated method stub

	}


	public void doLogin() {
		// TODO Auto-generated method stub

	}


	public void editOccurrence(Occurrence topic, boolean needsSave) {
		// TODO Auto-generated method stub

	}


	public void newMeta(Meta meta, AsyncCallback callback) {
		// TODO Auto-generated method stub

	}


	// @Override
	public User getUser() {
		return user;
	}


	public boolean isEdittable() {
		return false;
	}


	public void loadFinished() {
		// TODO Auto-generated method stub

	}



}
