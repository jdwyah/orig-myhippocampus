package com.aavu.client.service;


import java.util.Date;

import org.gwm.client.GInternalFrame;
import org.gwm.client.impl.DefaultGInternalFrame;
import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.Interactive;
import com.aavu.client.LinkPlugin.AddLinkPopup;
import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.GoogleData;
import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.S3File;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.AddTopicPopup;
import com.aavu.client.gui.EditMetaWindow;
import com.aavu.client.gui.EntryEditWindow;
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
import com.aavu.client.gui.timeline.HasDate;
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



	private TagLocalService tagLocalService;
	private User user;

	private UserActionListener userActionListener;



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

		setMap(new MainMap(this));

		getGadgetManager().addGadgetClickListener(this);
	}


	// public void show(Topic topic, boolean editMode) {

	public void addDeliciousTags(String username, String password, AsyncCallback callback) {
		getHippoCache().getSubjectService().addDeliciousTags(username, password, callback);
	}

	public void bringUpChart(Topic topic) {

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

	public void gadgetClicked(Gadget gadget, int[] lngLat, Date dateCreated) {

		if (lngLat != null) {
			System.out.println("MindscapeManager lngLat " + lngLat[0] + " " + lngLat[1]);
		} else {
			System.out.println("MindscapeManager lngLat " + lngLat);
		}
		gadget.createInstance(this, lngLat, dateCreated);


		// getMap().growIsland();
	}


	/**
	 * 
	 * 
	 * trick is to get us the ID, so that if we do a create, then an edit, we don't dupe on server.
	 * 
	 * @param prototype
	 *            The type of object to create
	 * @param lnglat
	 *            int[2] of lnglat, if not null we will assume you were created by
	 *            HierarchyContextMenu
	 * @param dateCreated
	 *            if not null we will assume you were created by TimelineContextMenu
	 * @param doNamePopup
	 *            Set to true if you want a popup to pick the title
	 * @param needsPopupForParent
	 *            Set to true if you want a popup to pick the parent, otherwise we will use
	 *            getCurrentTopic()
	 */
	public void createNew(final Topic prototype, final int[] lnglat, final Date dateCreated,
			boolean doNamePopup, boolean needsPopupForParent) {
		createNew(prototype, null, lnglat, dateCreated, doNamePopup, needsPopupForParent);
	}

	private void createNew(final Topic prototype, Topic parentParam, final int[] lnglat,
			final Date dateCreated, boolean doNamePopup, boolean needsPopupForParent) {



		final Topic ourP;
		// switcheroo here to get something into a final variable so we can see it in the callback
		if (null == parentParam) {
			ourP = getCurrentTopic();
		} else {
			ourP = parentParam;
		}

		System.out.println("Create new " + prototype + " " + GWT.getTypeName(prototype) + " "
				+ lnglat + " parent " + ourP);

		// used by HippoDate creation. This will happen in 2 creation steps. 1st, A HippoDate
		// requires a parent topic. Prompt to get that, create if necessary. 2nd do a quick,
		// nameless create of the HippoDate with that parent.
		if (needsPopupForParent) {
			AddTopicPopup n = new AddTopicPopup(this, ConstHolder.myConstants.topic_new(), ourP,
					null, null, new EZCallback() {
						public void onSuccess(Object result) {
							TopicIdentifier res = (TopicIdentifier) result;
							Topic createdParent = doCreationCallback(new RealTopic(), ourP, null,
									null, res);
							System.out.println("MindscapeManager.needsPopupForParent created "
									+ createdParent);
							createNew(prototype, createdParent, lnglat, dateCreated, false, false);
						}
					});
			return;
		}

		// creation requires a new title, prompt. This will be a window with a TopicCompleter that
		// will do the add for us and callback
		if (doNamePopup) {
			AddTopicPopup n = new AddTopicPopup(this, ConstHolder.myConstants.topic_new(), ourP,
					lnglat, dateCreated, new EZCallback() {
						public void onSuccess(Object result) {
							TopicIdentifier res = (TopicIdentifier) result;
							doCreationCallback(prototype, ourP, lnglat, dateCreated, res);
						}
					});
		} else {
			getTopicCache().createNew(prototype.getDefaultName(), prototype, ourP, lnglat,
					dateCreated, new StdAsyncCallback(ConstHolder.myConstants.save_async()) {
						public void onSuccess(Object result) {
							super.onSuccess(result);

							TopicIdentifier res = (TopicIdentifier) result;
							doCreationCallback(prototype, ourP, lnglat, dateCreated, res);
						}
					});
		}

	}

	/**
	 * Deal with the topic that we've created, update appropriate GUI objects
	 * 
	 * @param prototype
	 * @param parent
	 * @param lnglat
	 * @param dateCreated
	 * @param result
	 * @return
	 */
	private Topic doCreationCallback(final Topic prototype, Topic parent, final int[] lnglat,
			final Date dateCreated, TopicIdentifier result) {

		prototype.setId(result.getTopicID());
		prototype.setTitle(result.getTopicTitle());
		if (dateCreated != null) {
			prototype.setCreated(dateCreated);
		}

		System.out.println("-----------------\nMindscapeManager.createCallback Received "
				+ prototype + " " + GWT.getTypeName(prototype) + " Parent " + parent);

		// IF OCC
		if (prototype instanceof Occurrence) {
			parent.addOccurence((Occurrence) prototype);
		}
		// IF RealTopic
		if (prototype instanceof RealTopic) {
			TopicTypeConnector conn = prototype.addType(parent);
			parent.getInstances().add(conn);
		}


		// IF DATE
		// TODO cast
		if (dateCreated != null) {
			TimeLineObj tlo = null;
			if (prototype instanceof HippoDate) {
				System.out.println("DO HippoDate add proto: " + prototype);
				tlo = new TimeLineObj(parent.getIdentifier(), (HasDate) prototype);

				parent.addMetaValue(null, prototype);

				// parent was created, add to map
				getMap().growIsland(parent, lnglat);
				fireIslandCreated();
			} else {
				System.out.println("DO TopicTime add proto: " + prototype);
				tlo = new TimeLineObj(prototype.getIdentifier(), (HasDate) prototype);
				// prototype was created, add to map
				getMap().growIsland(prototype, lnglat);
				fireIslandCreated();
			}
			getMap().getTimeline().addSingle(tlo);
		}

		// ELSE if from hierarchy panel
		if (lnglat != null) {
			getMap().growIsland(prototype, lnglat);
			fireIslandCreated();
		}
		return prototype;
	}


	public void delete(final Topic topic, final AsyncCallback callback) {
		getTopicCache().delete(topic, new StdAsyncCallback(ConstHolder.myConstants.delete_async()) {
			public void onSuccess(Object result) {
				super.onSuccess(result);
				callback.onSuccess(result);


				System.out.println("Map.remove " + topic.getId());

				getMap().removeIsland(topic.getId());

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



	public TagLocalService getTagLocalService() {
		if (tagLocalService == null) {
			tagLocalService = new TagLocalService();
		}
		return tagLocalService;
	}



	public User getUser() {
		return user;
	}



	public void loadFinished() {
		Preloader.preload("HippoPreLoad.html");
	}

	/**
	 * User is logged in. Update GUI.
	 * 
	 */
	private void loadGUI() {
		getMap().onLoginComplete(this);

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

		getMap().addFrame(frame);

		return frame;
	}

	public GadgetPopup newFrameGadget(Gadget gadget, GadgetDisplayer gDisplayer) {

		GadgetPopup frame = new GadgetPopup(gadget, gDisplayer);

		getMap().addFrame(frame);

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
		getMap().refreshIslands();
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

	/**
	 * Topic was saved with this command.
	 * 
	 * Main responsibility is for the map to redraw everything involving these topics.
	 * 
	 */
	public void topicSaved(Topic t, AbstractCommand command) {
		getMap().update(t, command);


	}


	public void unselect() {
		System.out.println("MindscapeManger UNSELECT()");
		getMap().unselect(selectedTopics);
		selectedTopics.clear();
	}


	public void updateStatus(int i, String call, StatusCode send) {
		getMap().updateStatusWindow(i, call, send);
	}

	public void userNeedsToUpgrade() {
		displayInfo(ConstHolder.myConstants.userNeedsToUpgrade());
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


	public int load(Topic myTopic) {
		bringUpChart(myTopic);
		return 0;
	}


	public void addSelected(Topic t) {
		selectedTopics.add(t);
		getMap().editSelectStatus(t.getIdentifier(), true);

		System.out.println("MindscapeManager addSelected " + t + " Now: " + selectedTopics);
	}



}
