package com.aavu.client.service;

import java.util.Set;

import org.gwm.client.GInternalFrame;
import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.GUIManager;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetManager;
import com.aavu.client.gui.gadgets.GadgetPopup;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTExternalServiceAsync;
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

	GWTExternalServiceAsync getSubjectService();

	TopicCache getTopicCache();

	User getUser();

	Set getSelectedTopics();

	GInternalFrame newFrame();

	void newMeta(Meta meta, AsyncCallback callback);

	void explore();

	void showPreviews(long int0);

	void updateStatus(int myNum, String string, StatusCode fail);

	void userNeedsToUpgrade();

	void createNew(Topic topic, int[] lnglat);

	GadgetPopup newFrameGadget(Gadget gadget);

	void addDeliciousTags(String username, String password, AsyncCallback callback);

	void refreshAll();

	PopupWindow showProgressBar(ProgressBar progressBar);

	void addSelected(Topic topic);

	void unselect();

	GUIManager getGui();

	// void explore(Topic myTag, List topics);

}
