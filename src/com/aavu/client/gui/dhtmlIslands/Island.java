package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.User;
import com.aavu.client.gui.Ocean;
import com.aavu.client.util.MiddleSquarePseudoRandom;
import com.aavu.client.util.PsuedoRandom;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class Island extends AbsolutePanel implements ClickListener, SourcesMouseEvents{

	private static final int IMG_WIDTH = 30;
	private static final int IMG_HEIGHT = 30;
	
	private static final int IMG_SPACING_W = 20;
	private static final int IMG_SPACING_H = 20;
		
	private static final int GRID = 100;
	
	int max_x = GRID/2;
	int min_x = GRID/2;
	int max_y = GRID/2;
	int min_y = GRID/2;	
	
	private MouseListenerCollection mouseListeners;
	
	private PsuedoRandom pr;
	
	boolean[][] used = new boolean[GRID][GRID];
	
	private TagInfo tagStat;
	private OceanDHTMLImpl ocean;
	private int top;
	private int left;


	
	public Island(TagInfo stat, OceanDHTMLImpl ocean, DragHandler d, User user) {
		super();
		this.tagStat = stat;
		this.ocean = ocean;
		
		for (int i = 0; i < GRID; i++) {
			for (int j = 0; j < GRID; j++) {
				used[i][j] = false;
			}
		}
		
		long seed = user.getId()+tagStat.getTagId();
		pr = new MiddleSquarePseudoRandom(seed,4);
		
		sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS );	    
		
		d.add(this);
			
		setStyleName("H-Island");
		
		situate(ocean);		
		
		//incr ie add a (no topic here) spot		
		grow(tagStat.getNumberOfTopics() + 1);	
		
		int width = (max_x + 1 - min_x) * IMG_SPACING_W + IMG_WIDTH - IMG_SPACING_W;
		int height = (max_y + 1 - min_y) * IMG_SPACING_H + IMG_HEIGHT - IMG_SPACING_H;
		
		DOM.setStyleAttribute(getElement(), "width", width+"px");
		DOM.setStyleAttribute(getElement(), "height", height+"px");
		
		
		//position: absolute; left: 610px; top: 155px;
		//DOM.setStyleAttribute(getElement(), "position", "absolute"); 		
		
		
		//this code for lat long as 0-1 floats
//		int top = (int) (tagStat.getLatitude() * ocean.getLatitude()) - gridToRelativeY(min_y);
//		int left = (int) (tagStat.getLongitude() * ocean.getLongitude()) - gridToRelativeX(min_x);;
		
		/*
		 * Ocean will pull these from us when it adds us
		 */		
		top = tagStat.getLatitude()  - gridToRelativeY(min_y);
		left = tagStat.getLongitude()  - gridToRelativeX(min_x);		
		
		
		//String s = tagStat.getLongitude()+" "+tagStat.getLatitude()+" "+left+" "+top;

		add(new IslandBanner(tagStat.getTagName()),0,0);
		 
		
		System.out.println(tagStat.getTagName()+" minx "+min_x+" max x "+max_x+" miny "+min_y+" maxy "+max_y);
		System.out.println("Island top: "+top+" left "+left);		
		System.out.println("Island width: "+width+" height "+height);		
		System.out.println("Island longi x "+tagStat.getLongitude());
		System.out.println("Island lat y "+tagStat.getLatitude());
		
		
	}
	


	public int getLeft() {
		return left;
	}
	public int getTop() {
		return top;
	}



	private int gridToRelativeX(int gridValue){		
		return (gridValue - min_x)* IMG_SPACING_W;		
	}
	private int gridToRelativeY(int gridValue){	
		return (gridValue - min_y)* IMG_SPACING_H;
	}



	private void situate(Ocean o) {
		System.out.println("Situate "+tagStat.getLongitude()+" "+tagStat.getLatitude());
		if(tagStat.getLatitude() < 1
				&&
				tagStat.getLongitude() < 1){
			
			System.out.println("Setting to random!!");
			tagStat.setLongitude((int)(pr.nextDouble() * o.getLongitude()));
			tagStat.setLatitude((int)(pr.nextDouble() * o.getLatitude()));
			
			
		//	tagStat.setLongitude(pr.nextInt(10)+o.getLongitude()/2);
		//	tagStat.setLatitude(pr.nextInt(10));//+o.getLatitude()/2);
			
		}
		if(tagStat.getLatitude() < -1 ){
			System.out.println("-----------------");
			System.out.println("lat was "+tagStat.getLatitude());
			tagStat.setLatitude(0);
		}
		if(tagStat.getLongitude() < -1 ){
			System.out.println("long was "+tagStat.getLongitude());
			tagStat.setLongitude(0);
		}

		
	}



	public void grow() {
		grow(1);
	}

	/**
	 * 
	 * TODO will crash if things go beyond bounds of tag GRID
	 * 
	 * @param i
	 */
	private void grow(int i) {

		for (int j = 0; j < i; j++) {
				
			int x = GRID/2;			
			int y = GRID/2;
			
			//TODO take this out.. only to prevent loops if Von Neuman PRG explodes
			int c = 0;
			while(true == used[x][y] && c < 200){
				//System.out.println("check "+x+" "+y+" c "+c+" used "+used[x][y]);
				c++;
				
				int dx = pr.nextInt(3) - 1;
				int dy = pr.nextInt(3) - 1;
				x += dx;
				y += dy;
				//System.out.println("sw: "+sw);
			}
	//		System.out.println("FOUND: "+x+" "+y);
			used[x][y] = true;
			
			//update BOUNDS
			if(x < min_x){
				min_x = x;
			}
			if(x > max_x){
				max_x = x;
			}
			if(y < min_y){
				min_y = y;
			}
			if(y > max_y){
				max_y = y;
			}
				
		}
		
		//need to re-loop after all the min/maxes are set
		//otherwise the gridToRelative will calc using the wrong min/max 
		for (int cc = 0; cc < GRID; cc++) {
			for (int j = 0; j < GRID; j++) {
				if(used[cc][j])
					addAcre(cc,j);
			}
		}
		
	}
	
	/**
	 * takes values from -50 -> 50 (GRID/2)
	 */
	private void addAcre(int x, int y){
		
		int corrected_x = gridToRelativeX(x);
		int corrected_y = gridToRelativeY(y);				
		
//		System.out.println("x "+x+" cx "+corrected_x);
//		System.out.println("y "+y+" cy "+corrected_y);
		
		add(new Acre(this,x,y),corrected_x,corrected_y);
	}

	

	public void onClick(Widget sender) {			
		ocean.islandClicked(tagStat.getTagId());
	}
	
	private class Acre extends AbsolutePanel {
		public Acre(ClickListener listener, int x, int y){
			
			Image isle = new Image("img\\earth"+(1+(x*y)%4)+".png");
			isle.addClickListener(listener);
			
			add(isle,0,0);
			
			DOM.setStyleAttribute(getElement(), "width", IMG_WIDTH+"px");
			DOM.setStyleAttribute(getElement(), "height", IMG_HEIGHT+"px");
			
//			
//			Image shadow = new Image("img\\earth"+(1+(x*y)%4)+"over.png");		
//			add(shadow,0,0);
//			shadow.addStyleName("Overlay");
//			
			
		}
	}

	public void addMouseListener(MouseListener listener) {
		if (mouseListeners == null)
			mouseListeners = new MouseListenerCollection();
		mouseListeners.add(listener);
	}

	public void removeMouseListener(MouseListener listener) {
		if (mouseListeners != null)
			mouseListeners.remove(listener);
	}
	public void onBrowserEvent(Event event) {
		boolean wasMouseUp = false;

	    switch (DOM.eventGetType(event)) {
//	      case Event.ONCLICK: {
//	        if (clickListeners != null)
//	          clickListeners.fireClick(this);
//	        break;
//	      }
	      case Event.ONMOUSEDOWN:
	      case Event.ONMOUSEUP:
	    	  wasMouseUp = true;
	      case Event.ONMOUSEMOVE:
	      case Event.ONMOUSEOVER:
	      case Event.ONMOUSEOUT: {
	        if (mouseListeners != null)
	          mouseListeners.fireMouseEvent(this, event);
	        break;
	      }
	     
	    }
	    /*
	     * detecting move requires subtracting out the @&**@#% shift of the ocean usually (8,8) in FF and (10,15) in IE7 
	     */
	    if(wasMouseUp){
	    	int absLeft = getAbsoluteLeft();
	    	int absTop = getAbsoluteTop();
	    	int oceanLeft = ocean.getAbsoluteLeft();
	    	int oceanTop = ocean.getAbsoluteTop();
	    	if(absLeft != left + oceanLeft
	    			||
	    			absTop != top + oceanTop){
	    		int newLeft = absLeft - oceanLeft;
	    		int newTop = absTop - oceanTop;
	    		//System.out.println("\n\n\n\nMove DETECTED!!!!!!!!!!!!");
	    		ocean.islandMoved(tagStat.getTagId(), newLeft, newTop);
	    		left = newLeft;
	    		top = newTop;
	    	}
	    	//System.out.println("Moved "+getAbsoluteLeft()+" "+left+" "+getAbsoluteTop()+" "+top);
	    	//System.out.println("Ocean "+ocean.getAbsoluteLeft()+" top "+ocean.getAbsoluteTop()+" ");
	    }
	  }
	
	private class IslandBanner extends Label{
		public IslandBanner(String text){
			super(text);
			setStyleName("H-IslandBanner");
			addClickListener(Island.this);
		}
	}
	
}
