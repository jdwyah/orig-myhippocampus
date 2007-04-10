package com.aavu.client.gui.gadgets;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.MetaDate;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;

/**
 * Gadget to display a topic's metadate information
 * 
 * @author Jeff Dwyer
 *
 */
public class TimeGadget extends MetaGadget  {
	
	public TimeGadget(Manager _manager){		
		super(_manager, ConstHolder.myConstants.gadget_time_title(), new MetaDate());
		
		addStyleName("H-TimeGadget");				
	}


	//@Override
	public ImageButton getPickerButton() {		
		ImageButton b = new ImageButton(ConstHolder.myConstants.img_gadget_time(),40,60);
		b.addMouseListener(new TooltipListener(0,40,ConstHolder.myConstants.gadget_time_title()));
		return b;
	}


	
}
