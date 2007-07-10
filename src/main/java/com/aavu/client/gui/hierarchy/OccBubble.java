package com.aavu.client.gui.hierarchy;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.service.Manager;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class OccBubble extends AbstractBubble implements Bubble, ClickListener {

	private TopicOccurrenceConnector owl;

	public OccBubble(TopicOccurrenceConnector owl,HierarchyDisplay display){
		
		super(owl.getLongitude(),
				owl.getLatitude(),
				owl.getOccurrence().getTitle(),
				new Image(ImageHolder.getImgLoc("hierarchy/")+"ball_green.png"),
				display);
		
		this.owl = owl;
		
		
		
		addClickListener(this);
		
		//addMouseListener(new OccDisplayListener(owl.getOccurrence()));
	}

	//@Override

	public TopicIdentifier getIdentifier() {		
		return owl.getOccurrence().getIdentifier();
	}

	public void onClick(Widget sender) {
		System.out.println("OccBubble onClick");
		getDisplay().getManager().editOccurrence(owl.getOccurrence());
	}

	public void receivedDrop(Bubble bubble) {
		// TODO Auto-generated method stub

	}

	protected void saveLocation() {
		
		getDisplay().getManager().getTopicCache().saveOccLocationA(getDisplay().getCurrentRoot().getId(), owl.getOccurrence().getId(), getTop(), getLeft(), 
				new StdAsyncCallback("Save Occurrence LatLong"){});
	}

	

}
