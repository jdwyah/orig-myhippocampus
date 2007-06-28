package com.aavu.client.gui.hierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.TopicOccurrenceConnector;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class HierarchyDisplay  extends ViewPanel implements SpatialDisplay {

	private static final int UNSET_LAT_START = 50;
	private static final int UNSET_LAT_INCR = 110;

	private DropController backdropDropController;

	
	
	//<long,TopicBubble>
	/**
	 * Not all bubbles, just topic bubbles. If you want all, use objects.
	 */
	private Map topicBubbles = new HashMap();
	
	private Topic currentRoot;


	private PickupDragController dragController;

	private Manager manager;

	private int unsetLatitude;
	
	

	public HierarchyDisplay(Manager manager){
		super();
		this.manager = manager;
		
		addStyleName("H-Hierarchy");
		
		//passing (this,true) kinda worked, but we'd often miss entry events which led to problems.
		dragController = new PickupDragController(null, true);
		//dragController.setDragProxyEnabled(true);

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
	private void addBubble(Bubble bubble){
		
		if(bubble.getLeft() == -1 || bubble.getLeft() == 0){			
			//System.out.println(fti.getTopicTitle()+"incr unsetLatitude "+unsetLatitude+" "+fti.getLatitudeOnIsland()+" "+fti.getLongitudeOnIsland());
			bubble.setTop(unsetLatitude);			
			unsetLatitude += UNSET_LAT_INCR;
			
		}		
		
		bubble.getFocusPanel().addMouseWheelListener(this);
				
		dragController.makeDraggable(bubble.getWidget());
		
		if(bubble.getDropController() != null){
			dragController.registerDropController(bubble.getDropController());
		}
		
		addObject(bubble);	
		
		topicBubbles.put(new Long(bubble.getIdentifier().getTopicID()), bubble);
		
		bubble.zoomToScale(currentScale);
	
		
		
	}
	

	public boolean centerOn(Topic topic) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public void clear() {		
		
		
		
		unsetLatitude = UNSET_LAT_START;
		
		
		for (Iterator iterator = objects.iterator(); iterator.hasNext();) {
		
			Bubble bubble = (Bubble) iterator.next();
			
		
			dragController.unregisterDropController(bubble.getDropController());
		}
		
		
		topicBubbles.clear();
		
		//needs to be after we iter over objects
		super.clear();
		
		System.out.println("HDisplay.clear() DragController"+dragController);
	}

	/**
	 * on drag finish, update the lat/long of the topicBubble & save
	 */
	public void dragFinished(Widget dragging) {
		System.out.println("HierarchyDisplay.Drag Finished");
		Bubble tb = (Bubble) dragging;
		
		tb.processDrag(currentScale);
		
//		if(tb.possibleMoveOccurred(currentScale)){
//			
//						
//			System.out.println("HierarchyDisplay.Drag Finished Saving "+tb.getLeft()+" "+tb.getTop());
//			
//			manager.getTopicCache().saveTopicLocationA(currentRoot.getId(), tb.getFTI().getTopicID(), tb.getTop(), tb.getLeft(), 
//					new StdAsyncCallback("SaveLatLong"){});
//			
//				
//		}
	}
	
//	public void dragged(Widget dragging, int newX, int newY) {
//		// TODO Auto-generated method stub
//		
//	}
	
	
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
	public void growIsland(Topic thought) {
		
		TopicBubble bubble = (TopicBubble) topicBubbles.get(new Long(thought.getId()));
		
		
		if(null != bubble){
			bubble.grow();
		}else{
			System.out.println("Grow "+thought.getId()+" "+GWT.getTypeName(thought));			
			
			Bubble newBubble = BubbleFactory.createBubbleFor(thought,currentRoot,this);		
			addBubble(newBubble);
			redraw();
			
		}
		
		
		
	}
	
	


	public void load(final Topic t,final LoadFinishedListener loadFinished){
		System.out.println("Hdisplay.load DragController"+dragController);

		clear();
		
		loadTopicOcc(t);		
		loadChildTopics(t,loadFinished);
		
		
	}
	private void loadTopicOcc(Topic t) {
		System.out.println("Load "+t+" occs "+t.getOccurences().size());
		
		for (Iterator iterator = t.getOccurences().iterator(); iterator.hasNext();) {
			TopicOccurrenceConnector owl = (TopicOccurrenceConnector) iterator.next();
			
			addBubble(BubbleFactory.createBubbleFor(owl,this));
			
		}
	}

	private void loadChildTopics(final Topic t, final LoadFinishedListener loadFinished) {
		manager.getTopicCache().getTopicsWithTag(t.getId(),new StdAsyncCallback(ConstHolder.myConstants.getRoot_async()){
			//@Override
			public void onSuccess(Object result) {
				super.onSuccess(result);
				
				List all_ftis = (List) result;
				
				for (Iterator iterator = all_ftis.iterator(); iterator.hasNext();) {
					
					FullTopicIdentifier fti = (FullTopicIdentifier) iterator.next();					
					addBubble(BubbleFactory.createBubbleFor(fti, HierarchyDisplay.this));
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
//	//@Override
//	public void processDrop(Bubble receiver, Bubble received) {
//		Logger.debug("HierarchyDisplay.removeBubble ");
//		
//		
//		
//		topicBubbles.remove(new Long(received.getFTI().getTopicID()));
//		dragController.unregisterDropController(received.getDropController());
//		removeObj(received);
//		
//		manager.getTopicCache().executeCommand(received.getTopic(),new SaveTagtoTopicCommand(received.getTopic(),receiver.getTopic(),currentRoot), 
//				new StdAsyncCallback(ConstHolder.myConstants.save()){
//					//@Override
//					public void onSuccess(Object result) {
//						super.onSuccess(result);						
//					}			
//		});		
//		
//		
//	}

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

		//System.out.println("Setting "+objects.size()+" islands to zoom level "+currentScale);

		for (Iterator iter = objects.iterator(); iter.hasNext();) {

			Bubble bubble = (Bubble) iter.next();

			bubble.zoomToScale(currentScale);		

		}


	}

	public void update(Topic t, AbstractCommand command) {
		// TODO Auto-generated method stub
		
	}

	public void navigateTo(FullTopicIdentifier fti) {
		manager.bringUpChart(fti);
	}

	public void removeTopicBubble(TopicBubble received) {
		topicBubbles.remove(new Long(received.getFTI().getTopicID()));
		dragController.unregisterDropController(received.getDropController());
		removeObj(received.getWidget());
	}

	public Topic getCurrentRoot() {		
		return currentRoot;
	}

	public Manager getManager() {		
		return manager;
	}

}
