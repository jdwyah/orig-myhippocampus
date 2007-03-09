package com.aavu.client.LinkPlugin;

import org.gwm.client.GInternalFrame;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.gadgets.LinkDisplayWidget;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;

public class AddLinkPopup extends PopupWindow {

	private static final int HEIGHT = 200;
	private static final int WIDTH = 550;
	private WebLink link;
	private Manager manager;

	public AddLinkPopup(final LinkDisplayWidget widget, Manager _manager, GInternalFrame frame, WebLink _link, final Topic myTopic) {
		super(frame, ConstHolder.myConstants.link_add_title(),WIDTH,HEIGHT);
		this.manager = _manager;
		this.link = _link;
		
		AddLinkContent addLinkContent = new AddLinkContent(widget,manager.getTopicCache(),link,myTopic,this,true);
		
		setContent(addLinkContent);
		
	}


	
}
