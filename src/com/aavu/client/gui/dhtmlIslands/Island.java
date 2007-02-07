package com.aavu.client.gui.dhtmlIslands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.DraggableLabel;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class Island extends AbstractIsland implements ClickListener, SourcesMouseEvents, RemembersPosition, DragFinishedListener{
	
		
	private static final int MIN_HEIGHT = 15;


	/**
	 * All new or (0,0) islands will show up at (move_me,move_me) + static 30 * number of islands
	 */
	private static int move_me = 400;
	
			
	private MouseListenerCollection mouseListeners;	
	

	private OceanDHTMLImpl ocean;
	private Manager manager;
	
	private IslandBanner banner;

	private int height;

	private DragHandler dragHandler;

	private boolean haveShownTopics = false;


	private int width;

	private List topicLabelList;


	private boolean topicsInvisible;

	private DraggableTopicLabel selectedTopic;
	
	public Island(TagInfo stat, OceanDHTMLImpl ocean, User user,Manager manager) {
		super();
		this.tagStat = stat;
		this.ocean = ocean;
		this.manager = manager;
		
		sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS );	    
			
		
		setStyleName("H-Island");
		
		listener = this;
		
		dragHandler = new DragHandler(this);
		
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

		doPositioning(banner.setToZoom(1));
	
		
		
		//position: absolute; left: 610px; top: 155px;
		//DOM.setStyleAttribute(getElement(), "position", "absolute"); 		
		
		
		//this code for lat long as 0-1 floats
//		int top = (int) (tagStat.getLatitude() * ocean.getLatitude()) - gridToRelativeY(min_y);
//		int left = (int) (tagStat.getLongitude() * ocean.getLongitude()) - gridToRelativeX(min_x);;
		
	
		//String s = tagStat.getLongitude()+" "+tagStat.getLatitude()+" "+left+" "+top;

				
		
		System.out.println(tagStat.getTagName()+" SIZE "+tagStat.getNumberOfTopics()+" minx "+repr.min_x+" max x "+repr.max_x+" miny "+repr.min_y+" maxy "+repr.max_y);
//		System.out.println("Island top: "+top+" left "+left);						
//		System.out.println("Island longi x "+tagStat.getLongitude());
//		System.out.println("Island lat y "+tagStat.getLatitude());
		
		
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
	private void doPositioning(int minWidth) {
	
		top = tagStat.getLatitude() - gridToRelativeY(repr.min_y,my_spacing); 
		left = tagStat.getLongitude() - gridToRelativeX(repr.min_x,my_spacing);
		
		/*
		 * TODO clean this crud up. How do we size this DIV dynamically? Or take another look
		 * at putting these elements in another div.. but then we need to sort out drag-your-buddy system.
		 */
		width = (repr.max_x - repr.min_x) * my_spacing  + img_size;
		height = (repr.max_y - repr.min_y) * my_spacing  + img_size;
		
		height = (height < MIN_HEIGHT) ? MIN_HEIGHT : height;
		
//		if(tagStat.getTagName().equals("Person")){
//			System.out.println("People");
//			System.out.println("width "+width+" Nx "+(repr.max_x + 1 - repr.min_x)+" Spacing "+my_spacing+" img_size "+img_size);
//			System.out.println("height "+height+" Ny "+(repr.max_y + 1 - repr.min_y)+" Spacing "+my_spacing+" img_size "+img_size);
//		}
//		if(tagStat.getTagName().equals("Coffee")){
//			System.out.println("Coffee");
//			System.out.println("width "+width+" Nx "+(repr.max_x + 1 - repr.min_x)+" Spacing "+my_spacing+" img_size "+img_size);
//			System.out.println("height "+height+" Ny "+(repr.max_y + 1 - repr.min_y)+" Spacing "+my_spacing+" img_size "+img_size);
//		}
		
		
		
		//int predicted = getPredictedBannerWidth();
		//System.out.println("Predicted Width "+predicted);
		if(minWidth > width){
			DOM.setStyleAttribute(getElement(), "width", minWidth+"px");	
		}else{
			DOM.setStyleAttribute(getElement(), "width", width+"px");	
		}
	
		DOM.setStyleAttribute(getElement(), "height", height+"px");
		
		//not working
		//banner.setWidth(width+"em");
		
		//System.out.println("Island width: "+width+" height "+height);
	}

	
	public void ensureWidthOf(int i){
		if(i > width){
			DOM.setStyleAttribute(getElement(), "width", i+"px");
		}
	}
	
	
	/*
	 * 114px == 'Entrepreneurship at font .8, 16 chars
	 * (114/.8) = 142
	 * 142/16 = 9
	 * 
	 *  (chars * 9) * fontSize
	 */
	private int getPredictedBannerWidth(){
		//System.out.println("FP "+tagStat.getTagName().length() * 9);
		//System.out.println("SP: "+banner.getFontFor(tagStat.getNumberOfTopics()));
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
		
		doPositioning(banner.setToZoom(scale));
		//System.out.println("set to left "+left+" top "+top);
		ocean.setWidgetPosition(this, left, top);		
	}

	
	public void onClick(Widget sender) {			
		
		System.out.println("island onClick");
		
		ocean.islandClicked(tagStat.getTopicIdentifier(),this);
				
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
	      case Event.ONCLICK: {
	    	  onClick(this);
//	        if (clickListeners != null)
//	          clickListeners.fireClick(this);
//	        break;
	      }
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
	}
//
//	    }
//	    /*
//	     * detecting move requires subtracting out the possible shift of the ocean 
//	     */
//	    if(wasMouseUp){
//	    	int absLeft = getAbsoluteLeft();
//	    	int absTop = getAbsoluteTop();
//	    	int oceanLeft = ocean.getBackX();
//	    	int oceanTop = ocean.getBackY();
//	    	if(absLeft != left + oceanLeft
//	    			||
//	    			absTop != top + oceanTop){
//	    		int newLeft = absLeft - oceanLeft;
//	    		int newTop = absTop - oceanTop;
//	    		//System.out.println("\n\n\n\nMove DETECTED!!!!!!!!!!!!");
//	    		ocean.islandMoved(tagStat.getTagId(), newLeft, newTop);
//	    		left = newLeft;
//	    		top = newTop;
//	    	}else{
//	    		/*
//	    		 * hmm...  
//	    		 * detecting here is good bc then we know if it was a drag, 
//	    		 * but the detection area is the whole island div.
//	    		 * Alternative #2 is listener on acres & banner, but then we need a 
//	    		 * new way to cancel if dragged. 
//	    		 */
//	    		onClick(this);
//	    	}
//	    	//System.out.println("Moved "+getAbsoluteLeft()+" "+left+" "+getAbsoluteTop()+" "+top);
//	    	//System.out.println("Ocean "+ocean.getAbsoluteLeft()+" top "+ocean.getAbsoluteTop()+" ");
//	    }
//	  }
	

	public void youveBeenDraggedSetYourLeftAndTop() {
		int absLeft = getAbsoluteLeft();
    	int absTop = getAbsoluteTop();
    	int oceanLeft = ocean.getBackX();
    	int oceanTop = ocean.getBackY();
    	if(absLeft != left + oceanLeft
    			||
    			absTop != top + oceanTop){
    		int newLeft = absLeft - oceanLeft;
    		int newTop = absTop - oceanTop;
    		System.out.println("\n\n\n\nMove DETECTED!!!!!!!!!!!! Scale "+scale+" newLeft "+newLeft+" newTop "+newTop+" "+oceanLeft+" "+oceanTop);
    		//ocean.islandMoved(tagStat.getTagId(), newLeft, newTop);
    		left = (int) (newLeft / scale);
    		top = (int) (newTop / scale);
    		
    		tagStat.setLongitude(left);
    		tagStat.setLatitude(top);    		
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


	public void zoomToScale(double currentScale) {
		scale = currentScale;
		setTypeAndSpacing();			
		
		for (Iterator iter = levels.keySet().iterator(); iter.hasNext();) {
			Level level = (Level) iter.next();
			level.setToScale(currentScale);
			
			//PointLocation loc = (PointLocation) levels.get(level);
			
			int corrected_x = gridToRelativeX(level.getX(),my_spacing);
			int corrected_y = gridToRelativeY(level.getY(),my_spacing);		

			setWidgetPosition(level, corrected_x, corrected_y);
		}
		

		int minWidth = banner.setToZoom(currentScale);

		doPositioning(minWidth);
		
		
		
		//banner.setText("X: "+left+" Y "+top+" * "+currentScale);
		
//		if(tagStat.getTagName().equals("Person")){
//			System.out.println("Person left: "+left+" top "+top);
//			System.out.println(" ("+winLeft+", "+winTop+")  ("+winRight+", "+winBottom+")");
//		}
				
		if(haveShownTopics && topicLabelList != null){
			for (Iterator iter = topicLabelList.iterator(); iter.hasNext();) {
				DraggableLabel label = (DraggableLabel) iter.next();
				
				int x = (int) (label.getXPct() * width);
				int y = (int) (label.getYPct() * height);
				
				setWidgetPosition(label, x,y);
				
			}
		}
	}

	public boolean isWithin(int winLeft, int winTop, int winRight, int winBottom){
		return (left > winLeft
		&&
		left < winRight
		&&
		top > winTop
		&&
		top < winBottom);
	}
	
	public void removeTopics(){
		if(!topicsInvisible){
			setTopicVisibility(false);
		}
	}
	private void setTopicVisibility(boolean visible){
		if(visible == topicsInvisible && topicLabelList != null){
			for (Iterator iter = topicLabelList.iterator(); iter.hasNext();) {
				DraggableTopicLabel label = (DraggableTopicLabel) iter.next();
				label.setVisible(visible);			
			}
			topicsInvisible = !visible;
		}
	}
	public void showTopics() {
		if(!haveShownTopics){
			
			//not exactly true, since the async hasn't suceeded yet,
			//but otherwise we might keep trying to add if more req's come in
			haveShownTopics = true;
			
			manager.getTopicCache().getTopicsWithTag(tagStat.getTagId(), new StdAsyncCallback(Manager.myConstants.tag_topicIsA()){
				public void onSuccess(Object result) {
					super.onSuccess(result);
					FullTopicIdentifier[] topics = (FullTopicIdentifier[]) result;

					System.out.println("Show Topics results "+topics.length);
					
					addTopicLabels(topics);				
				}	
				public void onFailure(Throwable caught) {
					super.onFailure(caught);
					haveShownTopics = false;				
				}	
			});
		}
		if(topicsInvisible){
			setTopicVisibility(true);
		}
	}
	private void addTopicLabels(FullTopicIdentifier[] topics) {
		int x = 0;
		int y = 0;

		if(topicLabelList == null){
			topicLabelList = new ArrayList();
		}
		topicLabelList.clear();

		
		for (int i = 0; i < topics.length; i++) {
			FullTopicIdentifier fti = topics[i];
			
			System.out.println("fti: "+fti);
			
			if(fti.getLongitudeOnIsland() < 0){
				//just try to space them out a bit if they don't have real values
				fti.setLongitudeOnIsland(.5);
			}
			if(fti.getLatitudeOnIsland() < 0){
				fti.setLatitudeOnIsland(.5);
			}

			x = (int) (fti.getLongitudeOnIsland() * width);
			y = (int) (fti.getLatitudeOnIsland() * height);

			DraggableTopicLabel l = new DraggableTopicLabel(fti,this);

			dragHandler.add(l,Island.this);

			System.out.println("add label "+fti.getTopicTitle()+" "+x+" "+y);
			add(l,x,y);
			
			topicLabelList.add(l);
		}
		haveShownTopics = true;
	}
	
	


	public void dragFinished(Widget dragging) {
		
		DraggableLabel label = (DraggableLabel) dragging;
		
		label.setXPct(getWidgetLeft(label) / (double)width);
		label.setYPct(getWidgetTop(label) / (double)height);
		
		
		System.out.println("finished dragging "+label.getText()+" "+label.getXPct()+" "+label.getYPct());
		
		topicMoved((DraggableTopicLabel) label);
	}
	
	private void topicMoved(DraggableTopicLabel label){

		manager.getTopicCache().saveTopicLocationA(tagStat.getTagId(),label.getTopicId(),label.getXPct(),label.getYPct(),
				new StdAsyncCallback(Manager.myConstants.save_async()));
	}


	public void topicClicked(TopicIdentifier ident,DraggableTopicLabel topic) {
		
		if(selectedTopic == topic){ 
			manager.bringUpChart(ident);
		}else{
			manager.showPreviews(ident.getTopicID());
		}
		if(selectedTopic != null){
			selectedTopic.setSelected(false);
		}
		selectedTopic = topic;
		selectedTopic.setSelected(true);
	}


	public void setSelected(boolean b) {
		selectedTopic = null;
		banner.setSelected(b);
	}


	/**
	 * change this islands banner to this text
	 * @param topicTitle
	 */
	public void setBannerTitle(String topicTitle) {
		
		banner.setText(topicTitle);
		
		//redraw
		zoomToScale(scale);
	}

	
}
