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

import com.aavu.client.service.remote.TopicServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.core.client.GWT;

public class RemoteTopicAutoCompletionItems implements CompletionItems {

	private TopicServiceAsync service;

	public RemoteTopicAutoCompletionItems (String entryEndPoint) {
		service = (TopicServiceAsync)
		GWT.create(AutoCompletionService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + entryEndPoint);
	}
	
	public RemoteTopicAutoCompletionItems(TopicServiceAsync topicService) {
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
