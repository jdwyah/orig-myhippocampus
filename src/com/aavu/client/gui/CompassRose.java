package com.aavu.client.gui;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CompassRose extends SimplePanel {
	
	private TextBox searchText = new TextBox();
	private Manager manager;	
	
	private AbsolutePanel absPanel = new AbsolutePanel();
	
	/**
	 * It seems redundant to have this absPanel as a member instead of just extending
	 * AbsolutePanel, but positioning things on the map absolutely seems to only really
	 * work with SimplePanel's that have Style AbsolutePanel  
	 * @param _manager
	 */
	public CompassRose(Manager _manager){
		this.manager = _manager;

		PNGImage rose = new PNGImage("img/plaque.png",66,120);

		Button searchB = new Button(ConstHolder.myConstants.search());
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
		
		
		
		int DOWN = 100;
		absPanel.add(rose,0,0);		
		absPanel.add(searchText,70,DOWN);
		absPanel.add(searchB,210,DOWN);
		
		
		//interestingly, this is only necessary for FF
		absPanel.setPixelSize(300,120);

		/*
		 * override the AbsolutePanel position: relative
		 * otherwise we got a left: 8px; top: 8px;
		 */		
		//DOM.setStyleAttribute(getElement(), "position", "absolute");		
				
		add(absPanel);
		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-CompassRose");			

	}
	private void doSearch() {
		System.out.println("click "+searchText.getText());
		manager.doSearch(searchText.getText());
	}
	

}
