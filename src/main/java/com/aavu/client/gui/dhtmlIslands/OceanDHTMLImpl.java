package com.aavu.client.gui.dhtmlIslands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwtwidgets.client.ui.PNGImage;
import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveLatLongCommand;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.Ocean;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.ext.GUIEffects;
import com.aavu.client.gui.ext.JSUtil;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.WheelListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * DHTML impl of the island interface.
 * 
 * @author Jeff Dwyer
 *
 */
public class OceanDHTMLImpl extends ViewPanel implements Ocean,  DragFinishedListener, WheelListener {

	
	//static final String IMG_LOC = "img/simplicityHighRes/";
	
	static final String IMG_LOC = "img/simplicity/";	
	//static final String IMG_LOC = "img/oldmapStyle/";

	
	private static final int CLOUD_REMOVE = 8000;

	private static final int CLOUD_MOVE_PX = 2000;
	private static final int CLOUD_MOVE_MSEC = 7000;

	private static final double NO_ISLAND_DRAG_AT_THIS_ZOOM = 8;	

	private Manager manager;

	private Map islands = new HashMap();	

	private DragHandler dragHandler;

	private Panel rightCloud;



	private Panel leftCloud;

	private boolean focussed = false;

	private Island selectedIsland;
	
	private PopupWindow progressWindow;


	private OceanKeyBoardListener oceanKeyboardListener;	


	public OceanDHTMLImpl(Manager manager) {
		super();
		this.manager = manager;

		addStyleName("H-Ocean");

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
		setBackground();
		//url("../img/bluecheck-bullet-14.gif");
	}

	private void setBackground(){
		int pix = (int) (currentScale * 100);
		if(pix > 1600 || pix < 6){
			return;
		}			
		if(pix > 400){
			DOM.setStyleAttribute(getElement(), "backgroundImage","url("+IMG_LOC+"ocean"+pix+".jpg)");
		}
		else{
			DOM.setStyleAttribute(getElement(), "backgroundImage","url("+IMG_LOC+"ocean"+pix+".png)");
		}
	}
	
	private void clouds() {
		leftCloud = new SimplePanel();
		
		PNGImage lc = new PNGImage(ConstHolder.myConstants.clouds_src(),120,120);		
		lc.setStyleName("H-Clouds");
		leftCloud.setStyleName("H-Clouds");
		lc.setWidth("100%");
		lc.setHeight("100%");
		leftCloud.add(lc);

		rightCloud = new SimplePanel();
		PNGImage rc = new PNGImage(ConstHolder.myConstants.clouds_src(),120,120);
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

		
		GUIEffects.move(leftCloud,-CLOUD_MOVE_PX,0,CLOUD_MOVE_MSEC);
		GUIEffects.move(rightCloud,CLOUD_MOVE_PX,0,CLOUD_MOVE_MSEC);



		GUIEffects.removeInXMilSecs(leftCloud, CLOUD_REMOVE);
		GUIEffects.removeInXMilSecs(rightCloud, CLOUD_REMOVE);
	}


	private void decorate() {

		OceanLabel lab = new OceanLabel("Hippo<BR>Campus<BR>Ocean",300,300);
		JSUtil.disableSelect(lab.getElement());
		addObject(lab);

		//addObject(new DashedBox(-1000,140,3000,60));


		oceanKeyboardListener = new OceanKeyBoardListener(this);
		
		//added in order because the event can be cancelled
		focusBackdrop.addWheelistener(manager);
		focusBackdrop.addWheelistener(this);
		
		focusBackdrop.addKeyboardListener(oceanKeyboardListener);
		
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
			Long e = (Long) iter.next();
			Widget w = (Widget) islands.get(e);
			remove((Widget) w);
			objects.remove(w);
		}
		islands.clear();

				
		final ProgressBar progressBar = new ProgressBar(10
				,ProgressBar.SHOW_TEXT);
		progressBar.setProgress(0);
		
