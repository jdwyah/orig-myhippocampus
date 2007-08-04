package com.aavu.client.gui.gadgets;

import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * Not exactly what we'd envisioned as a 'gadget' since it should just be used to create new topics.
 * 
 * @author Jeff Dwyer
 * 
 */
public class CreateTopicGadget extends Gadget {


	private Topic topic;


	public CreateTopicGadget(Manager _manager) {

		super(_manager);


		// PEND shoudl never be displayed
		initWidget(new Label("New Topic"));

	}

	// @Override
	public int load(Topic topic) {
		this.topic = topic;
		return 1;
	}

	// @Override
	public boolean isOnContextMenu() {
		return true;
	}

	// @Override
	public Image getPickerButton() {
		Image b = ConstHolder.images.newTopic().createImage();
		b.addMouseListener(new TooltipListener(0, 40, getDisplayName()));
		return b;
	}

	// @Override
	public void showForFirstTime() {
		super.showForFirstTime();
	}

	// @Override
	public boolean isOnForTopic(Topic topic) {
		return true;
	}

	// @Override
	public void createInstance(Manager manager, int[] lngLat) {
		RealTopic realT = new RealTopic();
		manager.createNew(realT, lngLat);
	}

	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.topic_new();
	}


}
