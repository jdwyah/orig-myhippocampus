package com.aavu.client.widget.edit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.EZCallback;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.gadgets.ConnectionBoard;
import com.aavu.client.service.cache.TopicCache;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;

public class TopicCompleteOracle extends SuggestOracle {

	protected class TopicSuggestion implements Suggestion {
		private final TopicIdentifier value;

		public TopicSuggestion(TopicIdentifier topic){
			this.value = topic;
			
		}
		public String getDisplayString() {
			return value.getTopicTitle();
		}

		/**
		 * odd. can't return the TI, since .toString() is called on it and that is put
		 * in the box. worse, there's no way to call suggestBox, getSelectedValue()
		 * Just return the string and use the old creataeOrNew() for now
		 */
		public Object getValue() {
			return value.getTopicTitle();
		}
	}
	private TopicCache topicCache;

	public TopicCompleteOracle(TopicCache topicCache) {
		this.topicCache = topicCache;
	}

	/**
	 * Convenience method to use our TopicService.
	 * 
	 * @param completeText
	 * @param callback
	 */
	public void getTopicIdentForNameOrCreateNew(String completeText, AsyncCallback callback) {
		topicCache.getTopicIdentForNameOrCreateNew(completeText, callback);
	}

	//@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		
		//computeItemsFor(request.getQuery(), request.getLimit());
		
		topicCache.match(request.getQuery(), new EZCallback(){

			public void onSuccess(Object result) {
				List suggestions = new ArrayList();
				
				for (Iterator iter = ((List)result).iterator(); iter.hasNext();) {
					TopicIdentifier ti = (TopicIdentifier) iter.next();
					suggestions.add(new TopicSuggestion(ti));
				}				
				callback.onSuggestionsReady(request, new Response(suggestions));
			}});		
	}

	public void setCompleteListener(final SuggestBox box,final CompleteListener completeListener) {
		box.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender) {
				
				System.out.println("ONCHANGE "+box.getText());
//				getTopicIdentForNameOrCreateNew(box.getText(), new EZCallback(){
//					public void onSuccess(Object result) {
//						completeListener.completed((TopicIdentifier) result);
//					}});				
			}});		
	}


}
