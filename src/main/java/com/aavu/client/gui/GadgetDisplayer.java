package com.aavu.client.gui;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetPicker;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public interface GadgetDisplayer {

	void addGadget(Gadget gadget);

	void load(Topic topic, List gadgetsToUse);

	void unload();

	Widget getWidget();
	
}
