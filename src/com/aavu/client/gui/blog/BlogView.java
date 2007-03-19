package com.aavu.client.gui.blog;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.widget.datepicker.DateFormatter;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * A way to see things that have been recently updated
 * 
 * @author Jeff Dwyer
 *
 */
public class BlogView extends Composite {

	private VerticalPanel mainPanel = new VerticalPanel();

	public BlogView(Manager manager, Map tagToIdentifierMap) {


		for (Iterator iter = tagToIdentifierMap.entrySet().iterator(); iter.hasNext();) {

			Entry entry = (Entry) iter.next();

			FullTopicIdentifier[] topics = (FullTopicIdentifier[]) entry.getValue();

			for (int i = 0; i < topics.length; i++) {				
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(new TopicLink(topics[i]));
				hp.add(new Label(""+topics[i].getCreated()));
				mainPanel.add(hp);
			}
		}
			
		
		initWidget(mainPanel);
		
	}
	
	
}
