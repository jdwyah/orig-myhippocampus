package com.aavu.client.widget.browse;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.widget.edit.TopicWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BrowseBlogView extends Composite{

	private GWTTopicServiceAsync topicService;

	private int start = 0;
	private int numberPerScreen = 10;

	private VerticalPanel topicPanel;
	private VerticalPanel mainPanel;

	private Topic[] topics;

	private int curStart;

	public BrowseBlogView(GWTTopicServiceAsync topicService){

		this.topicService = topicService;

		topicPanel = new VerticalPanel();

		mainPanel = new VerticalPanel();

		mainPanel.add(new ButtonBoard());

		mainPanel.add(topicPanel);
		
		load();

		initWidget(mainPanel);
	}


	private void load() {
		topicService.getBlogTopics(start,numberPerScreen,new StdAsyncCallback(){
			public void onSuccess(Object result) {

				topics = (Topic[]) result;
				
				System.out.println("finished blog load: "+topics.length);
				draw();
			}
		});
	}


	private void draw() {
		draw(0);
	}
	private void draw(int start) {
		if(start < 0){
			start = 0;
		}
		curStart = start;


		topicPanel.clear();
		topicPanel.addStyleName("middle-column-box-white");
		
		//TODO change to setText
		topicPanel.add(new Label("Topics "+curStart+" to "+(curStart+numberPerScreen)));
		
		for (int i = curStart; i < curStart + numberPerScreen; i++) {
			
			if(i >= topics.length){

			}else{
				Topic topic = topics[i];	
				//System.out.println(topic.getTitle()+" "+topic.getText()+" "+topic.getId());
				TopicWidget topicWidget = new TopicWidget(topic);
				//topicWidget.addStyleName("");
				topicPanel.add(topicWidget);
			}
		}		
		
	}

	private class ButtonBoard extends Composite{

		public ButtonBoard(){

			HorizontalPanel hp = new HorizontalPanel();

			Button leftB = new Button("&lt;");
			leftB.addClickListener(new ClickListener(){

				public void onClick(Widget sender) {
					draw(curStart - numberPerScreen);
				}});

			Button rightB = new Button("&gt;");
			rightB.addClickListener(new ClickListener(){

				public void onClick(Widget sender) {
					draw(curStart + numberPerScreen);
				}});




			hp.add(leftB);
			hp.add(rightB);

			setWidget(hp);
		}
	}


}
