package com.aavu.client.help;

import org.gwm.client.GFrame;
import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.SaveStopLight;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HelpWindow extends PopupWindow {

	private static final int WIDTH = 550;
	private static final int HEIGHT = 500;
	private Manager manager;
	
	private VerticalPanel mainP;
	
	public HelpWindow(Manager manager, GInternalFrame frame) {
		super(frame,ConstHolder.myConstants.help_welcome(),WIDTH,HEIGHT);
		this.manager = manager;

		mainP = new VerticalPanel();
		
				
		mainP.add(new Label(ConstHolder.myConstants.help_help()));
				
		mainP.add(new HelpPicker());
		
		setContent(mainP);
		
	}
}