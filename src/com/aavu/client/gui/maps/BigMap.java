package com.aavu.client.gui.maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.commands.SaveMetaLocationCommand;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.gui.explorer.ExplorerPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mapitz.gwt.googleMaps.client.GLatLng;

/**
 * Show all selected tag's map meta info.
 * 
 * @author Jeff Dwyer
 *
 */
public class BigMap extends Composite implements ExplorerPanel, MapController {

	private static final int DEFAULT_ZOOM = 2;
	
	private HippoMapWidget mapWidget;	
	private Manager manager;
	
	public BigMap(Manager manager, int width, int height) {
		this.manager = manager;
		
		VerticalPanel mainP = new VerticalPanel();
		
		mapWidget = new HippoMapWidget(this,width,height,DEFAULT_ZOOM);		
		manager.addWheelistener(mapWidget);
		
		mainP.add(mapWidget);
				
		initWidget(mainP);
	}

	public Widget getWidget() {
		return this;
	}

	public void load(Set tags) {
		List tagL = new ArrayList();
		tagL.addAll(tags);
		
		manager.getTopicCache().getLocationsFor(tagL,new StdAsyncCallback(ConstHolder.myConstants.bigmap_getall_async()){
			//@Override
			public void onSuccess(Object result) {
				super.onSuccess(result);				
				List locationsByTag = (List) result;
				List allLocations = new ArrayList();
				for (Iterator iter = locationsByTag.iterator(); iter.hasNext();) {
					List locs = (List) iter.next();
					allLocations.addAll(locs);
				}
				addToMap(allLocations);							
			}});
	}

	public void loadAll() {
		
		manager.getTopicCache().getAllLocations(new StdAsyncCallback(ConstHolder.myConstants.bigmap_getall_async()){
			//@Override
			public void onSuccess(Object result) {
				super.onSuccess(result);				
				List allLocations = (List) result;				
				addToMap(allLocations);							
			}});
		
	}

	private void addToMap(List allLocations) {
		mapWidget.clear();
		for (Iterator iter = allLocations.iterator(); iter.hasNext();) {
			LocationDTO locdto = (LocationDTO) iter.next();				
			mapWidget.add(locdto);
		}	
	}
	
	/**
	 * no add, so return null here
	 */
	public LocationDTO getNewLocationForPoint(GLatLng point) {
		return null;
	}


	public void update(LocationDTO dragged) {
		Window.alert("Dragging not supported here yet. Use the MapGadget to edit locations");
//		System.out.println("Update "+dragged+" selectedMeta "+selectedMeta);
//		Set locations = myTopic.getMetaValuesFor(selectedMeta);		
//		locations.add(dragged.getLocation());		
//		
//		System.out.println("Updating locations size "+locations.size());
//		for (Iterator iter = locations.iterator(); iter.hasNext();) {
//			HippoLocation hl = (HippoLocation) iter.next();
//			System.out.println("hl "+hl);
//		}
//		
//		
//		manager.getTopicCache().executeCommand(myTopic,new SaveMetaLocationCommand(dragged.getTopic(),dragged.getMeta(),dragged.getLocation()),
//				new StdAsyncCallback(ConstHolder.myConstants.save()){});

	}

	/**
	 * 
	 */
	public void userClicked(LocationDTO selected) {
		System.out.println("big select "+selected);
		manager.bringUpChart(selected.getTopic());		
	}

}
