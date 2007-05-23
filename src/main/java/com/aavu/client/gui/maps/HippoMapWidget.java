package com.aavu.client.gui.maps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.gui.maps.ext.MarkerManager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.mapitz.gwt.googleMaps.client.GControl;
import com.mapitz.gwt.googleMaps.client.GLatLng;
import com.mapitz.gwt.googleMaps.client.GMap2;
import com.mapitz.gwt.googleMaps.client.GMap2EventClickListener;
import com.mapitz.gwt.googleMaps.client.GMap2EventManager;
import com.mapitz.gwt.googleMaps.client.GMap2EventMouseListener;
import com.mapitz.gwt.googleMaps.client.GMap2EventMoveListener;
import com.mapitz.gwt.googleMaps.client.GMap2Widget;
import com.mapitz.gwt.googleMaps.client.GMarker;
import com.mapitz.gwt.googleMaps.client.GMarkerEventClickListener;
import com.mapitz.gwt.googleMaps.client.GMarkerEventDragListener;
import com.mapitz.gwt.googleMaps.client.GMarkerEventManager;
import com.mapitz.gwt.googleMaps.client.GMarkerManagerOptions;
import com.mapitz.gwt.googleMaps.client.GMarkerOptions;
import com.mapitz.gwt.googleMaps.client.GOverlay;
import com.mapitz.gwt.googleMaps.client.JSObject;


/**
 * display meta locations
 * 
 * @author Jeff Dwyer
 *
 */
public class HippoMapWidget extends Composite implements GMarkerEventDragListener, GMap2EventMouseListener, GMarkerEventClickListener {
	
	private static final int MAX_ZOOM = 17;
	private static final int AMALGAMIZED_START = 4;
	private static final int AMALGAM_END = 3;
	private GMap2 gmaps;
	private GMap2Widget mapWidget;
	private MapController controller;
	
	private Map markerToLocationSet = new HashMap();

	private GMarkerEventManager markerEventManager;
	private boolean weHaveFocus;
	private MarkerManager markerManager;
	
	
	
	public HippoMapWidget(MapController gadget, int width, int height,int zoom) {
		this.controller = gadget;
		
		//middle of atlantic
		GLatLng center = new GLatLng(33.13755119234614, -35.15625);
		
		
		
	    mapWidget = new GMap2Widget(height+"", width+"",center,zoom,null);
	    
	    //Alternative method: Setting your own center value and zoom in the constructor
	    //GMap2Widget mapWidget = new GMap2Widget("300", "300",new GLatLng(37.4419, -122.1419), 13);
	    

	    //Retrieve the GMap2 object and start manipulating your map
	    gmaps = mapWidget.getGmap();
	    GMarkerManagerOptions opts = new GMarkerManagerOptions();
	    opts.setTrackMarkers(true);
	    markerManager = new MarkerManager(gmaps,opts);
	    
	    gmaps.addControl(GControl.GSmallZoomControl());
	    
	    //Add the widget to your GWT UI (You can do this before or after you call getGmap() now).
	    //gmaps.openInfoWindowHtml(pos, "The center of the world"); 
		
	    
	    GMap2EventManager eventManager = GMap2EventManager.getInstance();

	    eventManager.addOnMoveStartListener(gmaps, new GMap2EventMoveListener() {
	    	public void onMove(GMap2 map){
	    	}

	    	public void onMoveEnd(GMap2 map){
	    	}

	    	public void onMoveStart(GMap2 map) {
	    		//Logger.log("move started");
	    	}
	    });
	    
	    markerEventManager = GMarkerEventManager.getInstance();
	    
	    
	    eventManager.addOnClickListener(gmaps, new GMap2EventClickListener() {
	    	public void onClick(GMap2 map, GOverlay overlay, GLatLng point){
	    		    		
	    		//null if clicking on marker
	    		//but we catch marker using the markerEventManager
	    		if(point != null){
	    			//Logger.log("Clicked on " + point.toString());
	    			newPoint(point);	
	    		}
	    	}
	    });
	    
	    eventManager.addOnMouseOutListener(gmaps, this);
	    eventManager.addOnMouseOverListener(gmaps, this);
	    

		enableScrollWheelZoom(gmaps.getJSObject());
	    
	    initWidget(mapWidget);
	}
	

	public static native void enableScrollWheelZoom(JSObject map)/*-{
  		map.enableScrollWheelZoom();
  	}-*/;
	
	public void add(LocationDTO locObj,boolean partOfAmalgam) {
		createPoint(locObj,partOfAmalgam);		
	}
	
