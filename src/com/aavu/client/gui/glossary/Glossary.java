package com.aavu.client.gui.glossary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.ext.tabbars.TabHasWidgets;
import com.aavu.client.gui.ext.tabbars.TabPanelExt;
import com.aavu.client.gui.ext.tabbars.VertableTabPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Glossary extends FocusPanel {
	
	//PEND unused
	private static final String KEYS = "<span class=\"H-SideBarKey\">";
	private static final String KEYS_NOELEM = "<span class=\"H-SideBarKey H-SideBarKeyNoElements\">";
	private static final String KEYEND = "</span>";

	private static final String OTHER = "#'s";
	private static final int MAX_LINK_CHARS = 25;//11;
	
	/**
	 * case insensitve comparator for topic names
	 */
	private static Comparator caseInsensitive = new Comparator(){
		public int compare(Object o1, Object o2) {
			String o = (String)o1;
			String oo = (String) o2;
			return o.toLowerCase().compareTo(o.toLowerCase());
		}};

	private Manager manager;

	
	//protected VertableTabPanel tabPanel;
	private boolean dirty = true;
				TabPanel panel;
	protected TabHasWidgets tabPanel;
				
	public Glossary(Manager manager,Orientation orient){
		
		this.manager = manager;
		
		
		if(orient == Orientation.VERTICAL){
			VertableTabPanel tabP = new VertableTabPanel(orient);
			tabPanel = tabP;
			add(tabP);
		}else{
			TabPanel tabP = new TabPanelExt();
			tabPanel = (TabHasWidgets) tabP;
			add(tabP);
		}
		
				
		//sets
		//addStyleName("H-AbsolutePanel");		
			
		
	}

	/**
	 * Convert to list repr
	 *  
	 * @param t
	 */
	public void load(TopicIdentifier[] topics) {
		List conv = new ArrayList();
		for (int i = 0; i < topics.length; i++) {
			conv.add(topics[i]);
		}		
		load(conv);
	}	
	
	/**
	 * Called with no parameter default behavior is to do a lookup for all topic identifiers
	 *
	 */
	public void load(){		
		manager.getTopicCache().getAllTopicIdentifiers(new StdAsyncCallback(ConstHolder.myConstants.topic_getAllAsync()){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				List topics = (List) result;
				load(topics);
			}
		});
	}

	private void load(List topics) {
		alphabetizeTopics(topics);
		dirty = false;
	}
	
	protected void alphabetizeTopics(List topics) {
		//<String,Map<String,TopicIdentifier>>
		Map allEntries = new GWTSortedMap();		

		System.out.println("topics! "+topics.size());
				
				
		for(char lett = 'A'; lett <= 'Z'; lett++){		
			Map thisLetter = new GWTSortedMap(caseInsensitive);			
			allEntries.put(lett+"", thisLetter);
		}
		Map othersMap = new GWTSortedMap();
		allEntries.put(OTHER, othersMap);
		
		TopicIdentifier topicIdent = null;		
		for (Iterator ident = topics.iterator(); ident.hasNext();) {
									
			topicIdent = (TopicIdentifier) ident.next();	
			
			char firstLetter = topicIdent.getTopicTitle().charAt(0);
						
			Map map = (Map) allEntries.get(""+Character.toUpperCase(firstLetter));
			if(map == null){
				map = (Map) allEntries.get(OTHER);
			}
			map.put(topicIdent.getTopicTitle(),topicIdent);							
		}
			
		addAsLabels(allEntries);	
	}


	private void addAsLabels(Map sidebarEntries) {
		tabPanel.clear();

		for (Iterator iter = sidebarEntries.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			
			GWTSortedMap topics = (GWTSortedMap) sidebarEntries.get(key);
			
			/*
			 * NOTE this. We're claiming it's not dirty.
			 * Need to have Service/DAO return sorted & ignoreCase
			 */
			topics.setDirty(false);
			
			GlossaryPage glossaryPage = new GlossaryPage(manager);
						
			String st = KEYS_NOELEM;			
			
			for (Iterator iterator = topics.keySet().iterator(); iterator.hasNext();) {
				String title = (String) iterator.next();
				final TopicIdentifier topic = (TopicIdentifier) topics.get(title);
				
				glossaryPage.add(topic,MAX_LINK_CHARS);
				
				st = KEYS;
			}

			//TODO VertableTabPanel.hideDeck() seems to die if there's nothing in the selected deck
			//fix??
			
			
			if(st == KEYS_NOELEM){
				//PEND any effect??
				//placeholder so there's always something in there			
				glossaryPage.add(new Label("    (no topics)     "));
			}
			
			//System.out.println("ADD key "+key.toString()+" "+st+key.toString()+KEYEND);
			
			//tabPanel.add(vp,new SidebarLabel(st+key.toString()+KEYEND));
			tabPanel.add(glossaryPage,st+key.toString()+KEYEND,true);

		}
	}

	public boolean isDirty() {
		return dirty;
	}


	

}
