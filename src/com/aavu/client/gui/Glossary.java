package com.aavu.client.gui;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.Orientation;
import com.aavu.client.gui.ext.VertableTabPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Glossary extends FocusPanel {
	
	
	private static final String KEYS = "<span class=\"H-SideBarKey\">";
	private static final String KEYS_NOELEM = "<span class=\"H-SideBarKey H-SideBarKeyNoElements\">";
	private static final String KEYEND = "</span>";

	private static final String OTHER = "#'s";
	
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

	
	protected VertableTabPanel tabPanel;
	private boolean dirty = true;
				
	public Glossary(Manager manager,Orientation orient){
		
		this.manager = manager;
		tabPanel = new VertableTabPanel(orient);
		
		add(tabPanel);
		
		//sets
		addStyleName("H-AbsolutePanel");		
		
	
		
	}

	/**
	 * TODO make this only load the referenced topic
	 * @param t
	 */
	public void load(Topic t) {
		load();
	}
	public void load(){		
		manager.getTopicCache().getAllTopicIdentifiers(new StdAsyncCallback(Manager.myConstants.topic_getAllAsync()){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				List topics = (List) result;
				alphabetizeTopics(topics);
				dirty = false;
			}
		});
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
			
			VerticalPanel vp = new VerticalPanel();
			vp.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
						
			String st = KEYS_NOELEM;			
			
			for (Iterator iterator = topics.keySet().iterator(); iterator.hasNext();) {
				String title = (String) iterator.next();
				final TopicIdentifier topic = (TopicIdentifier) topics.get(title);
				
				vp.add(new TopicLink(topic,11));
				
				st = KEYS;
			}

			//TODO VertableTabPanel.hideDeck() seems to die if there's nothing in the selected deck
			//fix??
			
			
			if(st == KEYS_NOELEM){
				//PEND any effect??
				//placeholder so there's always something in there			
				vp.add(new Label("    (no topics)     "));
			}
			
			//System.out.println("ADD key "+key.toString()+" "+st+key.toString()+KEYEND);
			
			//tabPanel.add(vp,new SidebarLabel(st+key.toString()+KEYEND));
			tabPanel.add(vp,st+key.toString()+KEYEND,true);

		}
	}

	public boolean isDirty() {
		return dirty;
	}


	

}
