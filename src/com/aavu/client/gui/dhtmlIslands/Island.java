package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.User;
import com.aavu.client.gui.Ocean;
import com.aavu.client.util.MiddleSquarePseudoRandom;
import com.aavu.client.util.PsuedoRandom;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class Island extends AbsolutePanel implements ClickListener, SourcesMouseEvents, RemembersPosition{
	
		
	
	private static ImageHolder imgHolder = new ImageHolder();
	
	private static final int GRID = 100;

	/**
	 * All new or (0,0) islands will show up at (move_me,move_me) + static 30 * number of islands
	 */
	private static int move_me = 400;
	
	
	private int my_spacing;
		
	int max_x = 0;
	int min_x = Integer.MAX_VALUE;
	int max_y = 0;
	int min_y = Integer.MAX_VALUE;
	
	private MouseListenerCollection mouseListeners;
	
	private PsuedoRandom pr;
	
	int[][] used = new int[GRID][GRID];
	
	private TagInfo tagStat;
	private OceanDHTMLImpl ocean;
	private int top;
	private int left;
	private IslandBanner banner;

	int bigs = 0;
	int meds = 0;
	int smalls = 0;
	private int theSize;
	private int height;

	
	public Island(TagInfo stat, OceanDHTMLImpl ocean, User user) {
		super();
		this.tagStat = stat;
		this.ocean = ocean;
		
		
		long seed = user.getId()+tagStat.getTagId();
		pr = new MiddleSquarePseudoRandom(seed,4);
		
		sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS );	    
					
		setStyleName("H-Island");
		
		
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
		
		
		setType();			
		
		situate(ocean);		
		
		//incr ie add a (no topic here) spot		
		growInternal();	
				
		
		//position: absolute; left: 610px; top: 155px;
		//DOM.setStyleAttribute(getElement(), "position", "absolute"); 		
		
		doPositioning();
		
		
		
		//this code for lat long as 0-1 floats
//		int top = (int) (tagStat.getLatitude() * ocean.getLatitude()) - gridToRelativeY(min_y);
//		int left = (int) (tagStat.getLongitude() * ocean.getLongitude()) - gridToRelativeX(min_x);;
		
	
		//String s = tagStat.getLongitude()+" "+tagStat.getLatitude()+" "+left+" "+top;

				
		
		System.out.println(tagStat.getTagName()+" SIZE "+tagStat.getNumberOfTopics()+" minx "+min_x+" max x "+max_x+" miny "+min_y+" maxy "+max_y);
		System.out.println("Island top: "+top+" left "+left);		
				
		System.out.println("Island longi x "+tagStat.getLongitude());
		System.out.println("Island lat y "+tagStat.getLatitude());
		
		
	}


	/**
	 * 
	 *
	 */
	private void setType() {
		if(theSize >= 16){
			my_spacing = Type.SPACING_30;//NOTE not using 100's			
		}
		else if(theSize >= 4){
			my_spacing = Type.SPACING_30;
		}
		else{
			my_spacing = Type.SPACING_30;
		}
	}


	private void clearUseArray() {
		for (int i = 0; i < GRID; i++) {
			for (int j = 0; j < GRID; j++) {
				used[i][j] = -1;
			}
		}
	}
	

	/*
	 * Ocean will pull these from us when it adds us
	 */	
	private void doPositioning() {			
		top = tagStat.getLatitude()  - gridToRelativeY(min_y,my_spacing);
		left = tagStat.getLongitude()  - gridToRelativeX(min_x,my_spacing);		
		

		/*
		 * TODO clean this crud up. How do we size this DIV dynamically? Or take another look
		 * at putting these elements in another div.. but then we need to sort out drag-your-buddy system.
		 */
		int width = (max_x + 1 - min_x) * my_spacing + Type.MAX_SIZE - my_spacing;
		height = (max_y + 1 - min_y) * my_spacing + Type.MAX_SIZE - my_spacing;
		
		
		//Magic # 11 is ~Coffee at 1.4em which == 68px 
		if(tagStat.getTagName().length() * 11 > width){
			DOM.setStyleAttribute(getElement(), "width", tagStat.getTagName().length()+"em");	
		}else{
			DOM.setStyleAttribute(getElement(), "width", width+"px");	
		}
		DOM.setStyleAttribute(getElement(), "height", height+"px");
		
		//not working
		banner.setWidth(width+"em");
		
		System.out.println("Island width: "+width+" height "+height);
	}



	public int getLeft() {
		return left;
	}
	public int getTop() {
		return top;
	}



	private int gridToRelativeX(int gridValue,int spacing){		
		return (gridValue - min_x)* spacing;		
	}
	private int gridToRelativeY(int gridValue,int spacing){				
		return (gridValue - min_y)* spacing;		
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
		setType();
		growInternal();
		
		doPositioning();
		//System.out.println("set to left "+left+" top "+top);
		ocean.setWidgetPosition(this, left, top);		
	}

	/**
	 * 
	 * TODO will crash if things go beyond bounds of tag GRID
	 * 
	 * @param i
	 */
	private void growInternal() {

		clearUseArray();
		pr.reInit();	
		
		/*
		 * calculate here
		 */
//		bigs = theSize /16;
//		meds = (theSize %16)/4;
//		smalls = theSize %4;
		
		/*
		 * don't use 100's for now
		 */		
		bigs = 0;//theSize /16;
		meds = theSize / 4;
		smalls = theSize %4;
		
		bigs = 0;
		meds = 0;
		smalls = theSize;
		
		
		System.out.println("grow "+theSize+" "+bigs+" "+meds+" "+smalls);
		int x = GRID/2;			
		int y = GRID/2;
		
		for (int j = 1; j < bigs + meds + smalls + 1; j++) {
				
			
				
//			x = GRID/2;			
//			y = GRID/2;
				
			//TODO take this out.. only to prevent loops if Von Neuman PRG explodes
			int c = 0;
			while(-1 != used[x][y] && c < 200){
				
				if(tagStat.getTagName().equals("Person"))
				System.out.println("check "+x+" "+y+" c "+c+" used "+used[x][y]);
				c++;
				
				int dx = pr.nextInt(3) - 1;
				int dy = pr.nextInt(3) - 1;
				x += dx;
				y += dy;
				//System.out.println("sw: "+sw);
			}
			System.out.println("FOUND: "+x+" "+y+" "+j);
			used[x][y] = j;
			
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

		
		clear();
		add(banner,0,height/2);

		//need to re-loop after all the min/maxes are set
		//		
		doIslandType(0);
		doIslandType(1);
		doIslandType(2);
		
		
		
	}
	
	/**
	 * used[][] will be an array with value of the order in which we "found"
	 * this square. Idea is to have the big ones in the center, surrounded by med,
	 * then small. 
	 * 
	 * Would like to make it a bit smarter about laying out new little guys.
	 * 
	 * @param style
	 */
	private void doIslandType(int style) {
		int x;
		int count = 0;
		AcreSize acreSize = null;
		for (x = 0; x < GRID; x++) {
			for (int j = 0; j < GRID; j++) {
				if(used[x][j] > -1){
					if(used[x][j] > bigs + meds){
						acreSize = AcreSize.SIZE_30;												
					}else if(used[x][j] > bigs){
						acreSize = AcreSize.SIZE_60;												
					}
					else{
						acreSize = AcreSize.SIZE_100;												
					}
					System.out.println("used "+x+" "+j+" "+used[x][j]+" Acre: "+acreSize.getSize());
					count++;
					
					if(0 == style){
						addShadow(x,j,acreSize);
					}
					else if(1 == style){
						addAcre(x,j,acreSize);
					}
					else if(2 == style){
						addInner(x,j,acreSize);
					}
				}
			}
		}
	}
	
	
	/**
	 * takes values from -50 -> 50 (GRID/2)
	 * 
	 * gridToRelative using my_type || type? 
	 * 
	 */
	private void addAcre(int x, int y,AcreSize acreSize){
		
		int corrected_x = gridToRelativeX(x,my_spacing);
		int corrected_y = gridToRelativeY(y,my_spacing);		
		
		
		System.out.println("x "+x+" cx "+corrected_x);
		System.out.println("y "+y+" cy "+corrected_y);
//		

		add(new Acre(this,x,y,acreSize),corrected_x,corrected_y);
		
	}

	private void addShadow(int x, int y,AcreSize acreSize){

		int corrected_x = gridToRelativeX(x,my_spacing);
		int corrected_y = gridToRelativeY(y,my_spacing);		

		add(new Shadow(x,y,acreSize),corrected_x,corrected_y);
	}
	private void addInner(int x, int y,AcreSize acreSize){

		int corrected_x = gridToRelativeX(x,my_spacing);
		int corrected_y = gridToRelativeY(y,my_spacing);		

		add(new Inner(x,y,acreSize),corrected_x,corrected_y);
	}
	
	

	public void onClick(Widget sender) {			
		ocean.islandClicked(tagStat.getTagId());
	}
	
	private class Level extends AbsolutePanel {		

		public Level(ClickListener listener, int x, int y,AcreSize acreSize,String extension,String style){
									
			Image isle = imgHolder.getImage(acreSize,tagStat.getTagId(),x,y,extension);//new Image(OceanDHTMLImpl.IMG_LOC+"type"+type.prefix+"_"+(1+(x*y)%type.numImages)+"_"+extension+".png");
			if(listener != null){
				isle.addClickListener(listener);
			}
			isle.setStyleName(style);
			add(isle,0,0);
						
			DOM.setStyleAttribute(getElement(), "width", acreSize.getSize()+"px");
			DOM.setStyleAttribute(getElement(), "height", acreSize.getSize()+"px");

		}
	}
	private class Acre extends Level {
		public Acre(ClickListener listener,int x, int y,AcreSize acreSize){
			super(listener,x,y,acreSize,"I","Isle");			
		}
	}
	private class Shadow extends Level {
		public Shadow(int x, int y,AcreSize acreSize){
			super(null,x,y,acreSize,"S","Overlay");			
		}
	}
	private class Inner extends Level {
		public Inner(int x, int y,AcreSize acreSize){
			super(null,x,y,acreSize,"Inner","Overlay");			
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
	
}
