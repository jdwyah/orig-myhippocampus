package com.aavu.client;

import com.aavu.client.domain.Topic;
import com.aavu.client.widget.autocompletion.AutoCompleteTextBox;
import com.aavu.client.widget.autocompletion.RemoteTAGAutoCompletionItems;
import com.aavu.client.widget.autocompletion.RemoteTopicAutoCompletionItems;

public class TagAutoCompleteBox extends AutoCompleteTextBox {

	private TagBoard parentBoard;

	private TagServiceAsync tagService;

	public TagAutoCompleteBox(TagBoard parentBoard,TagServiceAsync tagService){
		super();
		this.parentBoard = parentBoard;
		setTagServiceA(tagService);
		
		setCompletionItems(new RemoteTAGAutoCompletionItems(tagService));		
	}

	private void setTagServiceA(TagServiceAsync tagService2) {
		this.tagService = tagService2;		
	}

	protected void complete() {
		super.complete();
		System.out.println("In TagAutoCompleteBox's complete()");
		parentBoard.tagTopic(this.getText());
		this.setText("");
	}


}
