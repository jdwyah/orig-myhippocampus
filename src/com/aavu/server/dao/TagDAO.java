package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.server.domain.ServerSideUser;
import com.google.gwt.user.client.rpc.RemoteService;

public interface TagDAO {

	Tag getTag(ServerSideUser user,String tagName);

	void removeTag(ServerSideUser user,String itemText);

	List<Tag> getAllTags(ServerSideUser user);
	
	List<Tag> getTagsStarting(ServerSideUser user,String match);

	void save(ServerSideUser user,Tag selectedTag);
	
}
