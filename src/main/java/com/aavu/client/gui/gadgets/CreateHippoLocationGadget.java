package com.aavu.client.gui.gadgets;

import java.util.Date;

import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Image;

/**
 * Not exactly what we'd envisioned as a 'gadget' since it should just be used to create new topics.
 * 
 * @author Jeff Dwyer
 * 
 */
public class CreateHippoLocationGadget extends CreateTopicGadget {


	public CreateHippoLocationGadget(Manager _manager) {

		super(_manager);

	}

	// @Override
	public boolean isOnContextMenu() {
		return false;
	}

	// @Override
	public boolean isOnMapContextMenu() {
		return true;
	}

	// @Override
	public Image getPickerButton() {
		Image b = ConstHolder.images.gadgetMap().createImage();

		// b.addMouseListener(new TooltipListener(0, 40, getDisplayName()));
		return b;
	}


	// @Override
	public boolean isOnForTopic(Topic topic) {
		return true;
	}

	// @Override
	public void createInstance(Manager manager, int[] lngLat, Date dateCreated) {
		HippoLocation hippoLocation = new HippoLocation();
		manager.createNew(hippoLocation, lngLat, dateCreated, false, true);
	}

	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.hippolocation_new();
	}


}
