package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.domain.FullTopicIdentifier;
import com.aavu.client.domain.TagInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;

public class CloseUpIsland extends AbstractIsland {

	private int height;
	private FullTopicIdentifier[] topics;
	
	public CloseUpIsland(TagInfo stat, FullTopicIdentifier[] topics, OceanDHTMLImpl ocean, IslandRepresentation repr) {
		super();
		this.repr = repr;	
		this.tagStat = stat;
		this.topics = topics;
		
		theSize = tagStat.getNumberOfTopics()+1;
				
		setStyleName("H-Island-Closeup");
		
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
			
			x = fti.getLongitude() == -1 ? x + 40 : fti.getLongitude();
			y = fti.getLatitude() == -1 ? y + 40: fti.getLatitude();
			
			Label l = new Label(fti.getTopicTitle());
						
			add(l,x,y);			
			
		}
	}
	
	
	private void doPositioning() {			
		
		top = 100;  
		left = 100;									
		
		top -= gridToRelativeY(repr.min_y,my_spacing);
		left -= gridToRelativeX(repr.min_x,my_spacing);	

		/*
		 * TODO clean this crud up. How do we size this DIV dynamically? Or take another look
		 * at putting these elements in another div.. but then we need to sort out drag-your-buddy system.
		 */
		int width = (repr.max_x + 1 - repr.min_x) * my_spacing  + (Type.MAX_SIZE * scale) - my_spacing;
		height = (repr.max_y + 1 - repr.min_y) * my_spacing  + (Type.MAX_SIZE * scale) - my_spacing;
		
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
	
}
