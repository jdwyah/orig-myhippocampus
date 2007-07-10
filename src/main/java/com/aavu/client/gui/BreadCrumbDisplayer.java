package com.aavu.client.gui;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BreadCrumbDisplayer extends Gadget {

	private VerticalPanel mainPanel;

	private Map idToWidget = new HashMap();

	private ScrollPanel scrollP;

	public BreadCrumbDisplayer() {
		super(ConstHolder.myConstants.breadcrumbs());
		mainPanel = new VerticalPanel();
		scrollP = new ScrollPanel(mainPanel);

		scrollP.setStyleName("H-BreadCrumbScroll");

		initWidget(scrollP);

		addStyleName("H-AbsolutePanel");
		addStyleName("H-BreadCrumbs");
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

	// @Override
	public Image getPickerButton() {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	public boolean isOnForTopic(Topic topic) {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public void onClick(Manager manager) {

	}

}
