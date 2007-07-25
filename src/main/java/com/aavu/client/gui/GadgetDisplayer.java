package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.gadgets.GadgetClickListener;
import com.google.gwt.user.client.ui.Panel;

public interface GadgetDisplayer extends GadgetClickListener {


	void load(Topic topic);

	void unload();


	void addTo(Panel mainP);

}
