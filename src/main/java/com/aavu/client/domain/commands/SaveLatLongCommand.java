package com.aavu.client.domain.commands;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveLatLongCommand extends AbstractCommand implements IsSerializable {

	private int latitude;
	private int longitude;
	
	public SaveLatLongCommand(){};
		
	public SaveLatLongCommand(Topic topic, int latitude, int longitude) {
		super(topic);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	//@Override
	public void executeCommand() {		
		getTopic(0).setLatitude(latitude);
		getTopic(0).setLongitude(longitude);
	}

	//@Override
	public String toString() {
		return "SaveLatLong ID "+getTopicID(0)+" "+latitude+" "+longitude;
	}
	
	
	
}
