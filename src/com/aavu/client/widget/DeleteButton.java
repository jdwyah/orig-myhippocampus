package com.aavu.client.widget;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.strings.ConstHolder;

public class DeleteButton extends PNGImage {
	
	public DeleteButton(){
		super(ConstHolder.myConstants.img_delete(),
				16,16);
		addMouseListener(new TooltipListener(ConstHolder.myConstants.delete_tooltip()));			
	}

}
