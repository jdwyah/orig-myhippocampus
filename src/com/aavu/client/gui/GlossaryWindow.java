package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;

public class GlossaryWindow extends PopupWindow {


	public GlossaryWindow(Glossary g, GInternalFrame frame) {
		super(frame,Manager.myConstants.glossary());
		
		setContent(g);
		
	}

}
