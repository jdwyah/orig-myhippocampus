package com.aavu.client.gui;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CreateNewWindow extends PopupWindow {
	
	private static final int HEIGHT = 60;
	private static final int WIDTH = 290;
	
	private Manager manager;
	private Label messageLabel;
	private TextBox name;
	private AsyncCallback after;
	
	/**
	 * Prevents multiple instances with a semaphore.
	 * @param island 
	 * 
	 * @param manager
	 */
	public CreateNewWindow(Manager _manager, String title, AsyncCallback after) {
		
		super(_manager.newFrame(),
				title,
				WIDTH,HEIGHT);
		this.manager = _manager;
		this.after = after;
		
		setCenteredContent(new NewWidget());		
	}
	
	private void clicked(){
		if(!name.getText().equals("")){
	
			after.onSuccess(name.getText());
			
			close();
		}else{
			messageLabel.setText("Enter a name");
		}
	}
	/**
	 * private class so that we can override the onload() method
	 * @author Jeff Dwyer
	 *
	 */
	private class NewWidget extends Composite {
		public NewWidget(){
			
			HorizontalPanel panel = new HorizontalPanel();			
			
			name = new TextBox();
			name.addKeyboardListener(new KeyboardListenerAdapter(){
				public void onKeyPress(Widget sender, char keyCode, int modifiers) {
					if(keyCode == KEY_ENTER){
						clicked();			
					}
				}});
			
			panel.add(name);
			panel.add(new Button(ConstHolder.myConstants.island_create(), new ClickListener() {
				public void onClick(Widget sender) {
					clicked();
				}
			}));
			
			messageLabel = new Label("");		
			panel.add(messageLabel);
			
			initWidget(panel);
		}

		//@Override
		protected void onLoad() {		
			super.onLoad();
			name.setCursorPos(0);
		}
	}

}
