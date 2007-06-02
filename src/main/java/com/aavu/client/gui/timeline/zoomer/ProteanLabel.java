package com.aavu.client.gui.timeline.zoomer;

import java.util.Date;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.timeline.draggable.LabelWrapper;
import com.google.gwt.i18n.client.DateTimeFormat;

public class ProteanLabel extends LabelWrapper {
	
	private int idx;

	public ProteanLabel(int idx, int top){
		super("",0,top);
		this.idx = idx;
	}

	public void setLeft(int left) {
		this.left = left;
		
	}

	/**
	 * tell this label that there is a new center. We should update ouselves.
	 * Our idx tells us how many 'units' away from the center we should be. 
	 * 
	 * NOTE, first attempt did 1/higherScale * idx, which seems like it should work 
	 * until you remember leap years cumulative effects of months with different dates etc.
	 * Doing it that way it's too easy to end up with Dec 29 when you wanted Jan 1
	 * 
	 * @param d2
	 * @param currentScale
	 * @param index
	 * @param format
	 */
	public void setCenter(final Date d2, final int index,final DateTimeFormat format) {
				
		Date newD = new Date(d2.getTime());

		switch (index) {
		case 6:
			newD.setYear((newD.getYear() - (newD.getYear() % 10))  + idx*10);	
			newD.setMonth(0);
			newD.setDate(1);
			break;
		case 5:
			newD.setYear(newD.getYear() + idx);	
			newD.setMonth(0);
			newD.setDate(1);
			break;
		case 4:
			newD.setYear(newD.getYear() + idx);
			newD.setMonth(0);
			newD.setDate(1);
			break;
		case 3:
			newD.setMonth(newD.getMonth() + idx);
			newD.setDate(1);
			newD.setHours(0);
			newD.setMinutes(0);	
			break;
		case 2:
			newD.setDate(newD.getDate() + idx);
			newD.setHours(0);
			newD.setMinutes(0);
			break;
		case 1:
			newD.setHours(newD.getHours() + idx);			
			newD.setMinutes(0);
			newD.setSeconds(0);
			break;
		case 0:
			newD.setHours(newD.getHours() + idx);			
			newD.setMinutes(newD.getMinutes() + idx);
			newD.setSeconds(0);
			break;
		default:
			break;
		}
		
		
		//System.out.println(index+" "+idx+"d2 "+d2+" "+newD+" "+TimeLineObj.getLeftForDate(newD));
		
		
		int llleft = TimeLineObj.getLeftForDate(newD);				
		setLeft(llleft);
		setText(format.format(newD));
		
	}
}
