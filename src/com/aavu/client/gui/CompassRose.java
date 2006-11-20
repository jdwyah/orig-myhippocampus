package com.aavu.client.gui;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.gui.ext.MultiDivPanel;
import com.aavu.client.service.Manager;
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

public class CompassRose extends SimplePanel {

	private PopupPanel searchD;
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

		HorizontalPanel mainP = new HorizontalPanel();

		mainP.add(rose);		
		mainP.add(searchText);
		mainP.add(searchB);



		add(mainP);
		addStyleName("H-AbsolutePanel");
		addStyleName("H-CompassRose");

	}
	private void doSearch() {
		System.out.println("click "+searchText.getText());
		manager.doSearch(searchText.getText());
	}

}
