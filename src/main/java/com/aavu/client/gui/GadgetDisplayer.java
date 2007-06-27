package com.aavu.client.gui;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetClickListener;
import com.google.gwt.user.client.ui.Widget;

public interface GadgetDisplayer extends GadgetClickListener {

	
	void load(Topic topic, List gadgetsToUse);

	void unload();

	Widget getWidget();
	
}
