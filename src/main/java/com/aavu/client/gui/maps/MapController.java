package com.aavu.client.gui.maps;

import com.aavu.client.domain.dto.LocationDTO;
import com.mapitz.gwt.googleMaps.client.GLatLng;
import com.mapitz.gwt.googleMaps.client.GMarker;

public interface MapController {

	/**
	 * return null if you don't want to add a point.
	 * 
	 * @param point
	 * @return
	 */
	LocationDTO getNewLocationForPoint(GLatLng point);

	void update(LocationDTO dragged);

	void userDoubleClicked(LocationDTO selected);

	void userSelected(LocationDTO selected, GMarker marker);

}
