package com.aavu.server.service;

import java.util.List;

import org.dom4j.Document;

import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoException;
import com.aavu.server.domain.DeliciousBundle;
import com.aavu.server.domain.DeliciousPost;

public interface DeliciousService {

	int newLinksForUser(String username, String password) throws HippoException;

	List<DeliciousPost> getPostsFromXML(Document doc);

	List<DeliciousBundle> getBundlesFromXML(Document bundleDoc);

	void addBundles(Topic deliciousRoot, List<DeliciousBundle> bundles) throws HippoException;

	void addDeliciousTags(List<DeliciousPost> posts, Topic deliciousRoot) throws HippoException;

}
