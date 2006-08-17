package com.aavu.client.widget.edit;

import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.widget.autocompletion.AutoCompleteTextBox;
import com.aavu.client.widget.autocompletion.RemoteTopicAutoCompletionItems;


public class TopicCompleter extends AutoCompleteTextBox {

	private static final int LENGTH = 40;

	private static GWTTopicServiceAsync topicService;
		
	public static void setTopicService(GWTTopicServiceAsync topicService) {
		TopicCompleter.topicService = topicService;
	}



	public TopicCompleter() {
		setCompletionItems(new RemoteTopicAutoCompletionItems(topicService));
		
		//doesn't seem to work for formatting.. what prop do I want?
		setMaxLength(LENGTH);

	}

}
