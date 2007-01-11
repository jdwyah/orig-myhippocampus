package com.aavu.client.gui;

import com.aavu.client.HippoTest;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginWindow extends PopupWindow {
	
	private static final int HEIGHT = 150;
	private static final int WIDTH = 300;
	private static final String SECURITY_URL = "/site/j_acegi_security_check";
	
	private FormPanel form;
	private Manager manager;
	private Label messageLabel;	

	private static boolean semaphore = false;
	
	private static String lastNameEntered = "";
	
	/**
	 * Prevents multiple instances with a semaphore.
	 * 
	 * @param manager
	 */
	public LoginWindow(Manager _manager) {
		super(_manager.newFrame(),_manager.myConstants.loginTitle(),WIDTH,HEIGHT);
		this.manager = _manager;
		
		if(semaphore == false){
			System.out.println("CREATING");
			semaphore = true;
		}else{			
			System.out.println("KILLING");
			close();
			return;
		}
		
		form = new FormPanel();
		
		
		form.setAction(HippoTest.getRelativeURL(SECURITY_URL));

		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.

		VerticalPanel panel = new VerticalPanel();
		
		final TextBox username = new TextBox();
		username.setName("j_username");
		username.setText(lastNameEntered);

		final PasswordTextBox password = new PasswordTextBox();
		password.setName("j_password");
		password.addKeyboardListener(new KeyboardListenerAdapter(){
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if(keyCode == KEY_ENTER){
					form.submit();					
				}
			}});

		HorizontalPanel uP = new HorizontalPanel();

		uP.add(new Label(manager.myConstants.deliciousUsername()));
		uP.add(username);

		HorizontalPanel pP = new HorizontalPanel();
		pP.add(new Label(manager.myConstants.deliciousPassword()));
		pP.add(password);

		panel.add(uP);
		panel.add(pP);

		panel.add(new Button("Login", new ClickListener() {
			public void onClick(Widget sender) {
				form.submit();
			}
		}));
		
		messageLabel = new Label("");		
		panel.add(messageLabel);
		
		form.addFormHandler(new FormHandler() {			

			public void onSubmitComplete(FormSubmitCompleteEvent event) {

				//TODO parse bad password etc. Super-Fragile string comps				
				if(event.getResults() == null 
						||
						-1 != event.getResults().indexOf("not successful")
						||
						-1 != event.getResults().indexOf("Bad credentials")
						||
						-1 != event.getResults().indexOf("404")
				){
					Logger.log("Login Fail: "+event.getResults());
					failure();
				}else{
					Logger.log("DO SUCC |"+event.getResults()+"|");
					success();
				}
				
				
			}

			public void onSubmit(FormSubmitEvent event) {
				// This event is fired just before the form is submitted. We can take
				// this opportunity to perform validation.
//				if (username.getText().length() == 0) {
//					Window.alert("Username cannot be empty");
//					event.setCancelled(true);
//				}
//				if (password.getText().length() == 0) {
//					Window.alert("Password cannot be empty");
//					event.setCancelled(true);
//				}
				lastNameEntered = username.getText();
			}
		});

		
		
		form.setWidget(panel);
		
		setCenteredContent(form);
	}

	private void failure(){
		messageLabel.setText(Manager.myConstants.login_failure());
	}
	private void success(){
		messageLabel.setText(Manager.myConstants.login_success());
		manager.loginSuccess();
		
		Timer t = new Timer() {
		      public void run() {
		  		//free up the login lock for next time
		  		semaphore = false;
		  		close();    	  
		      }
		    };
		t.schedule(2000);		
		
	}

}
