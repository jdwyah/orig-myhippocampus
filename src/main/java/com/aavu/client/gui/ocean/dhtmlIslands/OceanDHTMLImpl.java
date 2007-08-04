package com.aavu.client.gui.ocean.dhtmlIslands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.gwtwidgets.client.ui.PNGImage;
import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.LoadFinishedListener;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.ext.GUIEffects;
import com.aavu.client.gui.ext.JSUtil;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ocean.SpatialDisplay;
import com.aavu.client.service.MindscapeManager;
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
public class OceanDHTMLImpl extends ViewPanel implements SpatialDisplay, DragEventListener {

	private static final int CLOUD_MOVE_MSEC = 7000;

	// static final String IMG_LOC =
	// "../../../../webapp/img/simplicityHighRes/";

	private static final int CLOUD_MOVE_PX = 2000;

	private static final int CLOUD_REMOVE = 8000;

	// static final String IMG_LOC = "img/oldmapStyle/";

	private static final double NO_ISLAND_DRAG_AT_THIS_ZOOM = 8;
	private static final int SHOW_TOPICS_AT_ZOOM = 3;

	private DragHandler dragHandler;

	private boolean focussed = false;

	private Map islands = new HashMap();

	private Panel leftCloud;

	private MindscapeManager manager;

	private OceanKeyBoardListener oceanKeyboardListener;

	private PopupWindow progressWindow;

	private Panel rightCloud;

	private Island selectedIsland;

	private LoadFinishedListener loadFinishedListener;

	public OceanDHTMLImpl(MindscapeManager manager) {
		super();
		this.manager = manager;

		addStyleName("H-Ocean");

		dragHandler = new DragHandler(this);

		// sinkEvents(Event.ONSCROLL);

		// Decorations that will be obcured by the focus panel
		//
		decorate();

		setDoZoom(true);

		clouds();

		/*
		 * override the AbsolutePanel position: relative otherwise we got a left: 8px; top: 8px;
		 */
		DOM.setStyleAttribute(getElement(), "position", "absolute");
		setBackground(currentScale);
		// url("../img/bluecheck-bullet-14.gif");
	}

	/**
	 * turn the tag stats into Islands.
	 * 
	 * NOTE: this takes a while and will lead to "this script is running slowly" warnings on IE if
	 * we're not careful. Avoid these by using Timers and doing it in pieces.
	 * 
	 * NOTE 2: doing it piecemeal also allows the progress bar to update. Before, it would never get
	 * the work thread until it was all over.
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

		final ProgressBar progressBar = new ProgressBar(10, ProgressBar.SHOW_TEXT);
		progressBar.setProgress(0);

		progressBar.setText(ConstHolder.myConstants.loading_islands());

		progressWindow = manager.showProgressBar(progressBar);

		Timer t = new Timer() {
			public void run() {
				addFrom(tagStats, 0, 10, progressBar);

			}
		};
		t.schedule(200);

	}

	private void addFrom(final TagStat[] tagStats, final int start, final int num,
			final ProgressBar progressBar) {
		TagStat stat = null;
		int i = 0;
		System.out.println("AddFrom " + start + " to " + (start + num));
		for (i = start; i < tagStats.length && i < start + num; i++) {
			stat = tagStats[i];

			Island isle = new Island(stat, this, manager.getUser(), manager);

			addIsland(stat, isle);

		}

		// could be null if tagStat.length == 0, ie new user
		if (stat != null) {
			progressBar.setProgress((int) (100 * i / (double) tagStats.length));
			progressBar.setText(stat.getTagName());
		}

		System.out.println("i " + i + " " + tagStats.length);
		if (i >= tagStats.length) {
			done(tagStats.length);
		} else {
			Timer t = new Timer() {
				public void run() {
					addFrom(tagStats, start + num, num, progressBar);
				}
			};
			t.schedule(100);
		}

	}

	private void addIsland(TagInfo info, Island isle) {

		dragHandler.add(isle, this);

		// isle.addMouseListener(this);
		isle.addKeyboardListener(oceanKeyboardListener);

		isle.addMouseWheelListener(this);

		// dragHandler.add(isle,isle,banner);
		// add(banner,isle.getLeft(),isle.getTop());

		addObject(isle);

		// GUIEffects.appear(isle,4000);
		islands.put(new Long(info.getTagId()), isle);

		// TODO a bit redundant, but otherwise the div sizes don't get set right
		// and banners get clipped
		isle.zoomToScale(currentScale);
	}

	/**
	 * 
	 * centerOn will move th map to center on the given topic. If it's an island, that's easy. If
	 * it's only a topic, loop through its islands and find the one closest to out current center.
	 * Otherwise we'll jump all over the place.
	 * 
	 * 
	 * invert this equation to find the x for a given center int centerX = (int)((-curbackX +
	 * halfWidth)/currentScale); int centerY = (int)((-curbackY + halfHeight)/currentScale);
	 * 
	 * return - should return true if it finds a good thing to center on
	 */
	public boolean centerOn(Topic topic) {

		int width = Window.getClientWidth();
		int height = Window.getClientHeight();

		int halfWidth = width / 2;
		int halfHeight = height / 2;

		Topic centerTopic = null;

		// TODO TAG
		if (9 == 9) {
			// if(topic instanceof Tag){
			centerTopic = topic;
		} else {

			// Loop through all tags and find the one closest to our
			// current center
			//
			int centerX = getCenterX(currentScale, width);
			int centerY = getCenterY(currentScale, height);

			double distance = Double.MAX_VALUE;

			Set s = topic.getTypesAsTopics();

			for (Iterator iter = s.iterator(); iter.hasNext();) {
				Topic tag = (Topic) iter.next();

				double dist = Math.pow(tag.getLatitude() - centerY, 2);
				dist += Math.pow(tag.getLongitude() - centerX, 2);
				dist = Math.sqrt(dist);

				if (dist < distance) {
					distance = dist;
					centerTopic = tag;
				}
			}

		}

		if (centerTopic == null) {
			return false;
		} else {

			Island isle = (Island) islands.get(new Long(centerTopic.getId()));

			if (isle != null) {

				// System.out.println("isle "+isle.getLeft()+" "+isle.getTop());
				// System.out.println("topic center on
				// "+isle.getCenterXAtScale()+ " "+isle.getCenterYAtScale());

				centerOn((int) isle.getCenterXAtScale(), (int) isle.getCenterYAtScale());
			}
		}
		return true;
	}

