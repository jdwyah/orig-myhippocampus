package com.aavu.client.service.cache;

import com.aavu.client.service.remote.GWTSubjectServiceAsync;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.service.remote.GWTUserServiceAsync;

public class HippoCache {

	public static final ReturnTypeConstant TAG = new ReturnTypeConstant(3);
	public static final ReturnTypeConstant TAG_LIST = new ReturnTypeConstant(4);

	private GWTTopicServiceAsync topicService;

	private GWTUserServiceAsync userService;
	private GWTSubjectServiceAsync subjectService;

	private TopicCache topicCache;

	public HippoCache(GWTTopicServiceAsync topicService, GWTUserServiceAsync userService,
			GWTSubjectServiceAsync subjectService2) {
		this.topicService = topicService;
		this.userService = userService;
		this.subjectService = subjectService2;

		topicCache = new TopicCache(topicService);

	}

	public TopicCache getTopicCache() {
		return topicCache;
	}

	public GWTUserServiceAsync getUserService() {
		return userService;
	}

	public GWTSubjectServiceAsync getSubjectService() {
		return subjectService;
	}

}
