package com.aavu.client.widget;

import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SourcesClickEvents;

public class DeleteButton extends Composite implements SourcesClickEvents {
	
	private Image image;

	public DeleteButton(){		
		image = ConstHolder.images.bin_closed().createImage();
		image.addMouseListener(new TooltipListener(ConstHolder.myConstants.delete_tooltip()));		
		initWidget(image);
	}

	public void addClickListener(ClickListener listener) {
		image.addClickListener(listener);
	}

	public void removeClickListener(ClickListener listener) {
		image.removeClickListener(listener);
	}

}
