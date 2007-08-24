package com.aavu.client.gui.timeline.draggable;

import java.util.Date;
import java.util.Iterator;

import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.ocean.dhtmlIslands.Island;
import com.aavu.client.gui.ocean.dhtmlIslands.RemembersPosition;
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

	private Date startDate;
	
	private GWTSortedMap zoneStarts = new GWTSortedMap();

	private int width;


	public Date getStartDate() {
		return startDate;
	}

	public Ether(Manager manager,int width, int height) {
		super();
		this.manager = manager;
		this.height = height;
		this.width = width;
		init();


		setPixelSize(width, height);
		setDoYTranslate(false);
	}

	public void add(TimeLineObj tlo, int depth, int key) {	

		//System.out.println("Ether.-----------------");
		//System.out.println("Ether.add "+tlo+" depth "+depth+" key "+key);

		if(startDate == null){
			startDate = tlo.getDate();
		}
		
		if(depth == lastDepth && key == lastKey){

		}else{

			//System.out.println("!= NEW zone "+key+" "+TreeOfTime.getLabelForDepth(depth,key,tlo.getDate())+" LastZone start "+zoneStart);

			int zoneStart = newSection(depth,key,tlo.getDate());
			
			zoneStarts.put(tlo.getDate(), new Integer(zoneStart));
			
			
			//System.out.println("Ether.Zone Start "+tlo.getDate()+" "+zoneStart);
			
		}


		double pct = TreeOfTime.getPctAtDepth(depth, tlo.getDate());

		
		int left = getRelLeft(pct);

		currentSlot = getBestSlotFor(left,tlo.getTopicIdentifier().getTopicTitle());
		if(currentSlot > 0){
			int top = currentSlot * getYPush();

			//System.out.println("Ether.add at pct "+pct+" top "+top+" left "+left);

			newObject(manager,tlo,left,top);
		}
		

		lastDepth = depth;
		lastKey = key;
	}


	/**
	 * get the date that we're currently centered on
	 * 
	 * loop through the stored zones and do some averaging to create a new date
	 * 
	 * @return
	 */
	protected Date getCenterDate() {
		int left = -getCurbackX() + width/2;
		
		//System.out.println("Ether.getCenterDate "+left);
		
		Date selected = getStartDate();
		int selectedLeft = 0;
		
		Date curStart = null;				
		int curLeft = 0; 
		
		for (Iterator iterator = zoneStarts.getKeyList().iterator(); iterator.hasNext();) {
			curStart = (Date) iterator.next();			
			curLeft = ((Integer) zoneStarts.get(curStart)).intValue();
			
			//System.out.println("Ether.zs "+curStart+" "+curLeft);
			
			if(curLeft <= left){
				//selected = zoneStart;
				selectedLeft = curLeft;
				selected = new Date(curStart.getTime());
			}else{				
				break;
			}			
		}
		
		//average
		if(curLeft != selectedLeft){
			double pct = (left - selectedLeft) / (double)(curLeft - selectedLeft);
			long diff = curStart.getTime() - selected.getTime(); 
			Date rtn = new Date((long) (selected.getTime() + (pct*diff)));
			return rtn;
		}
		
		//System.out.println("Ether.end of road "+curStart);
		return curStart;
	}
	/**
	 * move our view so that we're centered on the param date
	 * 
	 * loop throught the zone starts to get bounding zones, then average.
	 * 
	 * @param d
	 */
	protected void centerOn(Date d){
		
		Date selected = null;
		Date zoneStart = null;
		for (Iterator iterator = zoneStarts.getKeyList().iterator(); iterator.hasNext();) {
			zoneStart = (Date) iterator.next();
			
			//System.out.println("Ether.centerOn "+d+" zs "+zoneStart+" ");
			if(zoneStart != null){

				if(zoneStart.compareTo(d) <= 0){
					selected = zoneStart;
				}else{
					break;
				}
			}
		}
		//System.out.println("Ether.Selected "+selected+" next "+zoneStart);
		
		Integer left = (Integer) zoneStarts.get(selected);
		Integer nextLeft = (Integer) zoneStarts.get(zoneStart);
				
		//System.out.println("Ether.left "+left+" "+nextLeft);
				
		if(null != left){
			int val = left.intValue();			
			if(zoneStart.getTime() - selected.getTime() != 0){
				double pct = (d.getTime() - selected.getTime()) / (double)(zoneStart.getTime() - selected.getTime());
				val += pct * (nextLeft.intValue() - left.intValue());	
			}
			
			//System.out.println("Moving to "+(-val+(width/2))+" val "+val+" ");
			moveTo(-val+(width/2),0);
			
		}
				
	}
 
	
	protected abstract int getRelLeft(double pct);

	protected abstract void newObject(Manager manager,TimeLineObj tlo, int left, int top);

	protected abstract int newSection(int depth, int key, Date date) ;

	/**
	 * PEND MED performance question, is this logic helpful so that we can setVisible? or 
	 * a performance suck bc of the calcs. 
	 * Could probably speed this up regardless. L/R only etc
	 */
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

	/**
	 * return -1 if we can't fit 
	 * 
	 * @param left
	 * @param string
	 * @return
	 */
	private int getBestSlotFor(int left, String string) {
		int i = 0;
		for (; i < slots.length; i++) {
			int lastLeftForThisSlot = slots[i];

			//System.out.println("gb "+i+" "+lastLeftForThisSlot+" "+left);
			if(lastLeftForThisSlot < left){
				//PEND MED weak 10 * #letters = width assumption
				slots[i] = left + 10*string.length();
				//System.out.println("Ether.choose "+i);
				return i;
			}			
		}
		//System.out.println("Ether.fail!!!!!!!!");
		return -1;
	}

	public int getNumberOfSlots() {
		return slots.length;
	}

	public void init() {
		startDate = null;
		intervalTop = height - 20;
		lastDepth = 0;
		lastKey = 0;		
		slots = new int[(int)Math.floor(height / getYPush()) - 1];		
		for (int i = 0; i < slots.length; i++) {
			slots[i] = -getXSpan() -1;//leave room for 0's
		}
	}

	protected int getHeight() {
		return height;
	}
	protected int getWidth() {
		return width;
	}
	protected int getIntervalTop() {
		return intervalTop;
	}

	protected abstract int getXSpan();

	protected abstract int getYPush();

}
