package com.aavu.client.gui;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.service.Manager;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Zoomer extends SimplePanel implements MouseListener {

	private static final int BASE = 22;
	private static final int INCR = 8;
	private static final int SLIDER_LEFT = 1;
	private static final int TOT_INCR = 11;
	private static final int NUM_BELOW_1 = 4;
	
	private Manager manager;
	private PNGImage slider;
	private int curLevel;

	private AbsolutePanel absPanel = new AbsolutePanel();
	
	/**
	 * It seems redundant to have this absPanel as a member instead of just extending
	 * AbsolutePanel, but positioning things on the map absolutely seems to only really
	 * work with SimplePanel's that have Style AbsolutePanel  
	 * 
	 * @param manager
	 */
	public Zoomer(Manager manager){		
		this.manager = manager;
		
		//addStyleName("H-AbsolutePanel");		
		//setPixelSize(24, 138);
		
		slider = new PNGImage("img/zoomerSlider.png",24,14);
		
		PNGImage sliderBase = new PNGImage("img/zoomerBase.png",24,138);
		
		sliderBase.addMouseListener(this);
		
		absPanel.add(sliderBase,0,0);
		absPanel.add(slider,SLIDER_LEFT,BASE);
		
		setToZoom(convertFromScale(1.0));		
		
		//interestingly, this is only necessary for FF
		absPanel.setPixelSize(24,138);
		
		add(absPanel);
		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-Zoomer");	
		
	}

	private void setToZoom(int i) {
				
		i = i < 0 ? 0 : i;
		i = i > TOT_INCR ? TOT_INCR : i;
		
		curLevel = i;
		
		int top = i*INCR + BASE; 
		System.out.println("top "+top);
		absPanel.setWidgetPosition(slider, SLIDER_LEFT, top);
		
	}
	public void setToScale(double scale){
		setToZoom(convertFromScale(scale));
	}

	public void onMouseDown(Widget sender, int x, int y) {
		y -= BASE;
		
		int i = (int) (TOT_INCR*(y/(138.0 - 2*BASE)));
		
		//do the plus & minus buttons here
		//
		if(i < 0){
			i = curLevel-1;
		}if(i > TOT_INCR){
			i = curLevel+1;
		}
		
		
		//setToZoom(i);
		
		double scale = convertToScale(i);
		
		Logger.debug("ZOOOMER telling manager! i "+i+" scale "+scale);		
		
		manager.zoomTo(convertToScale(i));
		
	}

	/**
	 * it's exponential < 1
	 * but ++ > 1
	 * @param i
	 * @return
	 */
	public static double convertToScale(int i) {
		
		if((TOT_INCR - i) <= NUM_BELOW_1){			
			double pow = Math.pow(2, (TOT_INCR - i) - NUM_BELOW_1);
			return pow;
		}else{
			//add one, otherwise (6-5)^1 == (6-5) & two zoom levels are the same
			return (TOT_INCR - i) - NUM_BELOW_1 + 1;
		}
		
	}

	public static int convertFromScale(double d) {
		if(d <= 1){			
			int pow =(int)(Math.log(d)/Math.log(2));					
			return (TOT_INCR - pow) - NUM_BELOW_1;			
		}else{			
			return (int) (TOT_INCR - d) - NUM_BELOW_1 + 1;
		}		
	}


	public void onMouseEnter(Widget sender) {}
	public void onMouseLeave(Widget sender) {}
	public void onMouseMove(Widget sender, int x, int y) {}
	public void onMouseUp(Widget sender, int x, int y) {}
	
}
