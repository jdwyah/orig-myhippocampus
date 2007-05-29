package com.aavu.client.gui.timeline.draggable;

import java.util.Date;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.dhtmlIslands.RemembersPosition;

import com.aavu.client.service.Manager;

public class DetailsEther extends Ether {

	public DetailsEther(Manager manager, int width, int height) {
		super(manager, width, height);
		addStyleName("H-TimelineDetail");
	}

	//@Override
	protected int getYPush() {
		return 30;
	}

	//@Override
	protected int getXSpan() {
		return 250;
	}

	//@Override
	protected RemembersPosition getTLORepr(Manager manager, TimeLineObj tlo,
			int left, int top) {		
		return new TLOWrapper(manager,tlo,left,top);
	}

	//@Override
	protected void newSection(int depth, int key, Date date, int left) {
		System.out.println("time linebg height "+getHeight());
		addObject(new TimelineBG(depth,key,date,left,getXSpan(),getHeight(),this));
		
		LabelWrapper startIntervalLabel = new LabelWrapper(TreeOfTime.getLabelForDepth(depth,key,date),left,getIntervalTop()); 			
		addObject(startIntervalLabel);
	}

	//@Override
	protected void newObject(Manager manager,TimeLineObj tlo, int left, int top) {
		addObject(getTLORepr(manager,tlo,left,top));
	}

}
