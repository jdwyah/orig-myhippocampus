package com.aavu.client.gui.maps;

import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.service.Manager;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;

public interface MapController {

	/**
	 * return null if you don't want to add a point.
	 * 
	 * @param point
	 * @return
	 */
	LocationDTO getNewLocationForPoint(LatLng point);

	void update(LocationDTO dragged);

	void userDoubleClicked(LocationDTO selected);

	void userSelected(LocationDTO selected, Marker marker);

	Manager getManager();

}
