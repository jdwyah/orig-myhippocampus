package com.aavu.client.gui.explorer;

import com.aavu.client.gui.blog.BlogView;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.glossary.LostNFound;
import com.aavu.client.gui.maps.BigMap;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ButtonGroup;
import com.aavu.client.widget.SelectableButton;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Explorer extends Composite implements ButtonGroup {


	private SimplePanel currentView;
	private TimeLineWrapper timeline;
	private ExplorerGlossary glossary;
	private BlogView blogView;
	private BigMap bigMap;
	private LostNFound lostNFound;

	private ExplorerB selectedB;

	private Manager manager;


	// <TopicIdentifier>
	private boolean allmode;
	private SelectableButton currentButton;


	/**
	 * NOTE: you can't put a timeline in a TabPanel. Weird JS problem from the timeline.js
	 * 
	 * CTOR for when you've already got results for the tag
	 * 
	 * @param myTag
	 * @param topics
	 * @param manager
	 * @param height
	 * @param width
	 * @param frame
	 */
	public Explorer(Manager manager, int width, int height, PopupWindow window) {

		this.manager = manager;
		// this.tagToIdentifierMap = defaultMap;

		CellPanel mainP = new HorizontalPanel();

		CellPanel optionsPanel = new VerticalPanel();

		currentView = new SimplePanel();

		glossary = new ExplorerGlossary(manager, Orientation.HORIZONTAL);

		timeline = new TimeLineWrapper(manager, width, height, window);

		blogView = new BlogView(manager, width, height);

		bigMap = new BigMap(manager, window, width, height);

		lostNFound = new LostNFound(manager);


		ExplorerB timeB = new ExplorerB(ConstHolder.myConstants.explorer_timeline(), timeline, this);
		ExplorerB azB = new ExplorerB(ConstHolder.myConstants.explorer_atoz(), glossary, this);
		ExplorerB recentB = new ExplorerB(ConstHolder.myConstants.explorer_recent(), blogView, this);
		ExplorerB mapB = new ExplorerB(ConstHolder.myConstants.explorer_map(), bigMap, this);
		ExplorerB lostNFoundB = new ExplorerB(ConstHolder.myConstants.explorer_lostFound(),
				lostNFound, this);

		selectedB = recentB;

		optionsPanel.add(timeB);
		optionsPanel.add(azB);
		optionsPanel.add(recentB);
		optionsPanel.add(mapB);
		// optionsPanel.add(lostNFoundB);


		CellPanel leftPanel = new VerticalPanel();
		leftPanel.add(optionsPanel);


		mainP.add(leftPanel);
		mainP.add(currentView);

		initWidget(mainP);

		// default to glossary
		currentView.clear();
		currentView.add(glossary);

		recentB.onClick(azB);
	}



	public void loadAll() {
		allmode = true;
		selectedB.getExplorerP().loadAll();
	}

	// Set<TagStat>
	public void load() {
		ExplorerPanel selectedPanel = selectedB.getExplorerP();

		allmode = false;

		// selectedB.getExplorerP().load(manager.getCurrentT);


	}

	private class ExplorerB extends SelectableButton implements ClickListener {

		private ExplorerPanel explorerP;

		public ExplorerB(String label, ExplorerPanel w, ButtonGroup buttonGroup) {
			super(label, buttonGroup);
			addClickListener(this);
			addStyleName("H-ExplorerButton");
			this.explorerP = w;
		}


		public ExplorerPanel getExplorerP() {
			return explorerP;
		}


		public void onClick(Widget sender) {
			super.onClick();
			selectedB = (ExplorerB) sender;


			currentView.clear();
			if (allmode) {
				explorerP.loadAll();
			} else {
				// explorerP.load(manager.getCurrentObjs());
			}
			currentView.add(explorerP.getWidget());

		}
	}

	public void newSelection(SelectableButton button) {
		if (currentButton != null) {
			currentButton.setSelected(false);
		}
		currentButton = button;
		currentButton.setSelected(true);
	}



}
