package com.aavu.client.gui.timeline.zoomer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.gui.ocean.dhtmlIslands.RemembersPosition;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.gui.timeline.HippoTimeline;
import com.aavu.client.gui.timeline.draggable.TLOWrapper;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ZoomableTimeline extends ViewPanel implements HippoTimeline {


	private static List backGroundList = new ArrayList();
	static final String IMG_POSTFIX = "timeline/";

	private static List labelFormatters = new ArrayList();


	static final double MIN_HOUR = 60;
	static final double MIN_DAY = MIN_HOUR * 24;
	static final double MIN_WEEK = MIN_DAY * 7;
	static final double MIN_MONTH = MIN_DAY * 30.43;

	static final double MIN_YEAR = MIN_DAY * 365.25;
	static final double MIN_DECADE = MIN_YEAR * 10;
	static final double MIN_CENTURY = MIN_YEAR * 100;


	static final double MIN_3CENTURY = MIN_CENTURY * 3;
	static final double MIN_3MONTH = MIN_DAY * 91.31;
	static final double MIN_3YEAR = MIN_YEAR * 3;


	static final double MIN_MILL = MIN_YEAR * 1000;



	private static final int NUM_LABELS = 5;
	/**
	 * This needs to correspond to the width of the background images. It's basically an extra zoom
	 */
	private static final int X_SPREAD = 600;

	private static List zoomList = new ArrayList();
	static {
		// zoomList.add(new Double(1));
		zoomList.add(new Double(1 / MIN_HOUR));
		zoomList.add(new Double(1 / MIN_DAY));
		zoomList.add(new Double(1 / MIN_WEEK));

		zoomList.add(new Double(1 / MIN_MONTH));
		zoomList.add(new Double(1 / MIN_3MONTH));

		zoomList.add(new Double(1 / MIN_YEAR));
		zoomList.add(new Double(1 / MIN_3YEAR));
		zoomList.add(new Double(1 / MIN_DECADE));
		zoomList.add(new Double(1 / MIN_CENTURY));

		zoomList.add(new Double(1 / MIN_3CENTURY));

		zoomList.add(new Double(1 / MIN_MILL));
		//		
		// System.out.println("\n\n\nMin Day "+MIN_DAY+" "+zoomList.get(1));
		// System.out.println("\n\n\nMin Year "+MIN_YEAR+" "+zoomList.get(4));
		// System.out.println("\n\n\nMin Decade "+MIN_DECADE+" "+zoomList.get(5));
	}

	static {

		// need new image
		backGroundList.add("hour");

		backGroundList.add("hour");
		backGroundList.add("day");
		backGroundList.add("week");
		backGroundList.add("3way");
		backGroundList.add("month");
		backGroundList.add("3way");
		backGroundList.add("year");
		backGroundList.add("decade");// 1970 30 yr offset
		backGroundList.add("3century");// 1970 20 yr offset

	}

	static {
		labelFormatters.add(DateTimeFormat.getFormat("HH:mm"));
		labelFormatters.add(DateTimeFormat.getFormat("HH"));
		labelFormatters.add(DateTimeFormat.getFormat("MMM d"));
		labelFormatters.add(DateTimeFormat.getFormat("MMM, d yyyy"));// week
		labelFormatters.add(DateTimeFormat.getFormat("MMMM yyyy"));
		labelFormatters.add(DateTimeFormat.getFormat("yyyy"));
		labelFormatters.add(DateTimeFormat.getFormat("yyyy"));
		labelFormatters.add(DateTimeFormat.getFormat("yyyy"));
		labelFormatters.add(DateTimeFormat.getFormat("yyyy"));
		labelFormatters.add(DateTimeFormat.getFormat("yyyy"));
		labelFormatters.add(DateTimeFormat.getFormat("yyyy"));
	}



	private int height;

	private List labelList = new ArrayList();
	private ProteanLabel ll;
	private Image magBig;
	private Image magSmall;

	private Manager manager;
	private GWTSortedMap sorted = new GWTSortedMap();
	private Label whenlabel;
	private int width;
	private int yEnd;
	private int[] ySlots;

	private boolean ySlotsDirty = false;
	private int ySpread;

	private int yStart;

	public ZoomableTimeline(Manager manager, int width, int height, CloseListener window) {
		super();
		this.manager = manager;
		this.height = height;
		this.width = width;
		init();


		setPixelSize(width, height);
		setDoYTranslate(false);

		setDoZoom(true);

		currentScale = ((Double) zoomList.get(5)).doubleValue();

		createDecorations();
		drawHUD();
		setBackground(currentScale);


	}

	public void add(List timelines) {

		System.out.println("!!!!!Zoom add " + timelines.size() + " sorted size " + sorted.size());



		for (Iterator iter = timelines.iterator(); iter.hasNext();) {
			TimeLineObj tlo = (TimeLineObj) iter.next();
			sorted.put(tlo, null);
		}

		super.clear();


		System.out.println("addObj " + sorted.size());

		for (Iterator iter = sorted.keySet().iterator(); iter.hasNext();) {

			TimeLineObj tlo = (TimeLineObj) iter.next();

			// int top = (int) (Math.random()*(double)height);

			int slot = getBestSlotFor(tlo);

			int top = yStart + (slot * ySpread);
			RemembersPosition rp = getTLORepr(manager, tlo, tlo.getLeft(), top);

			if (slot < 0) {
				rp.getWidget().setVisible(false);
			}
			addObject(rp);


		}

		for (int i = -NUM_LABELS; i < NUM_LABELS; i++) {
			ProteanLabel ll = new ProteanLabel(i, yStart - 15);
			labelList.add(ll);
			addObject(ll);
		}

		if (!sorted.isEmpty()) {
			TimeLineObj last = (TimeLineObj) sorted.getKeyList().get(sorted.size() - 1);
			System.out.println("last " + last);
			if (last != null) {
				// System.out.println("move to "+last.getLeft()+"
				// "+TimeLineObj.getDateForLeft(last.getLeft()));
				centerOn(last.getLeft(), 0);
			}
		}

		updateLabels();
		redraw();
	}

	// @Override
	public void clear() {
		super.clear();
		sorted.clear();

	}

	private void createDecorations() {
		whenlabel = new Label();

		magBig = ConstHolder.images.magnifyingBig().createImage();
		magBig.addClickListener(new ClickListener() {
			public void onClick(Widget arg0) {
				zoomIn();
			}
		});

		magSmall = ConstHolder.images.magnifyingSmall().createImage();
		magSmall.addClickListener(new ClickListener() {
			public void onClick(Widget arg0) {
				zoomOut();
			}
		});

		add(magSmall);
		add(whenlabel);
		add(magBig);

	}

	private void drawHUD() {
		int center = width / 2 - 50;
		int y = yEnd + 30;
		setWidgetPosition(magSmall, center - 40, y - 15);
		setWidgetPosition(whenlabel, center, y);
		setWidgetPosition(magBig, center + 70, y - 15);
	}

	/**
	 * return -1 if we can't fit
	 * 
	 * @param left
	 * @param string
	 * @return
	 */
	private int getBestSlotFor(TimeLineObj tlo) {
		int i = 0;
		// PEND MED weak 11 * #letters = width assumption
		int mywidth = 11 * (int) (tlo.getTopicIdentifier().getTopicTitle().length() / (double) getXSpread() / currentScale);
		for (; i < ySlots.length; i++) {
			int lastLeftForThisSlot = ySlots[i];

			// System.out.println("gb "+i+" "+lastLeftForThisSlot+" "+tlo.getLeft()+" mywid
			// "+mywidth);
			if (lastLeftForThisSlot < tlo.getLeft()) {

				ySlots[i] = (int) (tlo.getLeft() + mywidth);
				// System.out.println("Ether.choose "+i);
				return i;
			}
		}
		// System.out.println("Ether.fail!!!!!!!!");
		return -1;
	}

	// @Override
	protected int getHeight() {
		return height;
	}



	// @Override
	protected RemembersPosition getTLORepr(Manager manager, TimeLineObj tlo, int left, int top) {
		TLOWrapper tlow = new TLOWrapper(manager, tlo, left, top);
		tlow.addMouseWheelListener(this);
		return tlow;
	}

	public Widget getWidget() {
		return this;
	}

	// @Override
	protected int getWidth() {
		return width;
	}

	// @Override
	protected int getXSpread() {
		return X_SPREAD;
	}

	private String getZoomStr(double scale) {
		int index = zoomList.indexOf(new Double(scale));
		return (String) backGroundList.get(index);
	}

	private void init() {
		yStart = 25;
		yEnd = height - 60;
		ySpread = 15;

		ySlots = new int[(yEnd - yStart) / ySpread];
		initYSlots();
	}

	private void initYSlots() {
		for (int i = 0; i < ySlots.length; i++) {
			ySlots[i] = Integer.MIN_VALUE;
		}
	}

	// @Override
	protected void moveOccurredCallback() {

		// 600, otherwise 1 pixel per SCALE length



		updateLabels();

		ySlotsDirty = false;

		// setWidgetPosition(ll.getWidget(), ll.getLeft(), ll.getTop());
	}


	protected void objectHasMoved(RemembersPosition o, int halfWidth, int halfHeight, int centerX,
			int centerY) {
		if (ySlotsDirty) {
			if (o instanceof TLOWrapper) {
				TLOWrapper tlw = (TLOWrapper) o;


				int slot = getBestSlotFor(tlw.getTlo());

				// System.out.println("best top "+slot+" "+tlw.getTlo().getTopic().getTopicTitle()+"
				// "+(yStart + (slot * ySpread)));

				if (slot < 0) {
					o.getWidget().setVisible(false);
				} else {
					tlw.setTop(yStart + (slot * ySpread));

					o.getWidget().setVisible(true);
				}

			}
		}

	}



	// @Override
	protected void postZoomCallback(double currentScale) {
		updateLabels();
	}

	public void resize(int newWidth, int newHeight) {

		width = newWidth;
		height = newHeight;

		init();

		setPixelSize(width, height);

		drawHUD();

		redraw();

	}


	// @Override
	protected void setBackground(double scale) {

		int index = zoomList.indexOf(new Double(scale));


		String img = (String) backGroundList.get(index);

		System.out.println("setBack " + scale + " " + index + " " + img);

		DOM.setStyleAttribute(getElement(), "backgroundImage", "url("
				+ ImageHolder.getImgLoc(IMG_POSTFIX) + img + ".png)");
	}

	private void updateLabels() {

		int index = zoomList.indexOf(new Double(currentScale));

		int ii = getCenterX();
		Date d2 = TimeLineObj.getDateForLeft(ii);

		whenlabel.setText(((DateTimeFormat) labelFormatters.get(3)).format(d2));

		// System.out.println("ZoomableTimeline.updateLabels curback "+-getCurbackX()+" "+" "+d2+"
		// ii "+ii+" "+backGroundList.get(index));

		DateTimeFormat format = (DateTimeFormat) labelFormatters.get(index);
		for (Iterator iterator = labelList.iterator(); iterator.hasNext();) {
			ProteanLabel label = (ProteanLabel) iterator.next();
			label.setCenter(d2, index, format);
		}
	}

	public void zoom(int upDown) {

		double oldScale = currentScale;


		int index = zoomList.indexOf(new Double(oldScale));

		System.out.println("ZoomableTL zoom: index " + index + " next  " + (index + upDown));
		index += upDown;

		index = index < 0 ? 0 : index;

		// TODO !!!!!!!
		// NOTE the 2 this makes us unable to go up to Millenium, which is
		// only there to give us a higherScale
		index = index >= zoomList.size() - 1 ? zoomList.size() - 2 : index;

		currentScale = ((Double) zoomList.get(index)).doubleValue();

		System.out.println("ZoomableTL cur " + currentScale + " old " + oldScale + " "
				+ currentScale / oldScale);
		System.out.println("ZoomableTL cur " + getZoomStr(currentScale) + " old "
				+ getZoomStr(oldScale));

		// force a re-jiggering of the yslots on the next redraw()
		ySlotsDirty = true;
		initYSlots();

		finishZoom(oldScale);

	}

	// @Override
	public void zoomIn() {

		zoom(-1);
	}

	// @Override
	public void zoomOut() {
		zoom(1);
	}
}
