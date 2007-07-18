package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OptionsPanel extends DisplayPanel {

	public OptionsPanel(Topic topic, Manager manager) {
		super("Options");

		HorizontalPanel mainPanel = new HorizontalPanel();

		Label renameL = new Label("Rename");
		renameL.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {

			}
		});

		Label removeL = new Label("Remove");
		removeL.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {

			}
		});

		Label deleteL = new Label("Delete");
		deleteL.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {

			}
		});


		mainPanel.add(renameL);
		mainPanel.add(removeL);
		mainPanel.add(deleteL);


		add(mainPanel);

		// setStyleName("H-OptionsPanel");
		// addStyleName("H-Gadget");
	}

}
