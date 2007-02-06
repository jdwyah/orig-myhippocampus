package com.aavu.client.domain.commands;

import com.aavu.client.domain.Meta;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveTagPropertiesCommand extends AbstractSaveCommand implements IsSerializable {

	private Meta[] metas;
	
	public SaveTagPropertiesCommand(){};
	
	public SaveTagPropertiesCommand(long id, Meta[] metas){
		setTopicID(id);
		this.metas = metas;		
	}

	//@Override
	public void executeCommand() {
				
		topic.getMetas().clear();
		for (int i = 0; i < metas.length; i++) {
			topic.addMeta(metas[i]);			
		}
				
	}

	//@Override
	public String toString() {
		return "SaveTagProperties ID "+getTopicID()+" "+getData();
	}
	
	
	
}
