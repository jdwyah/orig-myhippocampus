package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.User;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class Island extends AbstractIsland implements ClickListener, SourcesMouseEvents, RemembersPosition{
	
	
	
	/**
	 * All new or (0,0) islands will show up at (move_me,move_me) + static 30 * number of islands
	 */
	private static int move_me = 400;
	
			
	private MouseListenerCollection mouseListeners;	
	

	private OceanDHTMLImpl ocean;

	
	private IslandBanner banner;

	private int height;

	
	public Island(TagInfo stat, OceanDHTMLImpl ocean, User user) {
		super();
		this.tagStat = stat;
		this.ocean = ocean;
		
		
		sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS );	    
					
		setStyleName("H-Island");
		
		listener = this;
		
//		switch (pr.nextInt(4)) {
//		case 0:
//			banner = new IslandBanner("Island of "+tagStat.getTagName(),tagStat.getNumberOfTopics());	
//			break;
//		case 1:
//			banner = new IslandBanner(tagStat.getTagName()+" Isle",tagStat.getNumberOfTopics());	
//			break;
//		case 2:
//			banner = new IslandBanner("The Republic of "+tagStat.getTagName(),tagStat.getNumberOfTopics());	
//			break;
//		case 3:
//			banner = new IslandBanner(tagStat.getTagName(),tagStat.getNumberOfTopics());	
//			break;
//		default:
//			break;
//		}
		banner = new IslandBanner(tagStat.getTagName(),tagStat.getNumberOfTopics());
		//banner.addClickListener(this);
		theSize = tagStat.getNumberOfTopics()+1;
		
		
		setTypeAndSpacing();			
		
		situate(ocean);		
		
		repr = new IslandRepresentation(GRID,stat,user);
				
		
		scale = 1;
		
		drawInOcean();

		doPositioning();
	
		
		
		//position: absolute; left: 610px; top: 155px;
		//DOM.setStyleAttribute(getElement(), "position", "absolute"); 		
		
		
		//this code for lat long as 0-1 floats
//		int top = (int) (tagStat.getLatitude() * ocean.getLatitude()) - gridToRelativeY(min_y);
//		int left = (int) (tagStat.getLongitude() * ocean.getLongitude()) - gridToRelativeX(min_x);;
		
	
		//String s = tagStat.getLongitude()+" "+tagStat.getLatitude()+" "+left+" "+top;

				
		
		System.out.println(tagStat.getTagName()+" SIZE "+tagStat.getNumberOfTopics()+" minx "+repr.min_x+" max x "+repr.max_x+" miny "+repr.min_y+" maxy "+repr.max_y);
		System.out.println("Island top: "+top+" left "+left);		
				
		System.out.println("Island longi x "+tagStat.getLongitude());
		System.out.println("Island lat y "+tagStat.getLatitude());
		
		
	}


	private void drawInOcean() {

		clear();
		add(banner,0,height/2);

		//need to re-loop after all the min/maxes are set
		//				
		//doIslandType(0);		
		doIslandType(1);
		doIslandType(2);
				
	}
	


	

	/*
	 * Ocean will pull these from us when it adds us
	 */	
	private void doPositioning() {
	
		top = tagStat.getLatitude() - gridToRelativeY(repr.min_y,my_spacing); 
		left = tagStat.getLongitude() - gridToRelativeX(repr.min_x,my_spacing);
		
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
		
		//not working
		banner.setWidth(width+"em");
		
		System.out.println("Island width: "+width+" height "+height);
	}

	/*
	 * 114px == 'Entrepreneurship at font .8, 16 chars
	 * (114/.8) = 142
	 * 142/16 = 9
	 * 
	 *  (chars * 9) * fontSize
	 */
	private int getPredictedBannerWidth(){
		System.out.println("FP "+tagStat.getTagName().length() * 9);
		System.out.println("SP: "+banner.getFontFor(tagStat.getNumberOfTopics()));
		return (int) (tagStat.getTagName().length() * 9 * banner.getFontFor(tagStat.getNumberOfTopics()));
	}





	private void situate(OceanDHTMLImpl o) {
		System.out.println("Situate "+tagStat.getLongitude()+" "+tagStat.getLatitude());
		if(tagStat.getLatitude() == 0
				&&
				tagStat.getLongitude() == 0){
			
			
			System.out.println("Setting to random!!");
			//avoid putting it on the edge
			
//			int longi = (int) (120 + pr.nextDouble() * 800);
//			int lati = (int) (120 + pr.nextDouble() * 600);
			int longi = -o.getBackX() + move_me;
			int lati = -o.getBackY() + move_me ;
			move_me += 30;
			tagStat.setLongitude(longi);
			tagStat.setLatitude(lati);
						
		}
		
		
	}



	public void grow() {
		theSize++;
		setTypeAndSpacing();
		repr.growByOne();
		
		doPositioning();
		//System.out.println("set to left "+left+" top "+top);
		ocean.setWidgetPosition(this, left, top);		
	}

	
	public void onClick(Widget sender) {			
		ocean.islandClicked(tagStat.getTagId());
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
	    case Event.ONMOUSEUP:
	    	wasMouseUp = true;
	    case Event.ONMOUSEDOWN:
	    case Event.ONMOUSEMOVE:
	    case Event.ONMOUSEOVER:
	    case Event.ONMOUSEOUT: {
	    	if (mouseListeners != null)
	    		mouseListeners.fireMouseEvent(this, event);
	    	break;
	    }

	    }
	    /*
	     * detecting move requires subtracting out the possible shift of the ocean 
	     */
	    if(wasMouseUp){
	    	int absLeft = getAbsoluteLeft();
	    	int absTop = getAbsoluteTop();
	    	int oceanLeft = ocean.getBackX();
	    	int oceanTop = ocean.getBackY();
	    	if(absLeft != left + oceanLeft
	    			||
	    			absTop != top + oceanTop){
	    		int newLeft = absLeft - oceanLeft;
	    		int newTop = absTop - oceanTop;
	    		//System.out.println("\n\n\n\nMove DETECTED!!!!!!!!!!!!");
	    		ocean.islandMoved(tagStat.getTagId(), newLeft, newTop);
	    		left = newLeft;
	    		top = newTop;
	    	}else{
	    		/*
	    		 * hmm...  
	    		 * detecting here is good bc then we know if it was a drag, 
	    		 * but the detection area is the whole island div.
	    		 * Alternative #2 is listener on acres & banner, but then we need a 
	    		 * new way to cancel if dragged. 
	    		 */
	    		onClick(this);
	    	}
	    	//System.out.println("Moved "+getAbsoluteLeft()+" "+left+" "+getAbsoluteTop()+" "+top);
	    	//System.out.println("Ocean "+ocean.getAbsoluteLeft()+" top "+ocean.getAbsoluteTop()+" ");
	    }
	  }
	

	
	
	public Widget getWidget() {		
	
		
		return this;
	}

	
	public IslandRepresentation getRepr() {
		return repr;
	}
	public TagInfo getStat() {
		return tagStat;
	}
	
}
