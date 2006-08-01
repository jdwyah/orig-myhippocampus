package com.aavu.client.domain;

import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.widget.tags.MetaListBox;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MetaText extends Meta {

	private static final String TYPE = "Text";


	public Widget getWidget(boolean editable) {
		HorizontalPanel widget = new HorizontalPanel();
		Label label = new Label(name);

		//Maybe make this meta auto complete later on
		TextBox metaValue = new TextBox();
		metaValue.setText(name);

		widget.add(label);
		widget.add(metaValue);	
		return widget;
	}

	public Widget getEditorWidget(boolean editable) {
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
			
			metaType.addChangeListener(new ChangeListener(){
				public void onChange(Widget sender){
					MetaListBox typeBox = (MetaListBox)sender;
					
					
					//type = typeBox.getSelectedMeta();
					
					
					
					//System.out.println("changing " + name + " type to " + type);
				}
			});

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
	}

	//@Override
	public String getType() {		
		return TYPE;
	}


}
