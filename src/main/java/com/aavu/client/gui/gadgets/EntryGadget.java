package com.aavu.client.gui.gadgets;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.edit.EntryPreviewWidget;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EntryGadget extends Gadget {


	private VerticalPanel entryP;

	private ScrollPanel scroll;

	private static final int MAX_HEIGHT = 200;
	private static final int HEIGHT_PER_ENTRY = 120;



	public EntryGadget(Manager _manager) {

		super(_manager);


		entryP = new VerticalPanel();
		entryP.add(new Label(" "));

		scroll = new ScrollPanel(entryP);
		scroll.setAlwaysShowScrollBars(false);


		scroll.setHeight(MAX_HEIGHT + "px");

		initWidget(scroll);

		addStyleName("H-EntryPreview");

	}

	// @Override
	public int load(Topic topic) {
		super.load(topic);
		Set entries = topic.getEntries();
		entryP.clear();

		if (entries.size() < 1) {
			entryP.add(new Label(" "));
		}


		for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			// PEND MED whatever are we to do w/ "" titles?
			if (entry.getTitle() != null) {
				entryP.add(new Label(" " + entry.getTitle()));
			}
			EntryPreviewWidget epw = new EntryPreviewWidget();
			epw.load(entry);
			entryP.add(epw);
		}

		int scrollH = entries.size() * HEIGHT_PER_ENTRY;
		scrollH = (scrollH < MAX_HEIGHT) ? scrollH : MAX_HEIGHT;
		scroll.setHeight(scrollH + "px");
		System.out.println("EntryGadg scroll heigh " + scrollH);

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
	public void createInstance(Manager manager, int[] lngLat, Date dateCreated) {
		Entry e = new Entry();
		manager.createNew(e, lngLat, dateCreated, false, false);
	}

	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.entry();
	}


}
