/*
 * This source code is under public domain.
 * You may use this source code as you wish, even distribute it
 * with different licenssing terms.
 * 
 * Contribution of bug fixes and new features would be appreciated.
 * 
 * Oliver Albers <oliveralbers@gmail.com>
 */ 

package com.aavu.client.widget.autocompletion;

import com.aavu.client.service.cache.TopicCache;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RemoteTopicAutoCompletionItems implements CompletionItems {

	private TopicCache service;

	
	public RemoteTopicAutoCompletionItems(TopicCache topicService) {
		this.service = topicService;
	}
	public void getCompletionItems(final String match, final
			MatchesRequiring widget) {

		AsyncCallback call = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			public void onSuccess(Object result) {
				widget.setMatches( (String[]) result);
				widget.onMatch( match );
			}

		};

		service.match( match, call);
	}      
} 
