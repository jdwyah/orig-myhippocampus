package com.aavu.client.service;

import java.util.Date;
import java.util.Set;

import org.gwm.client.GInternalFrame;
import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.GUIManager;
import com.aavu.client.gui.GadgetDisplayer;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetManager;
import com.aavu.client.gui.gadgets.GadgetPopup;
import com.aavu.client.gui.ocean.MainMap;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTExternalServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public interface Manager {

	void addDeliciousTags(String username, String password, AsyncCallback callback);

	void addSelected(Topic topic);

	void bringUpChart(long id);

	void bringUpChart(Topic topic);

	void bringUpChart(TopicIdentifier ident);

	void createNew(final Topic prototype, final int[] lnglat, final Date dateCreated,
			boolean doNamePopup, boolean needsPopupForParent);

	void delete(Topic topic, AsyncCallback callback);

	PopupWindow displayInfo(String displayString);

	PopupWindow displayInfo(Widget widg);

	void doLogin();

	void doSearch(String text);

	void editMetas(AsyncCallback callback, Meta type);

	void editOccurrence(Occurrence topic, boolean needsSave);

	GadgetManager getGadgetManager();

	GUIManager getGui();

	Set getSelectedTopics();

	GWTExternalServiceAsync getSubjectService();

	TopicCache getTopicCache();

	User getUser();

	GInternalFrame newFrame();

	GadgetPopup newFrameGadget(Gadget gadget, GadgetDisplayer gDisplayer);

	void newMeta(Meta meta, AsyncCallback callback);

	void refreshAll();

	void showPreviews(long int0);

	PopupWindow showProgressBar(ProgressBar progressBar);

	void unselect();

	void updateStatus(int myNum, String string, StatusCode fail);

	void showHelp();

	void setMap(MainMap mainMap);

	Widget getRootWidget();

	void gotoTopic(String historyToken);

	void setup(String caller);

	boolean isEdittable();

	// void explore(Topic myTag, List topics);

}
