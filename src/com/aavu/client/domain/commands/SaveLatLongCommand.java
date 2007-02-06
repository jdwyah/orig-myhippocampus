package com.aavu.client.domain.commands;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveLatLongCommand extends AbstractSaveCommand implements IsSerializable {

	private int latitude;
	private int longitude;
	
	public SaveLatLongCommand(){};
		
	public SaveLatLongCommand(long id, int latitude, int longitude) {
		setTopicID(id);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	//@Override
	public void executeCommand() {		
		topic.setLatitude(latitude);
		topic.setLongitude(longitude);
	}

	//@Override
	public String toString() {
		return "SaveLatLong ID "+getTopicID()+" "+latitude+" "+longitude;
	}
	
	
	
}
