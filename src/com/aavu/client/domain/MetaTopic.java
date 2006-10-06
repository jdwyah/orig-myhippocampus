package com.aavu.client.domain;

import java.util.Map;

import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.widget.edit.MetaTopicWidget;
import com.aavu.client.widget.edit.TopicCompleter;
import com.aavu.client.widget.tags.MetaListBox;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MetaTopic extends Meta {

	private static final String TYPE = "Text";
	private String value;
	private String transientValue; //as widget changes, only this updates
	
	
	public MetaTopic(){
		this("");
	}

	public MetaTopic(String value){
		this.value = value;
	}

	public Widget getWidget(Topic topic) {
		HorizontalPanel widget = new HorizontalPanel();

		Label metaName = new Label(getName());

		metaName.setWidth("5em");

		
		Label metaValue = new Label(null);  
		Topic mv = (Topic) topic.getMetaValues().get(this);
		if(mv != null){
			metaValue.setText(mv.getTitle());
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

	public Widget getEditorWidget(final Topic topic) {
		
		
		MetaTopicWidget mtw = new MetaTopicWidget(this,topic);
	
		return mtw;
	}

	public boolean needsSaveCallback() {
		return true;
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
			if ( !(other instanceof MetaTopic) ) return false;
			MetaTopic castOther = ( MetaTopic ) other; 

			return ( (this.value==castOther.value) || ( this.value!=null && castOther.value!=null && this.value.equals(castOther.value) ) );
		}
	}


}
