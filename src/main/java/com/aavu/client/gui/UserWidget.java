package com.aavu.client.gui;

import com.aavu.client.Interactive;
import com.aavu.client.domain.User;
import com.aavu.client.gui.hierarchy.BorderThemedPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ExternalLink;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserWidget extends Composite {

	private Label usernameLabel;
	private Manager manager;


	private BorderThemedPanel btp;
	private VerticalPanel mainP;


	public UserWidget(final Manager manager) {
		this.manager = manager;
		mainP = new VerticalPanel();

		usernameLabel = new Label(ConstHolder.myConstants.userw_none());

		if (manager.isEdittable()) {
			HorizontalPanel noUserPanel = new HorizontalPanel();
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
		}


		btp = new BorderThemedPanel();
		btp.setCaption(usernameLabel);
		btp.setContent(mainP);

		initWidget(btp);

	}

	public void load() {
		User u = manager.getUser();

		if (u != null) {

			Label rootLabel = new Label("Desktop");
			rootLabel.addStyleName("H-External-Hyperlink");
			rootLabel.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					manager.gotoRoot();
				}
			});



			if (manager.isEdittable()) {

				btp.setCaption(new Label("User: " + u.getNickname()));
				mainP.clear();

				HorizontalPanel hp = new HorizontalPanel();
				hp.add(rootLabel);

				hp.add(new ExternalLink(ConstHolder.myConstants.userw_site(), Interactive
						.getRelativeURL(ConstHolder.myConstants.userw_site_url())));
				hp.add(new ExternalLink(ConstHolder.myConstants.userw_logout(), Interactive
						.getRelativeURL(ConstHolder.myConstants.userw_logout_url())));

				hp.setSpacing(5);

				mainP.add(hp);

			} else {
				btp.setCaption(new Label("Browse: " + u.getNickname()));
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(rootLabel);

				hp.add(new ExternalLink(ConstHolder.myConstants.userw_index(), Interactive
						.getRelativeURL(ConstHolder.myConstants.userw_index_url())));

				hp.setSpacing(5);

				mainP.add(hp);
			}
		}

	}
}
