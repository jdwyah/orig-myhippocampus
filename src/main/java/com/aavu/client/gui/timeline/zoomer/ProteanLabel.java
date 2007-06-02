package com.aavu.client.gui.timeline.zoomer;

import java.util.Date;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.timeline.draggable.LabelWrapper;
import com.google.gwt.i18n.client.DateTimeFormat;

public class ProteanLabel extends LabelWrapper {
	
	//PENG MED unfortunate probably screw labels up a bit at the hour scale
	private static final long SMIDGE_MIN = 600; // bump DEC 31 11:59 to Jan 1 etc
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
	 * 'units' will be equal to 1/higherScale (1 level more detailed than the zoom scale)
	 * 
	 * @param d2
	 * @param currentScale
	 * @param index
	 * @param format
	 */
	public void setCenter(final Date d2, final double higherScale,final int index,final DateTimeFormat format) {
		
		long mod = d2.getTime() % TimeLineObj.scale((long) (1/higherScale));
		long modded = d2.getTime() - mod;
		if(index > 1){			
			System.out.println("smidge "+new Date(TimeLineObj.scale(SMIDGE_MIN))+" mod "+mod+" modD "+new Date(mod));
			modded += TimeLineObj.scale(SMIDGE_MIN);
		}		
		
		Date newD = new Date(modded + TimeLineObj.scale((long) (idx/higherScale)));
				
		System.out.println(idx+"d2 "+d2+" "+(idx/higherScale)+" "+newD+" "+TimeLineObj.getLeftForDate(newD));
		int llleft = TimeLineObj.getLeftForDate(newD);				
		setLeft(llleft);
		setText(format.format(newD));
		
	}
}
