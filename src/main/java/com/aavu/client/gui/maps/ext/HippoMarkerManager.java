package com.aavu.client.gui.maps.ext;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerManagerOptions;
import com.google.gwt.maps.client.util.JsUtil;

public final class HippoMarkerManager {

	private final JavaScriptObject jsoPeer;


	public HippoMarkerManager(MapWidget map, MarkerManagerOptions options) {
		jsoPeer = HippoMarkerManagerImpl.impl.construct(map, options);
	}

	public void addMarker(Marker marker, int minZoom) {
		HippoMarkerManagerImpl.impl.addMarker(jsoPeer, marker, minZoom);
	}

	public void addMarker(Marker marker, int minZoom, int maxZoom) {
		HippoMarkerManagerImpl.impl.addMarker(jsoPeer, marker, minZoom, maxZoom);
	}

	public void addMarkers(Marker[] markers, int minZoom) {
		HippoMarkerManagerImpl.impl.addMarkers(jsoPeer, JsUtil.toJsList(markers), minZoom);
	}

	public void addMarkers(Marker[] markers, int minZoom, int maxZoom) {
		HippoMarkerManagerImpl.impl.addMarkers(jsoPeer, JsUtil.toJsList(markers), minZoom, maxZoom);
	}

	public int getMarkerCount(int zoomLevel) {
		return HippoMarkerManagerImpl.impl.getMarkerCount(jsoPeer, zoomLevel);
	}

	public void refresh() {
		HippoMarkerManagerImpl.impl.refresh(jsoPeer);
	}

	public void clearMarkers() {
		HippoMarkerManagerImpl.impl.clearMarkers(jsoPeer);
	}
}
