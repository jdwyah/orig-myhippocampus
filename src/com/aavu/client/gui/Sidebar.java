package com.aavu.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.VertableTabPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.HippoCache;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Sidebar extends SimplePanel {
	
	private Manager manager;

	private VertableTabPanel tabPanel;

	// 'A' -> a's
	// 'B-C' -> b's & c's
	//<String,List<Topic>>
	Map sidebarEntries = new GWTSortedMap();	


	public Sidebar(Manager manager){
		
		this.manager = manager;
		tabPanel = new VertableTabPanel(VertableTabPanel.VERTICAL);

		manager.getTopicCache().getAllTopicIdentifiers(new StdAsyncCallback("Sidebar"){

			public void onSuccess(Object result) {
				List topics = (List) result;
				alphabetizeTopics(topics);
			}
		});


		add(tabPanel);
		//sets
		addStyleName("H-AbsolutePanel");
		addStyleName("H-Sidebar");				
	}


	protected void alphabetizeTopics(List topics) {
		//<String,Topic>
		Map allEntries = new GWTSortedMap();			

		System.out.println("topics! "+topics.size());
		
		//sort topics by title
		//
		for (Iterator ident = topics.iterator(); ident.hasNext();) {
			TopicIdentifier topicIdent = (TopicIdentifier) ident.next();
			allEntries.put(topicIdent.getTopicTitle(), topicIdent);
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

			TopicIdentifier cur = (TopicIdentifier) allEntries.get(key);

			curLetter = cur.getTopicTitle().toUpperCase().charAt(0);

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
				final TopicIdentifier topic = (TopicIdentifier) iterator.next();

				Label l = new Label(topic.getTopicTitle());
				l.addClickListener(new ClickListener(){

					public void onClick(Widget sender) {
						System.out.println("CLICKED");

						manager.bringUpChart(topic);
					}});
				vp.add(l);
			}

			tabPanel.add(vp,key);

		}
	}

}
