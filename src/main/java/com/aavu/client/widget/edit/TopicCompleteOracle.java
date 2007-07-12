package com.aavu.client.widget.edit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.EZCallback;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.autocompletion.SuggestBoxExt;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SuggestOracle;

public class TopicCompleteOracle extends SuggestOracle {

	protected class TopicSuggestion implements Suggestion {
		private final TopicIdentifier value;
		private String query;

		public TopicSuggestion(TopicIdentifier topic, String query) {
			this.value = topic;
			this.query = query;
		}

		public String getDisplayString() {
			return highlight(value.getTopicTitle(), query);
			// return value.getTopicTitle();
		}

		/**
		 * odd. can't return the TI, since .toString() is called on it and that is put in the box.
		 * worse, there's no way to call suggestBox, getSelectedValue() Just return the string and
		 * use the old creataeOrNew() for now
		 */
		public Object getValue() {
			return value.getTopicTitle();
		}
	}

	private TopicCache topicCache;
	private SuggestBoxExt box;
	private CompleteListener completeListener;

	public TopicCompleteOracle(TopicCache topicCache) {
		this.topicCache = topicCache;
	}


	// @Override
	public void requestSuggestions(final Request request, final Callback callback) {

		// computeItemsFor(request.getQuery(), request.getLimit());

		topicCache.match(request.getQuery(), new EZCallback() {

			public void onSuccess(Object result) {
				List suggestions = new ArrayList();

				for (Iterator iter = ((List) result).iterator(); iter.hasNext();) {
					TopicIdentifier ti = (TopicIdentifier) iter.next();
					suggestions.add(new TopicSuggestion(ti, request.getQuery()));
				}
				callback.onSuggestionsReady(request, new Response(suggestions));
			}
		});
	}


	// @Override
	public boolean isDisplayStringHTML() {
		return true;
	}

	private static HTML convertMe = new HTML();
	private static final char WHITESPACE_CHAR = ' ';

	private String escapeText(String escapeMe) {
		convertMe.setText(escapeMe);
		String escaped = convertMe.getHTML();
		return escaped;
	}

	/**
	 * Simpler than the Google MultiWordSuggest highlighter in that it will only highlight the first
	 * occurrence
	 * 
	 * @param candidate
	 * @param query
	 * @return
	 */
	private String highlight(String candidate, String query) {

		int index = 0;
		int cursor = 0;

		// Create strong search string.
		StringBuffer accum = new StringBuffer();

		query = query.toLowerCase();

		index = candidate.toLowerCase().indexOf(query, index);

		if (index == -1) {
			accum.append(escapeText(candidate));
		} else {
			int endIndex = index + query.length();
			String part1 = escapeText(candidate.substring(cursor, index));
			String part2 = escapeText(candidate.substring(index, endIndex));
			cursor = endIndex;
			accum.append(part1).append("<strong>").append(part2).append("</strong>");
		}

		// Finish creating the formatted string.
		String end = candidate.substring(cursor);
		accum.append(escapeText(end));

		return accum.toString();
	}
}
