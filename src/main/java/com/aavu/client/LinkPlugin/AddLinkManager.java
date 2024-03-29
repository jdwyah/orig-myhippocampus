package com.aavu.client.LinkPlugin;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.gwm.client.GInternalFrame;
import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.LinkAndUser;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.GUIManager;
import com.aavu.client.gui.GadgetDisplayer;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.ext.GUIEffects;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetManager;
import com.aavu.client.gui.gadgets.GadgetPopup;
import com.aavu.client.gui.ocean.MainMap;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTExternalServiceAsync;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class AddLinkManager implements Manager, CloseListener {

	private GWTTopicServiceAsync topicService;

	private String description;
	private String notes;
	private String url;
	private WebLink weblink;
	private AddLinkContent panel;
	private TopicCache topicCache;

	public AddLinkManager(GWTTopicServiceAsync topicService, String url, String notes,
			String description) {

		this.topicService = topicService;
		this.url = url;
		this.notes = notes;
		this.description = description;
		this.topicCache = new TopicCache(topicService);

	}

	public void getExistingLinkAndCreatePanel(final AsyncCallback loadGUICallback) {
		topicService.getWebLinkForURLAndUser(url, new AsyncCallback() {
			public void onFailure(Throwable caught) {
				System.out.println("ALM fail " + caught.getMessage());
				loadGUICallback.onFailure(caught);
			}

			public void onSuccess(Object result) {
				Logger.debug("ALM succ " + result);

				LinkAndUser linkAndUser = (LinkAndUser) result;

				weblink = linkAndUser.getWeblink();

				Logger.debug("Found Weblink: " + weblink);

				if (weblink == null) {
					weblink = new WebLink();
					weblink.setUri(url);
				}
				if (weblink.getDescription() == null || weblink.getDescription().length() < 1) {
					weblink.setDescription(description);
				}
				if (weblink.getNotes() == null) {
					weblink.setNotes(notes);
				} else {
					weblink.setNotes(notes + " " + weblink.getNotes());
				}

				// panel = new
				// AddLinkPanel(AddLinkManager.this,weblink,url,notes,description);

				panel = new AddLinkContent(topicCache, weblink, AddLinkManager.this, linkAndUser
						.getUser().getUsername());

				loadGUICallback.onSuccess(panel);
			}
		});
	}

	public TopicCache getTopicCache() {
		return topicCache;
	}

	public void close() {
		GUIEffects.close();
	}

	public void bringUpChart(long id) {
		throw new UnsupportedOperationException();
	}

	public void bringUpChart(Topic topic) {
		throw new UnsupportedOperationException();
	}

	public void bringUpChart(TopicIdentifier ident) {
		throw new UnsupportedOperationException();
	}

	public void createNew(Topic topick, int[] lnglat, Date dateCreated) {
		throw new UnsupportedOperationException();
	}

	public void delete(Topic topic, AsyncCallback callback) {
		throw new UnsupportedOperationException();
	}

	public PopupWindow displayInfo(String gadget_not_available) {
		throw new UnsupportedOperationException();
	}

	public void doLogin() {
		throw new UnsupportedOperationException();
	}

	public void doSearch(String text) {
		throw new UnsupportedOperationException();
	}

	public void editMetas(AsyncCallback callback, Meta type) {
		throw new UnsupportedOperationException();
	}

	public void editOccurrence(Occurrence topic, boolean saveNeeded) {
		throw new UnsupportedOperationException();
	}

	public void explore() {
		throw new UnsupportedOperationException();
	}

	public List getCurrentObjs() {
		throw new UnsupportedOperationException();
	}

	public GadgetManager getGadgetManager() {
		throw new UnsupportedOperationException();
	}

	public GWTExternalServiceAsync getSubjectService() {
		throw new UnsupportedOperationException();
	}

	public User getUser() {
		throw new UnsupportedOperationException();
	}

	public GInternalFrame newFrame() {
		throw new UnsupportedOperationException();
	}

	public void newMeta(Meta meta, AsyncCallback callback) {
		throw new UnsupportedOperationException();
	}

	public void showPreviews(long int0) {
		throw new UnsupportedOperationException();
	}

	public void updateStatus(int myNum, String string, StatusCode fail) {
		throw new UnsupportedOperationException();
	}

	public void userNeedsToUpgrade() {
		throw new UnsupportedOperationException();
	}

	public GadgetPopup newFrameGadget(Gadget gadget, GadgetDisplayer gDisplayer) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addDeliciousTags(String username, String password, AsyncCallback callback) {
		// TODO Auto-generated method stub

	}

	public void refreshAll() {
		// TODO Auto-generated method stub

	}

	public PopupWindow showProgressBar(ProgressBar progressBar) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set getSelectedTopics() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addSelected(TopicIdentifier ti) {
		// TODO Auto-generated method stub

	}

	public void unselect() {
		// TODO Auto-generated method stub

	}

	public void addSelected(Topic topic) {
		// TODO Auto-generated method stub

	}

	public GUIManager getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	public PopupWindow displayInfo(Widget widg) {
		// TODO Auto-generated method stub
		return null;
	}

	public void createNew(Topic prototype, int[] lnglat, Date dateCreated, boolean doNamePopup,
			boolean needsPopupForParent) {
		// TODO Auto-generated method stub

	}

	public void showHelp() {

	}

	public void setMap(MainMap mainMap) {
		// TODO Auto-generated method stub

	}

	public Widget getRootWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	public void gotoTopic(String historyToken) {
		// TODO Auto-generated method stub

	}

	public void setup(String caller) {
		// TODO Auto-generated method stub

	}

	public boolean isEdittable() {
		return false;
	}

	public void gotoRoot() {
		// TODO Auto-generated method stub

	}



}
