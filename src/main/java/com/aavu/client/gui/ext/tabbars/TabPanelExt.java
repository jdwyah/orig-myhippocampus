package com.aavu.client.gui.ext.tabbars;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabPanelExt extends TabPanel implements TabHasWidgets {
	public TabPanelExt(){
		super();
	}
	public void hideDeck() {}
	public void showDeck() {}
	public void add(Widget w, String string, boolean b) {
		super.add(w, string, b);		
	}	
}
