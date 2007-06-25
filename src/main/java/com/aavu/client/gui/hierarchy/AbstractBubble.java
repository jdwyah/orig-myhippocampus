package com.aavu.client.gui.hierarchy;

import com.aavu.client.gui.ocean.dhtmlIslands.IslandBanner;
import com.aavu.client.util.Logger;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractBubble extends FocusPanel implements Bubble {

	
	private IslandBanner banner;
	private HierarchyDisplay display;
	private DropController dropController;
	private Image image;
	
	private int left;
	private AbsolutePanel mainPanel;

	private int top;
	private int unscaledHeight;

	private int unscaledWidth;
	private String title;
	
	public AbstractBubble(int longitude, int latitude, String title, Image image, HierarchyDisplay display) {
		this.left = longitude;
		this.top = latitude;
		this.display = display;
		this.title = title;

		unscaledWidth = 50;
		unscaledHeight = 50; 
		
		mainPanel = new AbsolutePanel();
		mainPanel.setPixelSize(unscaledWidth,unscaledHeight);
		
		this.image = image;		
		this.image.setPixelSize(unscaledWidth, unscaledHeight);
		
		//System.out.println("AbstractBubble left "+left+" top "+top);
		
		banner = new IslandBanner(title,5);
		
		mainPanel.add(image,0,0);
		mainPanel.add(banner,0,0);
		
		setWidget(mainPanel);				
		
	}

	public HierarchyDisplay getDisplay() {
		return display;
	}

	public DropController getDropController() {
		return dropController;
	}

	public Widget getDropTarget() {
		return image;
	}

	public FocusPanel getFocusPanel() {
		return this;
	}

	public int getLeft() {		
		return left;
	}
	public String getTitle(){
		return title;
	}
	public int getTop() {
		return top;
	}
	
	public Widget getWidget(){
		return this;
	}

	public void processDrag(double currentScale) {
		int absLeft = getAbsoluteLeft();
    	int absTop = getAbsoluteTop();
    	int oceanLeft = getDisplay().getBackX();
    	int oceanTop = getDisplay().getBackY();
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
    		
    		saveLocation();
    		
    		
			
    	}else{
    		Logger.debug("AbstractBubble no need to save drag");
    	}
	}

	protected abstract void saveLocation();


	public void setDropController(DropController dropController) {
		this.dropController = dropController;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public void zoomToScale(double currentScale) {
		
		
					
		image.setPixelSize((int)(unscaledWidth * currentScale),(int)( unscaledHeight * currentScale));
		
		
		Widget minimumWidget = banner.setToZoom(currentScale);
		
		int correctedWidth = (minimumWidget.getOffsetWidth() > (int)(unscaledWidth * currentScale)) ? minimumWidget.getOffsetWidth() : (int)(unscaledWidth * currentScale);
		
		int correctedHeight = ((int)( unscaledHeight * currentScale) < minimumWidget.getOffsetHeight()) ? minimumWidget.getOffsetHeight() : (int)( unscaledHeight * currentScale);
		
		//System.out.println("w "+correctedWidth+" h "+correctedHeight);
		
		mainPanel.setPixelSize(correctedWidth,correctedHeight);
		
	}
	
	

}
