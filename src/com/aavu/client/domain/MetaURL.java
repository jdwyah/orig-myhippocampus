package com.aavu.client.domain;

import java.util.Map;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MetaURL extends Meta {

	private static final String TYPE = "URL";
	private Map metaMap;

	//@Override
	public Widget getEditorWidget(Map mmp) {

		HorizontalPanel widget = new HorizontalPanel();

		this.metaMap = mmp;
		
		
		TextBox textBox = new TextBox();
		
		String mv = (String)mmp.get(toMapIdx());
		if(mv != null){
			textBox.setText(mv);		    	
		}
		textBox.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender) {				
				TextBox t = (TextBox) sender;
				
				metaMap.put(toMapIdx(), t.getText());
			}});
		
		widget.add(new Label(getName()));
		widget.add(textBox);

		return widget;
	}

	//@Override
	public String getType() {
		return TYPE;
	}

	//@Override
	public Widget getWidget(Map mmp) {
		HorizontalPanel widget = new HorizontalPanel();

		Label metaName = new Label(getName());

		metaName.setWidth("5em");

		
		Hyperlink metaValue = new Hyperlink();
		Object mv = mmp.get(toMapIdx());
		if(mv != null){
			metaValue.setText(mv.toString());
		}
		
		metaValue.setWidth("5em");

		widget.add(metaName);
		widget.add(metaValue);

		return widget;
	}

}
