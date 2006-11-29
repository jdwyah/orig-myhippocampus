package com.aavu.client.domain.mapper;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.mapper.generated.AbstractMapElement;

public class MindTreeElement extends AbstractMapElement {

	public MindTreeElement(String title, Topic topic, int lft, int rgt) {
		super(title,topic,lft,rgt);
	}
}
