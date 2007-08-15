package com.aavu.client.gui;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BreadCrumbDisplayer extends PopupWindow {

	private VerticalPanel mainPanel;

	private Map idToWidget = new HashMap();

	private ScrollPanel scrollP;

	public BreadCrumbDisplayer(Manager manager) {
		super(manager.newFrame(), ConstHolder.myConstants.breadcrumbs(), 120, 100);
		mainPanel = new VerticalPanel();
		mainPanel.add(new Label(" "));
		scrollP = new ScrollPanel(mainPanel);

		scrollP.setStyleName("H-BreadCrumbScroll");

		setContent(scrollP);
		frame.setLocation(300, 0);
		frame.setClosable(false);
		frame.setMaximizable(false);
		frame.setMinimizable(false);
		frame.setSize(120, 100);

		frame.getContent().addStyleName("H-BreadCrumbs");
	}

	/**
	 * Keep a list of all visitted topics. When the user re-visits, bump that entry to the top of
	 * the list instead of repeating it.
	 */
	public int load(Topic topic) {

		TopicLink link = (TopicLink) idToWidget.get(new Long(topic.getId()));
		if (link == null) {
			link = new TopicLink(topic);
			idToWidget.put(new Long(topic.getId()), link);
		} else {
			mainPanel.remove(link);
		}
		mainPanel.insert(link, 0);

		scrollP.setScrollPosition(0);

		return 0;
	}



}
