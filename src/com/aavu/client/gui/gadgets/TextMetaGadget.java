package com.aavu.client.gui.gadgets;

import java.util.Set;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;

/**
 * Gadget to display a topic's metatext information
 * 
 * @author Jeff Dwyer
 *
 */
public class TextMetaGadget extends MetaGadget  {
	
	public TextMetaGadget(Manager _manager){		
		super(_manager, ConstHolder.myConstants.gadget_text_title(), new MetaText());
		
		addStyleName("H-TextGadget");				
	}


	//@Override
	public ImageButton getPickerButton() {		
		ImageButton b = new ImageButton(ConstHolder.myConstants.img_gadget_text(),40,34);
		b.addMouseListener(new TooltipListener(0,40,ConstHolder.myConstants.gadget_text_title()));
		return b;
	}


	//@Override
	public boolean isOnForTopic(Topic topic) {
		return topic.hasTextMetas();
	}

	//@Override
	protected Set getMetasFor(Topic topic) {
		return topic.getAllMetaTexts();
	}


	
}
