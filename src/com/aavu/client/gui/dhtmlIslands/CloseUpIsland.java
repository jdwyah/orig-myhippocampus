package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.domain.FullTopicIdentifier;
import com.aavu.client.domain.TagInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class CloseUpIsland extends AbstractIsland implements DragFinishedListener {

	private int height;
	private FullTopicIdentifier[] topics;
	private DragHandler dragHandler;

	public CloseUpIsland(TagInfo stat, FullTopicIdentifier[] topics, OceanDHTMLImpl ocean, IslandRepresentation repr) {
		super();
		this.repr = repr;	
		this.tagStat = stat;
		this.topics = topics;

		theSize = tagStat.getNumberOfTopics()+1;

		setStyleName("H-Island-Closeup");

		dragHandler = new DragHandler(this);

		scale = 5;

		setTypeAndSpacing();			


		drawJustTheIsland();

		doPositioning();

	}

	private void drawJustTheIsland() {

		clear();

		//need to re-loop after all the min/maxes are set
		//				
		//doIslandType(0);		
		doIslandType(1);
		doIslandType(2);	

		int x = 100;
		int y = 100;
		for (int i = 0; i < topics.length; i++) {
			FullTopicIdentifier fti = topics[i];

			x = 30;// fti.getLongitude() == -1 ? x + 40 : fti.getLongitude();
			y = 30;//fti.getLatitude() == -1 ? y + 40: fti.getLatitude();

			DraggableLabel l = new DraggableLabel(fti.getTopicTitle(),.5,.5);

			dragHandler.add(l,this);

			System.out.println("add label "+x+" "+y);
			add(l,x,y);			

		}
	}


	private void doPositioning() {			

		top = 0;  
		left = 0;									

		top -= gridToRelativeY(repr.min_y,my_spacing);
		left -= gridToRelativeX(repr.min_x,my_spacing);	

		/*
		 * TODO clean this crud up. How do we size this DIV dynamically? Or take another look
		 * at putting these elements in another div.. but then we need to sort out drag-your-buddy system.
		 */
		int width = (repr.max_x + 1 - repr.min_x) * my_spacing  + img_size - my_spacing;
		height = (repr.max_y + 1 - repr.min_y) * my_spacing  + img_size - my_spacing;

		int predicted = getPredictedBannerWidth();
		System.out.println("Predicted Width "+predicted);
		if(predicted > width){
			DOM.setStyleAttribute(getElement(), "width", predicted+"px");	
		}else{
			DOM.setStyleAttribute(getElement(), "width", width+"px");	
		}
		DOM.setStyleAttribute(getElement(), "height", height+"px");

		System.out.println("Island width: "+width+" height "+height);
	}

	private int getPredictedBannerWidth() {

		return 0;
	}

	


	public void dragFinished(Widget dragging) {
		
		DraggableLabel label = (DraggableLabel) dragging;
		
		System.out.println("finished dragging "+label.getText());
		
	}

}
