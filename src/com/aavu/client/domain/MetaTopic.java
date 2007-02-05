package com.aavu.client.domain;

import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.MetaTopicEditWidget;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.google.gwt.user.client.ui.Widget;

public class MetaTopic extends Meta {

	private static final String TYPE = "Topic";//BAD Manager.myConstants.topic_meta();
	private String value;
	private String transientValue; //as widget changes, only this updates
	
	
	public MetaTopic(){
		this("");
	}

	public MetaTopic(String value){
		this.value = value;
	}

	/**
	 * Saves text from textbox widget as current value
	 */
	public void save(){
		this.value = this.transientValue;
	}

	public Widget getEditorWidget(final Topic topic, SaveNeededListener saveNeeded,Manager manager) {
		
		
		MetaTopicEditWidget mtw = new MetaTopicEditWidget(this,topic,saveNeeded,manager.getTopicCache());
	
		return mtw;
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
