package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.ext.DraggableLabel;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Widget;

public class DraggableTopicLabel extends DraggableLabel {

	private FullTopicIdentifier fti;
	private Island island;

	public DraggableTopicLabel(FullTopicIdentifier fti,Island island) {
		super(fti.getTopicTitle(),fti.getLongitudeOnIsland(),fti.getLatitudeOnIsland());
		this.fti = fti;
		this.island = island;
	}

	public long getTopicId() {
		return fti.getTopicID();
	}

	public void onClick(Widget sender) {

		System.out.println("clicked");

		island.topicClicked(fti);
		
	}
	
}
