package com.aavu.client.gui.maps;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.Meta;
import com.aavu.client.gui.gadgets.MapGadget;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.mapitz.gwt.googleMaps.client.GControl;
import com.mapitz.gwt.googleMaps.client.GLatLng;
import com.mapitz.gwt.googleMaps.client.GMap2;
import com.mapitz.gwt.googleMaps.client.GMap2EventClickListener;
import com.mapitz.gwt.googleMaps.client.GMap2EventManager;
import com.mapitz.gwt.googleMaps.client.GMap2EventMoveListener;
import com.mapitz.gwt.googleMaps.client.GMap2Widget;
import com.mapitz.gwt.googleMaps.client.GMarker;
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
public class HippoMapWidget extends Composite implements GMarkerEventDragListener {

	private static final int DEFAULT_ZOOM = 1;
	
	private GMap2 gmaps;
	private GMap2Widget mapWidget;
	private MapGadget gadget;
	
	private Map markerToLocation = new HashMap();

	private GMarkerOptions moveableMarkerOps;

	private GMarkerEventManager markerEventManager;
	
	
	public HippoMapWidget(MapGadget gadget) {
		this.gadget = gadget;
		
		//middle of atlantic
		GLatLng center = new GLatLng(33.13755119234614, -35.15625);
		
				
		moveableMarkerOps = new GMarkerOptions();		
		moveableMarkerOps.setDraggable(true); 
		
	    mapWidget = new GMap2Widget("200", "200",center,DEFAULT_ZOOM,null);
	    
	    //Alternative method: Setting your own center value and zoom in the constructor
	    //GMap2Widget mapWidget = new GMap2Widget("300", "300",new GLatLng(37.4419, -122.1419), 13);
	    

	    //Retrieve the GMap2 object and start manipulating your map
	    gmaps = mapWidget.getGmap();
	    gmaps.addControl(GControl.GSmallMapControl());

	    
	    //Add the widget to your GWT UI (You can do this before or after you call getGmap() now).
	    //gmaps.openInfoWindowHtml(pos, "The center of the world"); 
		
	    
	    GMap2EventManager eventManager = GMap2EventManager.getInstance();

	    eventManager.addOnMoveStartListener(gmaps, new GMap2EventMoveListener() {
	    	public void onMoveStart(GMap2 map) {
	    		Logger.log("move started");
	    	}

	    	public void onMoveEnd(GMap2 map){
	    	}

	    	public void onMove(GMap2 map){
	    	}
	    });
	    
	    markerEventManager = GMarkerEventManager.getInstance();
	    
	    
	    eventManager.addOnClickListener(gmaps, new GMap2EventClickListener() {
	    	public void onClick(GMap2 map, GOverlay overlay, GLatLng point){
	    	
	    		if(overlay != null){
	    			userSelected(overlay);
	    		}
	    		
	    		//null if clicking on marker
	    		if(point != null){
	    			Logger.log("Clicked on " + point.toString());
	    			newPoint(point);	
	    		}
	    	}

	    });
	    
	    initWidget(mapWidget);
	}
	

	private GMarker createPoint(GLatLng point) {
		
		//moveableMarkerOps.setTitle("Drag me to where you want to search.");
		
		GMarker m = new GMarker(point,moveableMarkerOps);
		markerEventManager.addOnDragEndListener(m, this);
		
		m.enableDragging();		
		gmaps.addOverlay(m);
		return m;
	}

	public void clear() {
		gmaps.clearOverlays();
		
	}

	private void newPoint(GLatLng point) {
		
		HippoLocation newLoc = gadget.getNewLocationForPoint(point);
		
		if(newLoc != null){		
			GMarker marker = createPoint(newLoc.getLocation());
			
			markerToLocation.put(marker, newLoc);
		}
		
		
		
	}
	
	
	private void userSelected(GOverlay overlay) {
		HippoLocation selected = (HippoLocation) markerToLocation.get(overlay);
		if(selected != null){
			gadget.userSelected(selected);
		}
	}
		
	public void add(Meta meta, HippoLocation mv) {
		createPoint(mv.getLocation());		
	}

	
	
	
	
	
	
	
	public void setSize(int i) {
		mapWidget.setPixelSize(i, i);
	}




	
	
	public void onDragEnd(GMarker marker) {
		// TODO Auto-generated method stub
		
	}
	public void onDragStart(GMarker marker) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
