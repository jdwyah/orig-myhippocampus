package com.aavu.client.gui.explorer;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.gui.timeline.HippoTimeLine;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;




public class TimeLineWrapper extends Composite {

	private HippoTimeLine timeline;
	private FullTopicIdentifier[] topics;
	private Manager manager;

	public TimeLineWrapper(Manager manager, Topic myTag, int width, int height, FullTopicIdentifier[] topics, CloseListener close) {
		
		VerticalPanel mainP = new VerticalPanel();
		HorizontalPanel typeSelector = new HorizontalPanel();
		
		this.topics = topics;
		this.manager = manager;
		
		TimeLineSelector lastUpdatedB = new TimeLineSelector(TimeLineObj.LAST_UPDATED,ConstHolder.myConstants.timeline_lastUpdated());
		TimeLineSelector createdB = new TimeLineSelector(TimeLineObj.CREATED,ConstHolder.myConstants.timeline_created());
		typeSelector.add(lastUpdatedB);
		typeSelector.add(createdB);
		
		Set metaDatesForTag = myTag.getMetaDates();
		for (Iterator iter = metaDatesForTag.iterator(); iter.hasNext();) {
			MetaDate datemeta = (MetaDate) iter.next();
			TimeLineSelector metaButton = new TimeLineSelector(datemeta.getId(),datemeta.getTitle());
			typeSelector.add(metaButton);
		}
		
		
			
		
		timeline = new HippoTimeLine(manager,width - 30,height,close);		
		
		getTimeLinesOfStyle(TimeLineObj.LAST_UPDATED,new TimeLineLookup());
		
		mainP.add(typeSelector);
		mainP.add(timeline);		
		
		initWidget(mainP);
	}
	
	/**
	 * We're storing last updated & created already, but if they want date seen, 
	 * we'l need to go async.
	 * 
	 * @param style_or_meta
	 * @return
	 */
	private void getTimeLinesOfStyle(long style_or_meta,AsyncCallback callback) {
		
		//not a created || updated, do async lookup of the meta id
		if(style_or_meta != TimeLineObj.CREATED 
				&&
				style_or_meta != TimeLineObj.LAST_UPDATED){
			manager.getTopicCache().getTimelineObjs(style_or_meta,callback);
		}else{

			TimeLineObj[] timelines = new TimeLineObj[topics.length];
			for (int i = 0; i < topics.length; i++) {

				Date date = null;
				if(TimeLineObj.CREATED == style_or_meta){
					date = topics[i].getCreated();
				}
				else if(TimeLineObj.LAST_UPDATED == style_or_meta){
					date = topics[i].getLastUpdated();
				}

				TimeLineObj tobj = new TimeLineObj(topics[i],date,null);	
				timelines[i] = tobj;
			}
			
			callback.onSuccess(timelines);			
		}
	}
	
	
	/**
	 * use a callback so that we can do async calls IF NEEDED
	 * created & last updated won't need the async call.
	 * 
	 * @author Jeff Dwyer	 
	 */
	private class TimeLineSelector extends Button implements ClickListener {
		private long style_or_meta;
		public TimeLineSelector(long l, String text){
			super(text);
			this.style_or_meta = l;
			addClickListener(this);
		}

		public void onClick(Widget sender) {					
			getTimeLinesOfStyle(style_or_meta,new TimeLineLookup());
		}
	}
	
	private class TimeLineLookup extends StdAsyncCallback {
		public TimeLineLookup(){
			super("Timeline Lookup");			
		}				
		//@Override
		public void onSuccess(Object result) {
			super.onSuccess(result);
			timeline.load((TimeLineObj[]) result);
		}
		
	}

}
