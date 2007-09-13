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
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.timeline.HippoTimeline;
import com.aavu.client.gui.timeline.zoomer.ZoomableTimeline;
import com.aavu.client.service.Manager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
		List l = new ArrayList();
		l.add(topic);
		load(l);
	}

	public void load(List topics) {


		timeline.clear();

		Topic curTopic = (Topic) topics.get(0);

		loadOccs(curTopic);

		loadChildren(curTopic);

		loadMetas(curTopic);

		loadMyMetas(curTopic);

		// getTimeLinesOfStyle(currentStyle);
	}

	private void loadMetas(Topic firstTopic) {
		List shoppingList = new ArrayList();
		shoppingList.add(firstTopic.getIdentifier());
		System.out.println("TimeLineWrapper.Getting Shopping list " + firstTopic.getIdentifier());
		AsyncCallback callback = new TimeLineLookupWMerge();
		manager.getTopicCache().getTimelineObjs(shoppingList, callback);
	}

	private void loadMyMetas(Topic firstTopic) {
		System.out.println("TimeLineWrapper.getMyMetas");
		List l = new ArrayList();
		for (Iterator iterator = firstTopic.getAssociations().iterator(); iterator.hasNext();) {
			Association assoc = (Association) iterator.next();
			for (Iterator iterator2 = assoc.getMembers().iterator(); iterator2.hasNext();) {
				Topic t = (Topic) iterator2.next();
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
		List l = new ArrayList();
		for (Iterator iterator = firstTopic.getInstances().iterator(); iterator.hasNext();) {
			TopicTypeConnector toc = (TopicTypeConnector) iterator.next();

			RealTopic childTopic = (RealTopic) toc.getTopic();
			l.add(new TimeLineObj(childTopic.getIdentifier(), childTopic));
		}
		timeline.add(l);

		// manager.getTopicCache().getTopicsWithTag(firstTopic.getId(), new TimeLineLookup());
	}

	private void loadOccs(Topic firstTopic) {
		List l = new ArrayList();
		for (Iterator iterator = firstTopic.getOccurenceObjs().iterator(); iterator.hasNext();) {
			Occurrence occ = (Occurrence) iterator.next();
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
	private class TimeLineLookupWMerge extends StdAsyncCallback {
		public TimeLineLookupWMerge() {
			super("Timeline Lookup WMerge");
		}

		// @Override
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

	public void addSingle(TimeLineObj tlo) {
		List l = new ArrayList();
		l.add(tlo);
		timeline.add(l);
	}


}