		progressBar.setText(ConstHolder.myConstants.loading_islands());
	
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
			done(tagStats.length);
		}else{
			Timer t = new Timer(){
				public void run() {
					addFrom(tagStats,start+num,num,progressBar);								
				}};
			t.schedule(100);
		}
		
	}
	private void done(final int size) {		
		progressWindow.close();
		clearClouds();
		Timer t = new Timer(){
			public void run() {
				manager.fireOceanLoaded(size);		
			}};
		t.schedule(CLOUD_REMOVE);	
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


	private void addIsland(TagInfo info,Island isle){			


		dragHandler.add(isle,this);

		//isle.addMouseListener(this);
		isle.addKeyboardListener(oceanKeyboardListener);

		//dragHandler.add(isle,isle,banner);				
		//add(banner,isle.getLeft(),isle.getTop());

		addObject(isle);
		
		//GUIEffects.appear(isle,4000);
		islands.put(new Long(info.getTagId()), isle);
		
		
		//TODO a bit redundant, but otherwise the div sizes don't get set right
		//and banners get clipped 
		isle.zoomToScale(currentScale);
	}


	public void zoomTo(double scale) {
		if(scale == currentScale){
			return;
		}
		double oldScale = currentScale;
		
		currentScale = scale;
		
		finishZoom(oldScale);
		
	}
	public void zoomOut() {
		//System.out.println("zoom up from "+currentScale);
		
		double oldScale = currentScale;

		
		currentScale /= 2;
		
//		if(currentScale <= 1){
//			currentScale /= 2;		
//		}else{
//			currentScale--;
//		}

		finishZoom(oldScale);
	}



	public void zoomIn() {		
		//System.out.println("zoom in from "+currentScale);

		centerOnMouse();
		
		double oldScale = currentScale;

		currentScale *= 2;
//		if(currentScale <= 1){
//			currentScale *= 2;		
//		}else{
//			currentScale++;
//		}

		finishZoom(oldScale);
	}
	
	

	private void finishZoom(double oldScale) {
		
		setBackground();
		
		int width = Window.getClientWidth();
		int height = Window.getClientHeight();

		int centerX = getCenterX(oldScale,width);
		int centerY = getCenterY(oldScale,height);

		int halfWidth = width/2;
		int halfHeight = height/2;
		reCenter(centerX,centerY,currentScale,halfWidth,halfHeight);


		setIslandsToZoom();

		//move all objects
		moveByDelta(0,0);
		
		if(currentScale >= NO_ISLAND_DRAG_AT_THIS_ZOOM){
			islandDrag = false;
						
		}else{			
			islandDrag = true;
		}
		dragHandler.setIslandDrag(islandDrag);
		
		manager.zoomTo(currentScale);
	}


	private void reCenter(int centerX, int centerY, double scale, int halfWidth, int halfHeight) {

		//System.out.println("back X "+backX+"  backy "+backY);
		//System.out.println("center X "+centerX+"  cy "+centerY);



		//System.out.println("hw "+halfWidth+" hh "+halfHeight);
		//backX = halfWidth - halfWidth/currentScale;

		int newCenterX = (int) (centerX * scale);
		int newCenterY = (int) (centerY * scale);

		//System.out.println("new center X "+newCenterX+" "+newCenterY);

		backX = -(newCenterX - halfWidth);
		backY = -(newCenterY - halfHeight);

		//System.out.println("Newback X "+backX+"  NEWbacky "+backY);


	}



	private void setIslandsToZoom() {

		//System.out.println("Setting all islands to zoom level "+currentScale);

		for (Iterator iter = islands.keySet().iterator(); iter.hasNext();) {
			Long e = (Long) iter.next();

			Island island = (Island) islands.get(e);

			island.zoomToScale(currentScale);		

		}


	}

	public void growIsland(Tag tag) {
		Island isle = (Island) islands.get(new Long(tag.getId()));
				
		if(isle == null){
			System.out.println("was null");
			
			//center the island

			tag.setLatitude(getCenterY());
			tag.setLongitude(getCenterX());
			
			TagStat tagStat = new TagStat(tag);
			Island newIsle = new Island(tagStat,this,manager.getUser(),manager);		
			addIsland(tagStat, newIsle);
			
			islandMoved(tag.getId(),tag.getLongitude(),tag.getLatitude());
			
				//forces redraw
			moveByDelta(0,0);
			
		}else{
			System.out.println(".grow()");
			isle.grow();
		}
	}
	public void removeIsland(long id) {
		Island isle = (Island) islands.get(new Long(id));
		if(isle != null){
			
			GUIEffects.fadeAndRemove(isle, 3000);

			islands.remove(new Long(id));			
			objects.remove(isle);						
		}
	}

	public Widget getWidget() {
		return this;
	}

	public void islandClicked(TopicIdentifier ident, int num_clicks, Island island) {

		System.out.println("CLICKED focussed "+focussed+" ID  "+ident);

//		if(focussed){
//			showOcean();
//			focussed = false;
//		}else{
//			manager.showTopicsForTag(tagId);
//			focussed = true;
//		}

		if(num_clicks > 1){
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

	//@Override
	protected void unselect() {
		if(selectedIsland != null){
			selectedIsland.setSelected(false);
			selectedIsland = null;
		}
		manager.unselect();
	}



	public void dragFinished(Widget dragging) {
		Island island = (Island) dragging;

		if(island.possibleMoveOccurred()){
			islandMoved(island.getStat().getTagId(), island.getLeft(), island.getTop());
		}
	}

	/**
	 * TODO double ASYNC!
	 * 
	 * @param islandID
	 * @param longitude
	 * @param latitude
	 */
	public void islandMoved(long islandID, final int longitude, final int latitude){

		System.out.println("isleMovedTo "+longitude+" "+latitude+" SAVING");	

		manager.getTopicCache().getTopicByIdA(islandID, new StdAsyncCallback("GetTopicById"){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				Topic t = (Topic) result;
				t.setLatitude(latitude);
				t.setLongitude(longitude);
				
				manager.getTopicCache().executeCommand(t,new SaveLatLongCommand(t,latitude,
						longitude),
						new StdAsyncCallback("SaveLatLong"){});								
			}
		});

	}

	
	



	public boolean onWheel(Widget widget, int delta) {
		if(delta < 0){
			zoomOut();
		}else{
			zoomIn();
		}
		return false;
	}

	public void update(Tag t, AbstractCommand command) {
		Island isle = (Island) islands.get(new Long(t.getId()));
		
		if(isle != null){
			System.out.println("Ocean.update "+isle.getTitle());
			isle.redraw(t);
		}else{			
			growIsland(t);			
		}
	}

	/**
	 * 
	 * centerOn will move th map to center on the given topic.
	 * If it's an island, that's easy.
	 * If it's only a topic, loop through its islands and find the one closest
	 * to out current center. Otherwise we'll jump all over the place.
	 * 
	 * 
	 * 	invert this equation to find the x for a given center
	 *	int centerX = (int)((-curbackX + halfWidth)/currentScale);
	 *  int centerY = (int)((-curbackY + halfHeight)/currentScale);		
	 * 
	 * return - should return true if it finds a good thing to center on 
	 */
	public boolean centerOn(Topic topic) {
	
		int width = Window.getClientWidth();
		int height = Window.getClientHeight();

		int halfWidth = width/2;
		int halfHeight = height/2;
	
			
		Topic centerTopic = null;		
		if(topic instanceof Tag){
			centerTopic = topic;
		}else{
					
			//Loop through all tags and find the one closest to our
			//current center
			//
			int centerX = getCenterX(currentScale,width);
			int centerY = getCenterY(currentScale,height);
				
			double distance = Double.MAX_VALUE;
			
			Set s = topic.getTypesAsTopics();
			
			for (Iterator iter = s.iterator(); iter.hasNext();) {
				Topic tag = (Topic) iter.next();
				
				double dist = Math.pow(tag.getLatitude() - centerY,2);
				dist += Math.pow(tag.getLongitude() - centerX,2);
				dist = Math.sqrt(dist);
				
				if(dist < distance){
					distance = dist;
					centerTopic = tag;
				}
			}
			
		}		
		
		if(centerTopic == null){
			return false;
		}
		else{

			Island isle = (Island) islands.get(new Long(centerTopic.getId()));

			if(isle != null){

				centerOn((int)isle.getCenterXAtScale(),(int)isle.getCenterYAtScale());
				
//				int left = (int) ((isle.getCenterXAtScale() * currentScale) - halfWidth);
//				int top = (int) ((isle.getCenterYAtScale() * currentScale) - halfHeight);


				//intuitively this is (left - curbackX) but things are reversed			
//				int dx = left + curbackX;
//				int dy = top + curbackY;
//				moveBy(dx, dy);

				
//				SYSTEM.OUT.PRINTLN("P.X "+P.X+" HW "+HALFWIDTH+" "+LEFT);
//				System.out.println("p.y "+p.y+" hw "+halfHeight+" "+top);

//				System.out.println("dx "+dx+" curbackX "+curbackX+" ");
//				System.out.println("dy "+dy+" curbackY "+curbackY+" ");			

			}
		}
		return true;
	}



	/**
	 * TODO fix 
	 *
	 */
	private void centerOnMouse() {
				
//		int mouseX = (int) (lastx/currentScale + curbackX);
//		int mouseY = (int) (lasty/currentScale + curbackY);
//		
//		System.out.println("last x "+lastx+" mousex "+mouseX+" curbackx "+curbackX);
//		System.out.println("last y "+lasty+" mousey "+mouseY+" curbacky "+curbackY);
//		
//		centerOn(mouseX,mouseY);
		
	}
	
	
	
	/**
	 * Make sure that we're zoomed to 'scale' or higher
	 * 
	 * return the value that we settle on
	 */
	public double ensureZoomOfAtLeast(double scale) {
		if(scale > currentScale){
			zoomTo(scale);
		}
		return currentScale;
	}


}
