package com.aavu.client.gui.glossary;

import java.util.ArrayList;
import java.util.Comparator;
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
	private static Comparator<String> caseInsensitive = new Comparator<String>() {
		public int compare(String o1, String o2) {
			String o = o1;
			return o.toLowerCase().compareTo(o2.toLowerCase());
		}
	};

	protected Manager manager;


	private boolean dirty = true;


	// private Map keyToGlossaryPage = new HashMap();
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

		innerLoad(new ArrayList<TopicIdentifier>());

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
				new StdAsyncCallback<List<TopicIdentifier>>("Glossary Fetch") {
					// @Override
					public void onSuccess(List<TopicIdentifier> result) {
						super.onSuccess(result);
						loadIdents(result);
					}
				});
	}

	/**
	 * List<TopicIdentifier>
	 * 
	 * @param topicIdents
	 */
	private void loadIdents(List<TopicIdentifier> topicIdents) {

		adHocMode = false;

		innerLoad(topicIdents);
	}

	private void innerLoad(List<TopicIdentifier> arrayList) {

		alphabetizeTopics(arrayList);
		dirty = false;
	}


	protected void alphabetizeTopics(List<TopicIdentifier> topics) {
		// 
		Map<String, GWTSortedMap<String, TopicIdentifier>> allEntries = new GWTSortedMap<String, GWTSortedMap<String, TopicIdentifier>>();

		System.out.println("topics! " + topics.size());


		for (char lett = 'A'; lett <= 'Z'; lett++) {
			GWTSortedMap<String, TopicIdentifier> thisLetter = new GWTSortedMap<String, TopicIdentifier>(
					caseInsensitive);
			allEntries.put(lett + "", thisLetter);
		}
		GWTSortedMap<String, TopicIdentifier> othersMap = new GWTSortedMap<String, TopicIdentifier>();
		allEntries.put(OTHER, othersMap);


		for (TopicIdentifier topicIdent : topics) {

			char firstLetter = topicIdent.getTopicTitle().charAt(0);

			Map<String, TopicIdentifier> map = allEntries.get(""
					+ Character.toUpperCase(firstLetter));
			if (map == null) {
				map = allEntries.get(OTHER);
			}
			map.put(topicIdent.getTopicTitle(), topicIdent);
		}

		addAsLabels(allEntries);
	}


	private void addAsLabels(Map<String, GWTSortedMap<String, TopicIdentifier>> sidebarEntries) {
		mainPanel.clear();
		// keyToGlossaryPage.clear();

		for (String key : sidebarEntries.keySet()) {

			GWTSortedMap<String, TopicIdentifier> topics = sidebarEntries.get(key);

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

			for (String title : topics.keySet()) {
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
