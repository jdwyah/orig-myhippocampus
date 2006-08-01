package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.TagServiceAsync;
import com.aavu.client.service.remote.TopicServiceAsync;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AddEditView extends Composite {

	private static final int MAX_PER_PAGE = 10;
	private TopicServiceAsync topicService;
	private TagServiceAsync tagService;

	private TopicList topicList;
	private TopicDetail topicDetail;
	
	private VerticalPanel topicPanel;

	public AddEditView(TopicServiceAsync topicService, TagServiceAsync tagService) {
		setTagService(tagService);
		setTopicService(topicService);


		HorizontalPanel mainPanel = new HorizontalPanel();
		
		VerticalPanel leftPanel = new VerticalPanel();
		
		
		
		topicDetail = new TopicDetail(topicService);		
		topicList = new TopicList(topicService,topicDetail);
		topicDetail.setTopicList(topicList);
		
		
		
		leftPanel.add(new SearchP());
		leftPanel.add(topicList);
		
		mainPanel.add(leftPanel);
		mainPanel.add(topicDetail);

		setWidget(mainPanel);		
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


	public void setTagService(TagServiceAsync tagService) {
		this.tagService = tagService;
	}
	public void setTopicService(TopicServiceAsync topicService) {
		this.topicService = topicService;
	}

	
	private class SearchP extends Composite{
		public SearchP(){
			HorizontalPanel panel = new HorizontalPanel();
			
			final TopicCompleter completer = new TopicCompleter(topicService); 
			
			panel.add(completer);
			Button addNew = new Button("Add");
			addNew.addClickListener(new ClickListener(){

				public void onClick(Widget sender) {
					Topic t = new Topic();
					t.setTitle(completer.getText());					
					topicDetail.load(t);
				}});
			
			panel.add(addNew);			
			setWidget(panel);
		}
	}
}
