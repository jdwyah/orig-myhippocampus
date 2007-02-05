package com.aavu.client.gui.dhtmlIslands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gwtwidgets.client.ui.PNGImage;
import org.gwtwidgets.client.ui.ProgressBar;
import org.gwtwidgets.client.wrap.EffectOption;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.Ocean;
import com.aavu.client.gui.ext.GUIEffects;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.WheelListener;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class OceanDHTMLImpl extends AbsolutePanel implements Ocean, MouseListener, DragFinishedListener, WheelListener {

	static final String IMG_LOC = "img/simplicity/";
	//static final String IMG_LOC = "img/oldmapStyle/";

	private static final int SHOW_TOPICS_AT_ZOOM = 3;

	private Manager manager;

	private Map islands = new HashMap();	
	private List objects = new ArrayList();

	private DragHandler dragHandler;

	private int backY = 0;
	private int backX = 0;
	private int curbackX = 0;
	private int curbackY = 0;

	private int dragStartX;

	private int dragStartY;

	private boolean dragging;

	private Panel rightCloud;



	private Panel leftCloud;

	private boolean focussed = false;

	private CloseUpIsland closeUp;

	private EventBackdrop focusBackdrop;

	private double currentScale = 1;

	private int curMouseX;

	private int curMouseY;

	private Island selectedIsland;
	
	private PopupWindow progressWindow;	


	public OceanDHTMLImpl(Manager manager) {
		super();
		this.manager = manager;

		setStyleName("H-Ocean");

		dragHandler = new DragHandler(this);

		//sinkEvents(Event.ONSCROLL);

		//Decorations that will be obcured by the focus panel
		//
		decorate();

		clouds();

		/*
		 * override the AbsolutePanel position: relative
		 * otherwise we got a left: 8px; top: 8px;
		 */		
		DOM.setStyleAttribute(getElement(), "position", "absolute");	
		DOM.setStyleAttribute(getElement(), "backgroundImage","url("+IMG_LOC+"ocean.png)");
		//url("../img/bluecheck-bullet-14.gif");
	}

	private void clouds() {
		leftCloud = new SimplePanel();
		PNGImage lc = new PNGImage(Manager.myConstants.clouds_src(),120,120);		
		lc.setStyleName("H-Clouds");
		leftCloud.setStyleName("H-Clouds");
		lc.setWidth("100%");
		lc.setHeight("100%");
		leftCloud.add(lc);

		rightCloud = new SimplePanel();
		PNGImage rc = new PNGImage(Manager.myConstants.clouds_src(),120,120);
		rc.setStyleName("H-Clouds");
		rightCloud.setStyleName("H-Clouds");
		rc.setWidth("100%");
		rc.setHeight("100%");
		rightCloud.add(rc);		

		leftCloud.setWidth("70%");
		leftCloud.setHeight("100%");

		rightCloud.setWidth("70%");
		rightCloud.setHeight("100%");

		add(leftCloud,-40,0);
		add(rightCloud,400,0);

	}

	private void clearClouds() {

		GUIEffects.move(leftCloud,new EffectOption[] {
				new EffectOption("x",-1000),
				new EffectOption("y",0),
				new EffectOption("duration",5.0)
		},-1000,0);
		GUIEffects.move(rightCloud,new EffectOption[] {
				new EffectOption("x",1000),
				new EffectOption("y",0),
				new EffectOption("duration",5.0)
		},1000,0);



		GUIEffects.removeInXMilSecs(leftCloud, 8000);
		GUIEffects.removeInXMilSecs(rightCloud, 8000);
	}


	private void decorate() {

		addObject(new OceanLabel("Hippo<BR>Campus<BR>Ocean",300,300));

		addObject(new DashedBox(-1000,140,3000,60));


		focusBackdrop = new EventBackdrop();
		focusBackdrop.addMouseListener(this);
		focusBackdrop.addWheelistener(this);
		add(focusBackdrop,0,0);

	}

	private void addObject(RemembersPosition rp) {		
		add(rp.getWidget(),rp.getLeft(),rp.getTop());
		objects.add(rp);
	}

	public void load() {
		manager.getTagCache().getTagStats(new StdAsyncCallback("Get Tag Stats"){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				TagStat[] tagStats = (TagStat[]) result;

				System.out.println("TagStat Result "+tagStats);

				addAll(tagStats);
			}
		});
	}

	
	/**
	 * turn the tag stats into Islands.
	 * 
	 * NOTE: this takes a while and will lead to "this script is running slowly"
	 * warnings on IE if we're not careful. Avoid these by using Timers and doing 
	 * it in pieces.
	 * 
	 * NOTE 2: doing it piecemeal also allows the progress bar to update. Before,
	 * it would never get the work thread until it was all over.
	 * 
	 * @param tagStats
	 */
	private void addAll(final TagStat[] tagStats) {

		System.out.println("---------------------------------------------------------------------");
		System.out.println(" ADDALL ");
		
		
		for (Iterator iter = islands.keySet().iterator(); iter.hasNext();) {
			Entry e = (Entry) iter.next();
			remove((Widget) e.getValue());
			objects.remove(e.getValue());
		}
		islands.clear();

				
		final ProgressBar progressBar = new ProgressBar(10
				,ProgressBar.SHOW_TEXT);
		progressBar.setProgress(0);
		
		progressBar.setText(Manager.myConstants.loading_islands());
	
		progressWindow = manager.showProgressBar(progressBar);
		
		
		
		Timer t = new Timer(){
			public void run() {
				addFrom(tagStats,0,10,progressBar);
				
			}};
			t.schedule(200);
		
	}

	private void addFrom(final TagStat[] tagStats, final int start, final int num, final ProgressBar progressBar) {
		TagStat stat = null;
		int i = 0;
		System.out.println("AddFrom "+start+" to "+(start+num));
		for (i = start; i < tagStats.length && i < start + num; i++) {
			stat = tagStats[i];

			Island isle = new Island(stat,this,manager.getUser(),manager);

			addIsland(stat, isle);

		}				
		
		//could be null if tagStat.length == 0, ie new user
		if(stat != null){
			progressBar.setProgress((int) (100*i/(double)tagStats.length));
			progressBar.setText(stat.getTagName());
		}
		
		
		System.out.println("i "+i+" "+tagStats.length);
		if(i >= tagStats.length){
			done();
		}else{
			Timer t = new Timer(){
				public void run() {
					addFrom(tagStats,start+num,num,progressBar);								
				}};
			t.schedule(100);
		}
		
	}
	private void done() {		
		progressWindow.close();
		clearClouds();
	}

	
	
	/**
	 * clear & remove old islands
	 * Actually, just set them to invisible
	 */
	private void clearIslands(){		
		for (Iterator iter = islands.keySet().iterator(); iter.hasNext();) {
			Long e = (Long) iter.next();

			((Widget) islands.get(e)).setVisible(false);					
		}
	}
	private void clearCloseup(){		

		remove(closeUp);					
		focusBackdrop.setVisible(true);
	}

	private void addIsland(TagInfo info,Island isle){			


		dragHandler.add(isle,this);

		//dragHandler.add(isle,isle,banner);		
		add(isle,isle.getLeft(),isle.getTop());
		//add(banner,isle.getLeft(),isle.getTop());

		GUIEffects.appear(isle,4000);
		islands.put(new Long(info.getTagId()), isle);
		objects.add(isle);
		
		//TODO a bit redundant, but otherwise the div sizes don't get set right
		//and banners get clipped 
		isle.zoomToScale(currentScale);
	}

	private void showOcean(){

		clearCloseup();

		for (Iterator iter = islands.keySet().iterator(); iter.hasNext();) {
			Long e = (Long) iter.next();

			Island island = (Island) islands.get(e);

			island.setVisible(true);

		}
	}


	public void zoomTo(double scale) {
		if(scale == currentScale){
			return;
		}
		double oldScale = currentScale;
		
		currentScale = scale;
		
		finishZoom(oldScale);
		
	}
	private void zoomUp() {
		System.out.println("zoom up from "+currentScale);

		double oldScale = currentScale;

		if(currentScale <= 1){
			currentScale /= 2;		
		}else{
			currentScale--;
		}

		finishZoom(oldScale);
	}



	private void zoomIn() {		
		System.out.println("zoom in from "+currentScale);

		double oldScale = currentScale;

		if(currentScale <= 1){
			currentScale *= 2;		
		}else{
			currentScale++;
		}

		finishZoom(oldScale);
	}
	
	

	private void finishZoom(double oldScale) {
		int width = Window.getClientWidth();
		int height = Window.getClientHeight();

		int centerX = (int)((-curbackX + (width / 2)) / oldScale);
		int centerY = (int)((-curbackY + (height / 2)) / oldScale);

		int halfWidth = width/2;
		int halfHeight = height/2;
		reCenter(centerX,centerY,currentScale,halfWidth,halfHeight);


		setIslandsToZoom();

		//move all objects
		moveByDelta(0,0);
		
		manager.zoomTo(currentScale);
	}


	private void reCenter(int centerX, int centerY, double scale, int halfWidth, int halfHeight) {

		System.out.println("back X "+backX+"  backy "+backY);
		System.out.println("center X "+centerX+"  cy "+centerY);



		System.out.println("hw "+halfWidth+" hh "+halfHeight);
		//backX = halfWidth - halfWidth/currentScale;

		int newCenterX = (int) (centerX * scale);
		int newCenterY = (int) (centerY * scale);

		System.out.println("new center X "+newCenterX+" "+newCenterY);

		backX = -(newCenterX - halfWidth);
		backY = -(newCenterY - halfHeight);

		System.out.println("Newback X "+backX+"  NEWbacky "+backY);


	}



	private void setIslandsToZoom() {

		System.out.println("Setting all islands to zoom level "+currentScale);

		for (Iterator iter = islands.keySet().iterator(); iter.hasNext();) {
			Long e = (Long) iter.next();

			Island island = (Island) islands.get(e);

			island.zoomToScale(currentScale);		

		}


	}





	public void showCloseup(long id, FullTopicIdentifier[] topics) {

//		clearIslands();

//		focusBackdrop.setVisible(false);

//		Island closeIsland = (Island) islands.get(new Long(id));

//		closeUp = new CloseUpIsland(closeIsland.getStat(),topics,this,closeIsland.getRepr());

//		add(closeUp,closeUp.getLeft(),closeUp.getTop());

//		manager.setFocussed(true);		
	}

	public void growIsland(Tag tag) {
		Island isle = (Island) islands.get(new Long(tag.getId()));
//		if(isle == null){

//		Island newIsle = new Island(tag,this,manager.getUser(),manager);		
//		addIsland(tag, newIsle);

//		}else{
//		isle.grow();
//		}
	}
	public void removeIsland(long id) {
		Island isle = (Island) islands.get(new Long(id));
		if(isle != null){

			GUIEffects.fadeAndRemove(isle, 3000);


		}
	}

	public Widget getWidget() {
		return this;
	}

	public void islandClicked(TopicIdentifier ident, Island island) {

		System.out.println("CLICKED focussed "+focussed+" ID  "+ident);

//		if(focussed){
//			showOcean();
//			focussed = false;
//		}else{
//			manager.showTopicsForTag(tagId);
//			focussed = true;
//		}

		if(selectedIsland == island){
			manager.bringUpChart(ident);
		}else{
			manager.showPreviews(ident.getTopicID());
		}
		
		
		if(selectedIsland != null){
			selectedIsland.setSelected(false);
		}
		island.setSelected(true);
		selectedIsland = island;
	}

	private void unselect() {
		if(selectedIsland != null){
			selectedIsland.setSelected(false);
			selectedIsland = null;
		}
		manager.unselectIsland();
	}



	public void dragFinished(Widget dragging) {
		Island island = (Island) dragging;

		island.youveBeenDraggedSetYourLeftAndTop();

		islandMoved(island.getStat().getTagId(), island.getLeft(), island.getTop());
	}


	public void islandMoved(long islandID, final int longitude, final int latitude){

		System.out.println("isleMovedTo "+longitude+" "+latitude+" SAVING");	

		manager.getTopicCache().getTopicByIdA(islandID, new StdAsyncCallback("GetTopicById"){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				Topic t = (Topic) result;
				t.setLatitude(latitude);
				t.setLongitude(longitude);					
				manager.getTopicCache().save(t, new StdAsyncCallback("SaveLatLong"){});
			}
		});

	}



	private void moveByDelta(int dx, int dy) {
		curbackX = dx + backX;
		curbackY = dy + backY;
		DOM.setStyleAttribute(getElement(), "backgroundPosition", curbackX+"px "+curbackY+"px");	

		int width = Window.getClientWidth();
		int height = Window.getClientHeight();

		int halfWidth = width/2;
		int halfHeight = height/2;

//		System.out.println("cur "+curbackX+" "+curbackY);

		int centerX = (int)((-curbackX + halfWidth)/currentScale);
		int centerY = (int)((-curbackY + halfHeight)/currentScale);

//		System.out.println("centerX "+centerX+" centerY "+centerY);

		for (Iterator iter = objects.iterator(); iter.hasNext();) {
			Object o = iter.next();
			RemembersPosition rp = (RemembersPosition) o;					

			//System.out.println("found: "+GWT.getTypeName(rp));

//			System.out.println("Left "+isle.getLeft()+"  Top "+isle.getTop());
//			System.out.println("cur "+curbackX+" cury "+curbackY);

			//setWidgetPosition(rp.getWidget(),(int)((rp.getLeft()+curbackX)*currentScale), (int)((rp.getTop()+curbackY)*currentScale));				
			setWidgetPosition(rp.getWidget(),(int)((rp.getLeft())*currentScale)+curbackX, (int)((rp.getTop())*currentScale)+curbackY);


			/*
			 * All the islands need to check to see if they're in the visible 
			 * window. If so, and we're zoomed in enough, show the topics. 
			 * 
			 * all others should turn topics off.
			 * 
			 */
			if(o instanceof Island){
				Island island = (Island) o;
				int left = (int) (centerX - halfWidth/currentScale);
				int top = (int) (centerY - halfHeight/currentScale);
				int right = (int) (centerX + halfWidth/currentScale);
				int bottom = (int) (centerY + halfHeight/currentScale);
				if(island.isWithin(left,top,right,bottom) && currentScale >= SHOW_TOPICS_AT_ZOOM ){
					island.showTopics();
				}else{
					island.removeTopics();
				}

			}
		}

	}

	public void onMouseEnter(Widget sender) {}
	public void onMouseLeave(Widget sender) {
		endDrag();
	}



	public void onMouseDown(Widget sender, int x, int y) {		
		dragging = true;
		dragStartX = x;
		dragStartY = y;
		
		unselect();
	}

	public void onMouseMove(Widget sender, int x, int y) {
		curMouseX = x;
		curMouseY = y;

		if (dragging) {			
			moveByDelta(x - dragStartX, y - dragStartY);			
		}
	}


	public void onMouseUp(Widget sender, int x, int y) {	
		endDrag();
	}

	private void endDrag() {
		if(dragging){
//			System.out.println("(old)back x "+backX+" cur(new) "+curbackX);
//			System.out.println("(old)back y "+backY+" cur(new) "+curbackY);
			backX = curbackX;
			backY = curbackY;			
		}
		dragging = false;
	}

	public int getBackX() {		
		return backX;
	} 
	public int getBackY() {		
		return backY;
	}

	public void unFocus() {
		showOcean();
	}

	public void onWheel(Widget widget, int delta) {
		if(delta < 0){
			zoomUp();
		}else{
			zoomIn();
		}
	}



}
