package com.aavu.client.gui.timeline.zoomer;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.GDocument;
import com.aavu.client.domain.GSpreadsheet;
import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ocean.dhtmlIslands.TimelineRemembersPosition;
import com.aavu.client.gui.timeline.draggable.TLORangeWidget;
import com.aavu.client.gui.timeline.draggable.TLOWrapper;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Image;

public class TimeLineObjFactory {

	/**
	 * mea culpa. putting all the instanceof stuff in one place. Is there a better way to do this
	 * when I can't put anything to do with ConstHolder.images into the real domain objects without
	 * fear of them exploding in a non GWT context?
	 * 
	 * 
	 * @param zoomableTimeline
	 * @param manager
	 * @param tlo
	 * @return
	 */
	public static TimelineRemembersPosition getWidget(ZoomableTimeline zoomableTimeline,
			Manager manager, TimeLineObj tlo) {

		if (tlo.getHasDate() instanceof HippoDate) {
			TLORangeWidget tlow = new TLORangeWidget(zoomableTimeline, tlo);
			tlow.addMouseWheelListener(zoomableTimeline);
			return tlow;
		} else {

			Image image = null;
			if (tlo.getHasDate() instanceof WebLink) {
				image = ConstHolder.images.gadgetLinksTimeline().createImage();
			} else if (tlo.getHasDate() instanceof Entry) {
				image = ConstHolder.images.entryZoomBackTimeline().createImage();
			} else if (tlo.getHasDate() instanceof GDocument) {
				image = ConstHolder.images.gDocumentTimeline().createImage();
			} else if (tlo.getHasDate() instanceof GSpreadsheet) {
				image = ConstHolder.images.gSpreadsheetTimeline().createImage();
			} else {
				image = ConstHolder.images.bullet_blue().createImage();
			}

			TLOWrapper tlow = new TLOWrapper(manager, zoomableTimeline, tlo, image);
			tlow.addMouseWheelListener(zoomableTimeline);
			return tlow;
		}
	}
}
