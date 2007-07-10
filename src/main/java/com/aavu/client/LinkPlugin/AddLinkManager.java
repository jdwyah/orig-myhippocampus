package com.aavu.client.LinkPlugin;

import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.LinkAndUser;
import com.aavu.client.gui.ext.GUIEffects;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AddLinkManager implements CloseListener {

	private GWTTopicServiceAsync topicService;

	private String description;
	private String notes;
	private String url;
	private WebLink weblink;
	private AddLinkContent panel;
	private TopicCache topicCache;

	public AddLinkManager(GWTTopicServiceAsync topicService, String url, String notes,
			String description) {

		this.topicService = topicService;
		this.url = url;
		this.notes = notes;
		this.description = description;
		this.topicCache = new TopicCache(topicService);

	}

	public void getExistingLinkAndCreatePanel(final AsyncCallback loadGUICallback) {
		topicService.getWebLinkForURLAndUser(url, new AsyncCallback() {
			public void onFailure(Throwable caught) {
				System.out.println("ALM fail " + caught.getMessage());
				loadGUICallback.onFailure(caught);
			}

			public void onSuccess(Object result) {
				Logger.debug("ALM succ " + result);

				LinkAndUser linkAndUser = (LinkAndUser) result;

				weblink = linkAndUser.getWeblink();

				if (weblink == null) {
					weblink = new WebLink();
					weblink.setUri(url);
				}
				if (weblink.getDescription() == null || weblink.getDescription().length() < 1) {
					weblink.setDescription(description);
				}
				if (weblink.getNotes() == null) {
					weblink.setNotes(notes);
				} else {
					weblink.setNotes(notes + " " + weblink.getNotes());
				}

				// panel = new
				// AddLinkPanel(AddLinkManager.this,weblink,url,notes,description);

				panel = new AddLinkContent(topicCache, weblink, AddLinkManager.this, linkAndUser
						.getUser().getUsername());

				loadGUICallback.onSuccess(panel);
			}
		});
	}

	public TopicCache getTopicCache() {
		return topicCache;
	}

	public void close() {
		GUIEffects.close();
	}

}
