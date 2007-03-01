package com.aavu.client.gui.gadgets;

import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
			
			ImageButton imgButton = gadget.getPickerButton();
			imgButton.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					gadgetManager.show(gadget);
					close();
				}				
			});
			
			gadgetOptionsP.add(imgButton);
						
		}
		
				
		gadgetOptionsP.setVisible(false);
				
		ImageButton addButton = new ImageButton(Manager.myConstants.img_gadget_picker(),60,60);
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
