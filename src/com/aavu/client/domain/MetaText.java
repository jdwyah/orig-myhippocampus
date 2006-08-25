package com.aavu.client.domain;

import java.util.Map;

import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.widget.tags.MetaListBox;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MetaText extends Meta {

	private static final String TYPE = "Text";
	private String value;
	private String transientValue; //as widget changes, only this updates
	//private TextBox valueTextBox;  <-- not serializable
	private Map metaMap;

	public MetaText(){
		this("");
	}

	public MetaText(String value){
		this.value = value;
	}

	public Widget getWidget(Map mmp) {
		HorizontalPanel widget = new HorizontalPanel();

		Label metaName = new Label(getName());

		metaName.setWidth("5em");

		
		Label metaValue = new Label(null);  
		Object mv = mmp.get(toMapIdx());
		if(mv != null){
			metaValue.setText(mv.toString());
		}
		
		metaValue.setWidth("5em");

		widget.add(metaName);
		widget.add(metaValue);

		return widget;
	}

	/**
	 * Saves text from textbox widget as current value
	 */
	public void save(){
		this.value = this.transientValue;
	}

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

	protected Object getValue() {
		return value;
	}

	//@Override
	public boolean equals(Object other) {
		{
			if ( (this == other ) ) return true;
			if ( (other == null ) ) return false;
			if ( !(other instanceof MetaText) ) return false;
			MetaText castOther = ( MetaText ) other; 

			return ( (this.value==castOther.value) || ( this.value!=null && castOther.value!=null && this.value.equals(castOther.value) ) );
		}
	}


}
