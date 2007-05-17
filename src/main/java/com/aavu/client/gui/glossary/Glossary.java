package com.aavu.client.gui.glossary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class Glossary extends FocusPanel {
	
	//PEND unused
//	private static final String KEYS = "<span class=\"H-SideBarKey\">";
//	private static final String KEYS_NOELEM = "<span class=\"H-SideBarKey H-SideBarKeyNoElements\">";
//	private static final String KEYEND = "</span>";

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

	protected Manager manager;

	
	//protected VertableTabPanel tabPanel;
	private boolean dirty = true;

	protected TabPanel tabPanel;
				
	private Map keyToGlossaryPage = new HashMap();
	private boolean adHocMode;
	
	public Glossary(Manager manager,Orientation orient){
		
		this.manager = manager;		
		
		if(orient == Orientation.VERTICAL){
			throw new UnsupportedOperationException();
//			VertableTabPanel tabP = new VertableTabPanel(orient);
//			tabPanel = tabP;
//			add(tabP);
			
		}else{

			tabPanel = new TabPanel();
			add(tabPanel);
		}
		
				
		//sets
		//addStyleName("H-AbsolutePanel");		
			
		
	}


	
	/**
	 * Called with no parameter default behavior is to do a lookup for all topic identifiers
	 *
	 */
	public void doAdHoc(){	
		
		adHocMode = true;
		
		innerLoad(new ArrayList());
		
//		manager.getTopicCache().getAllTopicIdentifiers(new StdAsyncCallback(ConstHolder.myConstants.topic_getAllAsync()){
//
//			public void onSuccess(Object result) {
//				super.onSuccess(result);
//				List topics = (List) result;
//				load(topics);
//			}
//		});
	}


	/**
	 * List<TopicIdentifier>
	 * @param topicIdents
	 */
	public void load(List topicIdents) {
		
		adHocMode = false;
	
		innerLoad(topicIdents);
	}

	private void innerLoad(List arrayList) {
				
		alphabetizeTopics(arrayList);
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
		keyToGlossaryPage.clear();
		
		for (Iterator iter = sidebarEntries.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			
			GWTSortedMap topics = (GWTSortedMap) sidebarEntries.get(key);
			
			/*
			 * NOTE this. We're claiming it's not dirty.
			 * Need to have Service/DAO return sorted & ignoreCase
			 */
			topics.setDirty(false);
			
			GlossaryPage glossaryPage = new GlossaryPage(manager);
						
			boolean hadElems = false;
					
			
			for (Iterator iterator = topics.keySet().iterator(); iterator.hasNext();) {
				String title = (String) iterator.next();
				final TopicIdentifier topic = (TopicIdentifier) topics.get(title);
				
				glossaryPage.add(topic,MAX_LINK_CHARS);
				
				hadElems = true;
			}

			//TODO VertableTabPanel.hideDeck() seems to die if there's nothing in the selected deck
			//fix??
			
			
			if(!hadElems){
				//PEND any effect??
				//placeholder so there's always something in there			
				glossaryPage.add(new Label("    (no topics)     "));
			}
			
			//System.out.println("ADD key "+key.toString()+" "+st+key.toString()+KEYEND);
			
			//tabPanel.add(vp,new SidebarLabel(st+key.toString()+KEYEND));
			Label label = new Label(key);
			label.addStyleName("H-SideBarKey");
			if(!hadElems){
				label.addStyleName("H-SideBarKeyNoElements");
			}
			tabPanel.add(glossaryPage,label);		
			
			keyToGlossaryPage.put(label,glossaryPage);
			
			
			/**
			 * PEND messy
			 * add a click listener to refresh on clicks...
			 * only go do the async if it's empty...
			 */
			if(adHocMode){
				label.addClickListener(new ClickListener(){
					public void onClick(Widget sender) {
						final Label label = (Label) sender;
						final GlossaryPage page = (GlossaryPage) keyToGlossaryPage.get(label);

						//NOTE!! ==1 bc of "  no topics   " 
						if(page.size() == 1){
							manager.getTopicCache().getAllTopicIdentifiers(0, 999, label.getText().substring(0,1),
									new StdAsyncCallback(""){
								//@Override
								public void onSuccess(Object result) {
									super.onSuccess(result);
									List ftis = (List) result;

									page.clear();

									for (Iterator iterator = ftis.iterator(); iterator.hasNext();) {								
										TopicIdentifier topic = (TopicIdentifier) iterator.next();								
										page.add(topic,MAX_LINK_CHARS);								
									}
									if(ftis.size() > 0){
										label.removeStyleName("H-SideBarKeyNoElements");
									}
								}});
						}
					}});
			}
		}
	}

	public boolean isDirty() {
		return dirty;
	}



	

}
