package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.gadgets.TagBoard;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class CenterTopicDisplayer extends Composite {

	private TagBoard tagBoard;
	// private TitleGadget titleG;

	private Topic topic;
	private Manager manager;


	public CenterTopicDisplayer(final Manager manager) {

		this.manager = manager;

		tagBoard = new TagBoard(manager);

		HorizontalPanel mainPanel = new HorizontalPanel();



		// titleG = new TitleGadget(manager);
		//		
		// mainPanel.add(titleG);


		mainPanel.add(tagBoard);

		initWidget(mainPanel);

		addStyleName("H-AbsolutePanel");
		addStyleName("H-CenterInfo");

		setVisible(false);
	}


	public void load(Topic topic) {

		this.topic = topic;

		setVisible(true);

		// titleG.load(topic);

		tagBoard.load(topic);

	}

	public void unload() {
		setVisible(false);
	}


	public void clearForLoading() {
		setVisible(false);
	}


}
