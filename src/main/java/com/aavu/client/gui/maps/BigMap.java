package com.aavu.client.gui.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.IntPair;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.gui.explorer.ExplorerPanel;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.glossary.SimpleTopicDisplay;
import com.aavu.client.gui.maps.ext.GWTInfoWidget;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mapitz.gwt.googleMaps.client.GLatLng;
import com.mapitz.gwt.googleMaps.client.GMarker;

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
			Logger.error("Couldn't Load Map. Connection to Google Maps may have failed.");
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

	private void addToMap(List allLocations) {
		mapWidget.clear();

		// <IntPair,Set<LocationDTO>>
		Map lowPassFilter = new HashMap();

		for (Iterator iter = allLocations.iterator(); iter.hasNext();) {
			LocationDTO locdto = (LocationDTO) iter.next();

			IntPair key = locdto.getLocation().getFilteredLocation();
			Set locations = (Set) lowPassFilter.get(key);
			if (locations == null) {
				locations = new HashSet();
			} else {
				System.out.println("match");
			}
			System.out.println("key " + key.getX() + " " + key.getY() + " "
					+ locdto.getOnMapTitle());
			locations.add(locdto);
			lowPassFilter.put(key, locations);

		}
		System.out.println("--fin--");
		for (Iterator iter = lowPassFilter.keySet().iterator(); iter.hasNext();) {
			IntPair key = (IntPair) iter.next();

			Set locations = (Set) lowPassFilter.get(key);

			System.out.println("key " + key.getX() + " " + key.getY());

			boolean partOfAmalgam = false;

			if (locations.size() > 1) {
				partOfAmalgam = true;
				mapWidget.addAmalgam(locations);
			}

			for (Iterator iterator = locations.iterator(); iterator.hasNext();) {
				LocationDTO locDTO = (LocationDTO) iterator.next();
				System.out.println("add regul " + locDTO.getOnMapTitle() + " " + partOfAmalgam);
				mapWidget.add(locDTO, partOfAmalgam);
			}



		}



	}

	/**
	 * no add, so return null here
	 */
	public LocationDTO getNewLocationForPoint(GLatLng point) {
		return null;
	}

	public Widget getWidget() {
		return this;
	}

	private List converTopicToTI(List topics) {
		List ll = new ArrayList();
		for (Iterator iterator = topics.iterator(); iterator.hasNext();) {
			Topic t = (Topic) iterator.next();
			ll.add(t.getIdentifier());
		}
		return ll;
	}

	public void load(Topic topic) {
		List l = new ArrayList();
		l.add(topic);
		load(l);
	}

	public void load(List tags) {


		manager.getTopicCache().getLocationsFor(converTopicToTI(tags),
				new StdAsyncCallback(ConstHolder.myConstants.bigmap_getall_async()) {
					// @Override
					public void onSuccess(Object result) {
						super.onSuccess(result);
						List locationsByTag = (List) result;
						List allLocations = new ArrayList();
						for (Iterator iter = locationsByTag.iterator(); iter.hasNext();) {
							List locs = (List) iter.next();
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
		manager.displayInfo("Dragging not supported here yet. Use the MapGadget to edit locations");
		// System.out.println("Update "+dragged+" selectedMeta "+selectedMeta);
		// Set locations = myTopic.getMetaValuesFor(selectedMeta);
		// locations.add(dragged.getLocation());

		// System.out.println("Updating locations size "+locations.size());
		// for (Iterator iter = locations.iterator(); iter.hasNext();) {
		// HippoLocation hl = (HippoLocation) iter.next();
		// System.out.println("hl "+hl);
		// }


		// manager.getTopicCache().executeCommand(myTopic,new
		// SaveMetaLocationCommand(dragged.getTopic(),dragged.getMeta(),dragged.getLocation()),
		// new StdAsyncCallback(ConstHolder.myConstants.save()){});

	}

	/**
	 * 
	 */
	public void userDoubleClicked(LocationDTO selected) {
		System.out.println("big select " + selected);
		manager.bringUpChart(selected.getTopic());
	}

	public void userSelected(LocationDTO selected, final GMarker marker) {

		// manager.getGui().showHover(selected.getTopic());

		SimpleTopicDisplay std = new SimpleTopicDisplay(selected.getTopic(), manager, closeable,
				BLURB_WIDTH, BLURB_HEIGHT, new EZCallback() {
					public void onSuccess(Object result) {

						SimpleTopicDisplay wi = (SimpleTopicDisplay) result;

						GWTInfoWidget gwtInfoWidg = new GWTInfoWidget(wi);

						marker.openInfoWindow(gwtInfoWidg);
					}
				});
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

}