	private void clearClouds() {

		GUIEffects.move(leftCloud, -CLOUD_MOVE_PX, 0, CLOUD_MOVE_MSEC);
		GUIEffects.move(rightCloud, CLOUD_MOVE_PX, 0, CLOUD_MOVE_MSEC);

		GUIEffects.removeInXMilSecs(leftCloud, CLOUD_REMOVE);
		GUIEffects.removeInXMilSecs(rightCloud, CLOUD_REMOVE);
	}

	/**
	 * clear & remove old islands Actually, just set them to invisible
	 */
	private void clearIslands() {
		for (Iterator iter = islands.keySet().iterator(); iter.hasNext();) {
			Long e = (Long) iter.next();

			((Widget) islands.get(e)).setVisible(false);
		}
	}

	private void clouds() {
		leftCloud = new SimplePanel();

		PNGImage lc = new PNGImage(ConstHolder.myConstants.clouds_src(), 120, 120);
		lc.setStyleName("H-Clouds");
		leftCloud.setStyleName("H-Clouds");
		lc.setWidth("100%");
		lc.setHeight("100%");
		leftCloud.add(lc);

		rightCloud = new SimplePanel();
		PNGImage rc = new PNGImage(ConstHolder.myConstants.clouds_src(), 120, 120);
		rc.setStyleName("H-Clouds");
		rightCloud.setStyleName("H-Clouds");
		rc.setWidth("100%");
		rc.setHeight("100%");
		rightCloud.add(rc);

		leftCloud.setWidth("70%");
		leftCloud.setHeight("100%");

		rightCloud.setWidth("70%");
		rightCloud.setHeight("100%");

		add(leftCloud, -40, 0);
		add(rightCloud, 400, 0);

	}

	private void decorate() {

		OceanLabel lab = new OceanLabel("Hippo<BR>Campus<BR>Ocean", 300, 300);
		JSUtil.disableSelect(lab.getElement());
		addObject(lab);

		// addObject(new DashedBox(-1000,140,3000,60));

		oceanKeyboardListener = new OceanKeyBoardListener(this);

		getFocusBackdrop().addKeyboardListener(oceanKeyboardListener);

	}

	private void done(final int size) {
		progressWindow.close();
		clearClouds();
		Timer t = new Timer() {
			public void run() {
				manager.fireOceanLoaded(size);
			}
		};
		t.schedule(CLOUD_REMOVE);

		loadFinishedListener.loadFinished();
	}

	public void dragFinished(Widget dragging) {
		Island island = (Island) dragging;

		if (island.possibleMoveOccurred()) {
			islandMoved(island.getStat().getTagId(), island.getLeft(), island.getTop());
		}
	}

	public void dragged(Widget dragging, int newX, int newY) {
	}

	public Widget getWidget() {
		return this;
	}

	protected int getWidth() {
		return Window.getClientWidth();
	}

	protected int getHeight() {
		return Window.getClientHeight();
	}

