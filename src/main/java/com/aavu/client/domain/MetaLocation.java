package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Widget;

public class MetaLocation extends Meta implements Serializable {

	private static final String TYPE = "Location";



	// @Override
	public String getType() {
		return TYPE;
	}

	// @Override
	public Widget getEditorWidget(final Topic topic, Manager manager) {
		return null;
	}



}
