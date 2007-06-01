package com.aavu.client.gui.explorer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.timeline.HippoTimeline;
import com.aavu.client.gui.timeline.TimeLineConst;
import com.aavu.client.gui.timeline.draggable.NewHippoTimeLine;
import com.aavu.client.gui.timeline.simple.SimpleTimeLine;
import com.aavu.client.gui.timeline.zoomer.ZoomableTimeline;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ButtonGroup;
import com.aavu.client.widget.SelectableButton;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;



/**
 * Wraps a timeline in a GUI that let's us pick from
 * 1) Last Updated
 * 2) Created
 * 3) Metas
 * 
 * Uses the functionality of FTICachingExplorerPanel to reduce trips to server if 
 * we're just looking at Updated or Created, since those should already be loaded.
 * 
 * @author Jeff Dwyer
 *
 */
public class TimeLineWrapper extends FTICachingExplorerPanel implements ButtonGroup {

	private HippoTimeline timeline;
	
	
	private HorizontalPanel typeSelector;

	private List lastLoadedftis;

	private TimeLineConst currentStyle = TimeLineConst.CREATED;

	private SelectableButton currentButton;
	
	public TimeLineWrapper(Manager manager, Map defaultMap, int width, int height, PopupWindow window) {
		super(manager, defaultMap);
		
		VerticalPanel mainP = new VerticalPanel();
		typeSelector = new HorizontalPanel();
		
		
		TimeLineSelector lastUpdatedB = new TimeLineSelector(TimeLineConst.UPDATED,ConstHolder.myConstants.timeline_lastUpdated(),this);
		TimeLineSelector createdB = new TimeLineSelector(TimeLineConst.CREATED,ConstHolder.myConstants.timeline_created(),this);
		TimeLineSelector metasB = new TimeLineSelector(TimeLineConst.META,ConstHolder.myConstants.timeline_metas(),this);
		typeSelector.add(lastUpdatedB);
		typeSelector.add(createdB);
		typeSelector.add(metasB);

		createdB.setSelected(true);
		
		//timeline = new NewHippoTimeLine(manager,width - 110,height-70,window);		
		//timeline = new SimpleTimeLine(manager,width - 110,height-70,window);
		timeline = new ZoomableTimeline(manager,width - 110,height-70,window);
		
		mainP.add(typeSelector);
		mainP.add(timeline.getWidget());		
		
		window.addInternalFrameListener(new GFrameAdapter(){
			//@Override
			public void frameResized(GFrameEvent evt) {				
				super.frameResized(evt);
				resize(evt);
			}});
		
		initWidget(mainP);
	}
	
	private void resize(GFrameEvent evt) {
		timeline.resize(evt);
	}
	
	/**
	 * We're storing last updated & created already, but we need to parse that out of the 
	 * allIdentifiers list.
	 * 
	 * @param style
	 * @param allIdentifiers 
	 * @return
	 */
	private void getTimeLinesOfStyle(TimeLineConst style, List allIdentifiers) {
		
		AsyncCallback callback;
		if(TimeLineConst.META == style 
				&&
				!allMode){
			callback = new TimeLineLookupWMerge();
		}else{
			callback = new TimeLineLookup();
		}
		
		//not a created || updated, go async
		if(TimeLineConst.META == style){
			
			if(allMode){				
				manager.getTopicCache().getAllTimelineObjs(callback);				
			}else{
				//no caching for now
				List shoppingList = new ArrayList();
				
				for (Iterator iter = tags.iterator(); iter.hasNext();) {
					TopicIdentifier tag = (TopicIdentifier) iter.next();				
					shoppingList.add(tag);								
				}				
				manager.getTopicCache().getTimelineObjs(shoppingList, callback);				
			}
			
		}else{

			List timelineList = new ArrayList();
			
			for (Iterator iter = allIdentifiers.iterator(); iter.hasNext();) {
			
				DatedTopicIdentifier fti = (DatedTopicIdentifier) iter.next();

				Date date = null;
				if(TimeLineConst.CREATED == style){
					date = fti.getCreated();
				}
				else if(TimeLineConst.UPDATED == style){
					date = fti.getLastUpdated();
				}

				TimeLineObj tobj = new TimeLineObj(fti,date,null);	
				//timelines[i] = tobj;
				timelineList.add(tobj);
				
			}
			
			callback.onSuccess(timelineList);			
		}
	}
	
	public void loadAll() {
		allMode = true; 
		
		manager.getTopicCache().getAllTopicIdentifiers(0, 200, new StdAsyncCallback(ConstHolder.myConstants.topic_getAllAsync()){
			//@Override
			public void onSuccess(Object result) {
				super.onSuccess(result);
				draw((List) result);
			}});		
	}
	
	/**
	 * use a callback so that we can do async calls IF NEEDED
	 * created & last updated won't need the async call.
	 * 
	 * @author Jeff Dwyer	 
	 */
	private class TimeLineSelector extends SelectableButton implements ClickListener {
		private TimeLineConst style;
		
		public TimeLineSelector(TimeLineConst l, String text,ButtonGroup buttonGroup){
			super(text,buttonGroup);
			this.style = l;
			addClickListener(this);
		}

		public void onClick(Widget sender) {
			super.onClick();
			
			currentStyle = style;
			getTimeLinesOfStyle(style,lastLoadedftis);
		}
	}
	
	/**
	 * Wrap the call to timeline.load() so that we have the option of doing an async fetch to 
	 * the server.
	 * 
	 * @author Jeff Dwyer
	 */
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
	/**
	 * Merge the shopping list style return into one big list.
	 * 
	 * Note, could cache here.
	 * 
	 * @author Jeff Dwyer	 
	 */
	private class TimeLineLookupWMerge extends StdAsyncCallback {
		public TimeLineLookupWMerge(){
			super("Timeline Lookup WMerge");			
		}				
		//@Override
		public void onSuccess(Object result) {
			super.onSuccess(result);
			List listOfListOfTimelines = (List) result;
			List all = new ArrayList();
			for (Iterator iter = listOfListOfTimelines.iterator(); iter.hasNext();) {
				List ftis = (List) iter.next();
				all.addAll(ftis);
			}			
			timeline.load(all);
		}		
	}

	public Widget getWidget() {
		return this;
	}

	

	//@Override
	protected void draw(List ftis) {
		this.lastLoadedftis = ftis;
		getTimeLinesOfStyle(currentStyle,ftis);		
	}

	public void newSelection(SelectableButton button) {		
		if(currentButton != null){			
			currentButton.setSelected(false);			
		}
		currentButton = button;
		currentButton.setSelected(true);
	}

}
