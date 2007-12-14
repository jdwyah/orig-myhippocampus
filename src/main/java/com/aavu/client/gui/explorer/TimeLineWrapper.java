package com.aavu.client.gui.explorer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Association;
import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.timeline.HippoTimeline;
import com.aavu.client.gui.timeline.zoomer.ZoomableTimeline;
import com.aavu.client.service.Manager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;



/**
 * Wraps a timeline in a GUI that let's us pick from 1) Last Updated 2) Created 3) Metas
 * 
 * Uses the functionality of FTICachingExplorerPanel to reduce trips to server if we're just looking
 * at Updated or Created, since those should already be loaded.
 * 
 * @author Jeff Dwyer
 * 
 */
public class TimeLineWrapper extends Composite implements ExplorerPanel {

	private static final int W_GUTTER = 0;


	private static final int H_GUTTER = 0;


	private HippoTimeline timeline;


	private Manager manager;

	public TimeLineWrapper(Manager manager, int width, int height, PopupWindow window) {
		this.manager = manager;


		// timeline = new NewHippoTimeLine(manager,width - W_GUTTER,height-H_GUTTER,window);
		// timeline = new SimpleTimeLine(manager,width - W_GUTTER,height-H_GUTTER,window);
		timeline = new ZoomableTimeline(manager, width - W_GUTTER, height - H_GUTTER, window);


		window.addInternalFrameListener(new GFrameAdapter() {
			// @Override
			public void frameResized(GFrameEvent evt) {
				super.frameResized(evt);
				resize(evt);
			}
		});

		initWidget(timeline.getWidget());
	}

	private void resize(GFrameEvent evt) {
		timeline.resize(evt.getGFrame().getWidth() - W_GUTTER, evt.getGFrame().getHeight()
				- H_GUTTER);
	}

	public void load(Topic topic) {
		List<Topic> l = new ArrayList<Topic>();
		l.add(topic);
		load(l);
	}

	public void load(List<Topic> topics) {


		timeline.clear();

		Topic curTopic = topics.get(0);

		loadOccs(curTopic);

		loadChildren(curTopic);

		loadMetas(curTopic);

		loadMyMetas(curTopic);

		// getTimeLinesOfStyle(currentStyle);
	}

	private void loadMetas(Topic firstTopic) {
		List<TopicIdentifier> shoppingList = new ArrayList<TopicIdentifier>();
		shoppingList.add(firstTopic.getIdentifier());
		System.out.println("TimeLineWrapper.Getting Shopping list " + firstTopic.getIdentifier());
		TimeLineLookupWMerge callback = new TimeLineLookupWMerge();
		manager.getTopicCache().getTimelineObjs(shoppingList, callback);
	}

	private void loadMyMetas(Topic firstTopic) {
		System.out.println("TimeLineWrapper.getMyMetas");
		List<TimeLineObj> l = new ArrayList<TimeLineObj>();
		for (Association assoc : firstTopic.getAssociations()) {
			for (Topic t : assoc.getMembers()) {
				if (t instanceof HippoDate) {
					HippoDate hippoDate = (HippoDate) t;
					l.add(new TimeLineObj(firstTopic.getIdentifier(), hippoDate));
				}
			}
		}
		timeline.add(l);

		// manager.getTopicCache().getTopicsWithTag(firstTopic.getId(), new TimeLineLookup());
	}

	private void loadChildren(Topic firstTopic) {
		System.out.println("TimeLineWrapper.getChildren");
		List<TimeLineObj> l = new ArrayList<TimeLineObj>();
		for (TopicTypeConnector toc : firstTopic.getInstances()) {
			RealTopic childTopic = (RealTopic) toc.getTopic();
			l.add(new TimeLineObj(childTopic.getIdentifier(), childTopic));
		}
		timeline.add(l);

		// manager.getTopicCache().getTopicsWithTag(firstTopic.getId(), new TimeLineLookup());
	}

	private void loadOccs(Topic firstTopic) {
		List<TimeLineObj> l = new ArrayList<TimeLineObj>();
		for (Occurrence occ : firstTopic.getOccurenceObjs()) {
			System.out.println("TimeLineWrapper new TimeLineObj " + GWT.getTypeName(occ) + " "
					+ occ);
			l.add(new TimeLineObj(occ.getIdentifier(), occ));
		}
		timeline.add(l);
	}


	// /**
	// * Wrap the call to timeline.load() so that we have the option of doing an async fetch to the
	// * server.
	// *
	// * @author Jeff Dwyer
	// */
	// private class TimeLineLookup extends StdAsyncCallback {
	// public TimeLineLookup() {
	// super("Timeline Lookup");
	// }
	//
	// // @Override
	// public void onSuccess(Object result) {
	// super.onSuccess(result);
	// List tis = (List) result;
	// List all = new ArrayList();
	// for (Iterator iter = tis.iterator(); iter.hasNext();) {
	// DatedTopicIdentifier dti = (DatedTopicIdentifier) iter.next();
	// all.add(new TimeLineObj(dti, dti.getCreated(), null));
	// }
	// timeline.add(all);
	// }
	//
	// }

	/**
	 * Merge the shopping list style return into one big list.
	 * 
	 * Note, could cache here.
	 * 
	 * @author Jeff Dwyer
	 */
	private class TimeLineLookupWMerge extends StdAsyncCallback<List<List<TimeLineObj>>> {
		public TimeLineLookupWMerge() {
			super("Timeline Lookup WMerge");
		}

		// @Override
		public void onSuccess(List<List<TimeLineObj>> listOfListOfTimelines) {
			super.onSuccess(listOfListOfTimelines);

			List<TimeLineObj> all = new ArrayList<TimeLineObj>();

			for (Iterator<List<TimeLineObj>> iter = listOfListOfTimelines.iterator(); iter
					.hasNext();) {
				List<TimeLineObj> ftis = iter.next();
				all.addAll(ftis);
			}
			timeline.add(all);
		}
	}

	public Widget getWidget() {
		return this;
	}

	public void addSingle(TimeLineObj tlo) {
		List<TimeLineObj> l = new ArrayList<TimeLineObj>();
		l.add(tlo);
		timeline.add(l);
	}


}
