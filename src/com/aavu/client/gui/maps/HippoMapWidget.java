package com.aavu.client.gui.maps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.gui.ext.WheelListener;
import com.aavu.client.gui.gadgets.MapGadget;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.Window;
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
import com.mapitz.gwt.googleMaps.client.GMarkerOptions;
import com.mapitz.gwt.googleMaps.client.GOverlay;


/**
 * display meta locations
 * 
 * @author Jeff Dwyer
 *
 */
public class HippoMapWidget extends Composite implements GMarkerEventDragListener, WheelListener, GMap2EventMouseListener, GMarkerEventClickListener {
	
	private GMap2 gmaps;
	private GMap2Widget mapWidget;
	private MapController controller;
	
	private Map markerToLocation = new HashMap();

	private GMarkerOptions moveableMarkerOps;

	private GMarkerEventManager markerEventManager;
	private boolean weHaveFocus;
	
	
	public HippoMapWidget(MapController gadget, int width, int height,int zoom) {
		this.controller = gadget;
		
		//middle of atlantic
		GLatLng center = new GLatLng(33.13755119234614, -35.15625);
		
				
		moveableMarkerOps = new GMarkerOptions();		
		moveableMarkerOps.setDraggable(true); 
		
	    mapWidget = new GMap2Widget(height+"", width+"",center,zoom,null);
	    
	    //Alternative method: Setting your own center value and zoom in the constructor
	    //GMap2Widget mapWidget = new GMap2Widget("300", "300",new GLatLng(37.4419, -122.1419), 13);
	    

	    //Retrieve the GMap2 object and start manipulating your map
	    gmaps = mapWidget.getGmap();
	    
	    gmaps.addControl(GControl.GSmallZoomControl());
	    
	    //Add the widget to your GWT UI (You can do this before or after you call getGmap() now).
	    //gmaps.openInfoWindowHtml(pos, "The center of the world"); 
		
	    
	    GMap2EventManager eventManager = GMap2EventManager.getInstance();

	    eventManager.addOnMoveStartListener(gmaps, new GMap2EventMoveListener() {
	    	public void onMoveStart(GMap2 map) {
	    		//Logger.log("move started");
	    	}

	    	public void onMoveEnd(GMap2 map){
	    	}

	    	public void onMove(GMap2 map){
	    	}
	    });
	    
	    markerEventManager = GMarkerEventManager.getInstance();
	    
	    
	    eventManager.addOnClickListener(gmaps, new GMap2EventClickListener() {
	    	public void onClick(GMap2 map, GOverlay overlay, GLatLng point){
	    		    		
	    		//null if clicking on marker
	    		//but we catch marker using the markerEventManager
	    		if(point != null){
	    			Logger.log("Clicked on " + point.toString());
	    			newPoint(point);	
	    		}
	    	}
	    });
	    
	    eventManager.addOnMouseOutListener(gmaps, this);
	    eventManager.addOnMouseOverListener(gmaps, this);
	    
	    initWidget(mapWidget);
	}
	

	private GMarker createPoint(LocationDTO locObj) {
		
		moveableMarkerOps.setTitle(locObj.getOnMapTitle());
		
		GMarker m = new GMarker(locObj.getLocation().getLocation(),moveableMarkerOps);
		markerEventManager.addOnDragEndListener(m, this);
		markerEventManager.addOnClickListener(m, this);
		
		m.enableDragging();		
		gmaps.addOverlay(m);
		
		markerToLocation.put(m, locObj);
		return m;
	}

	public void clear() {
		gmaps.clearOverlays();
		
	}

	private void newPoint(GLatLng point) {
		
		LocationDTO newLoc = controller.getNewLocationForPoint(point);
		
		if(newLoc != null){		
			GMarker marker = createPoint(newLoc);
		
		}
		
		
		
	}
	
	
	private void userSelected(GMarker marker) {
		
		System.out.println("Marker clicked "+marker);
		
		LocationDTO selected = (LocationDTO) markerToLocation.get(marker);
		System.out.println("sel "+selected);
				
		if(selected != null){
			controller.userClicked(selected);
		}
	}
		
	public void add(LocationDTO locObj) {
		createPoint(locObj);		
	}

	
	
	
	
	
	
	
	public void setSize(int i) {
		mapWidget.setPixelSize(i, i);
	}




	
	
	public void onDragEnd(GMarker marker) {
		LocationDTO dragged = (LocationDTO) markerToLocation.get(marker);
		
		if(dragged != null){
			
			System.out.println("!!!!!!!!!!!!!!!!!!!!drag ended "+marker.getPoint());
			gmaps.setCenter(marker.getPoint());
			
			dragged.getLocation().setLocation(marker.getPoint());			
			controller.update(dragged);
		}
		
	}
	public void onDragStart(GMarker marker) {
		// TODO Auto-generated method stub
		
	}


	public void centerOn(HippoLocation location) {
		System.out.println("!!!!!!!!!!!!!CENTER ON "+location.getLocation());
		//gmaps.setCenter(latlng)
		gmaps.setCenter(location.getLocation());
	}


	
	public boolean onWheel(Widget widget, int delta) {		
		if(weHaveFocus){
			System.out.println("Map processing wheel");
			if(delta < 0){
				gmaps.zoomOut();
			}else{
				gmaps.zoomIn();
			}
			return true;
		}
		return false;
	}


	public void onMouseMove(GMap2 map, GLatLng latlng) {}
	public void onMouseOut(GMap2 map, GLatLng latlng) {
		weHaveFocus = false;
	}
	public void onMouseOver(GMap2 map, GLatLng latlng) {
		weHaveFocus = true;
	}


	public void onClick(GMarker marker) {
		userSelected(marker);
	}
	public void onDblClick(GMarker marker) {
		
	}
	
	
	
}
