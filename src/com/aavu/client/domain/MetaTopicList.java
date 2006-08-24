package com.aavu.client.domain;

import java.util.Map;

import com.aavu.client.widget.tags.MetaTopicListWidget;
import com.google.gwt.user.client.ui.Widget;

public class MetaTopicList extends Meta{

	private static final String TYPE = "Topic List";
	
	
	
	public MetaTopicList(){
		
	}
	
	//@Override
	public Widget getEditorWidget(Map metaMap) {
		MetaTopicListWidget widget = new MetaTopicListWidget();
		widget.setName(getName());
		return widget;		
	}

	//@Override
	public String getType() {
		return TYPE;
	}

	//@Override
	public Widget getWidget(Map mmp) {
		MetaTopicListWidget widget = new MetaTopicListWidget();
		widget.setName(getName());
		return widget;
	}
	

}