package com.aavu.client.gui.maps;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.explorer.ExplorerPanel;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Show all selected tag's map meta info.
 * 
 * @author Jeff Dwyer
 *
 */
public class BigMap extends Composite implements ExplorerPanel {

	public BigMap(Manager manager) {
		

		
		initWidget(new Label("Map coming soon!"));
	}

	public Widget getWidget() {
		return this;
	}

	public void load(Set tags) {
		// TODO Auto-generated method stub
		
	}

	public void loadAll() {
		// TODO Auto-generated method stub
		
	}

}
