package com.aavu.client.widget;

import com.aavu.client.TopicServiceAsync;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicDetail extends Composite {


	private TopicServiceAsync topiceService;

	private VerticalPanel panel = new VerticalPanel();

	public TopicDetail(Topic t){
		//topiceService = (TopicServiceAsync) GWT.create(TopicService.class);
		//ServiceDefTarget target = (ServiceDefTarget) topiceService;
		//target.setServiceEntryPoint("/topicService");

		load(t);
		setWidget(panel);
	}

	public void load(Topic t) {
		panel.clear();

		if(t == null){
			panel.add(new HTML("None loaded"));
		}else{

			panel.add(new HTML(t.getTitle()));
			panel.add(new HTML(t.getText()));
		}
	}

}
