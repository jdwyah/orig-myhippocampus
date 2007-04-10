package com.aavu.client.domain.mapper;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.mapper.generated.AbstractMindTreeElement;
import com.google.gwt.user.client.rpc.IsSerializable;

public class MindTreeElement extends AbstractMindTreeElement implements IsSerializable  {

	public MindTreeElement(){}
	public MindTreeElement(String title, Topic topic, int lft, int rgt) {
		super(title,topic,lft,rgt);
	}
}
