package com.aavu.server.web.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.service.TopicService;

public class BrowseController extends BasicController  implements InitializingBean {
	private static final Logger log = Logger.getLogger(BrowseController.class);

	private String notFoundView;

	private TopicService topicService;
	private String userStartView;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.notFoundView, "Not Found must be set.");
		Assert.notNull(this.userStartView, "userStartView must be set.");
		Assert.notNull(this.topicService, "Topic Service must be set.");
	}


	public String getNotFoundView() {
		return notFoundView;
	}
	public String getUserStartView() {
		return userStartView;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse arg1) throws Exception {

		String path = req.getPathInfo();

		String[] pathParts = path.split("/");		
		log.debug("!path parts "+Arrays.toString(pathParts));



		Map<String,Object> model = getDefaultModel(req);


		// "/user/topic" splits to [,user,topic]
		if(pathParts.length < 2){
			return new ModelAndView(getNotFoundView());
		}

		if(2 == pathParts.length){
			String userString = pathParts[1];

			return userOnly(model,userString);
		}
		else{

			String userString = pathParts[1];
			String topicString = pathParts[2];
			return userAndTopic(model,userString,topicString);
		}


	}





	public void setNotFoundView(String notFoundView) {
		this.notFoundView = notFoundView;
	}





	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	public void setUserStartView(String userStartView) {
		this.userStartView = userStartView;
	}





	private ModelAndView userAndTopic(Map<String, Object> model, String userString, String topicString) {
		try{
			Topic result = topicService.getPublicTopic(userString,topicString);

			List<FullTopicIdentifier> onThisIsland = topicService.getPublicTopicIdsWithTag(result.getId());


			model.put("topic", result);
			model.put("onThisIsland",onThisIsland);
			model.put("username", userString);

//			model.put("command",new SearchCommand());

			return new ModelAndView(getView(), model);
		}catch (HippoBusinessException e) {			
			return new ModelAndView(getNotFoundView(),"message",topicString+" for "+userString +" not found.");
		}
	}



	private ModelAndView userOnly(Map<String, Object> model, String userString) {
		try{
			List<DatedTopicIdentifier> topics = topicService.getAllPublicTopicIdentifiers(userString,0, 10, null);
			
			model.put("topics", topics);
			
			model.put("username", userString);

			//model.put("command",new SearchCommand());

			return new ModelAndView(getUserStartView(), model);
			
		}catch (Exception e) {		
			log.debug(e.getMessage());
			return new ModelAndView(getNotFoundView(),"message","Browse User "+userString +" not found.");
		}
	}

}
