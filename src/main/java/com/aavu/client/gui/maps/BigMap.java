package com.aavu.client.gui.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.IntPair;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTopicBasicCommand;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.explorer.ExplorerPanel;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Show all selected tag's map meta info.
 * 
 * @author Jeff Dwyer
 * 
 */
public class BigMap extends Composite implements ExplorerPanel, MapController {

	private static final int DEFAULT_ZOOM = 2;

	private static final int BLURB_HEIGHT = 200;
	private static final int BLURB_WIDTH = 350;

	private HippoMapWidget mapWidget;
	private SimplePanel extraPanel;
	private Manager manager;

	private CloseListener closeable;

	private int width;
	private int height;



	public BigMap(Manager manager, CloseListener closeable, int width, int height,
			PopupWindow window) {
		this.manager = manager;
		this.closeable = closeable;
		this.width = width;
		this.height = height;

		extraPanel = new SimplePanel();


		try {
			mapWidget = new HippoMapWidget(this, width, height, DEFAULT_ZOOM);
		} catch (JavaScriptException e) {
			e.printStackTrace();
			Logger.error("Couldn't Load Map. Connection to Google Maps may have failed. " + e);
			throw new RuntimeException(e);
		}
		extraPanel.add(mapWidget);

		window.addInternalFrameListener(new GFrameAdapter() {
			// @Override
			public void frameResized(GFrameEvent evt) {
				super.frameResized(evt);
				resize(evt);
			}
		});

		initWidget(extraPanel);
	}

	private void addToMap(List<LocationDTO> allLocations) {
		mapWidget.clear();


		Map<IntPair, Set<LocationDTO>> lowPassFilter = new HashMap<IntPair, Set<LocationDTO>>();

		for (LocationDTO locdto : allLocations) {

			IntPair key = locdto.getLocation().getFilteredLocation();
			Set<LocationDTO> locations = lowPassFilter.get(key);
			if (locations == null) {
				locations = new HashSet<LocationDTO>();
			} else {
				System.out.println("match");
			}
			System.out.println("key " + key.getX() + " " + key.getY() + " "
					+ locdto.getOnMapTitle());
			locations.add(locdto);
			lowPassFilter.put(key, locations);

		}
		System.out.println("--fin--");
		for (IntPair key : lowPassFilter.keySet()) {

			Set<LocationDTO> locations = lowPassFilter.get(key);

			System.out.println("key " + key.getX() + " " + key.getY());

			boolean partOfAmalgam = false;

			if (locations.size() > 1) {
				partOfAmalgam = true;
				mapWidget.addAmalgam(locations);
			}

			for (LocationDTO locDTO : locations) {
				System.out.println("add regul " + locDTO.getOnMapTitle() + " " + partOfAmalgam);
				mapWidget.add(locDTO, partOfAmalgam);
			}



		}



	}

	/**
	 * 
	 */
	public LocationDTO getNewLocationForPoint(LatLng point) {


		//
		// HippoLocation newLoc = new HippoLocation();
		// newLoc.setLocation(point);
		//
		//
		// manager.createNew(realT, lngLat, dateCreated, true, false);
		//
		// saveLocation(newLoc);
		//
		// LocationDTO locObj = new LocationDTO(myTopic.getIdentifier(), newLoc, selectedMeta);
		//
		// return locObj;

		return null;
	}

	public Widget getWidget() {
		return this;
	}

	private List<TopicIdentifier> converTopicToTI(List<Topic> topics) {
		List<TopicIdentifier> ll = new ArrayList<TopicIdentifier>();
		for (Topic t : topics) {
			ll.add(t.getIdentifier());
		}
		return ll;
	}

	public void load(Topic topic) {
		List<Topic> l = new ArrayList<Topic>();
		l.add(topic);
		load(l);
	}

	public void load(List<Topic> tags) {


		manager.getTopicCache().getLocationsFor(
				converTopicToTI(tags),
				new StdAsyncCallback<List<List<LocationDTO>>>(ConstHolder.myConstants
						.bigmap_getall_async()) {
					// @Override
					public void onSuccess(List<List<LocationDTO>> locationsByTag) {
						super.onSuccess(locationsByTag);

						List<LocationDTO> allLocations = new ArrayList<LocationDTO>();
						for (List<LocationDTO> locs : locationsByTag) {
							allLocations.addAll(locs);
						}
						addToMap(allLocations);
					}
				});
	}

	public void loadAll() {

		manager.getTopicCache().getAllLocations(
				new StdAsyncCallback(ConstHolder.myConstants.bigmap_getall_async()) {
					// @Override
					public void onSuccess(Object result) {
						super.onSuccess(result);
						List allLocations = (List) result;
						addToMap(allLocations);
					}
				});

	}


	public void update(LocationDTO dragged) {

		manager.getTopicCache().executeCommand(
				dragged.getLocation(),
				new SaveTopicBasicCommand(dragged.getLocation(), dragged.getLocation()
						.getLatitude(), dragged.getLocation().getLongitude()),
				new StdAsyncCallback(ConstHolder.myConstants.save()) {
				});

	}

	/**
	 * 
	 */
	public void userDoubleClicked(LocationDTO selected) {
		System.out.println("big select " + selected);
		manager.bringUpChart(selected.getTopic());
	}

	public void userSelected(LocationDTO selected, final Marker marker) {

		manager.getGui().showHover(selected.getTopic());

	}

	// @Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		System.out.println("DOING extra P visiblenews");

		mapWidget.setSize(width, height);
		mapWidget.setVisible(true);
		extraPanel.setVisible(true);

	}


	private void resize(GFrameEvent evt) {
		mapWidget.setSize(evt.getGFrame().getWidth(), evt.getGFrame().getHeight());
	}

	public Manager getManager() {
		return manager;
	}

}
