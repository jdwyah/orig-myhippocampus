package com.aavu.client.gui.gadgets;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.edit.EntryPreviewWidget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class EntryPreview extends Gadget {


	private EntryPreviewWidget entryPrev;
	private Topic topic;
	private Manager manager;

	public EntryPreview(Manager _manager) {

		super(ConstHolder.myConstants.entry());

		this.manager = _manager;

		entryPrev = new EntryPreviewWidget();

		entryPrev.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				// manager.editOccurrence(topic);
			}
		});

		initWidget(entryPrev);


		addStyleName("H-EntryPreview");

	}

	// @Override
	public int load(Topic topic) {
		this.topic = topic;
		// entryPrev.load(topic);
		return 1;
	}

	// @Override
	public Image getPickerButton() {
		Image b = ConstHolder.images.gadgetEntry().createImage();
		b.addMouseListener(new TooltipListener(0, 40, getDisplayName()));
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
		manager.createNew(new Entry());
	}


	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.entry();
	}


}
