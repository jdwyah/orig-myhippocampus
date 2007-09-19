package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.GDocument;
import com.aavu.client.domain.GSpreadsheet;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.google.gwt.user.client.ui.Image;

public class BubbleFactory {

	public static TopicDisplayObj createBubbleFor(FullTopicIdentifier fti, HierarchyDisplay display) {
		if (fti.isPublicVisible()) {
			return new TopicBubble(fti, new Image(ImageHolder.getImgLoc("hierarchy/")
					+ "ball_white_shared.png"), display);
		} else {
			return new TopicBubble(fti, new Image(ImageHolder.getImgLoc("hierarchy/")
					+ "ball_white.png"), display);
		}
	}

	public static TopicDisplayObj createBubbleFor(TopicOccurrenceConnector owl,
			HierarchyDisplay display) {

		if (owl.getOccurrence() instanceof Entry) {
			return new EntryDisplay(owl, display);
		} else if (owl.getOccurrence() instanceof WebLink) {
			return new OccBubble(owl, ImageHolder.getImgLoc("hierarchy/") + "gadgetLinks.png", 59,
					29, display);
		} else if (owl.getOccurrence() instanceof GDocument) {
			return new GoogOccBubble(owl, ImageHolder.getImgLoc("hierarchy/") + "gDocument.png",
					40, 40, display);
		} else if (owl.getOccurrence() instanceof GSpreadsheet) {
			return new GoogOccBubble(owl, ImageHolder.getImgLoc("hierarchy/") + "gSpreadsheet.png",
					40, 40, display);
		} else {
			return new OccBubble(owl, display);
		}
	}

	/**
	 * WARN: This should only be used for new topics, otherwise use one of the location aware
	 * methods.
	 * 
	 * reminder, these methods will get called by Reference type, not the object's type. Could move
	 * these to Topic and override in Occurrence, but then we get the GWT stuff in the domain which
	 * can explode when not running in javascript. 6, 1/2 dozen.
	 * 
	 * http://www.velocityreviews.com/forums/t362644-polymorphic-method-call-from-third-class.html
	 * 
	 * @param tag
	 * @param hierarchyDisplay
	 * @return
	 */
	public static TopicDisplayObj createBubbleFor(Topic thought, Topic current, int[] lnglat,
			HierarchyDisplay hierarchyDisplay) {

		if (thought instanceof Occurrence) {
			return createBubbleFor(new TopicOccurrenceConnector(current, (Occurrence) thought,
					lnglat[1], lnglat[0]), hierarchyDisplay);
		} else {

			FullTopicIdentifier fti = new FullTopicIdentifier(thought);
			if (lnglat != null) {
				fti.setLatitudeOnIsland(lnglat[1]);
				fti.setLongitudeOnIsland(lnglat[0]);
			}

			return createBubbleFor(fti, hierarchyDisplay);
		}

	}
}
