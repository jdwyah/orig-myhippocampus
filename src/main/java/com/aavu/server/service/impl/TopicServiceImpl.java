package com.aavu.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveSeeAlsoCommand;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.LinkAndUser;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoPermissionException;
import com.aavu.client.exception.HippoSubscriptionException;
import com.aavu.server.dao.EditDAO;
import com.aavu.server.dao.SelectDAO;
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
	private SelectDAO selectDAO;
	private EditDAO editDAO;
	
	
	private UserService userService;

	//private SearchService searchService;

	public void addLinkToTags(WebLink link, String[] tags) throws HippoBusinessException {

		for (String string : tags) {			
			log.debug("str: "+string);			

			String clipped = string.trim();

			if(clipped.length() != 0){
				Topic t = getForName(clipped);			
				if(null == t){
					log.debug("was null, creating as Tag ");
					t = new Topic();
					t.setTitle(clipped);				
					t.setUser(userService.getCurrentUser());							
				}			
				t.addOccurence(link);
				Topic st = save(t);
			}

		}
	}
	/**
	 * 
	 * @throws HippoPermissionException 
	 */
	public void changeState(long topicID, boolean toIsland) throws HippoPermissionException {
		Topic t = selectDAO.get(topicID);
		if(t.getUser() != userService.getCurrentUser()){
			throw new HippoPermissionException();
		}
		editDAO.changeState(t, toIsland);
	}


	/**
	 * If parent is null, that's ok, we're saving a Meta or something. 
	 * Pass new Root(), to do a lookup and get the actual root element. 
	 * Pass parent to get tagged.
	 * 
	 */
	public Topic createNew(String title, Topic topicOrTagOrMeta, Topic parent) throws HippoBusinessException {

		if(userIsOverSubscriptionLimit()){
			log.info("User over Subscription Limit "+userService.getCurrentUser().getUsername());
			throw new HippoSubscriptionException("Too many topics for your subscription.");
		}

		topicOrTagOrMeta.setTitle(title);

		topicOrTagOrMeta = save(topicOrTagOrMeta);

		if(parent != null){		
			if(parent instanceof Root){
				topicOrTagOrMeta.tagTopic(selectDAO.getRoot(userService.getCurrentUser(), userService.getCurrentUser()));
			}else{
				topicOrTagOrMeta.tagTopic(parent);
			}
			topicOrTagOrMeta = save(topicOrTagOrMeta);
		}
		
		log.info("create New: "+title+" "+topicOrTagOrMeta.getClass()+" "+userService.getCurrentUser().getUsername());

		return topicOrTagOrMeta;
	}

	/*
	 * TODO maybe AOP secure this? 
	 * 
	 * (non-Javadoc)
	 * @see com.aavu.server.service.TopicService#delete(com.aavu.client.domain.Topic)
	 */
	public void delete(Topic topic) throws HippoBusinessException {
		if(userService.getCurrentUser().equals(topic.getUser())){
			editDAO.delete(topic);

			//TODO delete S3Files 
			//TODO delete Weblinks that were only referenced by us
			//TODO_ delete our Entries - done
			//TODO_ delete our MindTrees - done

		}else{
			throw new HippoBusinessException("User "+userService.getCurrentUser().getUsername()+" can't delete this topic");
		}
	}

	private void deleteCommand(AbstractCommand command) throws HippoBusinessException {		
		Set topics = command.getDeleteSet();
		for (Iterator iter = topics.iterator(); iter.hasNext();) {		
			Topic topic = (Topic) iter.next();
			log.info("DeleteCommand Delete: "+topic);
			delete(topic);
		}		
	}

	public void deleteOccurrence(long id) throws HippoPermissionException {
		Occurrence o = selectDAO.getOccurrrence(id);
		if(o.getUser() != userService.getCurrentUser()){
			throw new HippoPermissionException();
		}
		editDAO.deleteOccurrence(o);
	}

	/**
	 * 1) Hydrate. prepar the command. change the long id's into loaded hibernate objects.
	 * 2) Execute. use the domain classes logic & the command to enact the change
	 * 3) Save.
	 */
	public void executeAndSaveCommand(AbstractCommand command) throws HippoBusinessException {
		
		log.info(command+" "+userService.getCurrentUser().getUsername());
		
		hydrateCommand(command);
		command.executeCommand();		
		saveCommand(command);
		deleteCommand(command);		
	}






	public List<LocationDTO> getAllLocations() {	
		return selectDAO.getLocations(userService.getCurrentUser());
	}


	public List<Meta> getAllMetas() {		
		return selectDAO.getAllMetas(userService.getCurrentUser());
	}

	public List<DatedTopicIdentifier> getAllTopicIdentifiers() {
		return selectDAO.getAllTopicIdentifiers(userService.getCurrentUser(),false);
	}

	

	public List<DatedTopicIdentifier> getAllTopicIdentifiers(boolean all) {
		return selectDAO.getAllTopicIdentifiers(userService.getCurrentUser(),all);
	}
	public List<DatedTopicIdentifier> getAllTopicIdentifiers(int start, int max, String startStr) {
		return selectDAO.getAllTopicIdentifiers(userService.getCurrentUser(),start,max,startStr);
	}
	public List<DatedTopicIdentifier> getAllPublicTopicIdentifiers(String username,int start, int max, String startStr) {		
		return selectDAO.getAllTopicIdentifiers(userService.getUserWithNormalization(username),start,max,startStr);
	}	
	public Topic getForID(long topicID) {
		return selectDAO.getForID(userService.getCurrentUser(),topicID);
	}
	public Topic getForName(String string) {
		return selectDAO.getForName(userService.getCurrentUser(),string);
	}

	public List<TopicIdentifier> getLinksTo(Topic topic) {
		return selectDAO.getLinksTo(topic, userService.getCurrentUser());
	}
	public List<List<LocationDTO>> getLocationsForTags(List<TopicIdentifier> shoppingList) {
		List<List<LocationDTO>> rtn = new ArrayList<List<LocationDTO>>(shoppingList.size());

		for (TopicIdentifier tag : shoppingList) {
			rtn.add(selectDAO.getLocations(tag.getTopicID(),userService.getCurrentUser()));
		}		
		return rtn;		
	}
	public Topic getPublicTopic(String userString, String topicString) throws HippoBusinessException {		
		Topic t = selectDAO.getPublicForName(userString, topicString);
		if(t != null){
			return t;
		}else{
			if(userService.exists(userString)){
				throw new HippoBusinessException("No Topic Found");
			}else{				
				throw new HippoBusinessException("No User "+userString+" Found");				
			}
		}		
	}
	public List<FullTopicIdentifier> getPublicTopicIdsWithTag(long id){
		return connectorsToTIs(selectDAO.getTopicIdsWithTag(id));
	}
	public Root getRootTopic(User forUser){
		return selectDAO.getRoot(forUser, userService.getCurrentUser());		
	}
	
	public MetaSeeAlso getSeeAlsoMetaSingleton() throws HippoBusinessException{		
		if(seealsoSingleton == null){
			log.info("seealso single == null. Finding... ");

			seealsoSingleton = selectDAO.getSeeAlsoSingleton();

			if(seealsoSingleton == null){
				log.info("seealso single == null. Creating. First time DB?");

				seealsoSingleton =  new MetaSeeAlso();
				seealsoSingleton = (MetaSeeAlso) editDAO.save(seealsoSingleton);

			}
		}

		return seealsoSingleton;
	}

	public List<TimeLineObj> getTimeline() {
		return selectDAO.getTimeline(userService.getCurrentUser());
	}
	public List<List<TimeLineObj>>  getTimelineWithTags(List<TopicIdentifier> shoppingList) {
		List<List<TimeLineObj>> rtn = new ArrayList<List<TimeLineObj>>(shoppingList.size());

		for (TopicIdentifier tag : shoppingList) {
			rtn.add(selectDAO.getTimeline(tag.getTopicID(),userService.getCurrentUser()));
		}		
		return rtn;		
	}
	public List<FullTopicIdentifier> getTopicIdsWithTag(long id) {

		List<TopicTypeConnector> conns = selectDAO.getTopicIdsWithTag(id,userService.getCurrentUser());

		return connectorsToTIs(conns);
				
	}
	private List<FullTopicIdentifier> connectorsToTIs(List<TopicTypeConnector> conns){
		List<FullTopicIdentifier> rtn = new ArrayList<FullTopicIdentifier>(conns.size());
		for (TopicTypeConnector conn : conns) {
			rtn.add(new FullTopicIdentifier(conn));


			log.debug("Topic on island Found "+conn.getId()+" "+conn.getLatitude()+" "+conn.getLongitude());

		}
		return rtn;
	}

	public List<List<FullTopicIdentifier>> getTopicIdsWithTags(List<TopicIdentifier> shoppingList) {

		List<List<FullTopicIdentifier>> rtn = new ArrayList<List<FullTopicIdentifier>>(shoppingList.size());


		for (TopicIdentifier tag : shoppingList) {
			rtn.add(getTopicIdsWithTag(tag.getTopicID()));
		}

		return rtn;
	}
	public List<TopicIdentifier> getTopicsStarting(String match) {

		return selectDAO.getTopicsStarting(userService.getCurrentUser(),match);
	}






	public MindTree getTree(MindTreeOcc occ) {
		return selectDAO.getTree(occ);
	}
	public UserPageBean getUserPageBean(User su) {		
		return selectDAO.getUsageStats(su);
	}

	public LinkAndUser getWebLinkForURLAndUser(String url) {
		User u = userService.getCurrentUser();
		return new LinkAndUser(selectDAO.getWebLinkForURI(url,u),u);		
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
			command.setTopic(i, selectDAO.get(id));
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
		return editDAO.save(link);
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
				Topic sameNamed = (Topic) selectDAO.getForName(topic.getUser(), topic.getTitle());
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


		Set<Occurrence> occs = topic.getOccurenceObjs();
		for(Occurrence o : occs){
			o.setUser(userService.getCurrentUser());
		}





		return editDAO.save(topic);
	}

	private void saveCommand(AbstractCommand command) throws HippoBusinessException {

		List topics = command.getTopics();
		for (Iterator iter = topics.iterator(); iter.hasNext();) {
			Topic topic = (Topic) iter.next();
			save(topic);
		}		
	}
	public void saveTopicLocation(long tagId, long topicId, int lat, int lng) {
		editDAO.saveTopicsLocation(tagId, topicId, lat, lng);
	}
	public MindTree saveTree(MindTree tree) {
		return editDAO.save(tree);
	}
	public void setSelectDAO(SelectDAO selectDAO) {
		this.selectDAO = selectDAO;
	}
	public void setEditDAO(EditDAO editDAO) {
		this.editDAO = editDAO;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	private boolean userIsOverSubscriptionLimit(){
		User u = userService.getCurrentUser();		
		int curTopics = selectDAO.getTopicCount(u);		
		return u.getSubscription().getMaxTopics() < curTopics;
	}
	
	
	public List<TagStat> getTagStats() {
		log.info("getting tag stats "+userService.getCurrentUser().getUsername());
		return selectDAO.getTagStats(userService.getCurrentUser());
	}

	public List<TopicIdentifier> getTagsStarting(String match) {
		return selectDAO.getTagsStarting(userService.getCurrentUser(),match);
	}
	public Topic createNewIfNonExistent(String tagName) throws HippoBusinessException {
		
		Topic cur = getForName(tagName);
		if(cur == null){
			cur = createNew(tagName, new Topic(),new Root());
		}
		return cur;
	}


}
