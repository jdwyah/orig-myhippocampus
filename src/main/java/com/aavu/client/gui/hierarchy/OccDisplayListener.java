package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class OccDisplayListener extends MouseListenerAdapter {

	private static final int POPUP_LEFT_PUSH = 60;
	
	private static final String  DEFAULT_POPUP_STYLE   = "H-Tooltip";
	
	private Occurrence occ;
	private OccPopup tooltip;
	public OccDisplayListener(Occurrence occ){
		this.occ = occ;
		
		
	}
	
	public void onMouseEnter(Widget sender) {
		if (tooltip == null) {
			tooltip = new OccPopup(sender,POPUP_LEFT_PUSH);
		}
		tooltip.show(sender);
	}

	public void onMouseLeave(Widget sender) {
//		if (tooltip != null) {
//			tooltip.hide();
//		}
	} 
	
	private class OccPopup extends PopupPanel {
	
		private int relLeft;

		public OccPopup(Widget sender, int relLeft){
			super(true);
			this.relLeft = relLeft;

			situate(sender);
			

			HTML contents = new HTML( occ.getData() );
			add( contents );
			
			addStyleName( DEFAULT_POPUP_STYLE );
		}

		public void show(Widget sender) {
			situate(sender);
			show();
		}
		
		public void situate(Widget sender) {
			int left =  sender.getAbsoluteLeft() + relLeft;
			int top = sender.getAbsoluteTop();		
			setPopupPosition( left, top );
		}
	}

}
