package com.aavu.client.gui.gadgets;

import java.util.Iterator;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class GadgetPicker extends Composite {
	
	private HorizontalPanel gadgetOptionsP;
	private boolean open;
	
	

	/**
	 * The picker widget.
	 * 
	 * Get's the full list of ALL gadgets and displays them when asked. 
	 * Forwards clicks to gadgetManager.show(gadget)
	 * 
	 * @param gadgetManager
	 */
	public GadgetPicker(final GadgetManager gadgetManager) {
		
		gadgetOptionsP = new HorizontalPanel();
		
		for (Iterator iter = gadgetManager.getFullGadgetList().iterator(); iter.hasNext();) {
			final Gadget gadget = (Gadget) iter.next();
			
			Image imgButton = gadget.getPickerButton();
			imgButton.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					gadgetManager.show(gadget);
					close();
				}				
			});
			
			gadgetOptionsP.add(imgButton);
						
		}
		
				
		gadgetOptionsP.setVisible(false);
				
		Image addButton = ConstHolder.images.gadgetPicker().createImage();
		addButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {				
				toggle();		
			}});
		
		HorizontalPanel mainP = new HorizontalPanel();
		
		mainP.add(gadgetOptionsP);
		mainP.add(addButton);
		
		initWidget(mainP);
		
		gadgetOptionsP.addStyleName("H-Gadget");
		
		addStyleName("H-GadgetPicker");
		
	}
	
	private void toggle(){
		if(open){
			close();
		}else{
			open();
		}
	}
	
	public void close() {
		open = false;
		gadgetOptionsP.setVisible(false);
	}

	private void open(){
		open = true;
		gadgetOptionsP.setVisible(true);
	}

}
