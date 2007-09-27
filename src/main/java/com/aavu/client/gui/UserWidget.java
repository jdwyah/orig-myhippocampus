package com.aavu.client.gui;

import com.aavu.client.Interactive;
import com.aavu.client.domain.User;
import com.aavu.client.gui.hierarchy.BorderThemedPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ExternalLink;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserWidget extends Composite {

	private Label usernameLabel;
	private Manager manager;
	private HorizontalPanel userPanel;
	private HorizontalPanel noUserPanel;
	private HorizontalPanel topPanel;

	public UserWidget(final Manager manager) {
		this.manager = manager;
		VerticalPanel mainP = new VerticalPanel();

		usernameLabel = new Label(ConstHolder.myConstants.userw_none());

		Button rootB = new Button("Desktop");
		rootB.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				// manager.gotoRoot();
			}
		});

		topPanel = new HorizontalPanel();
		topPanel.add(rootB);
		topPanel.add(new Label(ConstHolder.myConstants.userw_usename()));
		topPanel.add(usernameLabel);
		mainP.add(topPanel);

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

		Label loginLabel = new Label("Login");
		loginLabel.addStyleName("H-External-Hyperlink");
		loginLabel.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				manager.doLogin();
			}
		});
		noUserPanel.add(loginLabel);
		mainP.add(noUserPanel);

		BorderThemedPanel btp = new BorderThemedPanel();
		btp.setCaption(new Label("User"));
		btp.setContent(mainP);

		initWidget(btp);

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
