package com.aavu.client.gui.hierarchy;

import java.util.Iterator;
import java.util.Set;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.S3File;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.WebLink;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class TopicDisplayOverlay implements MouseListener {

	private static final int OPTION_P_OFFSET = -60;
	private static final int ASSOC_MIN_WIDTH = 200;
	private static final int MAX_CHILDREN = 8;
	private Widget widget;
	private Timer hideTimer;
	private DisplayPanel childrenP;
	private DisplayPanel webLinkP;
	private DisplayPanel entryP;
	private DisplayPanel filesP;

	private DisplayPanel associationP;

	// private AbsolutePanel mainP;

	private Manager manager;
	private OptionsPanel optionsP;

	private int top;
	private int left;
	private boolean childrenOnly;


	public TopicDisplayOverlay(Topic topic, Widget widget, Manager manager, boolean childrenOnly) {
		// super(true);

		this.widget = widget;
		this.manager = manager;
		this.childrenOnly = childrenOnly;

		hideTimer = new Timer() {
			public void run() {
				hide();
			}

		};

		// mainP = new AbsolutePanel();


		childrenP = new DisplayPanel("Children");
		int childCount = 0;
		for (Iterator iterator = topic.getInstances().iterator(); iterator.hasNext();) {

			TopicTypeConnector conn = (TopicTypeConnector) iterator.next();

			if (childCount++ < MAX_CHILDREN) {
				System.out.println("Child Count " + childCount);
				Topic child = conn.getTopic();
				childrenP.add(new TopicLink(child));
			}

		}
		// mainP.add(childrenP, 0, widget.getOffsetHeight());


		associationP = new SeeAlsoPanel(topic);
		// mainP.add(associationP, 0, 0);


		webLinkP = new DisplayPanel("Links");

		Set weblinks = topic.getWebLinks();
		for (Iterator iterator = weblinks.iterator(); iterator.hasNext();) {
			WebLink webL = (WebLink) iterator.next();
			System.out.println("wlll " + webL.getTitle());
			webLinkP.add(new Label(webL.getTitle()));
		}

		entryP = new DisplayPanel("Entries");

		Set entries = topic.getEntries();
		for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			// PEND MED whatever are we to do w/ "" titles?
			entryP.add(new Label(" " + entry.getTitle()));
		}


		filesP = new DisplayPanel("Files");
		Set files = topic.getFiles();
		for (Iterator iterator = files.iterator(); iterator.hasNext();) {
			S3File file = (S3File) iterator.next();
			filesP.add(new Label(file.getTitle()));
		}


		optionsP = new OptionsPanel(topic, manager);
		// mainP.add(optionsP, 0, OPTION_P_OFFSET);


		webLinkP.addMouseListener(this);
		entryP.addMouseListener(this);
		filesP.addMouseListener(this);
		optionsP.addMouseListener(this);
		associationP.addMouseListener(this);
		childrenP.addMouseListener(this);

		// FocusPanel focusPanel = new FocusPanel(mainP);
		// focusPanel.addMouseListener(this);
		// add(focusPanel);

		// setStyleName("H-TopicOverlayDisplayer");

		// DOM.setStyleAttribute(mainP.getElement(), "position", "absolute");

	}



	private void hide() {
		webLinkP.hide();
		entryP.hide();
		filesP.hide();
		optionsP.hide();
		associationP.hide();
		childrenP.hide();
	}



	private class SeeAlsoPanel extends DisplayPanel {
		public SeeAlsoPanel(Topic topic) {
			super(ConstHolder.myConstants.seeAlsos());
			TopicCompleter seeAlsoComplete = new TopicCompleter(manager.getTopicCache());

			add(seeAlsoComplete);

			for (Iterator iterator = topic.getSeeAlsoAssociation().getMembers().iterator(); iterator
					.hasNext();) {

				Topic seeAlso = (Topic) iterator.next();
				add(new TopicLink(seeAlso));
			}
		}
	}


	/**
	 * hide on a little cancellable delay so that we can: 1) get mouseleave from bubble -> hide 2)
	 * get enter from this -> show
	 * 
	 * Cancelled by show()
	 * 
	 * Call this instead of hide() unless you're sure.
	 */

	public void hideIn1() {
		hideTimer.schedule(500);
	}

	// @Override
	public void show() {
		hideTimer.cancel();
		setPopupPosition(widget.getAbsoluteLeft(), widget.getAbsoluteTop());

		if (!childrenP.isEmpty()) {
			int wh = widget.getOffsetHeight();

			// PEND safari hack, widget.getOffsetHeight is returning 0 there
			if (wh < 1) {
				wh = 50;
			}
			childrenP.setPopupPosition(left, top + wh);
			childrenP.show();
		}

		if (childrenOnly) {
			return;
		}
		System.out.println("setting assoc P - " + associationP.getOffsetWidth());

		int assocLeft = associationP.getOffsetWidth();
		assocLeft = assocLeft < ASSOC_MIN_WIDTH ? ASSOC_MIN_WIDTH : assocLeft;
		associationP.setPopupPosition(left - assocLeft, top);
		associationP.show();

		int occurrenceX = childrenP.getOffsetWidth() > widget.getOffsetWidth() ? childrenP
				.getOffsetWidth() : widget.getOffsetWidth();

		int ctop = top;

		if (!webLinkP.isEmpty()) {
			webLinkP.setPopupPosition(left + occurrenceX, ctop);
			webLinkP.show();
			ctop += webLinkP.getOffsetHeight();
		}
		if (!entryP.isEmpty()) {
			entryP.setPopupPosition(left + occurrenceX, ctop);
			entryP.show();
			ctop += entryP.getOffsetHeight();
		}
		if (!filesP.isEmpty()) {
			filesP.setPopupPosition(left + occurrenceX, ctop);
			filesP.show();
			ctop += filesP.getOffsetHeight();
		}
		optionsP.setPopupPosition(left, top + OPTION_P_OFFSET);
		optionsP.show();


	}

	public void onMouseLeave(Widget sender) {
		hideIn1();
	}

	public void onMouseEnter(Widget sender) {
		show();
	}

	public void onMouseDown(Widget sender, int x, int y) {
	}

	public void onMouseMove(Widget sender, int x, int y) {
	}

	public void onMouseUp(Widget sender, int x, int y) {
	}



	public void setPopupPosition(int left, int top) {
		this.left = left;
		this.top = top;
	}



	public void hideImmediate() {
		hide();
	}

}
