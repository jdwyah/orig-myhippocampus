package com.aavu.client.gui.timeline.draggable;

import java.util.Date;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ocean.dhtmlIslands.RemembersPosition;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.datepicker.DateUtil;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class OverViewEther extends Ether {
	
	private transient static final DateTimeFormat df = DateTimeFormat.getFormat("MMM dd yyyy");	
	private transient static final DateTimeFormat overviewF = DateTimeFormat.getFormat("MMM yyyy");	

	
	public class Dot extends Composite implements RemembersPosition {

		private int left;
		private TimeLineObj tlo;
		private int top;

		public Dot(final TimeLineObj tlo,int left, int top){
			this.tlo = tlo;
			this.left = left;
			this.top = top;

			initWidget(ConstHolder.images.bullet_blue().createImage());
		}

		public int getLeft() {
			return left;
		}

		public int getTop() {
			return top;
		}

		public Widget getWidget() {
			return this;
		}
	}

	private int left;
	private int lastMonth;
	private int lastYear;
	
	public OverViewEther(Manager manager, int width, int height) {
		super(manager, width, height);
		addStyleName("H-TimelineOverview");
	}

	//@Override
	protected int getYPush() {		
		return 5;
	}

	//@Override
	protected int getXSpan() {
		return 100;
	}

	//@Override
	protected RemembersPosition getTLORepr(Manager manager, TimeLineObj tlo,
			int left, int top) {		
		return new Dot(tlo,left,top);
	}

	private int getWidthForDepth(int depth){
		if(depth <=3){
			return 100;
		}else if(depth == 4){
			return 65;			
		}else{
			return 35;
		}
			
	}
	
	protected int newSection(int depth, int key, Date date) {	
		
		addObject(new TimelineBG(depth,key,date,left,getWidthForDepth(depth),getHeight(),this));
		
		
		if(lastYear != date.getYear() || lastMonth != date.getMonth()){
			LabelWrapper startIntervalLabel = new LabelWrapper(overviewF.format(date),left,getIntervalTop() - 20);		 		
			addObject(startIntervalLabel);
		}
		
		LabelWrapper startIntervalLabel = new LabelWrapper(TreeOfTime.getShortLabelForDepth(depth,key,date),left,getIntervalTop());		 		
		addObject(startIntervalLabel);
		
		lastYear = date.getYear();
		lastMonth = date.getMonth();
		
		
		left += getWidthForDepth(depth);
		
		return left;
	}

	//@Override
	protected void newObject(Manager manager,TimeLineObj tlo, int left, int top) {
		
	}

	//@Override
	protected int getRelLeft(double pct) {
		return left;
	}
	protected int getLeft() {
		return getCurbackX();
	}

	public void init() {
		super.init();		
		left = 0;
	}
	//@Override
	protected void setBackground(double scale) {}
}
