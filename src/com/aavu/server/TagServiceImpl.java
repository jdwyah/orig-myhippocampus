package com.aavu.server;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.TagService;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.db4o.TagDAOdb4oImpl;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TagServiceImpl extends RemoteServiceServlet implements TagService {

	private TagDAO tagDAO; 
	
	public TagServiceImpl(){
		
		tagDAO = new TagDAOdb4oImpl();
		setTagDAO(tagDAO);
		
	}
	
	public void addTag(Tag tag) {
		tagDAO.addTag(tag);
	}

	public Tag[] getAllTags() {
		
		List<Tag> list = tagDAO.getAllTags();
		Tag[] rtn = new Tag[list.size()];
		
		int i = 0;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Tag element = (Tag) iter.next();
			rtn[i++] = element;
		}
		
		return rtn; 
	}

	public Tag getTag(String tagName) {
		return tagDAO.getTag(tagName);
	}

	public void removeTag(String itemText) {
		tagDAO.removeTag(itemText);
	}

	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}

	public String[] match(String match) {
		List<Tag> list = tagDAO.getTagsStarting(match);
		String[] rtn = new String[list.size()];
		int i=0;
		for(Tag t : list){
			rtn[i++] = t.getName();
		}		
		return rtn;				
	}

}
