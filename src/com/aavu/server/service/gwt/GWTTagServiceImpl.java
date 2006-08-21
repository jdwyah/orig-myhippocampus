package com.aavu.server.service.gwt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.service.remote.GWTTagService;
import com.aavu.server.service.TagService;

public class GWTTagServiceImpl extends GWTSpringController implements GWTTagService {
	
	private static final Logger log = Logger.getLogger(GWTTagServiceImpl.class);
	
	private TagService tagService;	

	public Tag[] getAllTags() {
		try {
			log.debug("getALLTags...");
			log.debug("\n\n");
			log.debug("Now..");
			List<Tag> list = tagService.getAllTags();
			log.debug("A");
			Tag[] rtn = new Tag[list.size()];
			log.debug("B");
			int i = 0;
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				log.debug("C");
				Tag element = (Tag) iter.next();
				rtn[i++] = convert(element);
			}

			log.debug("returning "+rtn);
			
			return rtn;
		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

	public Tag getTagAddIfNew(String tagName) {
		try{
			return convert(tagService.getTagAddIfNew(tagName));
		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

	public void removeTag(Tag selectedTag) {
		tagService.removeTag(selectedTag);
	}

	public String[] match(String match) {
		try{
			List<Tag> list = tagService.getTagsStarting(match);
			String[] rtn = new String[list.size()];
			int i=0;
			for(Tag t : list){
				rtn[i++] = t.getName();
			}		
			return rtn;			
		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

	public void saveTag(Tag selectedTag) {
		try{
			tagService.save(selectedTag);
		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
		}
	}

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	public static Tag convert(Tag t){		
		ArrayList<Meta> nMetas = new ArrayList<Meta>();
		nMetas.addAll(t.getMetas());
		t.setMetas(nMetas);		
		return t;
	}

}
