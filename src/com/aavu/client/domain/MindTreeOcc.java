package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractOccurrenceAbstractMindTreeOccurence;
import com.aavu.client.domain.mapper.MindTree;
import com.google.gwt.user.client.rpc.IsSerializable;

public class MindTreeOcc extends AbstractOccurrenceAbstractMindTreeOccurence implements Serializable,IsSerializable {

	public MindTreeOcc(){}
	
	public MindTreeOcc(Topic topic){
		setMindTree(new MindTree(topic));
	}
	
		
}
