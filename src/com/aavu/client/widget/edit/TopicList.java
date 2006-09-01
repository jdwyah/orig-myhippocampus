package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicList extends Composite {

	private VerticalPanel panel = new VerticalPanel();

	private TopicViewAndEditWidget topicViewAndEditWidget;

	private TopicCache topicCache;



	public TopicList(TopicCache topicCache,TopicViewAndEditWidget topicDetailI){
		this.topicCache = topicCache;
		
		setTopicDetail(topicDetailI);

		load();
		VerticalPanel mainPanel = new VerticalPanel();

//		Label addNew = new Label("Add new");
//		addNew.addClickListener(new ClickListener() {					
//			public void onClick(Widget sender) { 
//				topicDetail.load(new Topic());
//			}}); 
//		
//		mainPanel.add(addNew);

		mainPanel.add(panel);
		initWidget(mainPanel);
	}


	public void load() {
		topicCache.getAllTopics(0,0,new StdAsyncCallback("topiclist load") {

			public void onSuccess(Object result) {
				panel.clear();
				final Topic[] l = (Topic[]) result;
				System.out.println("TopicList success: "+l.length);
				for(int i=0; i<l.length; i++){

					TopicLabel h = new TopicLabel(l[i]);
					h.addClickListener(new ClickListener() {					
						public void onClick(Widget sender) { 
							topicViewAndEditWidget.load(((TopicLabel)sender).getT());
						}});
					panel.add(h);

				}
			}

		});	
	}


	public void setTopicDetail(TopicViewAndEditWidget topicViewAndEditWidget) {
		this.topicViewAndEditWidget = topicViewAndEditWidget;
	}

}
