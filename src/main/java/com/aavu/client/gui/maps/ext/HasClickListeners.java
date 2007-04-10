package com.aavu.client.gui.maps.ext;

import java.util.Set;

import com.google.gwt.user.client.ui.Widget;


/**
 * Wrap widget that would like to be in a GMap info panel. They'll need to expose 
 * listener/widget pairs.
 * @author Jeff Dwyer
 *
 */	
public interface HasClickListeners {

	Widget getMainWidget();

	//Set<ListenerWidget>
	Set getPairs();

}
