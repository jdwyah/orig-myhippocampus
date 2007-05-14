package com.aavu.client.gui;

import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Zoomer extends SimplePanel implements MouseListener {

	private static final int BASE = 24;
	private static final int INCR = 11;
	private static final int SLIDER_LEFT = 1;
	private static final int TOT_INCR = 7;
	private static final int NUM_BELOW_1 = 4;
	
	private Manager manager;
	private int curLevel;

	private AbsolutePanel absPanel = new AbsolutePanel();
	
	/**
	 * It seems redundant to have this absPanel as a member instead of just extending
	 * AbsolutePanel, but positioning things on the map absolutely seems to only really
	 * work with SimplePanel's that have Style AbsolutePanel  
	 * 
	 * @param manager
	 */
	public Zoomer(final Manager manager){		
		this.manager = manager;
		
		//addStyleName("H-AbsolutePanel");		
		//setPixelSize(24, 138);
		
//		slider = new PNGImage("img/zoomerSlider.png",24,14);
//		
//		PNGImage sliderBase = new PNGImage("img/zoomerBase.png",24,138);
//		
		
		Image magnifyBig = ConstHolder.images.magnifyingBig().createImage();		
		magnifyBig.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.zoomIn();				
			}});
		
		Image magnifySmall = ConstHolder.images.magnifyingSmall().createImage();
		magnifySmall.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.zoomOut();
			}});
		
		
//		sliderBase.addMouseListener(this);
//		
//		absPanel.add(sliderBase,0,0);
//		absPanel.add(slider,SLIDER_LEFT,BASE);
		
		absPanel.add(magnifyBig,0,0);
		absPanel.add(magnifySmall,0,40);
		
		
		setToZoom(convertFromScale(1.0));		
		
		//interestingly, this is only necessary for FF
		absPanel.setPixelSize(43,80);
		
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
		//absPanel.setWidgetPosition(slider, SLIDER_LEFT, top);
		
	}
	public void setToScale(double scale){
		setToZoom(convertFromScale(scale));
	}

	public void onMouseUp(Widget sender, int x, int y) {
		y -= BASE;
		
		int i = (int) (TOT_INCR*(y/(138.0 - 2*BASE)));
		
		System.out.println("y "+(y+BASE)+" py "+y+" i "+i);
		
		//do the plus & minus buttons here
		//
		if(i < 0){
			System.out.println("-_-");
			i = curLevel-1;
		}if(i >= TOT_INCR){
			System.out.println("+_+");
			i = curLevel+1;
		}
		
		
		//setToZoom(i);
		
		double scale = convertToScale(i);
		
		Logger.debug("ZOOOMER telling manager! i "+i+" scale "+scale);		
		
		manager.zoomTo(convertToScale(i));
		
	}

	/**
	 *  Change, let's do powers of 2 the whole way
	 *  
	 * 
	 * @param i
	 * @return
	 */
	public static double convertToScale(int i) {
		
		double pow = Math.pow(2, (TOT_INCR - i) - NUM_BELOW_1);
		return pow;
		
		
	}

	public static int convertFromScale(double d) {
		
		int pow =(int)(Math.log(d)/Math.log(2));					
		return (TOT_INCR - pow) - NUM_BELOW_1;
				
	}


	public void onMouseEnter(Widget sender) {}
	public void onMouseLeave(Widget sender) {}
	public void onMouseMove(Widget sender, int x, int y) {}
	public void onMouseDown(Widget sender, int x, int y) {}
	
}
