package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.WebLink;


public interface BrowseService {

	List<WebLink> getTopWeblinks();

	List<RealTopic> getTopTopics();



}
