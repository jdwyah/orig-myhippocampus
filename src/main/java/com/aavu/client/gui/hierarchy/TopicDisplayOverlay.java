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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicDisplayOverlay extends PopupPanel implements MouseListener {

	private static final int OPTION_P_OFFSET = -30;
	private Widget widget;
	private Timer hideTimer;
	private DisplayerPanel childrenP;
	private DisplayerPanel webLinkP;
	private DisplayerPanel entryP;
	private DisplayerPanel filesP;
	private AbsolutePanel mainP;
	private DisplayerPanel associationP;
	private Manager manager;

	public TopicDisplayOverlay(Topic topic, Widget widget, Manager manager) {

		super(true);

		this.widget = widget;
		this.manager = manager;

		hideTimer = new Timer() {
			public void run() {
				hide();
			}
		};

		mainP = new AbsolutePanel();


		childrenP = new DisplayerPanel();
		for (Iterator iterator = topic.getInstances().iterator(); iterator.hasNext();) {

			TopicTypeConnector conn = (TopicTypeConnector) iterator.next();
			Topic child = conn.getTopic();
			childrenP.add(new TopicLink(child));

		}
		mainP.add(childrenP, 0, widget.getOffsetHeight());


		associationP = new SeeAlsoPanel(topic);
		mainP.add(associationP, 0, 0);


		webLinkP = new DisplayerPanel();

		Set weblinks = topic.getWebLinks();
		if (!weblinks.isEmpty()) {
			webLinkP.add(new Label("Links"));
		}
		System.out.println("web links " + weblinks.isEmpty());
		for (Iterator iterator = weblinks.iterator(); iterator.hasNext();) {
			WebLink webL = (WebLink) iterator.next();
			System.out.println("wlll " + webL.getTitle());
			webLinkP.add(new Label(webL.getTitle()));
		}

		entryP = new DisplayerPanel();

		Set entries = topic.getEntries();
		if (!entries.isEmpty()) {
			entryP.add(new Label("Entries"));
		}
		for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			entryP.add(new Label(entry.getTitle()));
		}


		filesP = new DisplayerPanel();
		Set files = topic.getFiles();
		if (!files.isEmpty()) {
			filesP.add(new Label("Files"));
		}
		for (Iterator iterator = files.iterator(); iterator.hasNext();) {
			S3File file = (S3File) iterator.next();
			filesP.add(new Label(file.getTitle()));
		}


		OptionsPanel optionsP = new OptionsPanel(topic, manager);
		mainP.add(optionsP, 0, OPTION_P_OFFSET);


		FocusPanel focusPanel = new FocusPanel(mainP);
		focusPanel.addMouseListener(this);
		add(focusPanel);


	}

	// @Override
	protected void onAttach() {
		super.onAttach();

		DeferredCommand.addCommand(new Command() {

			public void execute() {
				// place occurrences to the right of either the topicbubble or the childrenPanel,
				// whichever
				// is furthest right
				int occurrenceX = childrenP.getOffsetWidth() > widget.getOffsetWidth() ? childrenP
						.getOffsetWidth() : widget.getOffsetWidth();

				System.out.println("2occX " + occurrenceX + " children "
						+ childrenP.getOffsetWidth() + " bubble " + widget.getOffsetWidth());

				// add occurrences on right
				mainP.add(webLinkP, occurrenceX, 0);
				mainP.add(entryP, occurrenceX, webLinkP.getOffsetHeight());
				mainP.add(filesP, occurrenceX, entryP.getOffsetHeight()
						+ webLinkP.getOffsetHeight());

				// move associations left
				mainP.setWidgetPosition(associationP, -associationP.getOffsetWidth(), 0);
			}
		});

	}

	private class DisplayerPanel extends VerticalPanel {
		public DisplayerPanel() {
			super();
			setStyleName("H-TopicOverlayDisplayer");
			addStyleName("H-Gadget");
		}

	}

	private class SeeAlsoPanel extends DisplayerPanel {
		public SeeAlsoPanel(Topic topic) {
			TopicCompleter seeAlsoComplete = new TopicCompleter(manager.getTopicCache());
			add(new Label(ConstHolder.myConstants.seeAlsos()));
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
		super.show();
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

}
