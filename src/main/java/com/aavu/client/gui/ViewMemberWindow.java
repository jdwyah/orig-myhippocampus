package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.explorer.Explorer;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;

public class ViewMemberWindow extends PopupWindow {

	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;
	private Explorer explorer;

	
	public ViewMemberWindow(String windowTitle, Manager manager, GInternalFrame frame) {
		super(frame,windowTitle,WIDTH,HEIGHT);		
		
		
		explorer = new Explorer(manager,WIDTH,HEIGHT,this);
				
		setContent(explorer);
			
	}


	public void load() {
		explorer.load();
	}

}
