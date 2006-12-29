package com.aavu.client.gui;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.gui.ext.MultiDivPanel;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CompassRose extends AbsolutePanel {
	
	private TextBox searchText = new TextBox();
	private Manager manager;

	public CompassRose(Manager _manager){
		this.manager = _manager;

		PNGImage rose = new PNGImage("img/CompassRose.png",120,120);

		Button searchB = new Button(manager.myConstants.search());
		searchB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				doSearch();				
			}});
		searchText.addKeyboardListener(new KeyboardListenerAdapter(){
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if(keyCode == KEY_ENTER){
					doSearch();
				}
			}});

		
		add(rose,0,0);		
		add(searchText,120,0);
		add(searchB,260,0);

		/*
		 * override the AbsolutePanel position: relative
		 * otherwise we got a left: 8px; top: 8px;
		 */		
		//DOM.setStyleAttribute(getElement(), "position", "absolute");		
				
		
		setStyleName("H-CompassRose");

	}
	private void doSearch() {
		System.out.println("click "+searchText.getText());
		manager.doSearch(searchText.getText());
	}

}
