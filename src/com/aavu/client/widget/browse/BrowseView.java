package com.aavu.client.widget.browse;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.TagServiceAsync;
import com.aavu.client.service.remote.TopicServiceAsync;
import com.aavu.client.widget.edit.TopicCompleter;
import com.aavu.client.widget.edit.TopicDetail;
import com.aavu.client.widget.edit.TopicList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BrowseView extends Composite {

	private TopicServiceAsync topicService;
	private TagServiceAsync tagService;

	
	private BrowseOptionPanel browseOptions; 	
	private DockPanel mainView;
	
	private TopicList topicList;
	private TopicDetail topicDetail;	
	private VerticalPanel topicPanel;

	public BrowseView(TopicServiceAsync topicService, TagServiceAsync tagService) {
		setTagService(tagService);
		setTopicService(topicService);

		mainView = new DockPanel();
		browseOptions = new BrowseOptionPanel(mainView,topicService);
		
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		
				
		mainPanel.add(browseOptions);
		mainPanel.add(mainView);

		setWidget(mainPanel);		

		
		//		load();
		
		
	}


	private void load(){
//		topicList.load();
		
		
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
			
			final TopicCompleter completer = new TopicCompleter(); 
			
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
