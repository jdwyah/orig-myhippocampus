package com.aavu.client.gui.timeline;

import java.util.Date;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.dhtmlIslands.Island;
import com.aavu.client.gui.dhtmlIslands.RemembersPosition;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class Ether extends ViewPanel {

	
	private int currentSlot;

	private int height;

	private int intervalTop;

	private int lastDepth;
	private int lastKey;


	private Manager manager;

	/**
	 * track the last x position along each row
	 */
	private int[] slots;
	

	private int zoneStart;

	public Ether(Manager manager,int width, int height) {
		super();
		this.manager = manager;
		this.height = height;
		init();


		setPixelSize(width, height);
		setDoYTranslate(false);
	}

	public void add(TimeLineObj tlo, int depth, int key) {	

		System.out.println("-----------------");
		System.out.println("add "+tlo+" depth "+depth+" key "+key);

		if(depth == lastDepth && key == lastKey){

		}else{

			//System.out.println("!= NEW zone "+key+" "+TreeOfTime.getLabelForDepth(depth,key,tlo.getDate())+" LastZone start "+zoneStart);

			zoneStart += getXSpan();			

			newSection(depth,key,tlo.getDate(),zoneStart);
			
			
		}


		double pct = TreeOfTime.getPctAtDepth(depth, tlo.getDate());

		int left = (int) (getXSpan() * pct) + zoneStart;

		currentSlot = getBestSlotFor(left,tlo.getTopic().getTopicTitle());

		int top = currentSlot * getYPush();

		System.out.println("add at pct "+pct+" top "+top+" left "+left);

		newObject(manager,tlo,left,top);
		

		lastDepth = depth;
		lastKey = key;
	}


	protected abstract void newObject(Manager manager,TimeLineObj tlo, int left, int top);

	protected abstract void newSection(int depth, int key, Date date, int left) ;

	
	//@Override
	protected void objectHasMoved(RemembersPosition o, int halfWidth, int halfHeight, int centerX, int centerY) {
		
		
			int left = (int) (centerX - halfWidth/currentScale);
			int top = (int) (centerY - halfHeight/currentScale);
			int right = (int) (centerX + halfWidth/currentScale);
			int bottom = (int) (centerY + halfHeight/currentScale);
			
			if(isWithin(o,left,top,right,bottom)){
				setVisible(o.getWidget().getElement(), true);
			}else{
				setVisible(o.getWidget().getElement(), false);
			}

	}

	public boolean isWithin(RemembersPosition rp,int winLeft, int winTop, int winRight, int winBottom){

		return (rp.getLeft() + 100 > winLeft
				&&
				rp.getLeft() < winRight
				&&
				rp.getTop() + getYPush() > winTop
				&&
				rp.getTop() < winBottom);
	}

	private int getBestSlotFor(int left, String string) {
		int i = 0;
		for (; i < slots.length; i++) {
			int lastLeftForThisSlot = slots[i];

			//System.out.println("gb "+i+" "+lastLeftForThisSlot+" "+left);
			if(lastLeftForThisSlot < left){
				//PEND MED weak 10 * #letters = width assumption
				slots[i] = left + 10*string.length();
				System.out.println("choose "+i);
				return i;
			}			
		}
		System.out.println("fail!!!!!!!!");
		return i;
	}

	public int getNumberOfSlots() {
		return slots.length;
	}

	public void init() {
		intervalTop = height - 20;
		lastDepth = 0;
		lastKey = 0;		
		slots = new int[(int)Math.floor(height / getYPush()) - 1];
		zoneStart = -getXSpan();
		for (int i = 0; i < slots.length; i++) {
			slots[i] = zoneStart -1;//leave room for 0's
		}
	}

	protected int getHeight() {
		return height;
	}

	protected int getIntervalTop() {
		return intervalTop;
	}

	protected abstract int getXSpan();

	protected abstract int getYPush();
}
