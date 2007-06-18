package com.aavu.server.service.gwt;

import java.util.List;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.exception.HippoException;
import com.aavu.client.service.remote.GWTTagService;
import com.aavu.server.service.TopicService;
import com.aavu.server.util.gwt.GWTSpringControllerReplacement;

public class GWTTagServiceImpl extends GWTSpringControllerReplacement implements GWTTagService {

	private static final Logger log = Logger.getLogger(GWTTagServiceImpl.class);

	private TopicService topicService;	

	

	public Topic createTagIfNonExistent(String tagName) throws HippoException {
		try{
			return GWTTopicServiceImpl.convert(topicService.createNewIfNonExistent(tagName));
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
			
			return topicService.getTagsStarting(match);			

		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}


	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}


	public TagStat[] getTagStats() throws HippoException {
		try{
			List<TagStat> stats = topicService.getTagStats();
			TagStat[] rtn = new TagStat[stats.size()];
			return stats.toArray(rtn);
		}catch(Exception e){
			log.error("FAILURE: "+e);
			e.printStackTrace();
			throw new HippoException(e);
		}
	}


}
