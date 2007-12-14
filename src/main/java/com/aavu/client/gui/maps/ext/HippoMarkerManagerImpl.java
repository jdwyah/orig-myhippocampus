package com.aavu.client.gui.maps.ext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.jsio.client.JSFlyweightWrapper;
import com.google.gwt.jsio.client.JSList;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerManagerOptions;

public interface HippoMarkerManagerImpl extends JSFlyweightWrapper {

	public static final HippoMarkerManagerImpl impl = (HippoMarkerManagerImpl) GWT
			.create(HippoMarkerManagerImpl.class);

	public void addMarker(JavaScriptObject jsoPeer, Marker marker, int minZoom);

	public void addMarker(JavaScriptObject jsoPeer, Marker marker, int minZoom, int maxZoom);

	public void addMarkers(JavaScriptObject jsoPeer, JSList<Marker[]> markers, int minZoom);

	public void addMarkers(JavaScriptObject jsoPeer, JSList<Marker[]> markers, int minZoom,
			int maxZoom);

	/**
	 * @gwt.constructor $wnd.MarkerManager
	 */
	public JavaScriptObject construct(MapWidget map, MarkerManagerOptions options);

	public int getMarkerCount(JavaScriptObject jsoPeer, int zoomLevel);

	public void refresh(JavaScriptObject jsoPeer);

	public void clearMarkers(JavaScriptObject jsoPeer);
}
