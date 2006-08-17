package com.aavu.client.domain;

import com.aavu.client.widget.tags.MetaTopicListWidget;
import com.google.gwt.user.client.ui.Widget;

public class MetaTopicList extends MetaValue {

	private static final String TYPE = "Topic List";
	
	
	
	public MetaTopicList(){
		
	}
	
	//@Override
	public Widget getEditorWidget(boolean editable) {
		
		System.out.println("UNUSED, right?");
		return null;
	}

	//@Override
	public String getType() {
		return TYPE;
	}

	//@Override
	public Widget getWidget(boolean editable) {
		MetaTopicListWidget widget = new MetaTopicListWidget();
		widget.setName(super.getMeta().getName());
		return widget;
	}

	//@Override
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	protected Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public Widget getWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}
	

}
