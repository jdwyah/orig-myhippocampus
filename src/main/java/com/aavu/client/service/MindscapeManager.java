package com.aavu.client.service;


import org.gwm.client.GInternalFrame;
import org.gwm.client.impl.DefaultGInternalFrame;
import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.Interactive;
import com.aavu.client.LinkPlugin.AddLinkPopup;
import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.GoogleData;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.S3File;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.AddTopicPopup;
import com.aavu.client.gui.EditMetaWindow;
import com.aavu.client.gui.EntryEditWindow;
import com.aavu.client.gui.GUIManager;
import com.aavu.client.gui.GadgetDisplayer;
import com.aavu.client.gui.LoadFinishedListener;
import com.aavu.client.gui.Preloader;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.TopicSaveListener;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetClickListener;
import com.aavu.client.gui.gadgets.GadgetPopup;
import com.aavu.client.gui.gadgets.TopicLoader;
import com.aavu.client.gui.ocean.MainMap;
import com.aavu.client.help.HelpWindow;
import com.aavu.client.help.UserHelper;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MindscapeManager extends AbstractManager implements Manager, TopicSaveListener,
		LoginListener, LoadFinishedListener, GadgetClickListener, TopicLoader {


	private class ProgressPopup extends PopupWindow {
		public ProgressPopup(GInternalFrame frame, String title, ProgressBar progressBar) {
			super(frame, title, 200, 100);
			setContent(progressBar);

		}
	}


	// private FramesManager framesManager;
	// private DefaultGDesktopPane desktop;



	private MainMap map;


	private TagLocalService tagLocalService;
	private User user;

	private UserActionListener userActionListener;

	private Topic currentTopic;


	public MindscapeManager(HippoCache hippoCache) {
		super(hippoCache);

		hippoCache.getTopicCache().addSaveListener(this);


		userActionListener = new UserHelper(this, user);

		// Note what is the purpose of "Desktop"?
		// If we implement that, note that it messed with our
		// theme application
		//		
		// framesManager = new FramesManagerFactory().createFramesManager();

		// desktop = new DefaultGDesktopPane();

		// RootPanel.get(HippoTest.MAIN_DIV).add((Widget) desktop);

		map = new MainMap(this);

		getGadgetManager().addGadgetClickListener(this);
	}


	// public void show(Topic topic, boolean editMode) {

	public void addDeliciousTags(String username, String password, AsyncCallback callback) {
		getHippoCache().getSubjectService().addDeliciousTags(username, password, callback);
	}

	public void bringUpChart(Topic topic) {

		try {
			currentTopic = null;
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

		currentTopic = topic;

		System.out.println("bring up chart Topic " + topic);

		map.displayTopic(topic);

		// only zoom if we find something good to center on
		if (map.centerOn(topic)) {
			map.ensureZoomOfAtLeast(4);
		}



		History.newItem("" + topic.getId());

	}

	public void gadgetClicked(Gadget gadget, int[] lngLat) {

		if (lngLat != null) {
			System.out.println("MindscapeManager lngLat " + lngLat[0] + " " + lngLat[1]);
		} else {
			System.out.println("MindscapeManager lngLat " + lngLat);
		}
		gadget.createInstance(this, lngLat);


		// map.growIsland();
	}


	/**
	 * keeping it simple right now. probably want some sort of Action framework here...
	 * 
	 * trick is to get us the ID, so that if we do a create, then an edit, we don't dupe on server.
	 * 
	 */
	public void createNew(final Topic prototype, final int[] lnglat) {


		System.out.println("Create new " + prototype + " " + GWT.getTypeName(prototype) + " "
				+ lnglat);

		if (prototype instanceof Occurrence) {


			getTopicCache().createNew(prototype.getDefaultName(), prototype, getCurrentTopic(),
					lnglat, new StdAsyncCallback(ConstHolder.myConstants.save_async()) {
						public void onSuccess(Object result) {
							super.onSuccess(result);

							TopicIdentifier res = (TopicIdentifier) result;

							prototype.setId(res.getTopicID());
							prototype.setTitle(res.getTopicTitle());

							System.out.println("Received " + prototype + " "
									+ GWT.getTypeName(prototype));

							System.out.println("create occ getCurrentTopic().addOccurence()");
							getCurrentTopic().addOccurence((Occurrence) prototype);

							map.growIsland(prototype, lnglat);

							fireIslandCreated();
						}
					});

		} else {

			AddTopicPopup n = new AddTopicPopup(this, ConstHolder.myConstants.topic_new(),
					getCurrentTopic(), lnglat, new EZCallback() {
						public void onSuccess(Object result) {

							TopicIdentifier res = (TopicIdentifier) result;

							prototype.setId(res.getTopicID());
							prototype.setTitle(res.getTopicTitle());
							map.growIsland(prototype, lnglat);

							// getTopicCache().createNew

							fireIslandCreated();
						}
					});
		}

	}


	// /**
	// *
	// * @param name
	// * @param parentTopic
	// * @param lnglat
	// */
	// public void createTopic(final Topic prototype, final String name, final Topic parentTopic,
	// final int[] lnglat) {
	//
	// getTopicCache().createNew(name, prototype, parentTopic, lnglat,
	// new StdAsyncCallback(ConstHolder.myConstants.save_async()) {
	// public void onSuccess(Object result) {
	// super.onSuccess(result);
	// TopicIdentifier res = (TopicIdentifier) result;
	//
	// System.out.println("CreateTopic Back " + lnglat);
	//
	// prototype.setId(res.getTopicID());
	// prototype.setTitle(res.getTopicTitle());
	// map.growIsland(prototype, lnglat);
	//
	//
	// fireIslandCreated();
	//
	// }
	// });
	// }

	public void delete(final Topic topic, final AsyncCallback callback) {
		getTopicCache().delete(topic, new StdAsyncCallback(ConstHolder.myConstants.delete_async()) {
			public void onSuccess(Object result) {
				super.onSuccess(result);
				callback.onSuccess(result);


				System.out.println("Map.remove " + topic.getId());

				map.removeIsland(topic.getId());

				refreshAll();
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



	public void doLogin() {
		LoginService.doLogin(newFrame(), this);
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

	public void editMetas(AsyncCallback callback, final Meta type) {
		EditMetaWindow ew = new EditMetaWindow(this, newFrame(), type, callback);
	}

	public void explore() {
		explore(getCurrentTopic());

	}

	public void explore(Topic myTag) {

	}

	public void fireIslandCreated() {
		if (userActionListener != null) {
			userActionListener.islandCreated();
		}
	}

	public void fireOceanLoaded(int num_islands) {
		if (userActionListener != null) {
			userActionListener.oceanLoaded(num_islands);
		}
	}

	public void fireTopicCreated() {
		if (userActionListener != null) {
			userActionListener.topicCreated();
		}
	}

	private Topic getCurrentTopic() {
		return currentTopic;
	}

	/**
	 * Called by HippoTest to replace the load screen with the map. Called before we've even checked
	 * if a user exists.
	 * 
	 * @return
	 */
	public Widget getRootWidget() {
		// AbsolutePanel p = new AbsolutePanel();
		// p.add(mainMap.getOcean(),0,0);
		// p.add(mainMap,0,0);
		//		
		// return p;
		return map;
	}

	public TagLocalService getTagLocalService() {
		if (tagLocalService == null) {
			tagLocalService = new TagLocalService();
		}
		return tagLocalService;
	}



	public User getUser() {
		return user;
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


	public void loadFinished() {
		Preloader.preload("HippoPreLoad.html");
	}

	/**
	 * User is logged in. Update GUI.
	 * 
	 */
	private void loadGUI() {
		map.onLoginComplete(this);

		getTopicCache().getRootTopic(getUser(),
				new StdAsyncCallback(ConstHolder.myConstants.getRoot_async()) {
					// @Override
					public void onSuccess(Object result) {
						super.onSuccess(result);

						Root root = (Root) result;

						bringUpChart(root);
					}
				});

	}

	/**
	 * Call when we've been not logged in, but we've now logged in, and we need to setup the GUI
	 * elements.
	 * 
	 * 
	 */
	public void loginSuccess() {
		setup("LOGIN SUCC");
	}


	public GInternalFrame newFrame() {
		return newFrame("");
	}

	private GInternalFrame newFrame(String title) {

		GInternalFrame frame = new DefaultGInternalFrame(title);

		map.addFrame(frame);

		return frame;
	}

	public GadgetPopup newFrameGadget(Gadget gadget, GadgetDisplayer gDisplayer) {

		GadgetPopup frame = new GadgetPopup(gadget, gDisplayer);

		map.addFrame(frame);

		return frame;
	}

	/**
	 * pass the type of meta you'd like created and. 1) we'll put up the create window box. 2) we'll
	 * go create it. 3) we'll call your AsyncCallback which will receive the TopicIdentifier of the
	 * newly created Meta.
	 * 
	 * @param meta
	 * @param callback
	 */
	public void newMeta(final Meta meta, final AsyncCallback callback) {
		Window.alert("Mindscape Manager Not Supp");
		// AddTopicPopup n = new AddTopicPopup(this, ConstHolder.myConstants.meta_new(),
		// new EZCallback() {
		// public void onSuccess(Object result) {
		// getTopicCache().createNew((String) result, meta, null, null,
		// new StdAsyncCallback(ConstHolder.myConstants.save_async()) {
		// public void onSuccess(Object result) {
		// super.onSuccess(result);
		// callback.onSuccess(result);
		// }
		// });
		// }
		// });
	}

	public void refreshAll() {
		map.refreshIslands();
	}

	public void setMap(MainMap map) {
		this.map = map;
	}

	/**
	 * This will try to get the current user. If it suceeds it will run the GUI load scripts. If it
	 * doesn't suceed it will bring up the login dialog.
	 * 
	 */
	public void setup(final String caller) {

		System.out.println("setup() from " + caller);

		getHippoCache().getUserService().getCurrentUser(new AsyncCallback() {
			/*
			 * Expected, since we are preloading in an IFrame whether or not they've logged in
			 * 
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
			 */
			public void onFailure(Throwable caught) {

				System.out.println("failed for " + caller);
				Logger.log("GetCurrentUser failed! " + caught + " \nEP:"
						+ Interactive.getRelativeURL(""));
				doLogin();
			}

			public void onSuccess(Object result) {

				user = (User) result;
				System.out.println("succ " + caller);
				if (user != null) {
					System.out.println("found a user: " + user.getUsername());

					// try {
					// throw new Exception();
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					System.out.println("LoadGUI " + caller);
					loadGUI();
				} else {
					System.out.println("null user ELSE " + caller);
					doLogin();
				}
			}
		});
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
						map.displayTopic(tag);

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

	/**
	 * Topic was saved with this command.
	 * 
	 * Main responsibility is for the map to redraw everything involving these topics.
	 * 
	 */
	public void topicSaved(Topic t, AbstractCommand command) {
		map.update(t, command);


	}


	public void unselect() {
		System.out.println("MindscapeManger UNSELECT()");
		map.unselect(selectedTopics);
		selectedTopics.clear();
	}


	public void updateStatus(int i, String call, StatusCode send) {
		map.updateStatusWindow(i, call, send);
	}

	public void userNeedsToUpgrade() {
		displayInfo(ConstHolder.myConstants.userNeedsToUpgrade());
	}

	public void zoomIn() {
		map.zoomIn();
	}



	public void zoomOut() {
		map.zoomOut();
	}


	public void zoomTo(double scale) {
		map.zoomTo(scale);
	}


	public int load(Topic myTopic) {
		bringUpChart(myTopic);
		return 0;
	}


	public void addSelected(Topic t) {
		selectedTopics.add(t);
		map.editSelectStatus(t.getIdentifier(), true);

		System.out.println("MindscapeManager addSelected " + t + " Now: " + selectedTopics);
	}


	public GUIManager getGui() {
		return map;
	}



}
