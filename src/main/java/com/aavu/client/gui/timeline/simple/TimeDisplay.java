package com.aavu.client.gui.timeline.simple;

import com.aavu.client.domain.dto.TimeLineObj;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;


public class TimeDisplay extends Composite {
	
	private class TimeBar extends Widget {
		private static final int MAX_IMG = 10;
		private int size = 0;
		//private HorizontalPanel panel;
		public TimeBar(){	
			//panel = new HorizontalPanel();
			
			
			Element element = DOM.createDiv();
			//IE can't render < 20px without content
			//http://archivist.incutio.com/viewlist/css-discuss/39150
			DOM.setInnerHTML(element, "&nbsp;");
			setElement(element);
			
			//initWidget(panel);
			setStyleName("H-TimeBar");
		}

		public void incr() {
			size++;
//			if(size < MAX_IMG){
//				panel.add(ConstHolder.images.bullet_blue().createImage());
//			}
			DOM.setStyleAttribute(getElement(), "height", 1*size+"px");			
		}
	}

	private static final int SIZE = 5;
	private static final int HEIGHT = 100;
	private TimeBar[] bars;
	
	public TimeDisplay(int index){
		HorizontalPanel p = new HorizontalPanel();
		p.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);
		bars = new TimeBar[SIZE];
		for(int i = 0; i < SIZE; i++){
			TimeBar tb = new TimeBar();
			p.add(tb);
			bars[i] = tb;
		}		
		initWidget(p);
		setStyleName("H-TimeDisplay");
		
		//DOM.setStyleAttribute(getElement(), "top", HEIGHT*index+"px");
	}

	public void add(TimeLineObj tlo, long start, long interval) {
		double pct = ((tlo.getDate().getTime() - start) % interval) / (double)interval;
		int bucket = (int) (SIZE * pct);
		
		//System.out.println("num "+((tlo.getDate().getTime() - start) % (double)interval)+"pct "+pct +" bucket "+bucket+" tlo "+tlo);
		
		bars[bucket].incr();
		
	}
}

