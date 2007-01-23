package com.aavu.client.gui.dhtmlIslands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gwtwidgets.client.ui.PNGImage;
import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.FullTopicIdentifier;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.Ocean;
import com.aavu.client.gui.ext.GUIEffects;
import com.aavu.client.service.Manager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class OceanDHTMLImpl  extends AbsolutePanel implements Ocean, MouseListener {

	static final String IMG_LOC = "img/simplicity/";
	//static final String IMG_LOC = "img/oldmapStyle/";
	
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

	private FocusPanel focusBackdrop;	
	
	public OceanDHTMLImpl(Manager manager) {
		super();
		this.manager = manager;

		setStyleName("H-Ocean");

		dragHandler = new DragHandler(this);


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
		

		focusBackdrop = new FocusPanel();
		focusBackdrop.setWidth("100%");
		focusBackdrop.setHeight("100%");
		focusBackdrop.addMouseListener(this);
		focusBackdrop.setStyleName("H-FocusBackDrop");		
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

	private void addAll(TagStat[] tagStats) {
		
		System.out.println("ADDALL");
		
		islands.clear();
		for (Iterator iter = islands.keySet().iterator(); iter.hasNext();) {
			Entry e = (Entry) iter.next();
			remove((Widget) e.getValue());
			objects.remove(e.getValue());
			
		}
		
		for (int i = 0; i < tagStats.length; i++) {
			TagStat stat = tagStats[i];

			Island isle = new Island(stat,this,manager.getUser());

			addIsland(stat, isle);

		}
		
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
		
		
		dragHandler.add(isle);
		
		//dragHandler.add(isle,isle,banner);		
		add(isle,isle.getLeft(),isle.getTop());
		//add(banner,isle.getLeft(),isle.getTop());
		
		GUIEffects.appear(isle,4000);
		islands.put(new Long(info.getTagId()), isle);
		objects.add(isle);
	}
	
	private void showOcean(){
		
		clearCloseup();
		
		for (Iterator iter = islands.keySet().iterator(); iter.hasNext();) {
			Long e = (Long) iter.next();
						
			Island island = (Island) islands.get(e);
						
			island.setVisible(true);
						
		}
	}
	

	public void showCloseup(long id, FullTopicIdentifier[] topics) {
				
		clearIslands();

		focusBackdrop.setVisible(false);
		
		Island closeIsland = (Island) islands.get(new Long(id));
			
		closeUp = new CloseUpIsland(closeIsland.getStat(),topics,this,closeIsland.getRepr());
				
		add(closeUp,closeUp.getLeft(),closeUp.getTop());
		
		manager.setFocussed(true);		
	}

	public void growIsland(Tag tag) {
		Island isle = (Island) islands.get(new Long(tag.getId()));
		if(isle == null){

			Island newIsle = new Island(tag,this,manager.getUser());		
			addIsland(tag, newIsle);

		}else{
			isle.grow();
		}
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

	public void islandClicked(long tagId) {
		
		System.out.println("CLICKED focussed "+focussed+" ID  "+tagId);
		
		if(focussed){
			showOcean();
			focussed = false;
		}else{
			manager.showTopicsForTag(tagId);
			focussed = true;
		}
		
		//manager.showTopicsForTag(tagId);
	}

	public void islandMoved(long islandID, final int longitude, final int latitude){

		System.out.println("isleMovedTo "+longitude+" "+latitude+" ");			
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

	

	private void moveTo(int dx, int dy) {
		curbackX = dx + backX;
		curbackY = dy + backY;
		DOM.setStyleAttribute(getElement(), "backgroundPosition", curbackX+"px "+curbackY+"px");	
		
		for (Iterator iter = objects.iterator(); iter.hasNext();) {
			RemembersPosition rp = (RemembersPosition) iter.next();					
			
			//System.out.println("found: "+GWT.getTypeName(rp));
			
//			System.out.println("Left "+isle.getLeft()+"  Top "+isle.getTop());
//			System.out.println("cur "+curbackX+" cury "+curbackY);
			
			setWidgetPosition(rp.getWidget(),rp.getLeft()+curbackX, rp.getTop()+curbackY);	
			
			
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
	}

	public void onMouseMove(Widget sender, int x, int y) {
		if (dragging) {			
			moveTo(x - dragStartX, y - dragStartY);			
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


}
