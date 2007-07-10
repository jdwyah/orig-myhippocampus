package com.aavu.client.widget.edit;

import com.aavu.client.async.EZCallback;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.autocompletion.SuggestBoxExt;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Widget;

public class TopicCompleter extends SuggestBoxExt {

	private TopicCache topicService;
	private TopicCompleteOracle oracle;
	private CompleteListener completeListener;

	public TopicCompleter(TopicCache topicService) {
		super();

		if (oracle == null) {
			oracle = new TopicCompleteOracle(topicService);
		}
		setOracle(oracle);
	}

	/**
	 * Convenience method to use our TopicService.
	 * 
	 * @param completeText
	 * @param callback
	 */
	public void getTopicIdentForNameOrCreateNew(String completeText, AsyncCallback callback) {
		topicService.getTopicIdentForNameOrCreateNew(completeText, callback);
	}

	public void setCompleteListener(CompleteListener completeListener) {
		this.completeListener = completeListener;
		addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				System.out.println("ONCHANGE " + getText());
				complete();
			}
		});
	}

	/**
	 * public so we can call this at any time
	 */
	public void complete() {
		getTopicIdentForNameOrCreateNew(getText(), new EZCallback() {
			public void onSuccess(Object result) {
				completeListener.completed((TopicIdentifier) result);
			}
		});
	}

}
