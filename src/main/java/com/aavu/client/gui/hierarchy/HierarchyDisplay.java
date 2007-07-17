package com.aavu.client.gui.hierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.LoadFinishedListener;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.ext.JSUtil;
import com.aavu.client.gui.ocean.SpatialDisplay;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.gui.ocean.dhtmlIslands.OceanLabel;
import com.aavu.client.gui.ocean.dhtmlIslands.RemembersPosition;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.allen_sauer.gwt.dragdrop.client.PickupDragController;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class HierarchyDisplay extends ViewPanel implements SpatialDisplay {

	private static final int THETA_START = 0;
	private static final double THETA_INCR_START = .628;
	private static final double THETA_DECR = .9;
	private static final int SPIRAL_SCALE = 250;
	private static final double PHI_SQUARED = Math.pow(1.61803399, 2);


	private double thetaIncr = THETA_INCR_START;

	private DropController backdropDropController;

	// <long,TopicBubble>
	/**
	 * Not all bubbles, just topic bubbles. If you want all, use objects.
	 */
	private Map topicBubbles = new HashMap();

	private Topic currentRoot;

	private PickupDragController dragController;

	private Manager manager;

	private double theta;
	private OceanLabel backdropLabel;

	public HierarchyDisplay(Manager manager) {
		super();
		this.manager = manager;

		// PEND MED NOTE: GWT bug? w/o this, we don't seem to import/compile this
		System.out.println("BubbleF " + BubbleFactory.class);


		addStyleName("H-Hierarchy");

		// passing (this,true) kinda worked, but we'd often miss entry events
		// which led to problems.
		dragController = new PickupDragController(null, true);
		// dragController.setDragProxyEnabled(true);

		backdropDropController = new BackdropDropController(this);
		dragController.registerDropController(backdropDropController);

		setDoZoom(true);

		decorate();

		getFocusBackdrop().addClickListener(new BackdropClickListener());

		DOM.setStyleAttribute(getElement(), "position", "absolute");
		setBackground(currentScale);
	}

	private void decorate() {
		backdropLabel = new OceanLabel("HippoCampus Ocean", -200, 0);

		makeThisADragHandle(backdropLabel);

		JSUtil.disableSelect(backdropLabel.getElement());
		addObject(backdropLabel);
	}

	private void decorateFor(Topic t) {
		System.out.println("adding label " + t.getTitle());
		backdropLabel.setText(t.getTitle());
		addObject(backdropLabel);
	}

	/**
	 * if their position is unset, space them out incrementally in latitude
	 * 
	 * @param fti
	 */
	private void addBubble(TopicDisplayObj bubble) {

		if (bubble.getLeft() == -1 || bubble.getLeft() == 0) {
			// System.out.println(fti.getTopicTitle()+"incr unsetLatitude
			// "+unsetLatitude+" "+fti.getLatitudeOnIsland()+"
			// "+fti.getLongitudeOnIsland());
			// bubble.setTop(unsetLatitude);
			// unsetLatitude += UNSET_LAT_INCR;

			placeNextSpiral(bubble);
		}

		bubble.addMouseWheelListener(this);

		dragController.makeDraggable(bubble.getWidget());

		if (bubble.getDropController() != null) {
			dragController.registerDropController(bubble.getDropController());
		}

		addObject(bubble);

		topicBubbles.put(new Long(bubble.getIdentifier().getTopicID()), bubble);

		bubble.zoomToScale(currentScale);

	}

	/**
	 * This spiral has the polar-coordinate (r, q) equation r = 2 (q)/p and describes mollusk-shell
	 * spirals, galaxy arms of stars, and other phenomena.
	 * http://www.edn.com/index.asp?layout=article&articleid=CA89630 r = phi^2 (theta) / pie
	 * 
	 * could change to an archimedean spiral http://www.liutaiomottola.com/myth/scroll.htm
	 * 
	 * then convert to rect coords
	 */
	private void placeNextSpiral(TopicDisplayObj bubble) {

		double r = SPIRAL_SCALE * PHI_SQUARED * theta / Math.PI;

		System.out.println(bubble.getTitle() + " r " + r + " theta " + theta + " l "
				+ (r * Math.cos(theta)) + " " + (r * Math.sin(theta)));

		bubble.setLeft((int) (r * Math.cos(theta)));
		bubble.setTop((int) (r * Math.sin(theta)));


		theta += thetaIncr;

		// reduce incr over the life of the spiral, otherwise they start really spreading out.
		thetaIncr *= THETA_DECR;
	}

	public boolean centerOn(Topic topic) {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public void clear() {

		theta = THETA_START;
		thetaIncr = THETA_INCR_START;

		for (Iterator iterator = objects.iterator(); iterator.hasNext();) {

			RemembersPosition rp = (RemembersPosition) iterator.next();

			if (rp instanceof TopicDisplayObj) {
				TopicDisplayObj bubble = (TopicDisplayObj) rp;
				dragController.unregisterDropController(bubble.getDropController());
			}

		}

		topicBubbles.clear();

		// needs to be after we iter over objects
		super.clear();

		System.out.println("HDisplay.clear() DragController" + dragController);
	}

	/**
	 * on drag finish, update the lat/long of the topicBubble & save
	 */
	public void dragFinished(Widget dragging) {
		System.out.println("HierarchyDisplay.Drag Finished");
		TopicDisplayObj tb = (TopicDisplayObj) dragging;

		tb.processDrag(currentScale);

		// if(tb.possibleMoveOccurred(currentScale)){
		//			
		//						
		// System.out.println("HierarchyDisplay.Drag Finished Saving "+tb.getLeft()+"
		// "+tb.getTop());
		//			
		// manager.getTopicCache().saveTopicLocationA(currentRoot.getId(),
		// tb.getFTI().getTopicID(), tb.getTop(), tb.getLeft(),
		// new StdAsyncCallback("SaveLatLong"){});
		//			
		//				
		// }
	}

	// public void dragged(Widget dragging, int newX, int newY) {
	// // TODO Auto-generated method stub
	//		
	// }

	// @Override
	protected int getHeight() {
		return Window.getClientHeight();
	}

	public Widget getWidget() {
		return this;
	}

	// @Override
	protected int getWidth() {
		return Window.getClientWidth();
	}

	/**
	 * create a dummy FullFTI
	 */
	public void growIsland(Topic thought) {

		TopicBubble bubble = (TopicBubble) topicBubbles.get(new Long(thought.getId()));

		if (null != bubble) {
			bubble.grow();
		} else {
			System.out.println("Grow " + thought.getId() + " " + GWT.getTypeName(thought));



			TopicDisplayObj newBubble = BubbleFactory.createBubbleFor(thought, currentRoot, this);
			addBubble(newBubble);
			redraw();

		}

	}

	public void load(final Topic t, final LoadFinishedListener loadFinished) {
		System.out.println("Hdisplay.load DragController" + dragController);

		clear();

		decorateFor(t);

		loadTopicOcc(t);
		loadChildTopics(t, loadFinished);

	}

	private void loadTopicOcc(Topic t) {
		System.out.println("Load " + t + " occs " + t.getOccurences().size());

		for (Iterator iterator = t.getOccurences().iterator(); iterator.hasNext();) {
			TopicOccurrenceConnector owl = (TopicOccurrenceConnector) iterator.next();

			// System.out.println("yoooooo");
			//
			// System.out.println("B " + BubbleFactory.class);
			// System.out.println("D " + new BubbleFactory());
			addBubble(BubbleFactory.createBubbleFor(owl, this));

		}
	}

	private void loadChildTopics(final Topic t, final LoadFinishedListener loadFinished) {
		manager.getTopicCache().getTopicsWithTag(t.getId(),
				new StdAsyncCallback(ConstHolder.myConstants.getRoot_async()) {
					// @Override
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

						if (loadFinished != null) {
							loadFinished.loadFinished();
						}
					}
				});
	}

	// @Override
	protected void postZoomCallback(double currentScale) {

		setIslandsToZoom();

	}

	// //@Override
	// public void processDrop(Bubble receiver, Bubble received) {
	// Logger.debug("HierarchyDisplay.removeBubble ");
	//		
	//		
	//		
	// topicBubbles.remove(new Long(received.getFTI().getTopicID()));
	// dragController.unregisterDropController(received.getDropController());
	// removeObj(received);
	//		
	// manager.getTopicCache().executeCommand(received.getTopic(),new
	// SaveTagtoTopicCommand(received.getTopic(),receiver.getTopic(),currentRoot),
	// new StdAsyncCallback(ConstHolder.myConstants.save()){
	// //@Override
	// public void onSuccess(Object result) {
	// super.onSuccess(result);
	// }
	// });
	//		
	//		
	// }

	public void removeIsland(long id) {
		// TODO Auto-generated method stub

	}

	// @Override
	protected void setBackground(double scale) {
		int pix = (int) (scale * 100);
		if (pix > 1600 || pix < 6) {
			return;
		}
		if (pix > 400) {
			DOM.setStyleAttribute(getElement(), "backgroundImage", "url(" + ImageHolder.getImgLoc()
					+ "ocean" + pix + ".jpg)");
		} else {
			DOM.setStyleAttribute(getElement(), "backgroundImage", "url(" + ImageHolder.getImgLoc()
					+ "ocean" + pix + ".png)");
		}
	}

	private void setIslandsToZoom() {

		// System.out.println("Setting "+objects.size()+" islands to zoom level
		// "+currentScale);

		for (Iterator iter = objects.iterator(); iter.hasNext();) {

			RemembersPosition rp = (RemembersPosition) iter.next();

			rp.zoomToScale(currentScale);

		}

	}

	public void update(Topic t, AbstractCommand command) {
		System.out.println("hierarchy display update " + t);

		TopicDisplayObj b = (TopicDisplayObj) topicBubbles.get(new Long(t.getId()));
		if (b != null) {
			b.update(t);
		} else {
			Logger.log("HierarchyDisplay: Told to Update NonExist");
		}
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

	private class BackdropClickListener implements ClickListener {

		public void onClick(Widget sender) {

			if (DOM.eventGetCtrlKey(getFocusBackdrop().getLastEvent())) {

				int x = DOM.eventGetClientX(getFocusBackdrop().getLastEvent());
				int y = DOM.eventGetClientY(getFocusBackdrop().getLastEvent());

				ContextMenu p = new ContextMenu(getManager());
				p.setPopupPosition(x, y);
				p.show();

			}
		}
	}

}
