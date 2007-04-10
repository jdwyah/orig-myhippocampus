package com.aavu.client.gui.glossary;

import org.gwm.client.GInternalFrame;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;

public class GlossaryWindow extends PopupWindow {

	private static final int WIDTH = 680;
	private static final int HEIGHT = 400;
	
	public GlossaryWindow(Glossary g, GInternalFrame frame) {
		super(frame,ConstHolder.myConstants.glossary(),WIDTH,HEIGHT);
		
		setContent(g);
		
	}

}
