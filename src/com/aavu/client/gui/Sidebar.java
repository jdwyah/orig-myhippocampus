package com.aavu.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.VertableTabPanel;
import com.aavu.client.service.cache.HippoCache;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Sidebar extends SimplePanel {

	private MainMap map;
	private HippoCache hippoCache;

	private VertableTabPanel tabPanel;

	// 'A' -> a's
	// 'B-C' -> b's & c's
	//<String,List<Topic>>
	Map sidebarEntries = new GWTSortedMap();	


	public Sidebar(MainMap _map, HippoCache hippoCache){
		this.map = _map;
		this.hippoCache = hippoCache;
		tabPanel = new VertableTabPanel(VertableTabPanel.VERTICAL);

		hippoCache.getTopicCache().getAllTopics(0, 0, new StdAsyncCallback("Sidebar"){

			public void onSuccess(Object result) {
				Topic[] topics = (Topic[]) result;
				alphabetizeTopics(topics);
			}
		});


		add(tabPanel);
		//sets
		setStyleName("GuiTest-Sidebar");
	}


	protected void alphabetizeTopics(Topic[] topics) {
		//<String,Topic>
		Map allEntries = new GWTSortedMap();			

		//sort topics by title
		//
		for (int i = 0; i < topics.length; i++) {
			Topic topic = topics[i];
			allEntries.put(topic.getTitle(), topic);
		}

		char lastLetter = 'A';
		char firstLetterThisList = 'A';
		char lastLetterOfAlph = 'Z';

		char curLetter = 'A';
		int numForCurrentDiv = 0;

		int maxSizeDiffLetter = 4;

		List curList = new ArrayList();

		for (Iterator iter = allEntries.keySet().iterator(); iter.hasNext();) {

			String key = (String) iter.next();

			Topic cur = (Topic) allEntries.get(key);

			curLetter = cur.getTitle().charAt(0);

			if(curLetter != lastLetter){

				//same list
				//
				if(curList.size() < maxSizeDiffLetter){
					curList.add(cur);
				}
				//new list
				//
				else{
					if(curLetter != lastLetter)
						sidebarEntries.put(firstLetterThisList+"-"+lastLetter, curList);
					else{
						sidebarEntries.put(firstLetterThisList+"", curList);
					}

					curList = new ArrayList();
					curList.add(cur);
					firstLetterThisList = curLetter;
				}

			}
			//Same letter, just add, no matter how many of this letter we've got
			//
			else{
				curList.add(cur);						
			}					
			lastLetter = curLetter;
		}


		//take care of last list
		//
		if(curLetter != lastLetterOfAlph)
			sidebarEntries.put(firstLetterThisList+"-"+lastLetterOfAlph, curList);
		else{
			sidebarEntries.put(firstLetterThisList+"", curList);
		}
	
		addAsLabels(sidebarEntries);		
	}


	private void addAsLabels(Map sidebarEntries) {
		for (Iterator iter = sidebarEntries.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();

			List topics = (List) sidebarEntries.get(key);

			VerticalPanel vp = new VerticalPanel();

			for (Iterator iterator = topics.iterator(); iterator.hasNext();) {
				final Topic topic = (Topic) iterator.next();

				Label l = new Label(topic.getTitle());
				l.addClickListener(new ClickListener(){

					public void onClick(Widget sender) {
						System.out.println("CLICKED");

						map.bringUpChart(topic);
					}});
				vp.add(l);
			}

			tabPanel.add(vp,key);

		}
	}

}
