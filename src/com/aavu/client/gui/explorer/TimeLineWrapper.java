package com.aavu.client.gui.explorer;

import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.gui.timeline.HippoTimeLine;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;




public class TimeLineWrapper extends Composite {

	private HippoTimeLine timeline;

	public TimeLineWrapper(Manager manager, int width, int height, FullTopicIdentifier[] topics, CloseListener close) {
		
		VerticalPanel mainP = new VerticalPanel();
		HorizontalPanel typeSelector = new HorizontalPanel();
		
		TimeLineSelector lastUpdatedB = new TimeLineSelector(TimeLineObj.LAST_UPDATED,Manager.myConstants.timeline_lastUpdated());
		TimeLineSelector createdB = new TimeLineSelector(TimeLineObj.CREATED,Manager.myConstants.timeline_created());
		typeSelector.add(lastUpdatedB);
		typeSelector.add(createdB);
		
		
		TimeLineObj[] timelines = new TimeLineObj[topics.length];
		for (int i = 0; i < topics.length; i++) {
			TimeLineObj tobj = new TimeLineObj(topics[i],topics[i].getLastUpdated(),null);	
			timelines[i] = tobj;
		}		
		
		timeline = new HippoTimeLine(manager,timelines,width - 30,height,close);		
		
		
		mainP.add(typeSelector);
		mainP.add(timeline);		
		
		initWidget(mainP);
	}
	
	
	private class TimeLineSelector extends Button implements ClickListener {
		public TimeLineSelector(int style_or_meta, String text){
			super(text);
			addClickListener(this);
		}

		public void onClick(Widget sender) {
			TimeLineObj[] timelines = null;
			timeline.load(timelines);
		}
	}

}
