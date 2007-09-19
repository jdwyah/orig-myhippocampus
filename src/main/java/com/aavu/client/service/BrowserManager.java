package com.aavu.client.service;

import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.GUIManager;
import com.aavu.client.gui.GadgetDisplayer;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetPopup;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class BrowserManager extends AbstractManager {
	public BrowserManager(HippoCache cache) {
		super(cache);

	}

	public void addSelected(Topic topic) {
		// TODO Auto-generated method stub

	}

	public void addSelected(TopicIdentifier ti) {
		// TODO Auto-generated method stub

	}

	// @Override
	public void bringUpChart(Topic topic) {
		// TODO Auto-generated method stub

	}

	public void createNew(Topic t, int[] lnglat, Date dateCreated) {
		// TODO Auto-generated method stub

	}

	public void createNew(Topic prototype, int[] lnglat, Date dateCreated, boolean doNamePopup,
			boolean needsPopupForParent) {
		// TODO Auto-generated method stub

	}

	public PopupWindow displayInfo(Widget widg) {
		// TODO Auto-generated method stub
		return null;
	}

	public void doLogin() {
		// TODO Auto-generated method stub

	}

	public void editOccurrence(Occurrence topic, boolean saveNeeded) {
		// TODO Auto-generated method stub

	}

	public void explore() {
		// TODO Auto-generated method stub

	}

	public void explore(Topic myTag, List topics) {
		// TODO Auto-generated method stub

	}

	public GUIManager getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	public GadgetPopup newFrameGadget(Gadget gadget, GadgetDisplayer gDisplayer) {
		// TODO Auto-generated method stub
		return null;
	}

	public void newIsland() {
		// TODO Auto-generated method stub

	}

	public void newMeta(Meta meta, AsyncCallback callback) {
		// TODO Auto-generated method stub

	}

	public void refreshAll() {
		// TODO Auto-generated method stub

	}

	public void setup(String caller) {


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

	public void showPreviews(long int0) {
		// TODO Auto-generated method stub

	}

	public PopupWindow showProgressBar(ProgressBar progressBar) {
		// TODO Auto-generated method stub
		return null;
	}

	public void unselect() {
		// TODO Auto-generated method stub

	}

	public void updateStatus(int myNum, String string, StatusCode fail) {
		// TODO Auto-generated method stub

	}

	public void userNeedsToUpgrade() {
		// TODO Auto-generated method stub

	}

}
