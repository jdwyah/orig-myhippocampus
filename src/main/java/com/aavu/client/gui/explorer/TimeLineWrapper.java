package com.aavu.client.gui.explorer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
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
import com.google.gwt.user.client.ui.Composite;
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
public class TimeLineWrapper extends Composite implements ExplorerPanel {

	private static final int W_GUTTER = 110;


	private static final int H_GUTTER = 70;


	private HippoTimeline timeline;
	

	private Manager manager;
	
	public TimeLineWrapper(Manager manager,int width, int height, PopupWindow window) {
		this.manager = manager;
		VerticalPanel mainP = new VerticalPanel();
		

		
		//timeline = new NewHippoTimeLine(manager,width - W_GUTTER,height-H_GUTTER,window);		
		//timeline = new SimpleTimeLine(manager,width - W_GUTTER,height-H_GUTTER,window);
		timeline = new ZoomableTimeline(manager,width - W_GUTTER,height-H_GUTTER,window);
		
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
		timeline.resize(evt.getGFrame().getWidth() - W_GUTTER,evt.getGFrame().getHeight()-H_GUTTER);
	}
	

	public void load(List tags){
		
		timeline.clear();
				
		Topic firstTopic = (Topic) tags.get(0);
		
		loadOccs(firstTopic);
		
		loadChildren(firstTopic);
		
		loadMetas(firstTopic);
		
		
		
		//getTimeLinesOfStyle(currentStyle);
	}
	
	private void loadMetas(Topic firstTopic) {
		List shoppingList = new ArrayList();
		shoppingList.add(firstTopic.getIdentifier());		
		System.out.println("Getting Shopping list "+firstTopic.getIdentifier());		
		AsyncCallback callback = new TimeLineLookupWMerge();
		manager.getTopicCache().getTimelineObjs(shoppingList, callback);
	}

	private void loadChildren(Topic firstTopic) {
		System.out.println("get topics w/ tag");
		manager.getTopicCache().getTopicsWithTag(firstTopic.getId(), new TimeLineLookup());		
	}

	private void loadOccs(Topic firstTopic) {
		List l = new ArrayList();
		for (Iterator iterator = firstTopic.getOccurenceObjs().iterator(); iterator.hasNext();) {
			Occurrence occ = (Occurrence) iterator.next();
			l.add(new TimeLineObj(new TopicIdentifier(-1,occ.getTitle()),
					occ.getCreated(),
					null));
		}
		timeline.add(l);
	}

	public void loadAll() {
		
		timeline.clear();
		
		manager.getTopicCache().getAllTopicIdentifiers(0, 200, new TimeLineLookup());
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
			List tis = (List) result;
			List all = new ArrayList();
			for (Iterator iter = tis.iterator(); iter.hasNext();) {
				DatedTopicIdentifier dti = (DatedTopicIdentifier) iter.next();
				all.add(new TimeLineObj(dti,dti.getCreated(),null));
			}			
			timeline.add(all);
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
			timeline.add(all);
		}		
	}

	public Widget getWidget() {
		return this;
	}


}
