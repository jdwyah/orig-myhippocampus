package com.aavu.client.gui.gadgets;

import java.util.Iterator;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChildrenGadget extends Gadget implements TopicLoader {

	private static final int MAX_HEIGHT = 200;
	private static final int HEIGHT_PER_ENTRY = 20;



	private VerticalPanel childPanel;



	private ScrollPanel scroll;


	public ChildrenGadget(Manager _manager) {

		super(_manager);

		childPanel = new VerticalPanel();
		childPanel.addStyleName("H-ChildDisplay");


		// PEND awkward. we'd really like to set scroll.setMaxHeight(),
		// but that doesn't exist. setting it in all cases leads to excess space
		scroll = new ScrollPanel(childPanel);
		scroll.setAlwaysShowScrollBars(false);


		scroll.setHeight(MAX_HEIGHT + "px");


		initWidget(scroll);


	}


	// @Override
	public boolean isDisplayer() {
		return true;
	}


	public int load(Topic topic) {
		childPanel.clear();


		int childCount = 0;
		for (Iterator iterator = topic.getInstances().iterator(); iterator.hasNext();) {

			TopicTypeConnector conn = (TopicTypeConnector) iterator.next();

			Topic child = conn.getTopic();
			childPanel.add(new TopicLink(child));

			childCount++;
		}

		if (childCount < 1) {
			childPanel.add(new Label(" "));
		}
		int scrollH = childCount * HEIGHT_PER_ENTRY;
		scrollH = (scrollH < MAX_HEIGHT) ? scrollH : MAX_HEIGHT;
		scroll.setHeight(scrollH + "px");
		System.out.println("ChildGadg scroll heigh " + scrollH);

		return childCount;
	}

	// @Override
	public Image getPickerButton() {
		return null;
	}

	// @Override
	public boolean isOnForTopic(Topic topic) {
		return !topic.getInstances().isEmpty();
	}



	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.children();
	}


	// @Override
	public void createInstance(Manager manager, int[] lngLat) {
		// TODO Auto-generated method stub

	}
}
