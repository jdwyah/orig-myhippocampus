package com.aavu.client.gui;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class Zoomer extends AbsolutePanel implements MouseListener {

	private static final int BASE = 22;
	private static final int INCR = 8;
	private static final int SLIDER_LEFT = 1;
	private static final int TOT_INCR = 11;
	private static final int NUM_BELOW_1 = 4;
	
	private Manager manager;
	private PNGImage slider;
	private int curLevel;

	public Zoomer(Manager manager){		
		this.manager = manager;
		
		addStyleName("H-Zoomer");
		//addStyleName("H-AbsolutePanel");		
		//setPixelSize(24, 138);
		
		slider = new PNGImage("img/zoomerSlider.png",24,14);
		
		PNGImage sliderBase = new PNGImage("img/zoomerBase.png",24,138);
		
		sliderBase.addMouseListener(this);
		
		add(sliderBase,0,0);
		add(slider,SLIDER_LEFT,BASE);
		
		setToZoom(6);		
		
	}

	private void setToZoom(int i) {
		
		
		i = i < 0 ? 0 : i;
		i = i > TOT_INCR ? TOT_INCR : i;
		
		curLevel = i;
		
		int top = i*INCR + BASE; 
		System.out.println("top "+top);
		setWidgetPosition(slider, SLIDER_LEFT, top);
		
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
		
		
		setToZoom(i);
		
		double scale = convertToScale(i);
		System.out.println("i "+i+" scale "+scale);
		manager.zoomTo(convertToScale(i));
		
		
	}

	/**
	 * it's exponential < 1
	 * but ++ > 1
	 * @param i
	 * @return
	 */
	private double convertToScale(int i) {
		
		if((TOT_INCR - i) <= NUM_BELOW_1){
			System.out.println("powing");
			double pow = Math.pow(2, (TOT_INCR - i) - NUM_BELOW_1);
			return pow;
		}else{
			System.out.println("subtracting ");
			return (TOT_INCR - i) - NUM_BELOW_1;
		}
		
	}

	public void onMouseEnter(Widget sender) {}
	public void onMouseLeave(Widget sender) {}
	public void onMouseMove(Widget sender, int x, int y) {}
	public void onMouseUp(Widget sender, int x, int y) {}
	
}
