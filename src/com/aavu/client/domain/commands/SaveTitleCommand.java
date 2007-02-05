package com.aavu.client.domain.commands;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveTitleCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveTitleCommand(){};
	
	public SaveTitleCommand(long id, String title){
		setTopicID(id);
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
	
	
	
}
