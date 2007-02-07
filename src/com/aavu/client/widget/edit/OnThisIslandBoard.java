package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTagtoTopicCommand;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnThisIslandBoard extends Composite implements CompleteListener {


	private TopicCache topicService;
	private TopicCompleter topicCompleter;
	private Topic myTopic;

	public OnThisIslandBoard(Manager manager) {		
		
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
		
		HorizontalPanel cp = new HorizontalPanel();
		cp.add(new HeaderLabel(Manager.myConstants.island_addTo()));
		cp.add(topicCompleter);
		cp.add(enterInfoButton);
		
		mainP.add(cp);
		//mainP.add(alsos);
		
		
		initWidget(mainP);
	}
	public void load(Tag topic) {
		myTopic = topic;		
	}

	public void completed(final String completeText) {
				
		//topicService.save(myTopic,new SaveSeeAlsoCommand(myTopic,new Topic(to)),
				
		topicService.createNew(completeText, false, new StdAsyncCallback(Manager.myConstants.save_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				long res = ((Long)result).longValue();
				
				Topic newTopic = new Topic();
				newTopic.setTitle(completeText);
				newTopic.setId(res);
				
				topicService.save(newTopic, new SaveTagtoTopicCommand(newTopic,(Tag) myTopic),
						new StdAsyncCallback(Manager.myConstants.save_async()){});
			}
			
			
		});
				
		
		
	}
	
}
