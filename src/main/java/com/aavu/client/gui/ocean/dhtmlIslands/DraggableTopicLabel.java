package com.aavu.client.gui.ocean.dhtmlIslands;

import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.ext.DraggableLabel;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Widget;

public class DraggableTopicLabel extends DraggableLabel {

	private FullTopicIdentifier fti;
	private Island island;

	private static final String REG_STYLE = "H-TopicBanner";
	private static final String HOVER_STYLE = "H-TopicBanner-Hover";
	
	public DraggableTopicLabel(FullTopicIdentifier fti,Island island) {
		super(fti.getTopicTitle(),fti.getLongitudeOnIsland(),fti.getLatitudeOnIsland());
		this.fti = fti;
		this.island = island;
		
		setStyleName(REG_STYLE);		
		
		addMouseListener(new MouseListenerAdapter(){
			public void onMouseEnter(Widget sender) {				
				addStyleName(HOVER_STYLE);
			}
			public void onMouseLeave(Widget sender) {
				removeStyleName(HOVER_STYLE);
			}	
		});
	}

	public long getTopicId() {
		return fti.getTopicID();
	}

	public void onClick(Widget sender) {

		System.out.println("clicked");

		island.topicClicked(fti,this);
				
	}
	public void setSelected(boolean b){
		if(b){
			addStyleName(IslandBanner.BANNER_SELECTED);
		}else{
			removeStyleName(IslandBanner.BANNER_SELECTED);
		}
	}
	
}
