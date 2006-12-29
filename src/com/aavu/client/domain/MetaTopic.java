package com.aavu.client.domain;

import com.aavu.client.widget.TopicLink;
import com.aavu.client.widget.edit.MetaTopicEditWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaTopic extends Meta {

	private static final String TYPE = "Topic";
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

		widget.add(metaName);
		Topic mv = (Topic) topic.getSingleMetaValueFor(this);
		if(mv != null){
			widget.add(new TopicLink(mv));
		}

		return widget;
	}

	/**
	 * Saves text from textbox widget as current value
	 */
	public void save(){
		this.value = this.transientValue;
	}

	public Widget getEditorWidget(final Topic topic) {
		
		
		MetaTopicEditWidget mtw = new MetaTopicEditWidget(this,topic);
	
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
