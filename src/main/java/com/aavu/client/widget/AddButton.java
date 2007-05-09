package com.aavu.client.widget;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.strings.ConstHolder;

public class AddButton extends PNGImage {
	
	public AddButton(String tooltip){
		super(ConstHolder.myConstants.img_add(),
				16,16);	
		addMouseListener(new TooltipListener(tooltip));		
		addStyleName("H-AddButton");
	}

}
