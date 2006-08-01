package com.aavu.client.widget;

import com.aavu.client.TopicServiceAsync;
import com.aavu.client.widget.autocompletion.AutoCompleteTextBox;
import com.aavu.client.widget.autocompletion.RemoteTopicAutoCompletionItems;


public class TopicCompleter extends AutoCompleteTextBox {

	private static final int LENGTH = 40;

	public TopicCompleter(TopicServiceAsync topicService) {
		setCompletionItems(new RemoteTopicAutoCompletionItems(topicService));
		
		//doesn't seem to work for formatting.. what prop do I want?
		setMaxLength(LENGTH);

	}

}
