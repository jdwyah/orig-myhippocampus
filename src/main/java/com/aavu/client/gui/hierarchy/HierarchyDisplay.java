package com.aavu.client.gui.hierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.LoadFinishedListener;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.ocean.SpatialDisplay;
import com.aavu.client.gui.ocean.dhtmlIslands.DragEventListener;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.allen_sauer.gwt.dragdrop.client.DragController;
import com.allen_sauer.gwt.dragdrop.client.PickupDragController;
import com.allen_sauer.gwt.dragdrop.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.allen_sauer.gwt.dragdrop.client.drop.SimpleDropController;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class HierarchyDisplay  extends ViewPanel implements SpatialDisplay, DragEventListener{

	private Manager manager;

	
	//<long,TopicBubble>
	private Map bubbles = new HashMap();
	
	private DragController dragController;


	private DropController backdropDropController;
	

	public HierarchyDisplay(Manager manager){
		super();
		this.manager = manager;
		
		addStyleName("H-Hierarchy");
		
		//passing (this,true) kinda worked, but we'd often miss entry events which led to problems.
		dragController = new PickupDragController(null, true);

		backdropDropController = new BackdropDropController(this);		
		dragController.registerDropController(backdropDropController);
		
		
		setDoZoom(true);
		
		DOM.setStyleAttribute(getElement(), "position", "absolute");	
		setBackground(currentScale);
	}
	
	public boolean centerOn(Topic topic) {
		// TODO Auto-generated method stub
		return false;
	}

	

	public Widget getWidget() {
		return this;
	}

	public void growIsland(Topic tag) {
		// TODO Auto-generated method stub
		
	}

	public void load(LoadFinishedListener loadFinished) {
		
		manager.getTopicCache().getRootTopics(manager.getUser(),new StdAsyncCallback(ConstHolder.myConstants.getRoot_async()){
			//@Override
			public void onSuccess(Object result) {
				super.onSuccess(result);
				
				List all_ftis = (List) result;
				
				for (Iterator iterator = all_ftis.iterator(); iterator.hasNext();) {
					
					FullTopicIdentifier fti = (FullTopicIdentifier) iterator.next();
					add(fti);
				}
				
				
			}
			
		});
		
	}
	
	private void add(FullTopicIdentifier fti){
		
		fti.setLatitudeOnIsland((int) (Math.random()*400.0));
		fti.setLongitudeOnIsland((int) (Math.random()*400.0));
		
		TopicBubble tb = new TopicBubble(fti,this);
		
		dragController.makeDraggable(tb);

		
		DropController dropController = new BubbleDropController(tb);		
		dragController.registerDropController(dropController);
		
		
		
		addObject(tb);	
		
		
		
		bubbles.put(new Long(fti.getTopicID()), tb);
	}

	

	//@Override
	protected void postZoomCallback(double currentScale){

		setIslandsToZoom();
		
	}
	
	private void setIslandsToZoom() {

		//System.out.println("Setting all islands to zoom level "+currentScale);

		for (Iterator iter = bubbles.keySet().iterator(); iter.hasNext();) {
			Long e = (Long) iter.next();

			TopicBubble bubble = (TopicBubble) bubbles.get(e);

			bubble.zoomToScale(currentScale);		

		}


	}
	
	public void removeIsland(long id) {
		// TODO Auto-generated method stub
		
	}

	public void update(Topic t, AbstractCommand command) {
		// TODO Auto-generated method stub
		
	}


	//@Override
	protected int getWidth(){
		return Window.getClientWidth();
	}
	//@Override
	protected int getHeight(){
		return Window.getClientHeight();
	}
	//@Override
	protected void setBackground(double scale) {
		int pix = (int) (scale * 100);
		if(pix > 1600 || pix < 6){
			return;
		}			
		if(pix > 400){
			DOM.setStyleAttribute(getElement(), "backgroundImage","url("+ImageHolder.getImgLoc()+"ocean"+pix+".jpg)");
		}
		else{
			DOM.setStyleAttribute(getElement(), "backgroundImage","url("+ImageHolder.getImgLoc()+"ocean"+pix+".png)");
		}
	}

	public void dragFinished(Widget dragging) {
		TopicBubble tb = (TopicBubble) dragging;
		
		tb.possibleMoveOccurred();
		
	}

	public void dragged(Widget dragging, int newX, int newY) {
		// TODO Auto-generated method stub
		
	}

}
