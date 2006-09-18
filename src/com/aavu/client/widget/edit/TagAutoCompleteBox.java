package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.cache.TagCache;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.aavu.client.widget.autocompletion.AutoCompleteTextBox;
import com.aavu.client.widget.autocompletion.RemoteTAGAutoCompletionItems;
import com.aavu.client.widget.autocompletion.RemoteTopicAutoCompletionItems;

public class TagAutoCompleteBox extends AutoCompleteTextBox {

	private CompleteListener listener;

	public TagAutoCompleteBox(TagCache cache){
		this(null,cache);
	}
	public TagAutoCompleteBox(CompleteListener listener,TagCache cache){
		super();
		this.listener = listener;

		setCompletionItems(new RemoteTAGAutoCompletionItems(cache));		
	}

	protected void complete() {
		super.complete();
		System.out.println("In TagAutoCompleteBox's complete()");
		
		if(listener != null){
			listener.completed(this.getText());
		}
		
		this.setText("");
	}


}
