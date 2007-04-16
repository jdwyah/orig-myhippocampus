package com.aavu.server.service.gwt;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.exception.HippoException;
import com.aavu.client.service.remote.GWTTagService;
import com.aavu.server.service.TagService;
import com.aavu.server.util.gwt.GWTSpringControllerReplacement;

public class GWTTagServiceImpl extends GWTSpringControllerReplacement implements GWTTagService {

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

	public Tag createTagIfNonExistent(String tagName) throws HippoException {
		try{
			return convert(tagService.createTagIfNonExistent(tagName));
		}  catch (HippoException ex) {
			throw ex;
		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

//	public void removeTag(Tag selectedTag) throws PermissionDeniedException {
//		tagService.removeTag(selectedTag);
//	}

	public List match(String match) {
		try{
			
			return tagService.getTagsStarting(match);			

		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}


	public Tag getTagForName(String completeText) {
		try{
			log.debug("getTagForName:");
			log.debug(completeText);

			return convert(tagService.getTagForName(completeText));
		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}


	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	public Tag[] convertAllTags(List<Tag> tags){
		Tag[] rtn = new Tag[tags.size()];
		for (int i = 0; i < rtn.length; i++) {
			rtn[i] = convert(tags.get(i));
		}
		return rtn;
	}

	/**
	 * Get ready for GWT Serialization.
	 * Static so it can also be called by topics.
	 * 
	 * Rule is that hibernate collections are no good! must convert to basic java.util 
	 * otherwise GWT barfs.
	 * 
	 * @param t
	 * @return
	 */
	public static Tag convert(Tag t){	
		return (Tag) GWTTopicServiceImpl.convert(t);
	}

	public TagStat[] getTagStats() throws HippoException {
		try{
			List<TagStat> stats = tagService.getTagStats();
			TagStat[] rtn = new TagStat[stats.size()];
			return stats.toArray(rtn);
		}catch(Exception e){
			log.error("FAILURE: "+e);
			e.printStackTrace();
			throw new HippoException(e);
		}
	}


}