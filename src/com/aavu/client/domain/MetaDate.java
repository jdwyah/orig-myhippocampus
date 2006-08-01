package com.aavu.client.domain;

import org.gwtwidgets.client.ui.cal.CalendarPanel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MetaDate extends Meta {

	private static final String TYPE = "Date";

	//@Override
	public String getType() {
		return TYPE;
	}

	//@Override
	public Widget getWidget(boolean editable) {

		HorizontalPanel widget = new HorizontalPanel();
		Label label = new Label(name);

		//Maybe make this meta auto complete later on
		CalendarPanel calendar = new CalendarPanel();
				
		widget.add(label);
		widget.add(calendar);	
		return widget;
	}

	//@Override
	public Widget getEditorWidget(boolean editable) {
		// TODO Auto-generated method stub
		return null;
	}


}
