package com.aavu.server.service.gwt;

import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.Tag;
import com.aavu.client.service.remote.GWTTagService;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.db4o.TagDAOdb4oImpl;
import com.aavu.server.service.TagService;

public class GWTTagServiceImpl extends GWTSpringController implements GWTTagService {

	private TagService tagService;	

	public Tag[] getAllTags() {
		
		List<Tag> list = tagService.getAllTags();
		Tag[] rtn = new Tag[list.size()];
		
		int i = 0;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Tag element = (Tag) iter.next();
			rtn[i++] = element;
		}
		
		return rtn; 
	}

	public Tag getTagAddIfNew(String tagName) {
		return tagService.getTagAddIfNew(tagName);
	}

	public void removeTag(Tag selectedTag) {
		tagService.removeTag(selectedTag);
	}

	public String[] match(String match) {
		List<Tag> list = tagService.getTagsStarting(match);
		String[] rtn = new String[list.size()];
		int i=0;
		for(Tag t : list){
			rtn[i++] = t.getName();
		}		
		return rtn;				
	}

	public void saveTag(Tag selectedTag) {
		tagService.save(selectedTag);	
	}

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

}
