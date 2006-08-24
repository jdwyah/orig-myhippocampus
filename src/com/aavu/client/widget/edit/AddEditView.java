package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AddEditView extends Composite {

	private static final int MAX_PER_PAGE = 10;
	private GWTTopicServiceAsync topicService;
	private GWTTagServiceAsync tagService;

	private TopicList topicList;
	private TopicViewAndEditWidget topicViewAndEditWidget;
	
	private VerticalPanel topicPanel;

	public AddEditView(GWTTopicServiceAsync topicService, GWTTagServiceAsync tagService) {
		setTagService(tagService);
		setTopicService(topicService);


		HorizontalPanel mainPanel = new HorizontalPanel();
		
		VerticalPanel leftPanel = new VerticalPanel();
		
		
		
		topicViewAndEditWidget = new TopicViewAndEditWidget(topicService,tagService);		
		topicList = new TopicList(topicService,topicViewAndEditWidget);
		topicViewAndEditWidget.setTopicList(topicList);
		
		
		
		leftPanel.add(new SearchP());
		leftPanel.add(topicList);
		
		mainPanel.add(leftPanel);
		mainPanel.add(topicViewAndEditWidget);

		initWidget(mainPanel);		
		load();
	}


	private void load(){
		topicList.load();
		
		
//		topicService.getAllTopics(0, MAX_PER_PAGE, new StdAsyncCallback(){
//			public void onSuccess(Object result) {
//				Topic[] topics = (Topic[]) result;
//				topicPanel.clear();
//				for(int i=0; i<topics.length; i++){
//					topicPanel.add(new TopicDetail(topics[i]));					
//				}
//			}
//		});
		
		
	}


	public void setTagService(GWTTagServiceAsync tagService) {
		this.tagService = tagService;
	}
	public void setTopicService(GWTTopicServiceAsync topicService) {
		this.topicService = topicService;
	}

	
	private class SearchP extends Composite{
		public SearchP(){
			HorizontalPanel panel = new HorizontalPanel();
			
			final TopicCompleter completer = new TopicCompleter(); 
			
			panel.add(completer);
			Button addNew = new Button("Add");
			addNew.addClickListener(new ClickListener(){

				public void onClick(Widget sender) {
					Topic t = new Topic();
					t.setTitle(completer.getText());					
					topicViewAndEditWidget.load(t);
				}});
			
			panel.add(addNew);			
			initWidget(panel);
		}
	}
}
