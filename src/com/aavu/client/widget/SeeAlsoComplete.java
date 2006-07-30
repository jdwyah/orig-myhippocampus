package com.aavu.client.widget;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.TopicServiceAsync;
import com.aavu.client.domain.Topic;
import com.aavu.client.widget.autocompletion.RemoteTopicAutoCompletionItems;


public class SeeAlsoComplete extends AutoCompleteTextBoxMultipleCompletes {

	private static final int LENGTH = 40;

	public SeeAlsoComplete(List alsos,TopicServiceAsync topicService) {
		setCompletionItems(new RemoteTopicAutoCompletionItems(topicService));

		StringBuffer sb = new StringBuffer();

		if(alsos == null){
			return;
		}

		for (Iterator iter = alsos.iterator(); iter.hasNext();) {
			Topic topic = (Topic) iter.next();

			sb.append(topic.getTitle());
			sb.append(SEPARATOR);
		}

		setText(sb.toString());
		
		
		//doesn't seem to work for formatting.. what prop do I want?
		setMaxLength(LENGTH);

	}

}
