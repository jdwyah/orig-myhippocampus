package com.aavu.client.LinkPlugin;

import org.gwtwidgets.client.util.WindowUtils;

import com.aavu.client.AddLink;
import com.aavu.client.domain.WebLink;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddLinkManager implements CloseListener {

	private GWTTopicServiceAsync topicService;
	private GWTTagServiceAsync tagService;
	private String description;
	private String notes;
	private String url;
	private WebLink weblink;
	private AddLinkContent panel;
	private TopicCache topicCache;
	
	public AddLinkManager(GWTTopicServiceAsync topicService, GWTTagServiceAsync tagService, String url, String notes, String description) {
	
		this.topicService = topicService;
		this.tagService = tagService;
		this.url = url;
		this.notes = notes;
		this.description = description;		
		this.topicCache = new TopicCache(topicService);
	
	}



	public void init(final AsyncCallback loadGUICallback) {
		topicService.getWebLinkForURL(url, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				System.out.println("ALM fail "+caught.getMessage());
				loadGUICallback.onFailure(caught);
			}

			public void onSuccess(Object result) {
				System.out.println("ALM succ "+result);
				weblink = (WebLink) result;
				
				//panel = new AddLinkPanel(AddLinkManager.this,weblink,url,notes,description);
				
				panel = new AddLinkContent(null,topicCache,weblink,null,AddLinkManager.this);
				
				loadGUICallback.onSuccess(panel);
			}});
	}


	public TopicCache getTopicCache() {
		return topicCache;
	}



	public void close() {
		
	}
	
}
