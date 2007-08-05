package com.aavu.client.gui.gadgets;

import java.util.Iterator;
import java.util.Set;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.edit.EntryPreviewWidget;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EntryGadget extends Gadget {


	private VerticalPanel entryP;


	public EntryGadget(Manager _manager) {

		super(_manager);


		entryP = new VerticalPanel();
		entryP.add(new Label(" "));

		initWidget(entryP);

		addStyleName("H-EntryPreview");

	}

	// @Override
	public int load(Topic topic) {

		Set entries = topic.getEntries();
		entryP.clear();

		if (entries.size() < 1) {
			entryP.add(new Label(" "));
		}

		for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			// PEND MED whatever are we to do w/ "" titles?
			entryP.add(new Label(" " + entry.getTitle()));
			EntryPreviewWidget epw = new EntryPreviewWidget();
			epw.load(entry);
			entryP.add(epw);
		}

		return entries.size();
	}

	// @Override
	public boolean isOnContextMenu() {
		return true;
	}


	// @Override
	public boolean isDisplayer() {
		return true;
	}


	// @Override
	public Image getPickerButton() {
		Image b = ConstHolder.images.gadgetEntry().createImage();
		// b.addMouseListener(new TooltipListener(0, 40, getDisplayName()));
		return b;
	}

	// @Override
	public void showForFirstTime() {
		super.showForFirstTime();
		// manager.editOccurrence(topic);
	}

	// @Override
	public boolean isOnForTopic(Topic topic) {
		return topic.hasEntry();
	}

	// @Override
	public void onClick(Manager manager) {

	}

	// @Override
	public void createInstance(Manager manager, int[] lngLat) {
		Entry e = new Entry();
		manager.createNew(e, lngLat);
	}

	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.entry();
	}


}
