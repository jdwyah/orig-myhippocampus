package com.aavu.client.domain.commands;

import java.util.Date;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveDateCreatedCommand extends AbstractCommand implements IsSerializable {

	private Date date;
	
	public SaveDateCreatedCommand(){};
	
	public SaveDateCreatedCommand(Topic topic, Date date){
		super(topic);
		this.date = date;
	}

	//@Override
	public void executeCommand() {
		getTopic(0).setCreated(date);
	}

	//@Override
	public String toString() {
		return "SaveDateCreated ID "+getTopicID(0)+" "+date;
	}

	

}
