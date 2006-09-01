package com.aavu.client.widget.edit;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.widget.AutoCompleteTextBoxMultipleCompletes;
import com.aavu.client.widget.autocompletion.RemoteTopicAutoCompletionItems;


public class SeeAlsoComplete extends AutoCompleteTextBoxMultipleCompletes {

	private static final int LENGTH = 40;

	/**
	 * 
	 * NOTE THIS CLASS IS UNUSED 
	 * 
	 * (but kinda cool, so it's still here)
	 * 
	 * @param alsos
	 * @param topicService
	 */
	public SeeAlsoComplete(List alsos,TopicCache topicService) {
		setCompletionItems(new RemoteTopicAutoCompletionItems(topicService));

		StringBuffer sb = new StringBuffer();

		if(alsos != null){
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

}
