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

import com.aavu.client.service.cache.TagCache;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RemoteTAGAutoCompletionItems implements CompletionItems {

	private TagCache service;
	
	public RemoteTAGAutoCompletionItems(TagCache cache) {
		this.service = cache;
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
