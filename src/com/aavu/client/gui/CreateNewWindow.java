package com.aavu.client.gui;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CreateNewWindow extends PopupWindow {
	
	private static final int HEIGHT = 60;
	private static final int WIDTH = 290;
	
	private Manager manager;
	private Label messageLabel;
	private TextBox name;
	private boolean isIsland;	
	
	/**
	 * Prevents multiple instances with a semaphore.
	 * @param island 
	 * 
	 * @param manager
	 */
	public CreateNewWindow(Manager _manager, boolean isIsland) {
		
		super(_manager.newFrame(),
				isIsland ? Manager.myConstants.island_new() : Manager.myConstants.topic_new(),
				WIDTH,HEIGHT);
		this.manager = _manager;
		this.isIsland = isIsland;
	
		
		HorizontalPanel panel = new HorizontalPanel();
		
		
		name = new TextBox();
		name.addKeyboardListener(new KeyboardListenerAdapter(){
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if(keyCode == KEY_ENTER){
					clicked();			
				}
			}});
		
		panel.add(name);
		panel.add(new Button(Manager.myConstants.island_create(), new ClickListener() {
			public void onClick(Widget sender) {
				clicked();
			}
		}));
		
		messageLabel = new Label("");		
		panel.add(messageLabel);
		
		setCenteredContent(panel);
		
	}
	
	private void clicked(){
		if(!name.getText().equals("")){
			
			manager.createTopic(name.getText(),isIsland);
			
			close();
		}else{
			messageLabel.setText("Enter a name");
		}
	}


}
