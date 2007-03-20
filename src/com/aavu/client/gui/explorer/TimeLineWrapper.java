package com.aavu.client.gui.explorer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
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




public class TimeLineWrapper extends FTICachingExplorerPanel {

	private HippoTimeLine timeline;
	
	private Manager manager;

	private HorizontalPanel typeSelector;

	private List lastLoadedftis;


	public TimeLineWrapper(Manager manager, Map defaultMap, int width, int height, CloseListener close) {
		super(manager, defaultMap);
		
		VerticalPanel mainP = new VerticalPanel();
		typeSelector = new HorizontalPanel();
		
		this.manager = manager;
		
		TimeLineSelector lastUpdatedB = new TimeLineSelector(TimeLineObj.LAST_UPDATED,ConstHolder.myConstants.timeline_lastUpdated());
		TimeLineSelector createdB = new TimeLineSelector(TimeLineObj.CREATED,ConstHolder.myConstants.timeline_created());
		typeSelector.add(lastUpdatedB);
		typeSelector.add(createdB);

		
		
		timeline = new HippoTimeLine(manager,width - 30,height,close);		
		
		
		mainP.add(typeSelector);
		mainP.add(timeline);		
		
		initWidget(mainP);
	}
	

	/**
	 * We're storing last updated & created already, but if they want date seen, 
	 * we'll need to go async.
	 * 
	 * @param style_or_meta
	 * @param allIdentifiers 
	 * @return
	 */
	private void getTimeLinesOfStyle(long style_or_meta,AsyncCallback callback, List allIdentifiers) {
		
		//not a created || updated, do async lookup of the meta id
		if(style_or_meta != TimeLineObj.CREATED 
				&&
				style_or_meta != TimeLineObj.LAST_UPDATED){
			manager.getTopicCache().getTimelineObjs(style_or_meta,callback);
		}else{

			List timelineList = new ArrayList();
			
			for (Iterator iter = allIdentifiers.iterator(); iter.hasNext();) {
			
				FullTopicIdentifier fti = (FullTopicIdentifier) iter.next();

				Date date = null;
				if(TimeLineObj.CREATED == style_or_meta){
					date = fti.getCreated();
				}
				else if(TimeLineObj.LAST_UPDATED == style_or_meta){
					date = fti.getLastUpdated();
				}

				TimeLineObj tobj = new TimeLineObj(fti,date,null);	
				//timelines[i] = tobj;
				timelineList.add(tobj);
				
			}
			
			callback.onSuccess(timelineList);			
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
			getTimeLinesOfStyle(style_or_meta,new TimeLineLookup(),lastLoadedftis);
		}
	}
	
	private class TimeLineLookup extends StdAsyncCallback {
		public TimeLineLookup(){
			super("Timeline Lookup");			
		}				
		//@Override
		public void onSuccess(Object result) {
			super.onSuccess(result);
			timeline.load((List) result);
		}
		
	}

	public Widget getWidget() {
		return this;
	}

//
//	public void load(Set tags) {
//		
////		
////		for (Iterator iter = tags.iterator(); iter.hasNext();) {
////			TopicIdentifier tagIdent = (TopicIdentifier) iter.next();
////			Set metaDatesForTag = tag.getTagPropertyDates();
////			for (Iterator iter2 = metaDatesForTag.iterator(); iter2.hasNext();) {
////				MetaDate datemeta = (MetaDate) iter2.next();
////				TimeLineSelector metaButton = new TimeLineSelector(datemeta.getId(),datemeta.getTitle());
////				typeSelector.add(metaButton);
////			}	
////		}
//		
//		
//		
//	}

	//@Override
//	protected void draw(Set tags) {
//		List all = new ArrayList();
//		
//		for (Iterator iter = tags.iterator(); iter.hasNext();) {			
//			TopicIdentifier tag = (TopicIdentifier) iter.next();
//			all.addAll(getFTI(tag));
//		}
//		draw(all);
//	} 


	//@Override
	protected void draw(List ftis) {
		this.lastLoadedftis = ftis;
		getTimeLinesOfStyle(TimeLineObj.LAST_UPDATED,new TimeLineLookup(),ftis);		
	}

}
