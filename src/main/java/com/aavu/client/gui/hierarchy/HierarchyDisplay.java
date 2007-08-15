package com.aavu.client.gui.hierarchy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.GUIManager;
import com.aavu.client.gui.LoadFinishedListener;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.ext.DblClickListener;
import com.aavu.client.gui.ext.JSUtil;
import com.aavu.client.gui.ext.PopupWindow;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class HierarchyDisplay extends ViewPanel implements SpatialDisplay, WindowResizeListener {

	private static final int THETA_START = 0;
	private static final double THETA_INCR_START = .628;
	private static final double THETA_DECR = .97;
	private static final double THETA_INCR_MIN = .06;
	private static final int SPIRAL_SCALE = 250;
	private static final double PHI_SQUARED = Math.pow(1.61803399, 2);

	private int CHILD_LOAD_BATCH_SIZE = 20;

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
	private GUIManager guiManager;
	private int i = 0;

	private PopupWindow progressWindow;
	private int height;
	private int width;

	private class FullBoundaryPanel extends AbsolutePanel {
		// @Override
		public int getOffsetHeight() {
			return Window.getClientHeight();
		}

		// @Override
		public int getOffsetWidth() {
			return Window.getClientWidth();
		}
	}



	public HierarchyDisplay(Manager manager, GUIManager map) {
		super();
		this.manager = manager;
		this.guiManager = map;

		// PEND MED NOTE: GWT bug? w/o this, we don't seem to import/compile this
		System.out.println("BubbleF " + BubbleFactory.class);


		addStyleName("H-Hierarchy");

		// passing (this,true) kinda worked, but we'd often miss entry events
		// which led to problems.


		// We're supposed to be able to use new PickupDragController(null, true);
		// to get the RootPanel, but our RootPanel is returning offsetHeight 0
		// so we'll use our panel. Only trick with that is that we now need to
		// capture resizes.
		//
		// sorry, just kidding. using 'this' sorts the drag n drop bc height is now no longer 0, but
		// window resize is still borked bc dropTarget doesn't get redone.
		//
		width = Window.getClientWidth();
		height = Window.getClientHeight();
		Window.addWindowResizeListener(this);

		dragController = new PickupDragController(this, true);

		// dragController.setDragProxyEnabled(true);

		backdropDropController = new BackdropDropController(this);
		dragController.registerDropController(backdropDropController);

		// AbsolutePanel boundaryPanel = dragController.getBoundaryPanel();
		// WidgetArea boundaryArea = new WidgetArea(boundaryPanel, null);

		// MouseDragHandler.logN("Boundary Panel: " + GWT.getTypeName(boundaryPanel));
		//
		// MouseDragHandler.logN("Boundary Area: " + boundaryArea.getLeft() + " "
		// + boundaryArea.getRight() + " " + boundaryArea.getTop() + " "
		// + boundaryArea.getBottom());

		setDoZoom(true);

		decorate();

		BackdropClickListener backdropL = new BackdropClickListener();
		getFocusBackdrop().addDblClickListener(backdropL);
		getFocusBackdrop().addClickListener(backdropL);

		DOM.setStyleAttribute(getElement(), "position", "absolute");
		setBackground(currentScale);
	}

	private void decorate() {
		backdropLabel = new OceanLabel(" ", -200, 0);

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

			Logger.debug("register drop controller " + bubble.getTitle());
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

		// System.out.println(bubble.getTitle() + " r " + r + " theta " + theta + " l "
		// + (r * Math.cos(theta)) + " " + (r * Math.sin(theta)));

		// System.out.println(bubble.getTitle() + " " + thetaIncr + i);
		i++;

		bubble.setLeft((int) (r * Math.cos(theta)));
		bubble.setTop((int) (r * Math.sin(theta)));



		theta += thetaIncr;

		// reduce incr over the life of the spiral, otherwise they start really spreading out.
		thetaIncr *= THETA_DECR;
		thetaIncr = (thetaIncr < THETA_INCR_MIN) ? THETA_INCR_MIN : thetaIncr;
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
	public void growIsland(Topic thought, int[] lnglat) {

		System.out.println("grow " + thought + " " + thought.getId() + " " + thought.getLatitude()
				+ " " + thought.getLongitude());

		TopicBubble bubble = (TopicBubble) topicBubbles.get(new Long(thought.getId()));

		if (null != bubble) {
			System.out.println("grow found ");
			bubble.grow();
		} else {
			System.out.println("Grow not found" + thought.getId() + " " + GWT.getTypeName(thought));



			TopicDisplayObj newBubble = BubbleFactory.createBubbleFor(thought, currentRoot, lnglat,
					this);
			addBubble(newBubble);
			redraw();

		}

	}

	public void load(final Topic t, final LoadFinishedListener loadFinished) {
		System.out.println("Hdisplay.load DragController" + dragController);

		try {
			clear();
		} catch (Exception e) {
			System.out.println("exception cleaing HD " + e);
		}


		decorateFor(t);

		loadTopicOcc(t);

		loadChildTopics(t, loadFinished);


		// Logger.debug("drag controller "+dragController.getMovableWidget());

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

		currentRoot = t;

		zoomTo(1);
		centerOn(0, 0);

		redraw();

	}

	private void loadChildTopics(final Topic t, final LoadFinishedListener loadFinished) {
		manager.getTopicCache().getTopicsWithTag(t.getId(),
				new StdAsyncCallback(ConstHolder.myConstants.getRoot_async()) {
					// @Override
					public void onSuccess(Object result) {
						super.onSuccess(result);

						final List all_ftis = (List) result;

						final Iterator iterator = all_ftis.iterator();


						final ProgressBar progressBar = new ProgressBar(10, ProgressBar.SHOW_TEXT);
						progressBar.setProgress(0);
						progressBar.setText(ConstHolder.myConstants.loading_islands());


						progressWindow = manager.showProgressBar(progressBar);

						Timer t = new Timer() {

							public void run() {


								addNextX(iterator, CHILD_LOAD_BATCH_SIZE, all_ftis.size(), this,
										progressBar, loadFinished);

							}
						};
						t.schedule(200);

					}
				});
	}

	private void addNextX(Iterator iterator, int batchSize, int totalSize, Timer timer,
			ProgressBar progressBar, LoadFinishedListener loadFinished) {

		int count = 0;

		int curProg = progressBar.getProgress();

		while (iterator.hasNext() && count < batchSize) {

			FullTopicIdentifier fti = (FullTopicIdentifier) iterator.next();
			addBubble(BubbleFactory.createBubbleFor(fti, HierarchyDisplay.this));

			curProg += (100 * (1.0 / totalSize));
			progressBar.setProgress(curProg);

			count++;
		}

		redraw();

		if (iterator.hasNext()) {
			timer.schedule(100);
		} else {
			progressWindow.hide();

			if (loadFinished != null) {
				loadFinished.loadFinished();
			}
		}
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
		AbstractBubble bubble = (AbstractBubble) topicBubbles.get(new Long(id));
		System.out.println("removing bubble " + bubble);
		removeTopicBubble(bubble);
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

		if (t == null) {
			Logger.error("update null");
			try {
				throw new Exception();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		TopicDisplayObj b = (TopicDisplayObj) topicBubbles.get(new Long(t.getId()));
		if (b != null) {
			System.out.println("update " + b);
			b.update(t);
		} else {
			System.out.println("HD not found " + t + " " + t.getId());
			for (Iterator iterator = topicBubbles.keySet().iterator(); iterator.hasNext();) {
				Long l = (Long) iterator.next();
				System.out.println("cont: " + l);
			}

			// Not necessarily problem. May be that we told a weblink to save, and it
			// said, update my parent, which is this, which doesn't contain this.
			//
			Logger.debug("HierarchyDisplay: Told to Update NonExist " + t.getId());
		}

		if (t.equals(currentRoot)) {
			backdropLabel.setText(t.getTitle());
		}

	}

	public void navigateTo(FullTopicIdentifier fti) {
		guiManager.hideCurrentHover();
		// HoverManager.hideCurrentHover();
		manager.bringUpChart(fti);
	}

	public void removeTopicBubble(AbstractBubble received) {
		topicBubbles.remove(new Long(received.getIdentifier().getTopicID()));
		dragController.unregisterDropController(received.getDropController());
		removeObj(received.getWidget());

	}

	public Topic getCurrentRoot() {
		return currentRoot;
	}

	public Manager getManager() {
		return manager;
	}

	private class BackdropClickListener implements ClickListener, DblClickListener {

		public void onClick(Widget sender) {

			if (getFocusBackdrop().getLastClickEventCtrl()) {
				openContextMenu();
			} else {
				guiManager.hideCurrentHover();
			}
		}

		private void openContextMenu() {
			int x = getFocusBackdrop().getLastClickClientX();
			int y = getFocusBackdrop().getLastClickClientY();

			ContextMenu p = new ContextMenu(getManager(), HierarchyDisplay.this, x, y);
			p.show(x, y);
		}

		public void onDblClick(Widget sender) {
			openContextMenu();
		}
	}



	public void showHover(TopicIdentifier ti) {
		guiManager.showHover(ti);
	}

	public void onWindowResized(int width, int height) {
		this.width = width;
		this.height = height;
		setPixelSize(width, height);

		// Need top reset the BackdropDropController DropTargetInfo
		System.out.println("WINDOW RESIZE! " + width + " " + height);
	}


}
