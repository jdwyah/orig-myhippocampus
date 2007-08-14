package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.Interactive;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.LoginListener;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginWindow extends PopupWindow {

	private static final int HEIGHT = 150;
	private static final int WIDTH = 300;
	private static final String SECURITY_URL = "j_acegi_security_check";
	private static final String SECURITY_URL_OPENID = "site/j_acegi_openid_start";

	private FormPanel form;
	private Label messageLabel;
	private LoginListener listener;
	private Label toggleL;
	private boolean isOpenID;
	private HorizontalPanel passPanel;
	private TextBox username;
	private TextBox openID;


	private static boolean semaphore = false;

	private static String lastNameEntered = "";

	/**
	 * Prevents multiple instances with a semaphore.
	 * 
	 * @param ConstHolder.myConstants -
	 *            NOTE: this is used by both AddLink & Hippo modules, so we can't rely on
	 *            Manager.ConstHolder.myConstants being initialized.
	 * 
	 * @param manager
	 */
	public LoginWindow(GInternalFrame frame, LoginListener listener) {
		super(frame, ConstHolder.myConstants.loginTitle(), WIDTH, HEIGHT);
		this.listener = listener;

		if (semaphore == false) {
			System.out.println("CREATING");
			semaphore = true;
		} else {
			System.out.println("KILLING");
			close();
			return;
		}

		form = new FormPanel();

		KeyboardListener enterListener = new KeyboardListenerAdapter() {
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if (keyCode == KEY_ENTER) {
					form.submit();
				}
			}
		};

		form.setAction(Interactive.getRelativeURL(SECURITY_URL));

		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.

		VerticalPanel panel = new VerticalPanel();

		username = new TextBox();
		username.setName("j_username");
		username.setText(lastNameEntered);

		openID = new TextBox();
		openID.setName("openid_url");
		openID.setText(lastNameEntered);


		final PasswordTextBox password = new PasswordTextBox();
		password.setName("j_password");
		password.addKeyboardListener(enterListener);

		toggleL = new Label();
		toggleL.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				toggle();
			}
		});
		toggleL.addStyleName("gwt-Hyperlink");

		HorizontalPanel uP = new HorizontalPanel();

		uP.add(new Label(ConstHolder.myConstants.deliciousUsername()));
		uP.add(username);
		uP.add(openID);

		passPanel = new HorizontalPanel();
		passPanel.add(new Label(ConstHolder.myConstants.deliciousPassword()));
		passPanel.add(password);

		panel.add(toggleL);

		panel.add(uP);
		panel.add(passPanel);

		panel.add(new Button("Login", new ClickListener() {
			public void onClick(Widget sender) {
				form.submit();
			}
		}));

		messageLabel = new Label("");
		panel.add(messageLabel);

		form.addFormHandler(new FormHandler() {

			public void onSubmitComplete(FormSubmitCompleteEvent event) {

				// TODO parse bad password etc. Super-Fragile string comps
				if (event.getResults() == null
						|| -1 != event.getResults().indexOf("not successful")
						|| -1 != event.getResults().indexOf("Bad credentials")
						|| -1 != event.getResults().indexOf("404")) {
					Logger.log("Login Fail: " + event.getResults());
					failure();
				} else {
					Logger.log("DO SUCC |" + event.getResults() + "|");
					success();
				}


			}

			public void onSubmit(FormSubmitEvent event) {
				System.out.println("submit to " + form.getAction());

				// This event is fired just before the form is submitted. We can take
				// this opportunity to perform validation.
				// if (username.getText().length() == 0) {
				// Window.alert("Username cannot be empty");
				// event.setCancelled(true);
				// }
				// if (password.getText().length() == 0) {
				// Window.alert("Password cannot be empty");
				// event.setCancelled(true);
				// }
				lastNameEntered = username.getText();
			}
		});

		frame.addFrameListener(new GFrameAdapter() {
			public void frameClosed(GFrameEvent evt) {
				semaphore = false;
			}
		});


		setToOpenID(false);

		form.setWidget(panel);

		setCenteredContent(form);
	}

	private void toggle() {
		setToOpenID(!isOpenID);
	}

	private void setToOpenID(boolean toOpenID) {
		if (toOpenID) {
			openID.setVisible(true);
			username.setVisible(false);
			toggleL.setText(ConstHolder.myConstants.login_Standard());
			form.setAction(Interactive.getRelativeURL(SECURITY_URL_OPENID));
			passPanel.setVisible(false);
		} else {
			username.setVisible(true);
			openID.setVisible(false);
			toggleL.setText(ConstHolder.myConstants.login_OpenID());
			form.setAction(Interactive.getRelativeURL(SECURITY_URL));
			passPanel.setVisible(true);
		}

		isOpenID = toOpenID;
	}

	private void failure() {
		messageLabel.setText(ConstHolder.myConstants.login_failure());
	}

	private void success() {
		messageLabel.setText(ConstHolder.myConstants.login_success());
		listener.loginSuccess();

		Timer t = new Timer() {
			public void run() {
				// free up the login lock for next time
				semaphore = false;
				close();
			}
		};
		t.schedule(2000);

	}

}
