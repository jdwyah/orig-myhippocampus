package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.gui.ocean.dhtmlIslands.IslandBanner;
import com.aavu.client.gui.ocean.dhtmlIslands.RemembersPosition;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class TopicBubble extends FocusPanel implements RemembersPosition, ClickListener {
	
	private static final int MIN_HEIGHT = 15;

	
	private int left;
	private int top;
	private Image image;
	private AbsolutePanel mainPanel;
	
	private int unscaledWidth;
	private int unscaledHeight;

	private HierarchyDisplay display;
	private IslandBanner banner;



	private FullTopicIdentifier fti;



	private DropController dropController;

	
	public TopicBubble(FullTopicIdentifier fti,HierarchyDisplay display) {
		left = fti.getLongitudeOnIsland();
		top = fti.getLatitudeOnIsland();
		
		this.display = display;
		this.fti = fti;
		
		
		unscaledWidth = 50;
		unscaledHeight = 50; 
		
		mainPanel = new AbsolutePanel();
		mainPanel.setPixelSize(unscaledWidth,unscaledHeight);
		
		image = new Image(ImageHolder.getImgLoc("hierarchy/")+"ball_white.png");		
		image.setPixelSize(unscaledWidth, unscaledHeight);
		
		System.out.println("TopicBubble left "+left+" top "+top);
		
		banner = new IslandBanner(fti.getTopicTitle(),5);
		
		mainPanel.add(image,0,0);
		mainPanel.add(banner,0,0);
		
		setWidget(mainPanel);		
		
		addClickListener(this);
		
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
		
		
					
		image.setPixelSize((int)(unscaledWidth * currentScale),(int)( unscaledHeight * currentScale));
		
		
		Widget minimumWidget = banner.setToZoom(currentScale);
		
		int correctedWidth = (minimumWidget.getOffsetWidth() > (int)(unscaledWidth * currentScale)) ? minimumWidget.getOffsetWidth() : (int)(unscaledWidth * currentScale);
		
		int correctedHeight = ((int)( unscaledHeight * currentScale) < minimumWidget.getOffsetHeight()) ? minimumWidget.getOffsetHeight() : (int)( unscaledHeight * currentScale);
		
		//System.out.println("w "+correctedWidth+" h "+correctedHeight);
		
		mainPanel.setPixelSize(correctedWidth,correctedHeight);
		
	}

	public boolean possibleMoveOccurred(double currentScale) {
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
    		System.out.println("\nMove DETECTED!!!!!!!!!!!! "+" Scale "+currentScale+" newLeft "+newLeft+" newTop "+newTop+" "+oceanLeft+" "+oceanTop);
    		//ocean.islandMoved(tagStat.getTagId(), newLeft, newTop);
    		left = (int) (newLeft / currentScale);
    		top = (int) (newTop / currentScale);
    		
    		  
    		//tagStat.setLongitude(left);
    		//tagStat.setLatitude(top);
    		
    		return true;
    	}else{
    		return false;
    	}
	}

	public void receivedDrop(Widget draggable) {
		TopicBubble received = (TopicBubble) draggable;
		
		display.processDrop(this,received);
		
		
	}

	public FullTopicIdentifier getFTI() {
		return fti;
	}

	public void setDropController(DropController dropController) {
		this.dropController = dropController;
	}

	public DropController getDropController() {
		return dropController;
	}

	public void onClick(Widget sender) {
		
		display.navigateTo(getFTI());
	}

	/**
	 * NOTE: just wrapping the FTI. Not a fully loaded topic.
	 * @return
	 */
	public Topic getTopic() {
		return new Topic(getFTI());
	}

	public void grow() {
		// TODO Auto-generated method stub
		
	}

	public Widget getDropTarget() {
		return image;
	}

}
