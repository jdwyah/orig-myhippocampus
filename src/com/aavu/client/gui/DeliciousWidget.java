package com.aavu.client.gui;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.util.LorumIpsum;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DeliciousWidget extends PopupWindow {

	private Manager manager;
	private PasswordTextBox password;
	private TextBox username;

	public DeliciousWidget(Manager _manager) {
		super(_manager.myConstants.deliciousPopupTitle());
		this.manager = _manager;

		VerticalPanel mainPanel = new VerticalPanel();

		username = new TextBox();
		password = new PasswordTextBox();

		HorizontalPanel uP = new HorizontalPanel();

		uP.add(new Label(manager.myConstants.deliciousUsername()));
		uP.add(username);

		HorizontalPanel pP = new HorizontalPanel();
		pP.add(new Label(manager.myConstants.deliciousPassword()));
		pP.add(password);

		mainPanel.add(uP);
		mainPanel.add(pP);

		Button godoitB = new Button(manager.myConstants.deliciousSubmit());
		godoitB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.addDeliciousTags(username.getText(),password.getText());	
			}});

		mainPanel.add(godoitB);

		setWidget(mainPanel);
	}	



}
