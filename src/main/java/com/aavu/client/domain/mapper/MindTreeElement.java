package com.aavu.client.domain.mapper;

import java.io.Serializable;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.mapper.generated.AbstractMindTreeElement;

public class MindTreeElement extends AbstractMindTreeElement implements Serializable {

	public MindTreeElement() {
	}

	public MindTreeElement(String title, Topic topic, int lft, int rgt) {
		super(title, topic, lft, rgt);
	}
}
