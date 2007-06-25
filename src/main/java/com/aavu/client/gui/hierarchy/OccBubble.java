package com.aavu.client.gui.hierarchy;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class OccBubble extends AbstractBubble implements Bubble {

	private TopicOccurrenceConnector owl;

	public OccBubble(TopicOccurrenceConnector owl,HierarchyDisplay display){
		
		super(owl.getLongitude(),
				owl.getLatitude(),
				owl.getOccurrence().getTitle(),
				new Image(ImageHolder.getImgLoc("hierarchy/")+"ball_green.png"),
				display);
		
		this.owl = owl;
		
		
		addMouseListener(new OccDisplayListener(owl.getOccurrence()));
	}

	//@Override

	protected void saveLocation() {
		getDisplay().getManager().getTopicCache().saveOccLocationA(getDisplay().getCurrentRoot().getId(), owl.getOccurrence().getId(), getTop(), getLeft(), 
				new StdAsyncCallback("SaveLatLong"){});
	}

	public void receivedDrop(Bubble bubble) {
		// TODO Auto-generated method stub

	}



}
