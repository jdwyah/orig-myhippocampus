package com.aavu.client.gui.timeline.draggable;

import java.util.Date;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.dhtmlIslands.RemembersPosition;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class OverViewEther extends Ether {
	
	private transient static final DateTimeFormat df = DateTimeFormat.getFormat("MMM dd yyyy");	

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

	protected void newSection(int depth, int key, Date date, int left) {		
		addObject(new TimelineBG(depth,key,date,left,getXSpan(),getHeight(),this));
		
		LabelWrapper startIntervalLabel = new LabelWrapper(df.format(date),left,getIntervalTop()); 			
		addObject(startIntervalLabel);
		
	}

	//@Override
	protected void newObject(Manager manager,TimeLineObj tlo, int left, int top) {
		
	}
	
}
