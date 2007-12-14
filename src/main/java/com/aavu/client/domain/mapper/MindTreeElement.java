package com.aavu.client.domain.mapper;

import java.io.Serializable;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.mapper.generated.AbstractMindTreeElement;

public class MindTreeElement extends AbstractMindTreeElement implements Serializable,
		Comparable<MindTreeElement> {

	public MindTreeElement() {
	}

	public MindTreeElement(String title, Topic topic, int lft, int rgt) {
		super(title, topic, lft, rgt);
	}

	public int compareTo(MindTreeElement o) {
		return getLft() - o.getLft();
	}
}
