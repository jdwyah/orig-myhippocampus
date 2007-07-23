package com.aavu.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.gui.ocean.dhtmlIslands.EventBackdrop;
import com.aavu.client.gui.ocean.dhtmlIslands.RemembersPosition;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelVelocity;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.Widget;

public abstract class ViewPanel extends AbsolutePanel implements MouseListener,
		SourcesChangeEvents, MouseWheelListener {

	protected int backX = 0;
	protected int backY = 0;
	private ChangeListenerCollection changeCollection = new ChangeListenerCollection();
	private int curbackX = 0;

	private int curbackY = 0;

	protected double currentScale = 1;

	private boolean doYTranslate = true;

	private boolean doZoom;

	private boolean dragging;

	private int dragStartX;

	private int dragStartY;

	private EventBackdrop focusBackdrop;

	protected boolean dragEnabled = true;
	private int lastx;
	private int lasty;
	protected List objects = new ArrayList();

	public ViewPanel() {
		super();

		setStyleName("H-ViewPanel");

		focusBackdrop = new EventBackdrop();
		makeThisADragHandle(focusBackdrop);
		add(focusBackdrop, 0, 0);

		/*
		 * override the AbsolutePanel position: relative otherwise we got a left: 8px; top: 8px;
		 */
		DOM.setStyleAttribute(getElement(), "position", "absolute");

	}

	public void addChangeListener(ChangeListener listener) {
		changeCollection.add(listener);
	}

	public void addObject(RemembersPosition rp) {
		add(rp.getWidget(), (int) rp.getLeft(), rp.getTop());
		objects.add(rp);
	}

	protected void centerOn(int x, int y) {

		// System.out.println("centering on "+x+" "+ y);

		int width = getWidth();
		int height = getHeight();

		int halfWidth = width / 2;
		int halfHeight = height / 2;

		int left = (int) (x * currentScale * getXSpread()) - halfWidth;
		int top = (int) (y * currentScale) - halfHeight;

		// SYSTEM.OUT.PRINTLN("P.X "+P.X+" HW "+HALFWIDTH+" "+LEFT);
		// System.out.println("left "+left+" top "+top);

		// intuitively this is (left - curbackX) but things are reversed
		// int dx = left + curbackX;
		// int dy = top + curbackY;

		int dx = left + curbackX;
		int dy = top + curbackY;
		moveBy(dx, dy);

	}

	protected abstract int getWidth();

	protected abstract int getHeight();

	/**
	 * 
	 */
	protected void centerOnMouse() {

		int halfWidth = getWidth() / 2;
		int halfHeight = getHeight() / 2;

		int dx = lastx - halfWidth;
		int dy = lasty - halfHeight;

		// System.out.println("last x "+lastx+" mousex "+0+" curbackx "+curbackX+" dx "+dx);
		// System.out.println("last y "+lasty+" mousey "+0+" curbacky "+curbackY+" dy "+dy);

		moveBy(dx, dy);

	}

	/**
	 * don't let a regular clear() happen or you'll lose the focusBackdrop
	 */
	public void clear() {
		// System.out.println("calling our clear()");
		for (Iterator iter = objects.iterator(); iter.hasNext();) {
			Widget w = (Widget) iter.next();
			remove(w);
			iter.remove();
		}
	}

	private void endDrag() {
		if (dragging) {
			// System.out.println("(old)back x "+backX+" cur(new) "+curbackX);
			// System.out.println("(old)back y "+backY+" cur(new) "+curbackY);
			backX = curbackX;
			backY = curbackY;
		}
		dragging = false;
	}

	protected void finishZoom(double oldScale) {

		setBackground(currentScale);

		int width = getWidth();
		int height = getHeight();

		int centerX = getCenterX(oldScale, width);
		int centerY = getCenterY(oldScale, height);

		// moveTo(centerX, centerY);

		int halfWidth = width / 2;
		int halfHeight = height / 2;
		reCenter(centerX, centerY, currentScale, halfWidth, halfHeight);

		redraw();

		postZoomCallback(currentScale);

		redraw();

	}

	public int getBackX() {
		return backX;
	}

	public int getBackY() {
		return backY;
	}

	protected int getCenterX() {
		return getCenterX(currentScale, getWidth());
	}

	protected int getCenterX(double scaleToUse, int width) {
		int halfWidth = width / 2;
		int centerX = (int) ((-curbackX + halfWidth) / (scaleToUse * getXSpread()));

		// System.out.println("get Center X "+scaleToUse+" "+(-curbackX + halfWidth)+" "+centerX);
		return centerX;
	}

	protected int getCenterY() {
		return getCenterY(currentScale, getHeight());
	}

	protected int getCenterY(double scaleToUse, int height) {
		int halfHeight = height / 2;
		int centerY = (int) ((-curbackY + halfHeight) / scaleToUse);
		return centerY;
	}

	public int getCurbackX() {
		return curbackX;
	}

	public int getCurbackY() {
		return curbackY;
	}

	public boolean isDoYTranslate() {
		return doYTranslate;
	}

	public boolean isDoZoom() {
		return doZoom;
	}

	/**
	 * This moves the background and then sets the back position. Call this when you want a move.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void moveBy(int dx, int dy) {
		moveByDelta(dx, dy);

		// this was normally set in finishDrag()
		backX = curbackX;
		backY = curbackY;
	}

	/**
	 * Internal move method. Doesn't actually 'finish' the move. This helps us make dragging
	 * smoother.
	 * 
	 * Use moveBy() unless you'll finish yourself.
	 * 
	 * Takes dx, dy as SouthEast (+,+) NW (-,-)
	 * 
	 * @param dx
	 * @param dy
	 */
	protected void moveByDelta(int dx, int dy) {
		curbackX = -dx + backX;

		double yScale = 1;
		if (isDoYTranslate()) {
			curbackY = -dy + backY;
			yScale = currentScale;
		}

		DOM.setStyleAttribute(getElement(), "backgroundPosition", curbackX + "px " + curbackY
				+ "px");

		int width = getWidth();
		int height = getHeight();

		int halfWidth = width / 2;
		int halfHeight = height / 2;

		int centerX = getCenterX(currentScale, width);
		int centerY = getCenterY(yScale, height);

		// System.out.println("centerX "+centerX+" centerY "+centerY);

		for (Iterator iter = objects.iterator(); iter.hasNext();) {
			Object o = iter.next();
			RemembersPosition rp = (RemembersPosition) o;

			// System.out.println("found: "+GWT.getTypeName(rp));

			// System.out.println("Left "+isle.getLeft()+" Top "+isle.getTop());
			// System.out.println("cur "+curbackX+" cury "+curbackY);

			// setWidgetPosition(rp.getWidget(),(int)((rp.getLeft()+curbackX)*currentScale),
			// (int)((rp.getTop()+curbackY)*currentScale));

			// System.out.println("move "+rp.getLeft()+" "+(int)((rp.getLeft())*currentScale)+"
			// "+(int)((rp.getLeft())*currentScale*getXSpread())+" cs "+currentScale);
			setWidgetPosition(rp.getWidget(), (int) ((rp.getLeft()) * currentScale * getXSpread())
					+ curbackX, (int) ((rp.getTop()) * yScale) + curbackY);

			objectHasMoved(rp, halfWidth, halfHeight, centerX, centerY);

		}

		moveOccurredCallback();
		// System.out.println("moved "+curbackX+" "+curbackY);

	}

	protected int getXSpread() {
		return 1;
	}

	protected void moveOccurredCallback() {
	}

	/**
	 * Do an absolute move
	 * 
	 * @param x
	 * @param y
	 */
	public void moveTo(int x, int y) {
		int dx = backX - x;
		int dy = backY - y;

		moveByDelta(dx, dy);

		// this was normally set in finishDrag()
		backX = curbackX;
		backY = curbackY;
	}

	/**
	 * Override this if you want object move event processing
	 * 
	 * @param o
	 * @param halfWidth
	 * @param halfHeight
	 * @param centerX
	 * @param centerY
	 */
	protected void objectHasMoved(RemembersPosition o, int halfWidth, int halfHeight, int centerX,
			int centerY) {
	}

	public void onMouseDown(Widget sender, int x, int y) {
		// System.out.println("down "+(sender instanceof Island) +" x "+x+" y "+y +" "+dragging);

		// System.out.println("mouse downd " + GWT.getTypeName(sender) + " x " + x + " y " + y + " "
		// + sender.getAbsoluteLeft() + " " + sender.getAbsoluteTop());

		// if (!dragEnabled || sender instanceof FocusPanel) {
		dragging = true;
		// }

		dragStartX = x + sender.getAbsoluteLeft();
		dragStartY = y + sender.getAbsoluteTop();

		// System.out.println("down " + GWT.getTypeName(sender) + "dsx " + dragStartX + " dsy "
		// + dragStartY + " x " + x + " y " + y + " " + sender.getAbsoluteLeft() + " "
		// + sender.getAbsoluteTop());

		unselect();
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
		endDrag();
	}

	public void onMouseMove(Widget sender, int x, int y) {
		lastx = x + sender.getAbsoluteLeft();
		lasty = y + sender.getAbsoluteTop();
		int dx = dragStartX - x - sender.getAbsoluteLeft();
		int dy = dragStartY - y - sender.getAbsoluteTop();

		// System.out.println("move "+(sender instanceof Island) +" x "+x+" y "+y +" "+dragging);
		if (dragging) {
			moveByDelta(dx, dy);
			changeCollection.fireChange(this);
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		// System.out.println("up " + GWT.getTypeName(sender) + "dsx " + dragStartX + " dsy "
		// + dragStartY + " x " + x + " y " + y + " " + sender.getAbsoluteLeft() + " "
		// + sender.getAbsoluteTop());

		// System.out.println("up "+(sender instanceof Island) +" x "+x+" y "+y +" "+dragging);
		endDrag();
	}

	public void onMouseWheel(Widget sender, MouseWheelVelocity velocity) {
		if (velocity.isSouth()) {
			zoomOut();
		} else {
			centerOnMouse();
			zoomIn();
		}
	}

	protected void postZoomCallback(double currentScale) {
	}

	private void reCenter(int centerX, int centerY, double scale, int halfWidth, int halfHeight) {

		// System.out.println("recenter\nback X "+backX+" backy "+backY);
		// System.out.println("center X "+centerX+" cy "+centerY+" scale "+scale);

		// System.out.println("hw "+halfWidth+" hh "+halfHeight);
		// backX = halfWidth - halfWidth/currentScale;

		int newCenterX = (int) (centerX * scale * getXSpread());
		int newCenterY = (int) (centerY * scale);

		// System.out.println("new center X "+newCenterX+" "+newCenterY);

		backX = -(newCenterX - halfWidth);
		backY = -(newCenterY - halfHeight);

		// System.out.println("Newback X "+backX+" NEWbacky "+backY);

	}

	public void redraw() {
		moveBy(0, 0);
	}

	public void removeChangeListener(ChangeListener listener) {
		changeCollection.remove(listener);
	}

	protected abstract void setBackground(double scale);

	public void setDoYTranslate(boolean doYTranslate) {
		this.doYTranslate = doYTranslate;
	}

	/**
	 * Make sure that we're zoomed to 'scale' or higher
	 * 
	 * return the value that we settle on
	 */
	public double ensureZoomOfAtLeast(double scale) {
		if (scale > currentScale) {
			zoomTo(scale);
		}
		return currentScale;
	}

	protected void makeThisADragHandle(Widget widget) {

		if (doZoom) {
			if (widget instanceof SourcesMouseWheelEvents) {
				SourcesMouseWheelEvents wheeler = (SourcesMouseWheelEvents) widget;
				wheeler.addMouseWheelListener(this);
			}
		}
		if (widget instanceof SourcesMouseEvents) {
			SourcesMouseEvents mouser = (SourcesMouseEvents) widget;
			mouser.addMouseListener(this);
		}
	}

	public void setDoZoom(boolean doZoom) {
		this.doZoom = doZoom;
		makeThisADragHandle(focusBackdrop);
	}

	protected void unselect() {

	}

	public void zoomIn() {
		double oldScale = currentScale;

		currentScale *= 2;

		finishZoom(oldScale);
	}

	public void zoomOut() {
		double oldScale = currentScale;

		currentScale /= 2;

		finishZoom(oldScale);
	}

	public void zoomTo(double scale) {
		if (scale == currentScale) {
			return;
		}
		double oldScale = currentScale;

		currentScale = scale;

		finishZoom(oldScale);

	}

	public boolean removeObj(Widget w) {
		System.out.println("ViewPanel.remove " + GWT.getTypeName(w));
		super.remove(w);
		return objects.remove(w);
	}

	public EventBackdrop getFocusBackdrop() {
		return focusBackdrop;
	}

	public int[] getLongLatForXY(int absLeft, int absTop) {

		int oceanLeft = getBackX();
		int oceanTop = getBackY();

		int newLeft = absLeft - oceanLeft;
		int newTop = absTop - oceanTop;

		int lng = (int) (newLeft / currentScale);
		int lat = (int) (newTop / currentScale);

		return new int[] { lng, lat };

	}
}
