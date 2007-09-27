package com.aavu.client.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gwm.client.GInternalFrame;
import org.gwm.client.impl.DefaultGInternalFrame;
import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.LinkPlugin.AddLinkPopup;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.GoogleData;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.S3File;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.EntryEditWindow;
import com.aavu.client.gui.GUIManager;
import com.aavu.client.gui.GadgetDisplayer;
import com.aavu.client.gui.SearchResultsWindow;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetManager;
import com.aavu.client.gui.gadgets.GadgetPopup;
import com.aavu.client.gui.ocean.MainMap;
import com.aavu.client.help.HelpWindow;
import com.aavu.client.images.Images;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTExternalServiceAsync;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.strings.Consts;
import com.aavu.client.util.Logger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractManager implements Manager {

	private class ProgressPopup extends PopupWindow {
		public ProgressPopup(GInternalFrame frame, String title, ProgressBar progressBar) {
			super(frame, title, 200, 100);
			setContent(progressBar);

		}
	}

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

	public void addSelected(Topic t) {
		selectedTopics.add(t);
		getMap().editSelectStatus(t.getIdentifier(), true);

		System.out.println("MindscapeManager addSelected " + t + " Now: " + selectedTopics);
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


	public void bringUpChart(Topic topic) {

		if (null == topic) {
			displayInfo("Couldn't Find Topic");
			return;
		}

		try {
			setCurrentTopic(null);
			selectedTopics.clear();
		} catch (Exception e) {
			Logger.error("Exception clearing " + e);
		}

		// if this is an occurrence, short circuit a bit. go to the first type instead.
		// since we don't display hieracrchies off an entry / weblink
		// PEND bring up a choice of the types instead?
		if (topic instanceof Occurrence) {
			Occurrence occ = (Occurrence) topic;
			Topic parent = (Topic) occ.getTopicsAsTopics().iterator().next();

			// do full load since parent will not have all info loaded
			bringUpChart(parent.getId());
			editOccurrence(occ, false);
			return;
		}

		setCurrentTopic(topic);

		System.out.println("bring up chart Topic " + topic);

		getMap().displayTopic(topic);

		// only zoom if we find something good to center on
		if (getMap().centerOn(topic)) {
			getMap().ensureZoomOfAtLeast(4);
		}



		History.newItem("" + topic.getId());

	}

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



	/**
	 * Simple warning dialog wrapper
	 * 
	 * NOTE: css style in public/themes/alphacube.css
	 * 
	 * @param warning
	 */
	public PopupWindow displayInfo(String warning) {
		return displayInfo(new Label(warning));
	}


	public PopupWindow displayInfo(Widget widg) {
		GInternalFrame f = newFrame();
		PopupWindow w = new PopupWindow(f, ConstHolder.myConstants.displayInfoTitle(), true);
		f.setContent(widg);
		return w;
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

	/**
	 * needs save will be true if the occurrence passed in is already dirty. ie, they were editting
	 * in the entryDisplay, then clicked on (rich edit) before they'd saved.
	 */
	public void editOccurrence(Occurrence occurrence, boolean needsSave) {
		System.out.println("edit occ " + GWT.getTypeName(occurrence));

		if (occurrence instanceof Entry) {
			EntryEditWindow gw = new EntryEditWindow((Entry) occurrence, this, newFrame(),
					needsSave);
		} else if (occurrence instanceof WebLink) {
			AddLinkPopup pop = new AddLinkPopup(null, this, newFrame(), (WebLink) occurrence,
					getCurrentTopic(), null);

			// EntryEditWindow gw = new EntryEditWindow((Entry) occurrence,this,newFrame());
		} else if (occurrence instanceof S3File) {
			// EntryEditWindow gw = new EntryEditWindow((Entry) occurrence,this,newFrame());
		} else if (occurrence instanceof GoogleData) {

		} else {

		}

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

	public abstract User getUser();

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
		System.out.println("AbstractManager.gotoTopic |" + historyToken + "|" + parsedID);
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
			if (getCurrentTopic() == null || getCurrentTopic().getId() != parsedID) {
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

	public int load(Topic myTopic) {
		bringUpChart(myTopic);
		return 0;
	}


	public GInternalFrame newFrame() {
		return newFrame("");
	}



	protected GInternalFrame newFrame(String title) {

		GInternalFrame frame = new DefaultGInternalFrame(title);

		getMap().addFrame(frame);

		return frame;
	}

	public GadgetPopup newFrameGadget(Gadget gadget, GadgetDisplayer gDisplayer) {

		GadgetPopup frame = new GadgetPopup(gadget, gDisplayer);

		getMap().addFrame(frame);

		return frame;
	}

	public void refreshAll() {
		getMap().refreshIslands();
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

	/*
	 * 
	 * 
	 */
	public void showPreviews(final long id) {

		System.out.println("showPreviews " + id);

		getTopicCache().getTopicByIdA(id,
				new StdAsyncCallback(ConstHolder.myConstants.oceanIslandLookupAsync()) {
					public void onSuccess(Object result) {
						super.onSuccess(result);
						Topic tag = (Topic) result;
						getMap().displayTopic(tag);

						// IslandDetailsWindow tcw = new
						// IslandDetailsWindow(tag,topics,Manager.this);
					}
				});
	}


	public PopupWindow showProgressBar(ProgressBar progressBar) {
		ProgressPopup win = new ProgressPopup(newFrame(progressBar.getTitle()), progressBar
				.getTitle(), progressBar);

		return win;
	}

	public void unselect() {
		System.out.println("MindscapeManger UNSELECT()");
		getMap().unselect(selectedTopics);
		selectedTopics.clear();
	}

	public void updateStatus(int i, String call, StatusCode send) {
		getMap().updateStatusWindow(i, call, send);
	}

	public void zoomIn() {
		getMap().zoomIn();
	}

	public void zoomOut() {
		getMap().zoomOut();
	}

	public void zoomTo(double scale) {
		getMap().zoomTo(scale);
	}
}
