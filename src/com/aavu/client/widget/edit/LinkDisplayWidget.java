package com.aavu.client.widget.edit;

import java.util.Iterator;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.URI;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.ExternalLink;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LinkDisplayWidget extends VerticalPanel {
	
	private int size = 0;

	public LinkDisplayWidget(Topic topic) {
		
		add(new HeaderLabel(Manager.myConstants.occurrences()));

		System.out.println("OCCUR: "+topic.getOccurences().size());
		System.out.println(topic.toPrettyString());
		
		for (Iterator iter = topic.getOccurences().iterator(); iter.hasNext();) {
			Occurrence occ = (Occurrence) iter.next();			
			if(occ instanceof URI){
				add(new ExternalLink(occ));
				size ++;
			}
		}

	}
	

	public int getSize() {
		return size;
	}

}
