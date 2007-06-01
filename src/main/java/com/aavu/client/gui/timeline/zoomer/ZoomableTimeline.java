package com.aavu.client.gui.timeline.zoomer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.gwm.client.event.GFrameEvent;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.dhtmlIslands.RemembersPosition;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.gui.timeline.HippoTimeline;
import com.aavu.client.gui.timeline.draggable.TLOWrapper;
import com.aavu.client.service.Manager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ZoomableTimeline extends ViewPanel implements HippoTimeline {

	private static final DateTimeFormat weekFormat = DateTimeFormat.getFormat("MMM, d yyyy");
	
	
	private static List backGroundList = new ArrayList();
	static final String IMG_LOC = "img/timeline/";
	
	static final double MIN_HOUR = 60;	
	static final double MIN_DAY = MIN_HOUR*24;	
	static final double MIN_WEEK = MIN_DAY*7;
	static final double MIN_MONTH = MIN_DAY*30;
	static final double MIN_YEAR = MIN_DAY*365;
	static final double MIN_DECADE = MIN_YEAR*10;
	static final double MIN_CENTURY = MIN_YEAR*100;
	static final double MIN_MILL = MIN_YEAR*1000;
	
	private static List zoomList = new ArrayList();
	static{
		//zoomList.add(new Double(1));
		zoomList.add(new Double(1/MIN_HOUR));
		zoomList.add(new Double(1/MIN_DAY));
		zoomList.add(new Double(1/MIN_WEEK));
		zoomList.add(new Double(1/MIN_MONTH));
		zoomList.add(new Double(1/MIN_YEAR));
		zoomList.add(new Double(1/MIN_DECADE));
		zoomList.add(new Double(1/MIN_CENTURY));
		
		zoomList.add(new Double(1/MIN_MILL));
		
		System.out.println("\n\n\nMin Day "+MIN_DAY+" "+zoomList.get(1));
		System.out.println("\n\n\nMin Year "+MIN_YEAR+" "+zoomList.get(4));
		System.out.println("\n\n\nMin Decade "+MIN_DECADE+" "+zoomList.get(5));
		
	}

	static{
		backGroundList.add("minute");
		
		backGroundList.add("hour");
		backGroundList.add("day");
		backGroundList.add("week");
		backGroundList.add("month");
		backGroundList.add("year");
		backGroundList.add("decade");
		backGroundList.add("century");
		
		//backGroundList.add("mill");
	}


	private int height;
	private Manager manager;
	private int width;
	private Label whenlabel;
	
	public ZoomableTimeline(Manager manager,int width, int height, CloseListener window){
		super();
		this.manager = manager;
		this.height = height;
		this.width = width;
		init();


		setPixelSize(width, height);
		setDoYTranslate(false);
		
		setDoZoom(true);
		
		currentScale = ((Double) zoomList.get(4)).doubleValue();
		
		decorate();
		
		setBackground(currentScale);

	}
	private void decorate() {
		whenlabel = new Label();
		add(whenlabel,width/2,height - 40);
	}
	//@Override
	protected RemembersPosition getTLORepr(Manager manager, TimeLineObj tlo,
			int left, int top) {		
		return new TLOWrapper(manager,tlo,left,top);
	}
	public Widget getWidget() {
		return this;
	}
	public void init() {		
//		startDate = null;
//		intervalTop = height - 20;
//		lastDepth = 0;
//		lastKey = 0;		
//		slots = new int[(int)Math.floor(height / getYPush()) - 1];		
//		for (int i = 0; i < slots.length; i++) {
//			slots[i] = -getXSpan() -1;//leave room for 0's
//		}
	}
	
//	private static List double[] zooms = {
//			1/MIN_HOUR,
//			1/MIN_DAY,
//			1/MIN_WEEK,
//			1/MIN_MONTH,
//			1/MIN_YEAR,
//			1/MIN_DECADE,
//			1/MIN_CENTURY};
	
	public void load(List timelines) {
		clear();
		
		for (Iterator iter = timelines.iterator(); iter.hasNext();) {
			TimeLineObj tlo = (TimeLineObj) iter.next();
			
			
			
			int top = (int) (Math.random()*(double)height);
			
			newObject(manager, tlo, top);
			
		}
		
		
//		TimeLineObj last = (TimeLineObj) timelines.get(timelines.size()-1);
//		System.out.println("last "+last);
//		if(last != null){
//			System.out.println("move to "+last.getLeft());
//			moveTo(-last.getLeft(), 0);
//		}
		
		redraw();
	}
	//@Override
	protected int getWidth(){
		return width;
	}
	//@Override
	protected int getHeight(){
		return height;
	}
	
	//@Override
	protected int getXSpread() {
		return 600;
	}
	//@Override
	protected void moveOccurredCallback() {
		
		//600, otherwise 1 pixel per SCALE length
		
		int index = zoomList.indexOf(new Double(currentScale)); 
		double higherScale = ((Double)zoomList.get(index)).doubleValue();

		
		int ii = (int) (-getCurbackX()/higherScale/getXSpread());
		Date d2 = TimeLineObj.getDateForLeft(ii);
		
		System.out.println("curback "+-getCurbackX()+" "+"      "+d2+" ii "+ii+" "+higherScale+" "+backGroundList.get(index));
		
		whenlabel.setText(weekFormat.format(d2));
	}

	/**
	 *
	 * @param manager
	 * @param tlo
	 * @param time
	 * @param top
	 */
	protected void newObject(Manager manager,TimeLineObj tlo, int top) {
		System.out.println("add "+tlo.getDate()+" "+tlo.getLeft()+" "+TimeLineObj.getDateForLeft(tlo.getLeft()));
		addObject(getTLORepr(manager,tlo,tlo.getLeft(),top));
	}
	
	public void resize(GFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
	//@Override
	protected void setBackground(double scale) {
		
		int index = zoomList.indexOf(new Double(scale)); 

		
		String img = (String) backGroundList.get(index);
		
		System.out.println("setBack "+scale+" "+index+" "+img);		
		
		//decade = 60 * 3
		DOM.setStyleAttribute(getElement(), "background-position","80px 0px");
		DOM.setStyleAttribute(getElement(), "backgroundImage","url("+IMG_LOC+img+".png)");		
	}
	
	
	public void zoom(int upDown) {

		double oldScale = currentScale;

		
		int index = zoomList.indexOf(new Double(oldScale)); 

		System.out.println("index "+index+" next  "+(index+upDown));
		index += upDown;
		
		index = index < 0 ? 0 : index;
		
		//TODO !!!!!!!
		//NOTE the 2 this makes us unable to go up to Millenium, which is 
		//only there to give us a higherScale
		index = index >= zoomList.size() - 1 ? zoomList.size() - 2 : index;
		
		currentScale = ((Double)zoomList.get(index)).doubleValue();
	
		finishZoom(oldScale);
	}
	
	//@Override
	public void zoomIn() {

		zoom(-1);		
	}
	//@Override
	public void zoomOut() {
		zoom(1);		
	}
}
