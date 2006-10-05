package com.aavu.client.widget.edit;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TagCache;
import com.aavu.client.widget.tags.SaveListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagBoard extends Composite implements CompleteListener {

	private VerticalPanel tagPanel = new VerticalPanel();
	private Button addTagButton = new Button("Add");
	//private TextBox tagBox = new TextBox();
	private TagAutoCompleteBox tagBox = null;
	
	
//	private List tags = new ArrayList();
//	private Map metaMap = new HashMap();

	private Topic cur_topic;
	private List listeners = new ArrayList();
	private TagCache tagCache;
	private Manager manager;
	

	public TagBoard(Manager manager) {
		this.manager = manager;
		this.tagCache = manager.getTagCache();

		tagBox = new TagAutoCompleteBox(this,tagCache);

		addTagButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				tagTopic(tagBox.getText());
			}
		});

		VerticalPanel mainPanel = new VerticalPanel();
				
		HorizontalPanel tagBoxP = new HorizontalPanel();
		tagBoxP.add(new Label("Add tag: "));
		tagBoxP.add(tagBox);
		mainPanel.add(tagBoxP);
		mainPanel.add(tagPanel);
				
		initWidget(mainPanel);				
	}

	public void tagTopic(final String tagName){
		//need to check if it's a built in tag or not bla bla
		//for now just creates new (bland) Tag and adds it to list
		//topic needs to get tagged when its saved

		//First, do a name lookup on this tag
		//
		tagCache.getTagAddIfNew(tagName, new AsyncCallback(){

			public void onFailure(Throwable caught) {
				System.out.println("fail tagservice.getTagAddIfNew "+caught);	
			}

			public void onSuccess(Object result) {
				Tag tag = (Tag) result;				
				addTag(tag);
				manager.growIsland(tag);
			}});


	}

	public void completed(String completeText) {
		tagTopic(completeText);
	}	
	
	/**
	 * load the topic into the GUI 
	 * 
	 * @param topic
	 */
	public void load(Topic topic){
		
		tagPanel.clear();
		
		cur_topic = topic;
		
		listeners.clear();
		
//		tags.clear();
//		
//		metaMap.clear();
//		
		
		for (Iterator iter = topic.getTags().iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			showTag(tag);
		}		
	}
	
	private void showTag(final Tag tag){
		String name = tag.getName();
		Label tagLabel = new Label(name);
		
		//Hyperlink link = new Hyperlink(name, name);
		tagPanel.add(tagLabel);
		tagLabel.setStyleName("ta-tagboard-TagLabel");		
		
		displayMetas(tag);
	}
	
	
	private void addTag(final Tag tag) {
		
		cur_topic.tagTopic(tag);		
		showTag(tag);
		
	}
	
	private void displayMetas(Tag tag) {
		Set metas = tag.getMetas();

		for (Iterator iter = metas.iterator(); iter.hasNext();) {
			Meta element = (Meta) iter.next();
			GWT.log("displayMetas", null);
			if(element.needsSaveCallback()){
				GWT.log("needs callback", null);
				SaveListener w = (SaveListener) element.getEditorWidget(cur_topic);
				tagPanel.add(w);
				listeners.add(w);
			}else{
				Widget w = element.getEditorWidget(cur_topic);
				tagPanel.add(w);
			}
		}

	}

	public void saveThingsNowEvent(StdAsyncCallback callback) {
		GWT.log("savethingsnowevent",null);

		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			SaveListener listener = (SaveListener) iterator.next();		
			
			listener.saveNowEvent();
			
		}
		callback.onSuccess(null);
							
	}


}
