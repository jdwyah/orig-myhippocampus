package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.WebLink;

public interface BrowseDAO {

	List<WebLink> getTopWeblinks();

	List<RealTopic> getTopTopics();
}
