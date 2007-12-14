package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractOccurrenceAbstractMindTreeOccurence;
import com.aavu.client.domain.mapper.MindTree;

public class MindTreeOcc extends AbstractOccurrenceAbstractMindTreeOccurence implements
		Serializable, ReallyCloneable {

	public MindTreeOcc() {
	}

	public MindTreeOcc(Topic topic) {
		setMindTree(new MindTree(topic));
	}

	// @Override
	public Object clone() {
		return copyProps(new MindTreeOcc());
	}

}
