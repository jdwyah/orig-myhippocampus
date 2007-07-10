package com.aavu.client;

import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.remote.GWTSubjectService;
import com.aavu.client.service.remote.GWTSubjectServiceAsync;
import com.aavu.client.service.remote.GWTTopicService;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.service.remote.GWTUserService;
import com.aavu.client.service.remote.GWTUserServiceAsync;
import com.aavu.client.util.Logger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Shared functionality for setting up the GWT connections.
 * 
 * @author Jeff Dwyer
 * 
 */
public abstract class AbstractClientApp {

	private HippoCache hippoCache;

	protected void initServices() {
		// if(9==9)
		// throw new RuntimeException("sdfs");

		// Window.alert("1");
		GWTTopicServiceAsync topicService;
		GWTUserServiceAsync userService;
		GWTSubjectServiceAsync subjectService;

		topicService = (GWTTopicServiceAsync) GWT.create(GWTTopicService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) topicService;
		// endpoint.setServiceEntryPoint("http://localhost:8080/HippoTestW/service/TopicService");

		// Window.alert("2");

		// realModuleBase = "";

		String pre = Interactive.getRelativeURL("service/");

		// Window.alert("3");

		endpoint.setServiceEntryPoint(pre + "topicService");

		userService = (GWTUserServiceAsync) GWT.create(GWTUserService.class);
		ServiceDefTarget endpointUser = (ServiceDefTarget) userService;
		endpointUser.setServiceEntryPoint(pre + "userService");

		subjectService = (GWTSubjectServiceAsync) GWT.create(GWTSubjectService.class);
		ServiceDefTarget endpointSubject = (ServiceDefTarget) subjectService;
		endpointSubject.setServiceEntryPoint(pre + "subjectService");

		// Window.alert("4");
		if (topicService == null || userService == null || subjectService == null) {
			Logger.error("Service was null.");
		}

		setHippoCache(new HippoCache(topicService, userService, subjectService));

	}

	public void setHippoCache(HippoCache hippoCache) {
		this.hippoCache = hippoCache;
	}

	public HippoCache getHippoCache() {
		return hippoCache;
	}

}
