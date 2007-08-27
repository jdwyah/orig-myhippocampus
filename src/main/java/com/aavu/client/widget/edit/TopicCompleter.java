package com.aavu.client.widget.edit;

import com.aavu.client.async.EZCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.cache.TopicCache;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.Widget;

public class TopicCompleter extends Composite {

	private TopicCache topicService;
	private TopicCompleteOracle oracle;
	private CompleteListener completeListener;
	private SuggestBox suggestBox;

	private int[] lnglat;
	private Topic parent;

	private Timer keyboardEnterTimer;

	public TopicCompleter(TopicCache topicService) {
		super();
		this.topicService = topicService;
		if (oracle == null) {
			oracle = new TopicCompleteOracle(topicService);
		}
		suggestBox = new SuggestBox(oracle);

		initWidget(suggestBox);
	}

	public TopicCompleter(TopicCache topicCache, Topic parent, int[] lnglat) {
		this(topicCache);

		this.parent = parent;

		this.lnglat = lnglat;
	}


	/**
	 * Convenience method to use our TopicService.
	 * 
	 * @param completeText
	 * @param callback
	 */
	public void getTopicIdentForNameOrCreateNew(String completeText, AsyncCallback callback) {
		if (parent == null) {
			topicService.createNewIfNonExistent(completeText, callback);
		} else {
			topicService.createNewIfNonExistent(completeText, parent, lnglat, callback);
		}
	}

	public void setCompleteListener(CompleteListener completeListener) {
		this.completeListener = completeListener;
		suggestBox.addEventHandler(new SuggestionHandler() {

			public void onSuggestionSelected(SuggestionEvent event) {
				System.out.println("On Suggestion Selected! "
						+ event.getSelectedSuggestion().getReplacementString());

				// Important, this prevents duplications
				if (keyboardEnterTimer != null) {
					keyboardEnterTimer.cancel();
				}

				complete(event.getSelectedSuggestion().getReplacementString());
			}
		});


		suggestBox.addKeyboardListener(new KeyboardListenerAdapter() {
			// @Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if (keyCode == KEY_ENTER) {

					keyboardEnterTimer = new Timer() {
						// @Override
						public void run() {
							complete(suggestBox.getText());
						}
					};
					keyboardEnterTimer.schedule(400);

				}
			}
		});

	}

	/**
	 * public so we can call this at any time
	 */
	public void complete() {
		complete(suggestBox.getText());
	}

	/**
	 * Careful to prevent dupes, one from enter key keyboard listener, one from the enter key
	 * selecting a suggestion. We need the keyboard listener because we want the enter key to add
	 * the current text when there's no suggestion.
	 * 
	 * @param completeStr
	 */
	private void complete(final String completeStr) {

		System.out.println("TopicCompleter:" + completeStr + " ");

		getTopicIdentForNameOrCreateNew(completeStr, new EZCallback() {
			public void onSuccess(Object result) {
				completeListener.completed((TopicIdentifier) result);
			}
		});


	}

	public void setText(String string) {
		suggestBox.setText(string);
	}

	public String getText() {
		return suggestBox.getText();
	}

	public void setFocus(final boolean b) {
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				suggestBox.setFocus(b);
			}
		});

	}
}
