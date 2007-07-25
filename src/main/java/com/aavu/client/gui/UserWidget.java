package com.aavu.client.gui;

import com.aavu.client.Interactive;
import com.aavu.client.domain.User;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ExternalLink;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserWidget extends Composite {

	private Label usernameLabel;
	private Manager manager;
	private HorizontalPanel userPanel;
	private HorizontalPanel noUserPanel;

	public UserWidget(Manager manager) {
		this.manager = manager;
		VerticalPanel mainP = new VerticalPanel();

		usernameLabel = new Label(ConstHolder.myConstants.userw_none());

		HorizontalPanel h1 = new HorizontalPanel();
		h1.add(new Label(ConstHolder.myConstants.userw_usename()));
		h1.add(usernameLabel);
		mainP.add(h1);

		userPanel = new HorizontalPanel();

		userPanel.add(new ExternalLink(ConstHolder.myConstants.userw_site(), Interactive
				.getRelativeURL(ConstHolder.myConstants.userw_site_url())));
		userPanel.add(new ExternalLink(ConstHolder.myConstants.userw_logout(), Interactive
				.getRelativeURL(ConstHolder.myConstants.userw_logout_url())));
		userPanel.setVisible(false);
		userPanel.setSpacing(5);

		mainP.add(userPanel);

		noUserPanel = new HorizontalPanel();
		noUserPanel.add(new ExternalLink(ConstHolder.myConstants.userw_signup(), Interactive
				.getRelativeURL(ConstHolder.myConstants.userw_logout_url())));
		mainP.add(noUserPanel);


		initWidget(mainP);
		addStyleName("H-BlueFade");
	}

	public void load() {
		User u = manager.getUser();

		if (u == null) {
			usernameLabel.setText(ConstHolder.myConstants.userw_none());
			userPanel.setVisible(false);
			noUserPanel.setVisible(true);
		} else {
			usernameLabel.setText(u.getUsername());
			noUserPanel.setVisible(false);
			userPanel.setVisible(true);
		}

	}
}