	public void growIsland(Topic tag, int[] lnglat) {
		Island isle = (Island) islands.get(new Long(tag.getId()));

		if (isle == null) {
			System.out.println("was null");

			// center the island
			System.out.println("CENTERING THE ISLAND");

			tag.setLatitude(getCenterY());
			tag.setLongitude(getCenterX());

			TagStat tagStat = new TagStat(tag);
			Island newIsle = new Island(tagStat, this, manager.getUser(), manager);
			addIsland(tagStat, newIsle);

			islandMoved(tag.getId(), tag.getLongitude(), tag.getLatitude());

			// forces redraw
			moveByDelta(0, 0);

		} else {
			System.out.println(".grow()");
			isle.grow();
		}
	}

	public void islandClicked(TopicIdentifier ident, int num_clicks, Island island) {

		System.out.println("CLICKED focussed " + focussed + " ID  " + ident);

		// if(focussed){
		// showOcean();
		// focussed = false;
		// }else{
		// manager.showTopicsForTag(tagId);
		// focussed = true;
		// }

		if (num_clicks > 1) {
			manager.bringUpChart(ident);
		} else {
			manager.showPreviews(ident.getTopicID());
		}

		if (selectedIsland != null) {
			selectedIsland.setSelected(false);
		}
		island.setSelected(true);
		selectedIsland = island;
	}

	/**
	 * TODO double ASYNC!
	 * 
	 * @param islandID
	 * @param longitude
	 * @param latitude
	 */
	public void islandMoved(long islandID, final int longitude, final int latitude) {

		System.out.println("isleMovedTo " + longitude + " " + latitude + " SAVING");

		manager.getTopicCache().getTopicByIdA(islandID, new StdAsyncCallback("GetTopicById") {
			public void onSuccess(Object result) {
				super.onSuccess(result);
				Topic t = (Topic) result;
				t.setLatitude(latitude);
				t.setLongitude(longitude);

				// manager.getTopicCache().executeCommand(t,new
				// SaveLatLongCommand(t,latitude,
				// longitude),
				// new StdAsyncCallback("SaveLatLong"){});
			}
		});

	}

	public void load(Topic topic, LoadFinishedListener loadFinishedListener) {
		this.loadFinishedListener = loadFinishedListener;
		manager.getTopicCache().getTagStats(new StdAsyncCallback("Get Tag Stats") {
			public void onSuccess(Object result) {
				super.onSuccess(result);
				TagStat[] tagStats = (TagStat[]) result;

				System.out.println("TagStat Result " + tagStats);

				addAll(tagStats);
			}
		});
	}

	/**
	 * All the islands need to check to see if they're in the visible window. If so, and we're
	 * zoomed in enough, show the topics.
	 * 
	 * all others should turn topics off.
	 * 
	 */
	// @Override
	protected void objectHasMoved(RemembersPosition o, int halfWidth, int halfHeight, int centerX,
			int centerY) {

		if (o instanceof Island) {
			Island island = (Island) o;
			int left = (int) (centerX - halfWidth / currentScale);
			int top = (int) (centerY - halfHeight / currentScale);
			int right = (int) (centerX + halfWidth / currentScale);
			int bottom = (int) (centerY + halfHeight / currentScale);
			if (island.isWithin(left, top, right, bottom) && currentScale >= SHOW_TOPICS_AT_ZOOM) {
				island.showTopics();
			} else {
				island.removeTopics();
			}

		}
	}

	// @Override
	protected void postZoomCallback(double currentScale) {

		setIslandsToZoom();

		if (currentScale >= NO_ISLAND_DRAG_AT_THIS_ZOOM) {
			dragEnabled = false;

		} else {
			dragEnabled = true;
		}
		dragHandler.setIslandDrag(dragEnabled);

		manager.zoomTo(currentScale);
	}

	public void removeIsland(long id) {
		Island isle = (Island) islands.get(new Long(id));
		if (isle != null) {

			GUIEffects.fadeAndRemove(isle, 3000);

			islands.remove(new Long(id));
			objects.remove(isle);
		}
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

		// System.out.println("Setting all islands to zoom level
		// "+currentScale);

		for (Iterator iter = islands.keySet().iterator(); iter.hasNext();) {
			Long e = (Long) iter.next();

			Island island = (Island) islands.get(e);

			island.zoomToScale(currentScale);

		}

	}

	// @Override
	protected void unselect() {
		if (selectedIsland != null) {
			selectedIsland.setSelected(false);
			selectedIsland = null;
		}
		manager.unselect();
	}

	public void update(Topic t, AbstractCommand command) {
		Island isle = (Island) islands.get(new Long(t.getId()));

		if (isle != null) {
			System.out.println("Ocean.update redraw Title:" + t.getTitle() + " ID " + t.getId());
			isle.redraw(t);
		} else {
			System.out.println("Ocean.update new grow Title:" + t.getTitle() + " ID " + t.getId());
			growIsland(t, null);
		}
	}

}
