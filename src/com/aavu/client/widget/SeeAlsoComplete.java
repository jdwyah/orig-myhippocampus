package com.aavu.client.widget;

import java.util.List;

import com.aavu.client.TopicServiceAsync;
import com.aavu.client.domain.Topic;
import com.aavu.client.widget.autocompletion.AutoCompleteTextBox;
import com.aavu.client.widget.autocompletion.CompletionItems;
import com.aavu.client.widget.autocompletion.MatchesRequiring;
import com.aavu.client.widget.autocompletion.RemoteTopicAutoCompletionItems;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class SeeAlsoComplete extends AutoCompleteTextBox {

	private TopicServiceAsync topicService;

	public SeeAlsoComplete(Topic also,TopicServiceAsync topicService) {
		setCompletionItems(new RemoteTopicAutoCompletionItems(topicService));
		
		
		setTopicServiceA(topicService);
	}
	
	public void setTopicServiceA(TopicServiceAsync topicService) {
		this.topicService = topicService;
	}

}
