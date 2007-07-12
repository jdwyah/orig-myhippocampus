package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.dto.FullTopicIdentifier;

public class BubbleFactory {

	public static TopicDisplayObj createBubbleFor(FullTopicIdentifier fti,HierarchyDisplay display) {		
		return new TopicBubble(fti,display);
	}

	public static TopicDisplayObj createBubbleFor(TopicOccurrenceConnector owl,HierarchyDisplay display) {
		return new OccBubble(owl,display);
	}

	/**
	 * This should only be used for new topics, otherwise use one of the location aware methods.  
	 * 
	 * reminder, these methods will get called by Reference type, not the object's type. Could move these
	 * to Topic and override in Occurrence, but then we get the GWT stuff in the domain which can explode
	 * when not running in javascript. 6, 1/2 dozen.
	 *  
	 * http://www.velocityreviews.com/forums/t362644-polymorphic-method-call-from-third-class.html
	 * 
	 * @param tag
	 * @param hierarchyDisplay
	 * @return
	 */
	public static TopicDisplayObj createBubbleFor(Topic thought,Topic current,HierarchyDisplay hierarchyDisplay) {

		if (thought instanceof Occurrence) {
			
			Occurrence occ = (Occurrence) thought;
			return new OccBubble(new TopicOccurrenceConnector(current,occ),hierarchyDisplay);	
			
		}else{

			return new TopicBubble(new FullTopicIdentifier(thought),hierarchyDisplay);
		}
	}

}
