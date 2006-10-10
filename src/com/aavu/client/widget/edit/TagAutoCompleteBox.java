package com.aavu.client.widget.edit;

import com.aavu.client.service.cache.TagCache;
import com.aavu.client.widget.autocompletion.RemoteTAGAutoCompletionItems;

public class TagAutoCompleteBox extends AutoCompleteTextBoxWithCompleteCallback {

	public TagAutoCompleteBox(TagCache cache){
		this(null,cache);
	}
	public TagAutoCompleteBox(CompleteListener listener,TagCache cache){
		super();
		addListener(listener);
		setCompletionItems(new RemoteTAGAutoCompletionItems(cache));		
	}
	



}
