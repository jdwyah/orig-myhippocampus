package com.aavu.client.widget.browse;

import com.aavu.client.TagServiceAsync;
import com.aavu.client.TopicServiceAsync;
import com.aavu.client.widget.ComposeView;
import com.aavu.client.widget.TopicList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BrowseView extends Composite {

	private static final int MAX_PER_PAGE = 10;
	private TopicServiceAsync topicService;
	private TagServiceAsync tagService;

	private TopicList topicList;
	private ComposeView topicDetail;
	
	private VerticalPanel topicPanel;

	public BrowseView(TopicServiceAsync topicService, TagServiceAsync tagService) {
		setTagService(tagService);
		setTopicService(topicService);


		HorizontalPanel mainPanel = new HorizontalPanel();
		
		topicDetail = new ComposeView();
		
		topicList = new TopicList(topicService,topicDetail);
		
		mainPanel.add(topicList);
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

}
