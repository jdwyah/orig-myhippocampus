package com.aavu.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveSeeAlsoCommand;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoPermissionException;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.UserPageBean;

/**
 * 
 * @author Jeff Dwyer
 *
 */
public class TopicServiceImpl implements TopicService {
	private static final Logger log = Logger.getLogger(TopicServiceImpl.class);

	private static MetaSeeAlso seealsoSingleton;
	private TopicDAO topicDAO;

	private UserService userService;

	public void addLinkToTags(WebLink link, String[] tags) throws HippoBusinessException {

		for (String string : tags) {			
			log.debug("str: "+string);			

			String clipped = string.trim();
			
			if(clipped.length() != 0){
				Topic t = getForName(clipped);			
				if(null == t){
					log.debug("was null, creating as Tag ");
					t = new Tag();
					t.setTitle(clipped);				
					t.setUser(userService.getCurrentUser());							
				}			
				t.getOccurences().add(link);
				Topic st = save(t);
			}
			
		}
	}
	/**
	 * 
	 * @throws HippoPermissionException 
	 */
	public void changeState(long topicID, boolean toIsland) throws HippoPermissionException {
		Topic t = topicDAO.get(topicID);
		if(t.getUser() != userService.getCurrentUser()){
			throw new HippoPermissionException();
		}
		topicDAO.changeState(t, toIsland);
	}


	/*
	 * TODO maybe AOP secure this? 
	 * 
	 * (non-Javadoc)
	 * @see com.aavu.server.service.TopicService#delete(com.aavu.client.domain.Topic)
	 */
	public void delete(Topic topic) throws HippoBusinessException {
		if(userService.getCurrentUser().equals(topic.getUser())){
			topicDAO.delete(topic);

			//TODO delete S3Files 
			//TODO delete Weblinks that were only referenced by us
			//TODO_ delete our Entries - done
			//TODO_ delete our MindTrees - done
			
		}else{
			throw new HippoBusinessException("User "+userService.getCurrentUser().getUsername()+" can't delete this topic");
		}
	}

	public void deleteOccurrence(long id) throws HippoPermissionException {
		Occurrence o = topicDAO.getOccurrrence(id);
		if(o.getUser() != userService.getCurrentUser()){
			throw new HippoPermissionException();
		}
		topicDAO.deleteOccurrence(o);
	}

	/**
	 * 1) Hydrate. prepar the command. change the long id's into loaded hibernate objects.
	 * 2) Execute. use the domain classes logic & the command to enact the change
	 * 3) Save.
	 */
	public void executeAndSaveCommand(AbstractCommand command) throws HippoBusinessException {
		hydrateCommand(command);
		command.executeCommand();		
		saveCommand(command);	
	}
	
	public List<LocationDTO> getAllLocations() {	
		return topicDAO.getLocations(userService.getCurrentUser());
	}
	
	
	
	
	
	
	public List getAllMetas() {		
		return topicDAO.getAllMetas(userService.getCurrentUser());
	}

	
	public List<DatedTopicIdentifier> getAllTopicIdentifiers() {
		return topicDAO.getAllTopicIdentifiers(userService.getCurrentUser());
	}

	public List<DatedTopicIdentifier> getAllTopicIdentifiers(boolean all) {
		return topicDAO.getAllTopicIdentifiers(userService.getCurrentUser(),all);
	}

	
	
	public Topic getForID(long topicID) {
		return topicDAO.getForID(userService.getCurrentUser(),topicID);
	}
	public Topic getForName(String string) {
		return topicDAO.getForName(userService.getCurrentUser(),string);
	}
	public List<TopicIdentifier> getLinksTo(Topic topic) {
		return topicDAO.getLinksTo(topic, userService.getCurrentUser());
	}

	public List<List<LocationDTO>> getLocationsForTags(List<TopicIdentifier> shoppingList) {
		List<List<LocationDTO>> rtn = new ArrayList<List<LocationDTO>>(shoppingList.size());
		
		for (TopicIdentifier tag : shoppingList) {
			rtn.add(topicDAO.getLocations(tag.getTopicID(),userService.getCurrentUser()));
		}		
		return rtn;		
	}
	public MetaSeeAlso getSeeAlsoMetaSingleton() throws HippoBusinessException{		
		if(seealsoSingleton == null){
			log.info("seealso single == null. Finding... ");

			seealsoSingleton = topicDAO.getSeeAlsoSingleton();
			
			if(seealsoSingleton == null){
				log.info("seealso single == null. Creating. First time DB?");

				seealsoSingleton =  new MetaSeeAlso();
				seealsoSingleton = (MetaSeeAlso) topicDAO.save(seealsoSingleton);
			}
		}
		return seealsoSingleton;
	}
//	public List<TimeLineObj> getTimelineObjs(long tagID) {
//		return topicDAO.getTimeline(tagID,userService.getCurrentUser());
//	}
	public List<TimeLineObj> getTimeline() {
		return topicDAO.getTimeline(userService.getCurrentUser());
	}
	public List<List<TimeLineObj>>  getTimelineWithTags(List<TopicIdentifier> shoppingList) {
		List<List<TimeLineObj>> rtn = new ArrayList<List<TimeLineObj>>(shoppingList.size());
				
		for (TopicIdentifier tag : shoppingList) {
			rtn.add(topicDAO.getTimeline(tag.getTopicID(),userService.getCurrentUser()));
		}		
		return rtn;		
	}
	public List<FullTopicIdentifier> getTopicIdsWithTag(long id) {
		
		List<TopicTypeConnector> conns = topicDAO.getTopicIdsWithTag(id,userService.getCurrentUser());
		
		List<FullTopicIdentifier> rtn = new ArrayList<FullTopicIdentifier>(conns.size());
		for (TopicTypeConnector conn : conns) {
			rtn.add(new FullTopicIdentifier(conn));
			
			
			log.debug("Topic on island Found "+conn.getId()+" "+conn.getLatitude()+" "+conn.getLongitude());
			
		}
		return rtn;
		//return topicDAO.getTopicIdsWithTag(id,userService.getCurrentUser());
	}
	public List<List<FullTopicIdentifier>> getTopicIdsWithTags(List<TopicIdentifier> shoppingList) {
		
		List<List<FullTopicIdentifier>> rtn = new ArrayList<List<FullTopicIdentifier>>(shoppingList.size());
		
			
		for (TopicIdentifier tag : shoppingList) {
			rtn.add(getTopicIdsWithTag(tag.getTopicID()));
		}
		
		return rtn;
	}
	public List<String> getTopicsStarting(String match) {
		return topicDAO.getTopicsStarting(userService.getCurrentUser(),match);
	}
	
