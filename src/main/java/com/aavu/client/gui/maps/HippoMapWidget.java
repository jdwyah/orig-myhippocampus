package com.aavu.client.gui.maps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.gui.ContextMenu;
import com.aavu.client.gui.maps.ext.HippoMarkerManager;
import com.aavu.client.gui.timeline.zoomer.BigMapContextMenu;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.DragListener;
import com.google.gwt.maps.client.event.MapClickListener;
import com.google.gwt.maps.client.event.MarkerClickListener;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerManagerOptions;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;


/**
 * display meta locations
 * 
 * @author Jeff Dwyer
 * 
 */
public class HippoMapWidget extends Composite {

	private static final int MAX_ZOOM = 17;
	private static final int AMALGAMIZED_START = 4;
	private static final int AMALGAM_END = 3;

	private MapWidget mapWidget;
	private MapController controller;

	// <Marker,LocationDTO>
	private Map markerToLocationSet = new HashMap();

	private Marker selectedMarker;

	private HippoMarkerManager markerManager;


	public HippoMapWidget(MapController gadget, int width, int height, int zoom) {
		this.controller = gadget;

		// middle of atlantic
		LatLng center = new LatLng(33.137551, -35.15625);


		mapWidget = new MapWidget(center, zoom);
		mapWidget.setScrollWheelZoomEnabled(true);
		mapWidget.setSize(width + "px", height + "px");

		MarkerManagerOptions options = new MarkerManagerOptions();
		options.setTrackMarkers(true);
		try {
			markerManager = new HippoMarkerManager(mapWidget, options);
		} catch (Exception e) {
			// f-d up on ie
			// markerManager = new MarkerManager(mapWidget, options);
		}


		mapWidget.addClickListener(new MapClickListener() {

			public void onClick(MapWidget sender, Overlay overlay, LatLng point) {

				System.out.println("on click pt" + point + " sender " + sender);

				// null if clicking on marker
				// but we catch marker using the markerEventManager
				if (point != null) {
					System.out.println("HippoMapWidget.Clicked on " + point.toString());
					newPoint(point);

					if (controller.getManager().isEdittable()) {

						Event e = DOM.eventGetCurrentEvent();

						System.out.println("ctrl " + DOM.eventGetCtrlKey(e));

						if (DOM.eventGetCtrlKey(e)) {
							HippoLocation hl = new HippoLocation();
							hl.setLocation(point);

							ContextMenu p = new BigMapContextMenu(controller.getManager(),
									HippoMapWidget.this, hl);



							// int x = DOM.eventGetClientX(e);
							// int y = DOM.eventGetClientY(e);

							p.show(300, 300);
						}
					}

				}
			}
		});



		// eventManager.addOnMouseOutListener(gmaps, this);
		// eventManager.addOnMouseOverListener(gmaps, this);



		initWidget(mapWidget);


	}

	public void add(LocationDTO locObj, boolean partOfAmalgam) {
		createPoint(locObj, partOfAmalgam);
	}

	/**
	 * NOTE PEND MED replace with a generalized TreeOfTime
	 * 
	 * @param locations
	 */
	public void addAmalgam(Set locations) {

		double totalLat = 0;
		double totalLong = 0;

		int count = 0;
		StringBuffer amalgamText = new StringBuffer();

		for (Iterator iterator = locations.iterator(); iterator.hasNext();) {
			LocationDTO locDTO = (LocationDTO) iterator.next();
			amalgamText.append(locDTO.getTopic().getTopicTitle());
			amalgamText.append("\n");

			totalLat += locDTO.getLocation().getLocation().getLatitude();
			totalLong += locDTO.getLocation().getLocation().getLongitude();

			if (++count > 5) {
				amalgamText.append(ConstHolder.myConstants.map_amalgam_more());
				break;
			}

		}

		// just use the last location
		// PEND LOW do averaging
		LocationDTO locDTO = (LocationDTO) locations.iterator().next();
		System.out.println("add amalgam " + amalgamText.toString());

		// only avg based on count, not locations.size() since we don't add all of them
		LatLng point = new LatLng(totalLat / count, totalLong / count);


		createPoint(point, locations, amalgamText.toString(), false, 0, AMALGAM_END);
	}


	public void centerOn(HippoLocation location) {
		if (location != null) {
			System.out.println("!!!!!!!!!!!!!CENTER ON " + location.getLocation());
			// gmaps.setCenter(latlng)
			mapWidget.setCenter(location.getLocation());
		}
	}

	private void centerOn(LatLng point, int zoom) {
		mapWidget.setCenter(point, zoom);
	}


	public void clear() {
		System.out.println("HippoMapWidget.clear()");

		mapWidget.clearOverlays();

		// weird. no markerManager.clear() sounds like google spaced this functionality.
		// http://groups.google.com/group/Google-Maps-API/browse_thread/thread/8f21c0763d4d3283/848f91821bb6b2c6
		// http://googlemapsapi.blogspot.com/2007/03/new-open-source-utility-library-for.html

		if (markerManager != null) {
			markerManager.clearMarkers();
		}

		markerToLocationSet.clear();


		// fixed by wrapping the new MarkerManager (not GMarkerManager) API


	}


