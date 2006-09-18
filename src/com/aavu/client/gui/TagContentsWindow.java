package com.aavu.client.gui;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TagContentsWindow extends PopupWindow {

	private VerticalPanel mainPanel;	

	public TagContentsWindow(Manager manager, Tag tag) {
		super(manager.myConstants.tagContentsTitle());

		mainPanel = new VerticalPanel();


		manager.getTopicCache().getTopicsWithTag(tag,new StdAsyncCallback("Get Topics with Tag"){

			public void onSuccess(Object result) {
				Topic[] topics = (Topic[]) result;
				mainPanel.clear();
				
				
				for (int i = 0; i < topics.length; i++) {
					Topic topic = topics[i];
					mainPanel.add(new Label(topic.getTitle()));
				}
			}});

		setContentPanel(mainPanel);

	}


}
