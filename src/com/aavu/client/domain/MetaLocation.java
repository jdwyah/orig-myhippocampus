package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Date;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.commands.SaveMetaDateCommand;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.datepicker.DateFormatter;
import com.aavu.client.widget.datepicker.DatePicker;
import com.aavu.client.widget.datepicker.HDatePicker;
import com.aavu.client.widget.datepicker.SimpleDatePicker;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaLocation extends Meta implements IsSerializable,Serializable {

	private static final String TYPE = "Location";
		
	//@Override
	public String getType() {
		return TYPE;
	}
	
	//@Override
	public Widget getEditorWidget(final Topic topic, Manager manager) {		
		return null;		
	}



}
