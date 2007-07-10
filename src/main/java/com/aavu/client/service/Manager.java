package com.aavu.client.service;

import java.util.List;

import org.gwm.client.GInternalFrame;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.gadgets.GadgetManager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTSubjectServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Manager {

	void bringUpChart(long id);

	void bringUpChart(Topic topic);

	void bringUpChart(TopicIdentifier ident);

	void delete(Topic topic, AsyncCallback callback);

	void displayInfo(String gadget_not_available);

	void doLogin();

	void doSearch(String text);

	void editOccurrence(Occurrence topic);

	void editMetas(AsyncCallback callback, Meta type);

	GadgetManager getGadgetManager();

	GWTSubjectServiceAsync getSubjectService();

	TopicCache getTopicCache();

	User getUser();

	List getCurrentObjs();

	GInternalFrame newFrame();

	void newMeta(Meta meta, AsyncCallback callback);

	void explore();

	void showPreviews(long int0);

	void updateStatus(int myNum, String string, StatusCode fail);

	void userNeedsToUpgrade();

	void createNew(Topic topic);

	// void explore(Topic myTag, List topics);

}
