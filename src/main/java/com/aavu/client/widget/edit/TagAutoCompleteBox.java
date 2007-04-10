package com.aavu.client.widget.edit;

import com.aavu.client.async.EZCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.service.cache.TagCache;
import com.aavu.client.widget.autocompletion.RemoteTAGAutoCompletionItems;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TagAutoCompleteBox extends AutoCompleteTextBoxWithCompleteCallback {

	private TagCache cache;

	public TagAutoCompleteBox(TagCache cache){
		this(null,cache);
	}
	public TagAutoCompleteBox(CompleteListener listener,TagCache cache){
		super();
		addListener(listener);
		setCompletionItems(new RemoteTAGAutoCompletionItems(cache));
		this.cache = cache;
	}

	//@Override
	protected void getTopicIdentForNameOrCreateNew(String completeText, final AsyncCallback callback) {		
		//need to convert Tag to TopicIdentifier
		cache.getTagAddIfNew(completeText, new EZCallback(){
			public void onSuccess(Object result) {
				Tag tag = (Tag) result;
				callback.onSuccess(tag.getIdentifier());
			}});	
	}
	



}
