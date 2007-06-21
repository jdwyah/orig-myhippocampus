package com.aavu.client.gui.hierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveTagtoTopicCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.LoadFinishedListener;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.ocean.SpatialDisplay;
import com.aavu.client.gui.ocean.dhtmlIslands.DragEventListener;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.allen_sauer.gwt.dragdrop.client.DragController;
import com.allen_sauer.gwt.dragdrop.client.PickupDragController;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class HierarchyDisplay  extends ViewPanel implements SpatialDisplay, DragEventListener{

	private static final int UNSET_LAT_START = 50;
	private static final int UNSET_LAT_INCR = 110;

	private DropController backdropDropController;

	
	//<long,TopicBubble>
	private Map bubbles = new HashMap();
	
	private Topic currentRoot;


	private DragController dragController;


	private Manager manager;


	private int unsetLatitude;
	

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
	
	/**
	 * if their position is unset, space them out incrementally in latitude
	 * @param fti
	 */
	private void add(FullTopicIdentifier fti){
		
		if(fti.getLatitudeOnIsland() == -1 || fti.getLatitudeOnIsland() == 0){			
			System.out.println(fti.getTopicTitle()+"incr unsetLatitude "+unsetLatitude+" "+fti.getLatitudeOnIsland()+" "+fti.getLongitudeOnIsland());
			fti.setLatitudeOnIsland(unsetLatitude);			
			unsetLatitude += UNSET_LAT_INCR;
			
		}
		

//		fti.setLongitudeOnIsland((int) (Math.random()*400.0));
		
		TopicBubble tb = new TopicBubble(fti,this);		
		tb.addMouseWheelListener(this);
		
		dragController.makeDraggable(tb);
				
		DropController dropController = new BubbleDropController(tb);		
		tb.setDropController(dropController);
		dragController.registerDropController(dropController);
		
		//System.out.println("HierarchyAdd "+fti.getTopicTitle()+" "+fti.getLatitudeOnIsland()+" "+fti.getLongitudeOnIsland());
		
		addObject(tb);	
		
		tb.zoomToScale(currentScale);
		
		bubbles.put(new Long(fti.getTopicID()), tb);
	}

	

	public boolean centerOn(Topic topic) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public void clear() {		
		super.clear();
		
		unsetLatitude = UNSET_LAT_START;
		
		for (Iterator iter = bubbles.keySet().iterator(); iter.hasNext();) {
			Long e = (Long) iter.next();
			TopicBubble bubble = (TopicBubble) bubbles.get(e);
			dragController.unregisterDropController(bubble.getDropController());
		}
		bubbles.clear();
		
	}

	/**
	 * on drag finish, update the lat/long of the topicBubble & save
	 */
	public void dragFinished(Widget dragging) {
		System.out.println("HierarchyDisplay.Drag Finished");
		TopicBubble tb = (TopicBubble) dragging;
		
		if(tb.possibleMoveOccurred(currentScale)){
			
						
			System.out.println("HierarchyDisplay.Drag Finished Saving "+tb.getLeft()+" "+tb.getTop());
			
			manager.getTopicCache().saveTopicLocationA(currentRoot.getId(), tb.getFTI().getTopicID(), tb.getTop(), tb.getLeft(), 
					new StdAsyncCallback("SaveLatLong"){});
			
				
		}
	}
	
	public void dragged(Widget dragging, int newX, int newY) {
		// TODO Auto-generated method stub
		
	}
	
	
	//@Override
	protected int getHeight(){
		return Window.getClientHeight();
	}

	public Widget getWidget() {
		return this;
	}

	

	//@Override
	protected int getWidth(){
		return Window.getClientWidth();
	}
	
	/**
	 * create a dummy FullFTI
	 */
	public void growIsland(Topic tag) {
		
		TopicBubble bubble = (TopicBubble) bubbles.get(new Long(tag.getId()));
		if(null != bubble){
			bubble.grow();
		}else{
			FullTopicIdentifier fti = new FullTopicIdentifier(tag);		
			add(fti);
			redraw();
		}
		
	}
	
	


	public void load(final Topic t,final LoadFinishedListener loadFinished){
		manager.getTopicCache().getTopicsWithTag(t.getId(),new StdAsyncCallback(ConstHolder.myConstants.getRoot_async()){
			//@Override
			public void onSuccess(Object result) {
				super.onSuccess(result);
				
				clear();
				
				
				List all_ftis = (List) result;
				
				for (Iterator iterator = all_ftis.iterator(); iterator.hasNext();) {
					
					FullTopicIdentifier fti = (FullTopicIdentifier) iterator.next();					
					add(fti);
				}				
				
				currentRoot = t;
				
				zoomTo(1);
				centerOn(0, 0);
				
				redraw();
				
				if(loadFinished != null){
					loadFinished.loadFinished();
				}
			}			
		});
	}
	//@Override
	protected void postZoomCallback(double currentScale){

		setIslandsToZoom();
		
	}
	//@Override
	public void processDrop(TopicBubble receiver, TopicBubble received) {
		Logger.debug("HierarchyDisplay.removeBubble ");
		
		bubbles.remove(new Long(received.getFTI().getTopicID()));
		dragController.unregisterDropController(received.getDropController());
		removeObj(received);
		
		manager.getTopicCache().executeCommand(received.getTopic(),new SaveTagtoTopicCommand(received.getTopic(),receiver.getTopic(),currentRoot), 
				new StdAsyncCallback(ConstHolder.myConstants.save()){
					//@Override
					public void onSuccess(Object result) {
						super.onSuccess(result);						
					}			
		});		
		
		
	}

	public void removeIsland(long id) {
		// TODO Auto-generated method stub
		
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

	private void setIslandsToZoom() {

		//System.out.println("Setting all islands to zoom level "+currentScale);

		for (Iterator iter = bubbles.keySet().iterator(); iter.hasNext();) {
			Long e = (Long) iter.next();

			TopicBubble bubble = (TopicBubble) bubbles.get(e);

			bubble.zoomToScale(currentScale);		

		}


	}

	public void update(Topic t, AbstractCommand command) {
		// TODO Auto-generated method stub
		
	}

	public void navigateTo(FullTopicIdentifier fti) {
		manager.bringUpChart(fti);
	}

}
