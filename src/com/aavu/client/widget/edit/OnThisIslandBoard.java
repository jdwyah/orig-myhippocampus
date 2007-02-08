package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveSeeAlsoCommand;
import com.aavu.client.domain.commands.SaveTagtoTopicCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnThisIslandBoard extends Composite implements CompleteListener {


	private TopicCache topicService;
	private TopicCompleter topicCompleter;
	private Topic myTag;
	private Manager manager;
	private CellPanel onThisIslandPanel;

	public OnThisIslandBoard(Manager manager) {		
		this.manager = manager;
		
		topicService = manager.getTopicCache();
		
		topicCompleter = new TopicCompleter(manager.getTopicCache());		
		topicCompleter.addListener(this);

		//alsos = new SeeAlsoWidget();
		
		
		EnterInfoButton enterInfoButton = new EnterInfoButton();		
		enterInfoButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				completed(topicCompleter.getText());
			}
		});

		
		VerticalPanel mainP = new VerticalPanel();
		
		mainP.add(new HeaderLabel(Manager.myConstants.island_topics_on()));
		
		HorizontalPanel cp = new HorizontalPanel();
		cp.add(new HeaderLabel(Manager.myConstants.island_addTo()));
		cp.add(topicCompleter);
		cp.add(enterInfoButton);
		
		onThisIslandPanel = new VerticalPanel();
		mainP.add(onThisIslandPanel);
		mainP.add(cp);
		//mainP.add(alsos);
		
		
		initWidget(mainP);
	}
	public void load(Tag topic) {
		myTag = topic;	
		
		manager.getTopicCache().getTopicsWithTag(myTag.getId(), new StdAsyncCallback(Manager.myConstants.tag_topicIsA()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				FullTopicIdentifier[] topics = (FullTopicIdentifier[]) result;

				System.out.println("Show Topics results "+topics.length);
				
				addTopicLabels(topics);				
			}					
		});
	}

	protected void addTopicLabels(FullTopicIdentifier[] topics) {
		onThisIslandPanel.clear();
		for (int i = 0; i < topics.length; i++) {
			FullTopicIdentifier fti = topics[i];
			onThisIslandPanel.add(new TopicLink(fti,null));
		}		
	}	
	
	/**
	 * lookup the string, create a new topic if necessary.
	 * 
	 * Then tag and save it.
	 */
	public void completed(String completeText) {
		
		topicService.getTopicIdentForNameOrCreateNew(completeText,new StdAsyncCallback(Manager.myConstants.save_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				TopicIdentifier to = (TopicIdentifier) result;
				
				Topic newTopic = new Topic();
				newTopic.setTitle(to.getTopicTitle());
				newTopic.setId(to.getTopicID());
				
				topicCompleter.setText("");

				onThisIslandPanel.add(new TopicLink(newTopic));				
				
				topicService.save(newTopic, new SaveTagtoTopicCommand(newTopic,(Tag) myTag),
						new StdAsyncCallback(Manager.myConstants.save_async()){});				
			}});
		
	}
	
	
}
