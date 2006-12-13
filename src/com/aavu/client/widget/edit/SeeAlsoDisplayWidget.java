package com.aavu.client.widget.edit;

import java.util.Iterator;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Topic;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.VerticalPanel;



public class SeeAlsoDisplayWidget extends VerticalPanel {

	private int size = 0;

	public SeeAlsoDisplayWidget(Topic top){
		
		Association assoc = top.getSeeAlsoAssociation();
		
		for (Iterator iter = assoc.getMembers().iterator(); iter.hasNext();) {
			Topic link = (Topic) iter.next();
			add(new TopicLink(link));			
			size ++;
		}		
	}

	public int getSize() {
		return size;
	}
}
