package com.aavu.client.gui;

import com.aavu.client.domain.Entry;
import com.aavu.client.widget.edit.TopicWidget;
import com.aavu.client.wiki.TextDisplay;

public class EntryPreview extends TopicWidget {

	//@Override
	public void setText(Entry entry) {		
		
		textPanel.clear();		
		
		//TODO make sure we don't cut off in the middle of an HTML tag
		if(entry.getData().length() > 40){
			textPanel.add(new TextDisplay(entry.getData().substring(0,40)));
		}else{
			textPanel.add(new TextDisplay(entry.getData()));
		}
		
	}

	

	
}
