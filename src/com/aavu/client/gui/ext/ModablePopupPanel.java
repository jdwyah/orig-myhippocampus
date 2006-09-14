package com.aavu.client.gui.ext;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.PopupPanel;

public class ModablePopupPanel extends PopupPanel {

	private boolean modal;

	public ModablePopupPanel(boolean modal) {
		super();	    		
		this.modal = modal;
	}

	//@override
	public boolean onEventPreview(Event event) {
		if(modal){
			super.onEventPreview(event);
		}else{

			int type = DOM.eventGetType(event);
			switch (type) {
			case Event.ONKEYDOWN: {
				return onKeyDownPreview((char) DOM.eventGetKeyCode(event),
						KeyboardListenerCollection.getKeyboardModifiers(event));
			}
			case Event.ONKEYUP: {
				return onKeyUpPreview((char) DOM.eventGetKeyCode(event),
						KeyboardListenerCollection.getKeyboardModifiers(event));
			}
			case Event.ONKEYPRESS: {
				return onKeyPressPreview((char) DOM.eventGetKeyCode(event),
						KeyboardListenerCollection.getKeyboardModifiers(event));
			}

			case Event.ONMOUSEDOWN:
			case Event.ONMOUSEUP:
			case Event.ONMOUSEMOVE:
			case Event.ONCLICK:
			case Event.ONDBLCLICK: {
				return true;							
			}
			}
		}	
		return true;
	}
}