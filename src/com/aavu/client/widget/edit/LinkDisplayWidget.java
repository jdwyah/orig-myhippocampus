package com.aavu.client.widget.edit;

import java.util.Iterator;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.URI;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.ExternalLink;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LinkDisplayWidget extends VerticalPanel {
	
	private int size = 0;
	
	
	public LinkDisplayWidget(SaveNeededListener saveNeeded) {
		addStyleName("H-LinkDisplay");
		
//		System.out.println("OCCUR: "+topic.getOccurences().size());
//		System.out.println(topic.toPrettyString());
		
//		EnterInfoButton enterInfoButton = new EnterInfoButton();		
//		enterInfoButton.addClickListener(new ClickListener(){
//			public void onClick(Widget sender){
//				add(textBox.getText());
//			}
//		});

	}
	public void load(Topic topic){
		clear();
		size = 0;
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
