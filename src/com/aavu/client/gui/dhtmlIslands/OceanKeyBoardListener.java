package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.gui.Ocean;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

public class OceanKeyBoardListener implements KeyboardListener {

	protected static final int MOVEMENT_SCALE = 10;
	
	private int y;
	private int x;
	private Timer movementTimer;
	private boolean tracking = false;

	private Ocean ocean;
	
	

	public OceanKeyBoardListener(Ocean _ocean) {
		this.ocean = _ocean;
		movementTimer = new Timer(){
			public void run() {
				ocean.moveBy(x*MOVEMENT_SCALE, y*MOVEMENT_SCALE);
			}};
	}

	private void track() {		
		if(!tracking){
			movementTimer.scheduleRepeating(100);
		}
		tracking  = true;
	}
	private void stopTrack() {
		tracking = false;
		movementTimer.cancel();
	}
	
	public void onKeyDown(Widget sender, char keyCode, int modifiers) {

		switch (keyCode) {
		case KeyboardListener.KEY_UP:
			y = -1; 
			break;
		case KeyboardListener.KEY_DOWN:
			y = 1;
			break;
		case KeyboardListener.KEY_LEFT:
			x = -1;
			break;
		case KeyboardListener.KEY_RIGHT:
			x = 1;
			break;
		default:
			break;
		}
		
		track();
	}
	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		System.out.println("keycode "+keyCode);
		
		switch (keyCode) {
		case '-':
			 ocean.zoomOut();
			break;
		case '+':
			ocean.zoomIn();
			break;		
		default:
			break;
		}
	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		switch (keyCode) {
		case KeyboardListener.KEY_UP:
			y = 0; 
			break;
		case KeyboardListener.KEY_DOWN:
			y = 0;
			break;
		case KeyboardListener.KEY_LEFT:
			x = 0;
			break;
		case KeyboardListener.KEY_RIGHT:
			x = 0;
			break;
		default:
			break;
		}		
		if(x == 0 && y == 0){		
			stopTrack();
		}
	}

	

}
