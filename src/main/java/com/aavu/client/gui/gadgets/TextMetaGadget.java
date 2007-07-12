package com.aavu.client.gui.gadgets;

import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Image;

/**
 * Gadget to display a topic's metatext information
 * 
 * @author Jeff Dwyer
 * 
 */
public class TextMetaGadget extends MetaGadget {

	public TextMetaGadget(Manager _manager) {
		super(_manager, ConstHolder.myConstants.gadget_text_title(), new MetaText());

		addStyleName("H-TextGadget");
	}


	// @Override
	public Image getPickerButton() {
		Image b = ConstHolder.images.metaText().createImage();
		b.addMouseListener(new TooltipListener(0, 40, getDisplayName()));
		return b;
	}

	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.gadget_text_title();
	}

	// @Override
	public Topic getPrototypeObj() {
		return new MetaText();
	}

}
