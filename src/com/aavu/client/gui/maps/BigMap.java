package com.aavu.client.gui.maps;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * Show all selected tag's map meta info.
 * 
 * @author Jeff Dwyer
 *
 */
public class BigMap extends Composite {

	public BigMap(Manager manager, Map tagToIdentifierMap) {
		
	for (Iterator iter = tagToIdentifierMap.entrySet().iterator(); iter.hasNext();) {
			
			Entry entry = (Entry) iter.next();
			
			FullTopicIdentifier[] topics = (FullTopicIdentifier[]) entry.getValue();


			for (int i = 0; i < topics.length; i++) {
				
				
			}
		}
		
		initWidget(new Label("map goes here"));
	}

}