	public void addAmalgam(Set locations) {

		double totalLat = 0;
		double totalLong = 0;
		
		int count = 0;
		StringBuffer amalgamText = new StringBuffer();
		
		for (Iterator iterator = locations.iterator(); iterator.hasNext();) {
			LocationDTO locDTO = (LocationDTO) iterator.next();					
			amalgamText.append(locDTO.getTopic().getTopicTitle());
			amalgamText.append("\n");
			
			totalLat += locDTO.getLocation().getLocation().lat();
			totalLong += locDTO.getLocation().getLocation().lng();
			
			if(++count > 5){
				amalgamText.append(ConstHolder.myConstants.map_amalgam_more());				
				break;
			}
		
		}

		//just use the last location
		//PEND LOW do averaging
		LocationDTO locDTO = (LocationDTO) locations.iterator().next();
		System.out.println("add amalgam "+amalgamText.toString());	
		
		//only avg based on count, not locations.size() since we don't add all of them
		GLatLng point = new GLatLng(totalLat/count,totalLong/count);
		
		
		createPoint(point,locations,amalgamText.toString(),false,0,AMALGAM_END);	
	}
		
	
	public void centerOn(HippoLocation location) {
		if(location != null){
			System.out.println("!!!!!!!!!!!!!CENTER ON "+location.getLocation());
			//gmaps.setCenter(latlng)
			gmaps.setCenter(location.getLocation());
		}
	}
	private void centerOn(GLatLng point, int zoom) {
		gmaps.setCenter(point,zoom);
	}

	
	public void clear() {
		
		
		gmaps.clearOverlays();		
		
		//weird. no markerManager.clear() sounds like google spaced this functionality.
		//http://groups.google.com/group/Google-Maps-API/browse_thread/thread/8f21c0763d4d3283/848f91821bb6b2c6
		//http://googlemapsapi.blogspot.com/2007/03/new-open-source-utility-library-for.html
	    
		//fixed by wrapping the new MarkerManager (not GMarkerManager) API
		markerManager.clearMarkers();
	    
		
	}
	
	
	private GMarker createPoint(LocationDTO locObj) {
		return createPoint(locObj, false);
	}
	private GMarker createPoint(LocationDTO locObj,boolean amalgamized) {
		if(amalgamized){
			return createPoint(locObj, locObj.getOnMapTitle(),true,AMALGAMIZED_START,MAX_ZOOM);
		}else{
			return createPoint(locObj, locObj.getOnMapTitle(),true,0,MAX_ZOOM);	
		}
		
	}

	private GMarker createPoint(LocationDTO loc,String title,boolean draggable,int minZoom,int maxZoom) {
		Set justOne = new HashSet();
		justOne.add(loc);
		return createPoint(loc.getLocation().getLocation(), justOne, title, draggable, minZoom, maxZoom); 
	}
		
	private GMarker createPoint(GLatLng point,Set ref,String title,boolean draggable,int minZoom,int maxZoom) {
	
		
		GMarkerOptions moveableMarkerOps;		
		moveableMarkerOps = new GMarkerOptions();		
		moveableMarkerOps.setDraggable(draggable); 
		moveableMarkerOps.setTitle(title);
		
//		GIcon icon = GIcon.create(); 
//		icon.setImage("markerA.png"); 
//		icon.setShadow("shadow50.png"); 
//		icon.setIconSize(GSize.create(20, 34)); 
//		icon.setShadowSize(GSize.create(37, 34)); 
//		icon.setIconAnchor(GPoint.create(9, 34)); 
//		icon.setInfoWindowAnchor(GPoint.create(9, 2)); 

		
		GMarker m = new GMarker(point,moveableMarkerOps);
		markerEventManager.addOnDragEndListener(m, this);
		markerEventManager.addOnClickListener(m, this);
		
		
		if(draggable){
			m.enableDragging();
		}
		
		markerManager.addMarker(m,minZoom,maxZoom);
		//gmaps.addOverlay(m);
		
		
		markerToLocationSet.put(m, ref);
		return m;
	}

	private void newPoint(GLatLng point) {
		
		LocationDTO newLoc = controller.getNewLocationForPoint(point);
		
		if(newLoc != null){		
			GMarker marker = createPoint(newLoc);		
		}
		
		
		
	}
	
	
	
	
	
	
	
	public void onClick(GMarker marker) {		
		userSelected(marker);
	}
	public void onDblClick(GMarker marker) {
		userDoubleClicked(marker);		
	}
	public void onDragEnd(GMarker marker) {
		Set draggedS =  (Set) markerToLocationSet.get(marker);
		
		if(draggedS != null && !draggedS.isEmpty()){
		
			LocationDTO dragged = (LocationDTO) draggedS.iterator().next();
			System.out.println("!!!!!!!!!!!!!!!!!!!!drag ended "+marker.getPoint());
			gmaps.setCenter(marker.getPoint());
			
			dragged.getLocation().setLocation(marker.getPoint());			
			controller.update(dragged);
		}
		
	}


	public void onDragStart(GMarker marker) {
		// TODO Auto-generated method stub
		
	}


	
	public void onMouseMove(GMap2 map, GLatLng latlng) {}


	public void onMouseOut(GMap2 map, GLatLng latlng) {
		weHaveFocus = false;
	}
	public void onMouseOver(GMap2 map, GLatLng latlng) {
		weHaveFocus = true;
	}
	


	public void setSize(int i) {
		mapWidget.setPixelSize(i, i);
	}
	
	private void userSelected(GMarker marker) {
		
		System.out.println("Marker clicked "+marker);
		
		Set theSet =  (Set) markerToLocationSet.get(marker);		
		if(theSet != null && !theSet.isEmpty()){
			if(theSet.size() == 1){
				LocationDTO selected = (LocationDTO) theSet.iterator().next();
				System.out.println("sel "+selected);						
				if(selected != null){
					controller.userSelected(selected,marker);
				}
			}else{
				
				centerOn(marker.getPoint(),AMALGAMIZED_START);
				//sysout
				//marker.openInfoWindow(mapWidget)
			}		
		}		
	}
	

	private void userDoubleClicked(GMarker marker) {
		

		System.out.println("Marker clicked "+marker);
		
		Set theSet =  (Set) markerToLocationSet.get(marker);		
		if(theSet != null && !theSet.isEmpty()){
			if(theSet.size() == 1){
				LocationDTO selected = (LocationDTO) theSet.iterator().next();
				System.out.println("sel "+selected);						
				if(selected != null){
					controller.userDoubleClicked(selected);
				}
			}else{
				centerOn(marker.getPoint(),AMALGAMIZED_START);
				//sysout
				//marker.openInfoWindow(mapWidget)
			}
			
		}
	}

	
}
