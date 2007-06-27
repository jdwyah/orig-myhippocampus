package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.CopyOfAbstractOccurrenceAbstractMindTreeOccurence;
import com.aavu.client.domain.mapper.MindTree;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CopyOfMindTreeOcc extends CopyOfAbstractOccurrenceAbstractMindTreeOccurence implements Serializable,IsSerializable, ReallyCloneable {

	public CopyOfMindTreeOcc(){}
	
	public CopyOfMindTreeOcc(Topic topic){
		setMindTree(new CopyOfMindTreeOcc(topic));
	}
	
	//@Override
	public Object clone() {
		CopyOfMindTreeOcc newOne = new CopyOfMindTreeOcc();
		copyPropsIntoParam(newOne);
		return newOne;
	}
		
}