	// /**
	// * http://www.barklund.org/blog/2007/03/27/google-maps-is-great-but-some-hacks-are-needed/ It
	// is
	// * “hardcoded” to version 2.76 and
	// */
	// public native void clearMarkers()/*-{
	// try{
	// //alert("A");
	//																																					
	// alert("doc.gmap "+$doc.GMarkerManager);
	// alert("wnd.gmap "+$wnd.GMarkerManager);
	//																																					
	//																																					
	// // hack GMarkerManager
	// // to contain a method
	// // for clearing all
	// // markers
	// $wnd.GMarkerManager.prototype.clearMarkers = function() {
	// var me = this;
	// //
	// // clear currently
	// // shown
	// if (me.Wd > 0) {
	// me.Id(me.K, me.Kd);
	// }
	// //
	// // reset arrays
	// var maxZoom = me.df;
	// var maxWidth = 256;
	// for (var zoom = 0; zoom < maxZoom; ++zoom){
	// me.ed[zoom] = [];
	// me.oc[zoom] = 0;
	// me.gc[zoom] = Math.ceil(maxWidth/me.Ec);
	// maxWidth <<= 1;
	// }
	// me.refresh();
	// }
	//																						
	// $wnd.GMarkerManager.prototype.clearMarkers();
	//																						 
	// }catch(err){
	// alert(err);
	// }
	//																																												
	// }-*/;

	private Marker createPoint(LocationDTO locObj) {
		return createPoint(locObj, false);
	}

	private Marker createPoint(LocationDTO locObj, boolean amalgamized) {
		if (amalgamized) {
			return createPoint(locObj, locObj.getOnMapTitle(), true, AMALGAMIZED_START, MAX_ZOOM);
		} else {
			return createPoint(locObj, locObj.getOnMapTitle(), true, 0, MAX_ZOOM);
		}

	}

	private Marker createPoint(LocationDTO loc, String title, boolean draggable, int minZoom,
			int maxZoom) {
		Set justOne = new HashSet();
		justOne.add(loc);
		return createPoint(loc.getLocation().getLocation(), justOne, title, draggable, minZoom,
				maxZoom);
	}

	private Marker createPoint(LatLng point, Set ref, String title, boolean draggable, int minZoom,
			int maxZoom) {


		MarkerOptions moveableMarkerOps;
		moveableMarkerOps = new MarkerOptions();
		moveableMarkerOps.setTitle(title);

		boolean reallyDraggable = draggable && controller.getManager().isEdittable();
		moveableMarkerOps.setDraggable(reallyDraggable);


		Marker marker = new Marker(point, moveableMarkerOps);



		// !reallyDraggable, since that would not add for manager.isEdittable() = false
		if (draggable) {
			System.out.println("Make draggable " + marker + " " + ref.iterator().next());
			markerToLocationSet.put(marker, ref.iterator().next());
		}


		marker.addDragListener(new DragListener() {

			public void onDrag() {
				// TODO Auto-generated method stub

			}

			public void onDragEnd() {
				Marker marker = selectedMarker;

				LocationDTO dragged = (LocationDTO) markerToLocationSet.get(marker);

				System.out.println("Drag end " + marker + " " + dragged);

				if (dragged != null) {

					System.out.println("!!!!!!!!!!!!!!!!!!!!drag ended " + marker.getPoint());
					mapWidget.setCenter(marker.getPoint());


					dragged.getLocation().setLocation(marker.getPoint());
					controller.update(dragged);
				}
			}

			public void onDragStart() {



			}
		});

		marker.addClickListener(new MarkerClickListener() {

			public void onClick(Marker sender) {
				userSelected(sender);
			}

			public void onDoubleClick(Marker sender) {
				userDoubleClicked(sender);
			}
		});

		System.out.println("Marker manager add " + minZoom + " " + maxZoom + "  "
				+ point.getLatitude() + " " + point.getLongitude());

		if (markerManager != null) {
			markerManager.addMarker(marker, minZoom, maxZoom);
			markerManager.refresh();
		} else {
			if (draggable) {
				mapWidget.addOverlay(marker);
			}
		}



		return marker;
	}

	private void newPoint(LatLng point) {

		LocationDTO newLoc = controller.getNewLocationForPoint(point);

		if (newLoc != null) {
			Marker marker = createPoint(newLoc);
		}



	}

	public InfoWindow getInfoWindow() {
		return mapWidget.getInfoWindow();
	}


	public void setSize(int i, int j) {
		mapWidget.setPixelSize(i, j);
	}

	private void userSelected(Marker marker) {

		selectedMarker = marker;

		System.out.println("Marker clicked " + marker);

		LocationDTO selected = (LocationDTO) markerToLocationSet.get(marker);
		if (selected != null) {

			System.out.println("sel " + selected);
			if (selected != null) {
				controller.userSelected(selected, marker);
			}
		} else {

			centerOn(marker.getPoint(), AMALGAMIZED_START);
			// sysout
			// marker.openInfoWindow(mapWidget)
		}

	}


	private void userDoubleClicked(Marker marker) {

		selectedMarker = marker;

		System.out.println("Marker clicked " + marker);


		LocationDTO selected = (LocationDTO) markerToLocationSet.get(marker);
		if (selected != null) {

			System.out.println("sel " + selected);
			if (selected != null) {
				controller.userDoubleClicked(selected);
			}
		} else {
			centerOn(marker.getPoint(), AMALGAMIZED_START);
			// sysout
			// marker.openInfoWindow(mapWidget)
		}


	}


}
