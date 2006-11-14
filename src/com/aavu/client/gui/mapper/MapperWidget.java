package com.aavu.client.gui.mapper;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MapperWidget extends PopupWindow{
	
	private Manager manager;

	public MapperWidget(Manager _manager) {
		super(_manager.myConstants.deliciousPopupTitle());
		this.manager = _manager;

		VerticalPanel mainPanel = new VerticalPanel();


		StackPanel panel = new StackPanel();
	    panel.add(new Label("Foo"), "foo");
	    panel.add(new Label("Bar"), "bar");
	    panel.add(new Label("Baz"), "baz");
	    
	    mainPanel.add(panel);
		
		setWidget(mainPanel);
	}	
}
