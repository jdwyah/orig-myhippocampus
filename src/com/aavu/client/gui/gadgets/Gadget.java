package com.aavu.client.gui.gadgets;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.Topic;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Gadgets are the GUI building blocks for topics. 
 * An topic can have gadgets added to it they're a bit like super powers. 
 * 
 * 
 * @author Jeff Dwyer
 *
 */
public abstract class Gadget extends Composite {
	
	private VerticalPanel mainP;

	/**
	 * Child Gadget responsible for its own title bar w/ this constructor.
	 *
	 */
	public Gadget(){
		mainP = new VerticalPanel();		
		super.initWidget(mainP);		
		addStyleName("H-Gadget");
	}
	
	public Gadget(String title){
		mainP = new VerticalPanel();
		mainP.add(new HeaderLabel(title));		
		
		super.initWidget(mainP);
		
		addStyleName("H-Gadget");
	}
	
	
	//@Override
	protected void initWidget(Widget widget) {
		mainP.add(widget);				
	}
	
	public abstract int load(Topic topic);

	public abstract ImageButton getPickerButton();

	public void showForFirstTime() {}

	public abstract boolean isOnForTopic(Topic topic);

	/**
	 * Overridden by Gadgets that need to now when the GadgetDisplayer is visible
	 *
	 */
	public void nowVisible() {}
	
}
