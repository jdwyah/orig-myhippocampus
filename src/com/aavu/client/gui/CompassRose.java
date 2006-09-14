package com.aavu.client.gui;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.gui.ext.MultiDivPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CompassRose extends SimplePanel implements ClickListener {
	
	private PopupPanel searchD;

	public CompassRose(){
		
		PNGImage rose = new PNGImage("img/CompassRose.png",120,120);
		rose.addClickListener(this);
		
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Label("Search:"));
		hp.add(new TextBox());
		
		searchD = new PopupPanel(true);		
		searchD.add(hp);
		searchD.hide();
		
		MultiDivPanel mainP = new MultiDivPanel();
		
		mainP.add(rose);
		mainP.add(searchD);
		
		setElement(mainP.getElement());
		setStyleName("GuiTest-CompassRose");
	}

	public void onClick(Widget sender) {
		searchD.show();
	}

}
