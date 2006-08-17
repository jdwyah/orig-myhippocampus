package com.aavu.client.domain;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MetaText extends MetaValue {

	private static final String TYPE = "Text";
	private String value;
	private String transientValue; //as widget changes, only this updates
	//private TextBox valueTextBox;  <-- not serializable

	public MetaText(){
		this("");
	}
	
	public MetaText(String value){
		this.value = value;
	}
	
	public Widget getWidget() {
		//HorizontalPanel widget = new HorizontalPanel();
		//Label label = new Label(name);

		//Make this auto complete
		TextBox valueTextBox = new TextBox();
		valueTextBox.setText(value);
		//keep track of value as user changes it
		valueTextBox.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender){
				transientValue = ((TextBox)sender).getText();
			}
		});

		//widget.add(label);
		//widget.add(valueTextBox);	
		return valueTextBox;
	}
	
	/**
	 * Saves text from textbox widget as current value
	 */
	public void save(){
		this.value = this.transientValue;
	}

	/*public Widget getEditorWidget(boolean editable) {
		HorizontalPanel widget = new HorizontalPanel();

		if (editable){
			TextBox metaName = new TextBox();
			metaName.setText(this.name);
			metaName.addChangeListener(new ChangeListener(){
				public void onChange(Widget sender){
					name = ((TextBox)sender).getText();
				}
			});

			MetaListBox metaType = new MetaListBox();
			
			TagLocalService tls = new TagLocalService();
			
			metaType.addItems(tls.getAllMetaTypes());

			widget.add(metaName);
			widget.add(metaType);
		}
		else{
			Label metaName = new Label(this.name);
			metaName.setWidth("5em");
			Label metaType = new Label(TYPE);  //this.type.getName()
			metaType.setWidth("5em");

			widget.add(metaName);
			widget.add(metaType);
		}

		//widget.add(editButton);

		return widget;
	}*/

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
