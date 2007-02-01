package com.aavu.client.gui;

import com.aavu.client.domain.Entry;
import com.aavu.client.widget.edit.TopicWidget;
import com.aavu.client.wiki.TextDisplay;

public class EntryPreview extends TopicWidget {

	private static final int SIZE = 120;

	//@Override
	public void setText(Entry entry) {		
		
		textPanel.clear();						
		
		System.out.println("entry "+entry.getData());
		
		//TODO make sure we don't cut off in the middle of an HTML tag
		if(entry.getData() != null && entry.getData().length() > SIZE){
			String str = entry.getData().substring(0,SIZE)+"</body>";
			System.out.println(str);
			textPanel.add(new TextDisplay(str));
		}else{
			textPanel.add(new TextDisplay(entry.getData()));
		}		
		
		System.out.println("textPanel "+textPanel);
	}

	

	
}
