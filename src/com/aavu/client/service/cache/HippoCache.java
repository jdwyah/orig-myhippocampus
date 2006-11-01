package com.aavu.client.service.cache;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.GWTSubjectService;
import com.aavu.client.service.remote.GWTSubjectServiceAsync;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.service.remote.GWTUserServiceAsync;

public class HippoCache {

	

	public static final ReturnTypeConstant TAG = new ReturnTypeConstant(3);
	public static final ReturnTypeConstant TAG_LIST = new ReturnTypeConstant(4);
	
	private GWTTopicServiceAsync topicService;
	private GWTTagServiceAsync tagService;
	private GWTUserServiceAsync userService;
	private GWTSubjectServiceAsync subjectService;

	private TopicCache topicCache;
	private TagCache tagCache;
	
	
	public HippoCache(GWTTopicServiceAsync topicService, GWTTagServiceAsync tagService, GWTUserServiceAsync userService, GWTSubjectServiceAsync subjectService2) {
		this.topicService = topicService;
		this.tagService = tagService;
		this.userService = userService;
		this.subjectService = subjectService2;
		
		topicCache = new TopicCache(topicService);
		tagCache = new TagCache(tagService);
				
	}

	public TagCache getTagCache() {
		return tagCache;
	}

	public TopicCache getTopicCache() {
		return topicCache;
	}

	public GWTSubjectServiceAsync getSubjectService() {		
		return subjectService;
	}






}
