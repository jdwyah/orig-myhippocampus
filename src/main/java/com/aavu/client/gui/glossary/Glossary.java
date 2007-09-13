package com.aavu.client.gui.glossary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Glossary extends FocusPanel {

	// PEND unused
	// private static final String KEYS = "<span class=\"H-SideBarKey\">";
	// private static final String KEYS_NOELEM = "<span class=\"H-SideBarKey
	// H-SideBarKeyNoElements\">";
	// private static final String KEYEND = "</span>";

	private static final String OTHER = "#'s";
	private static final int MAX_LINK_CHARS = 25;// 11;

	/**
	 * case insensitve comparator for topic names
	 */
	private static Comparator caseInsensitive = new Comparator() {
		public int compare(Object o1, Object o2) {
			String o = (String) o1;
			String oo = (String) o2;
			return o.toLowerCase().compareTo(o.toLowerCase());
		}
	};

	protected Manager manager;


	private boolean dirty = true;


	private Map keyToGlossaryPage = new HashMap();
	private boolean adHocMode;

	private VerticalPanel mainPanel;


	public Glossary(Manager manager, int height) {

		this.manager = manager;

		mainPanel = new VerticalPanel();

		ScrollPanel scrollP = new ScrollPanel(mainPanel);
		scrollP.setHeight(height + "px");
		scrollP.setWidth("200px");

		add(scrollP);


		// sets
		addStyleName("H-Glossary");


	}



	/**
	 * Called with no parameter default behavior is to do a lookup for all topic identifiers
	 * 
	 */
	public void doAdHoc() {

		adHocMode = true;

		innerLoad(new ArrayList());

		// manager.getTopicCache().getAllTopicIdentifiers(new
		// StdAsyncCallback(ConstHolder.myConstants.topic_getAllAsync()){
		//
		// public void onSuccess(Object result) {
		// super.onSuccess(result);
		// List topics = (List) result;
		// load(topics);
		// }
		// });
	}

	public void load(Topic toLoad) {
		manager.getTopicCache().getTopicsWithTag(toLoad.getId(),
				new StdAsyncCallback("Glossary Fetch") {
					// @Override
					public void onSuccess(Object result) {
						super.onSuccess(result);
						loadIdents((List) result);
					}
				});
	}

	/**
	 * List<TopicIdentifier>
	 * 
	 * @param topicIdents
	 */
	private void loadIdents(List topicIdents) {

		adHocMode = false;

		innerLoad(topicIdents);
	}

	private void innerLoad(List arrayList) {

		alphabetizeTopics(arrayList);
		dirty = false;
	}


	protected void alphabetizeTopics(List topics) {
		// <String,Map<String,TopicIdentifier>>
		Map allEntries = new GWTSortedMap();

		System.out.println("topics! " + topics.size());


		for (char lett = 'A'; lett <= 'Z'; lett++) {
			Map thisLetter = new GWTSortedMap(caseInsensitive);
			allEntries.put(lett + "", thisLetter);
		}
		Map othersMap = new GWTSortedMap();
		allEntries.put(OTHER, othersMap);

		TopicIdentifier topicIdent = null;
		for (Iterator ident = topics.iterator(); ident.hasNext();) {

			topicIdent = (TopicIdentifier) ident.next();

			char firstLetter = topicIdent.getTopicTitle().charAt(0);

			Map map = (Map) allEntries.get("" + Character.toUpperCase(firstLetter));
			if (map == null) {
				map = (Map) allEntries.get(OTHER);
			}
			map.put(topicIdent.getTopicTitle(), topicIdent);
		}

		addAsLabels(allEntries);
	}


	private void addAsLabels(Map sidebarEntries) {
		mainPanel.clear();
		keyToGlossaryPage.clear();

		for (Iterator iter = sidebarEntries.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();

			GWTSortedMap topics = (GWTSortedMap) sidebarEntries.get(key);

			/*
			 * NOTE this. We're claiming it's not dirty. Need to have Service/DAO return sorted &
			 * ignoreCase
			 */
			topics.setDirty(false);



			VerticalPanel letterPage = new VerticalPanel();

			boolean hadElems = false;


			Label letter = new Label(key);
			letter.addStyleName("H-GlossaryLetter");
			letterPage.add(letter);

			for (Iterator iterator = topics.keySet().iterator(); iterator.hasNext();) {
				String title = (String) iterator.next();
				final TopicIdentifier topic = (TopicIdentifier) topics.get(title);

				letterPage.add(new TopicLink(topic));

				hadElems = true;
			}


			// TODO VertableTabPanel.hideDeck() seems to die if there's nothing in the selected deck
			// fix??


			if (hadElems) {
				mainPanel.add(letterPage);
			}


			// keyToGlossaryPage.put(label, letterPage);


		}
	}

	public boolean isDirty() {
		return dirty;
	}



}