	public MindTree getTree(MindTreeOcc occ) {
		return topicDAO.getTree(occ);
	}
	public UserPageBean getUserPageBean(User su) {
		UserPageBean rtn = new UserPageBean();
		rtn.setUser(su);
		topicDAO.populateUsageStats(rtn);
		return rtn;
	}
	
	
	
	
	
	
	public WebLink getWebLinkForURL(String url) {
		return topicDAO.getWebLinkForURI(url,userService.getCurrentUser());		
	}
	/**
	 * PEND Would prefer to make these loads instead of gets, 
	 * but Tag's won't instanceof Tag.class when we do that.
	 * 
	 * @param command
	 * @throws HippoBusinessException 
	 */
	private void hydrateCommand(AbstractCommand command) throws HippoBusinessException {
		
		log.debug("Hydrate: "+command);
		
		List ids = command.getTopicIDs();
		int i = 0;
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			command.setTopic(i, topicDAO.get(id));
			i++;
		}
		
		log.debug("Hydrated. "+command.getTopic(0)+" "+command.getTopic(1)+" "+command.getTopic(2));
		
		//a bit messy, but it's tough to inject this otherwise, since we don't want the
		//domain knowing about this service.
		//
		if(command instanceof SaveSeeAlsoCommand){
			command.setTopic(2,getSeeAlsoMetaSingleton());
		}
	}
	
	public Occurrence save(Occurrence link) {
		return topicDAO.save(link);
	}
	/**
	 * TODO I don't really like setting the user here, but it's tricky 
	 * to give the Topic & Entry objects something that knows about users.
	 * 
	 */
	public Topic save(Topic topic) throws HippoBusinessException {
		
		topic.setLastUpdated(new Date());
		topic.setUser(userService.getCurrentUser());
		
		
		if(topic.getTitle().equals("")){
			log.info("Throw HBE exception for Empty Title");
			throw new HippoBusinessException("Empty Title");
		}	
		if(topic.mustHaveUniqueName()){
			log.debug("Getting same named");
			
			try {
				Topic sameNamed = (Topic) topicDAO.getForName(topic.getUser(), topic.getTitle());
				log.debug("Rec "+sameNamed);		

				if(sameNamed != null && sameNamed.getId() != topic.getId()){
					log.info("Throw HBE exception for Duplicate Title. ID: "+topic.getId()+" ID2:"+sameNamed.getId());
					throw new HippoBusinessException("Duplicate Name");
				}		
				//need to evict or we'll get a NonUniqueException
				//topicDAO.evict(sameNamed);
				
			} catch (IncorrectResultSizeDataAccessException e) {
				log.info(e.getMessage()+" Throw HBE exception for Duplicate Title. ID: "+topic.getId()+" "+topic.getTitle());
				throw new HippoBusinessException("Duplicate Name");
			}
			

		}
		
		//log.debug("save "+topic.toPrettyString());
				
		log.debug("Topic Save Setting User "+userService.getCurrentUser());
		
		
		Set<Occurrence> occs = topic.getOccurences();
		for(Occurrence o : occs){
			o.setUser(userService.getCurrentUser());
		}
		
		
		
		
		
		return topicDAO.save(topic);
	}
	
	private void saveCommand(AbstractCommand command) throws HippoBusinessException {
				
		List topics = command.getTopics();
		for (Iterator iter = topics.iterator(); iter.hasNext();) {
			Topic topic = (Topic) iter.next();
			save(topic);
		}
		
	}
	public void saveTopicLocation(long tagId, long topicId, double xpct, double ypct) {
		topicDAO.saveTopicsLocation(tagId, topicId, xpct, ypct);
	}
	public MindTree saveTree(MindTree tree) {
		return topicDAO.save(tree);
	}
	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	

}
