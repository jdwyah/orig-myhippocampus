package com.aavu.client.widget.edit;

import com.aavu.client.async.EZCallback;
import com.aavu.client.service.cache.TopicCache;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.Widget;

public class TopicCompleter extends Composite {

	private TopicCache topicService;
	private TopicCompleteOracle oracle;
	private CompleteListener completeListener;
	private SuggestBox suggestBox;

	public TopicCompleter(TopicCache topicService) {
		super();
		this.topicService = topicService;
		if (oracle == null) {
			oracle = new TopicCompleteOracle(topicService);
		}
		suggestBox = new SuggestBox(oracle);

		initWidget(suggestBox);
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
		suggestBox.addEventHandler(new SuggestionHandler() {

			public void onSuggestionSelected(SuggestionEvent event) {
				System.out.println("On Suggestion Selected! "
						+ event.getSelectedSuggestion().getReplacementString());
				// event.getSelectedSuggestion()
				complete();
			}
		});

		suggestBox.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				System.out.println("ON CHANGE " + suggestBox.getText());
			}
		});

		// addChangeListener(new ChangeListener() {
		// public void onChange(Widget sender) {
		// System.out.println("ONCHANGE " + getText());
		// complete();
		// }
		// });
	}

	/**
	 * public so we can call this at any time
	 */
	public void complete() {

		System.out.println("Complete t " + suggestBox.getText() + " ");

		getTopicIdentForNameOrCreateNew(suggestBox.getText(), new EZCallback() {
			public void onSuccess(Object result) {
				// completeListener.completed((TopicIdentifier) result);
			}
		});
	}

	public void setText(String string) {
		suggestBox.setText(string);
	}

}
