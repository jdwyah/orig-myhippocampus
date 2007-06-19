package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.gui.ocean.dhtmlIslands.IslandBanner;
import com.aavu.client.gui.ocean.dhtmlIslands.RemembersPosition;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TopicBubble extends FocusPanel implements RemembersPosition {
	
	private static final int MIN_HEIGHT = 15;


	
	private int left;
	private int top;
	private Image image;
	private AbsolutePanel mainPanel;
	
	private int unscaledWidth;
	private int unscaledHeight;
	private double scale;
	private ViewPanel display;
	private IslandBanner banner;

	
	public TopicBubble(FullTopicIdentifier fti,ViewPanel display) {
		left = fti.getLatitudeOnIsland();
		top = fti.getLongitudeOnIsland();
		this.display = display;
		
		unscaledWidth = 50;
		unscaledHeight = 50; 
		
		mainPanel = new AbsolutePanel();
		mainPanel.setPixelSize(unscaledWidth,unscaledHeight);
		
		image = new Image(ImageHolder.getImgLoc("hierarchy/")+"ball_aqua.png");		
		image.setPixelSize(unscaledWidth, unscaledHeight);
		
		
		banner = new IslandBanner(fti.getTopicTitle(),5);
		
		mainPanel.add(image,0,0);
		mainPanel.add(banner,0,0);
		
		setWidget(mainPanel);		
		
	}

	public Widget getWidget(){
		return this;
	}
	
	public int getLeft() {		
		return left;
	}

	public int getTop() {
		return top;
	}

	public void zoomToScale(double currentScale) {
		
		scale = currentScale;
		
		Widget minimumWidget = banner.setToZoom(currentScale);
		
		
					
		image.setPixelSize((int)(unscaledWidth * currentScale),(int)( unscaledHeight * currentScale));
		
		
		
		int correctedWidth = (minimumWidget.getOffsetWidth() > (int)(unscaledWidth * currentScale)) ? minimumWidget.getOffsetWidth() : (int)(unscaledWidth * currentScale);
		
		int correctedHeight = ((int)( unscaledHeight * currentScale) < MIN_HEIGHT) ? MIN_HEIGHT : (int)( unscaledHeight * currentScale);
		
		//System.out.println("w "+correctedWidth+" h "+correctedHeight);
		
		mainPanel.setPixelSize(correctedWidth,correctedHeight);
		
	}

	public boolean possibleMoveOccurred() {
		System.out.println("possible move occurred");
		
		int absLeft = getAbsoluteLeft();
    	int absTop = getAbsoluteTop();
    	int oceanLeft = display.getBackX();
    	int oceanTop = display.getBackY();
    	if(absLeft != left + oceanLeft
    			||
    			absTop != top + oceanTop){
    		int newLeft = absLeft - oceanLeft;
    		int newTop = absTop - oceanTop;
    		System.out.println("\nMove DETECTED!!!!!!!!!!!! "+" Scale "+scale+" newLeft "+newLeft+" newTop "+newTop+" "+oceanLeft+" "+oceanTop);
    		//ocean.islandMoved(tagStat.getTagId(), newLeft, newTop);
    		left = (int) (newLeft / scale);
    		top = (int) (newTop / scale);
    		
    		  
    		//tagStat.setLongitude(left);
    		//tagStat.setLatitude(top);
    		
    		return true;
    	}else{
    		return false;
    	}
	}

}
