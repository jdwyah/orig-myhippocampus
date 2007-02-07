package com.aavu.client.domain.commands;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveTitleCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveTitleCommand(){};
	
	public SaveTitleCommand(Topic topic, String title){
		super(topic);
		setData(title);
	}

	//@Override
	public void executeCommand() {
		topic.setTitle(getData());
	}

	//@Override
	public String toString() {
		return "SaveTitle ID "+getTopicID()+" "+getData();
	}

	//@Override
	public boolean updatesTitle() {	
		return true;
	};
	
	
	
	
	
}
