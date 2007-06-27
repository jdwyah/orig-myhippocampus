package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.dto.FullTopicIdentifier;

public class BubbleFactory {

	public static Bubble createBubbleFor(FullTopicIdentifier fti,HierarchyDisplay display) {		
		return new TopicBubble(fti,display);
	}

	public static Bubble createBubbleFor(TopicOccurrenceConnector owl,HierarchyDisplay display) {
		return new OccBubble(owl,display);
	}

	/**
	 * This should only be used for new topics, otherwise use one of the location aware methods.  
	 * 
	 * @param tag
	 * @param hierarchyDisplay
	 * @return
	 */
	public static Bubble createBubbleFor(Topic thought,Topic current,HierarchyDisplay hierarchyDisplay) {
		return new TopicBubble(new FullTopicIdentifier(thought),hierarchyDisplay);
	}
	public static Bubble createBubbleFor(Occurrence occ,Topic current,HierarchyDisplay hierarchyDisplay) {
		return new OccBubble(new TopicOccurrenceConnector(current,occ),hierarchyDisplay);
	}
	

}
