package com.aavu.client.gui.ocean.dhtmlIslands;

import com.aavu.client.gui.ocean.Ocean;
import com.aavu.client.util.Logger;
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

	private Timer cancelTimer;
	
	

	public OceanKeyBoardListener(Ocean _ocean) {
		this.ocean = _ocean;
		movementTimer = new Timer(){
			public void run() {
				ocean.moveBy(x*MOVEMENT_SCALE, y*MOVEMENT_SCALE);
			}};
			
		cancelTimer = new Timer() {
			public void run() {
				Logger.debug("cancelling now");
				tracking = false;
				movementTimer.cancel();
			}};
		
	}

	private void track() {		
		if(!tracking){
			movementTimer.scheduleRepeating(100);
		}
		tracking  = true;
	}
	private void stopTrack() {		
		stopTrack(1);
	}
	private void stopTrack(int sec) {
		Logger.debug("stop track in "+sec);
		cancelTimer.schedule(sec);
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
		Logger.debug("KEY DOWN!!!!!!!!!! "+x+" "+y);
		track();
	}
	
	/**
	 * NOTE FF calls this reliably... but before keyUp.. 
	 * and often calls it right after keyDown.
	 * 
	 * PEND MED. solution for now is to bail out. cancel the keypress to stop 
	 * infinite scroll, but stop it on a delay so that at least we scroll
	 * a little.
	 *  
	 * Also do note that this keyCode here is NOT the same as the keyCode returned
	 * from keyDown and keyUp. see javadoc for detail
	 *  
	 */
	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		
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
		
		Logger.debug("key press cancel no matter what");
		stopTrack(1000);
		
	}

	/**
	 * NOTE ie calls this reliably
	 */
	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		
		Logger.debug("key up");
		processKeyUp(keyCode);
		
	}

	/**
	 * could be called from either keyPress or keyUp. ie calls one, ff calls both, but often forgets
	 * to call keyUp, which lead to infinite scroll.
	 * 
	 * Because this gets called from both it should be sure not to cause any issues if it's called twice
	 * for 1 key press
	 * 
	 * @param keyCode
	 * @param msec 
	 */
	private void processKeyUp(char keyCode) {
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
		Logger.debug("proc key!!!!!!!!!! "+x+" "+y);
		if(x == 0 && y == 0){			
			stopTrack();			
		}
	}

	

}
