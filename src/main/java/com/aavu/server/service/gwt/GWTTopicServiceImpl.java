package com.aavu.server.service.gwt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Required;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.LinkAndUser;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.SearchResult;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.client.service.remote.GWTTopicService;
import com.aavu.server.service.SearchService;
import com.aavu.server.service.TopicService;
import com.aavu.server.util.gwt.GWTSpringControllerReplacement;

public class GWTTopicServiceImpl extends GWTSpringControllerReplacement implements GWTTopicService {

	private static final Logger log = Logger.getLogger(GWTTopicServiceImpl.class);

	/**
	 * Convert a topic to GWT serializable form.
	 * 
	 * This means; 1) Change org.hibernate.collection.PersistentList to java.util.ArrayList -because
	 * GWT can't handle 2) Initialize an "proxy" objects. We can't have any lazy references! Even if
	 * we won't peek on the client. We can't transfer these it seems. Google's AuthSub is a nice
	 * solution to some of these issues. I found it easy to authorize and import Google Docs without
	 * seeing a user's credentials or resorting to the 'benevolent phishing' maneuver.
	 * 
	 * http://code.google.com/apis/accounts/AuthForWebApps.html
	 * 
	 * @param t
	 * @return
	 */
	public static Topic convert(Topic t) {
		if (t != null) {
			log.debug("New Converter " + t.getId() + " " + t.getTitle());
		}

		// NewConverter.convertInPlace(t);


		log.debug("Scan turned up persistent: " + Converter.scan(t));


		return t;
	}



	private SearchService searchService;
	private TopicService topicService;

	public void changeState(long topicID, boolean toIsland) throws HippoException {
		topicService.changeState(topicID, toIsland);
	}

	private Set convertSetSimple(Set in) {
		HashSet rtn = new HashSet();
		try {
			for (Iterator iter = in.iterator(); iter.hasNext();) {
				rtn.add(iter.next());
			}
		} catch (LazyInitializationException ex) {
			log.debug("caught lazy ");
		}
		return rtn;
	}

	private Topic[] convertToArray(List<Topic> list) {

		log.debug("ConvertToArray");

		Topic[] rtn = new Topic[list.size()];

		for (int i = 0; i < list.size(); i++) {
			Topic t = list.get(i);

			// t.setUser(null);
			rtn[i] = convert(t);
		}

		return rtn;
	}

	private TopicIdentifier[] convertToArray(List<TopicIdentifier> list) {

		TopicIdentifier[] rtn = new TopicIdentifier[list.size()];
		for (int i = 0; i < list.size(); i++) {
			TopicIdentifier t = list.get(i);
			rtn[i] = t;
		}
		return rtn;
	}

	private List<TopicIdentifier> convertTopicToTIArray(List<Topic> list) {

		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());
		for (Topic topic : list) {
			rtn.add(topic.getIdentifier());
		}
		return rtn;
	}

	private MindTree convertTree(MindTree tree) {
		tree.setLeftSide(convertSetSimple(tree.getLeftSide()));
		tree.setRightSide(convertSetSimple(tree.getRightSide()));

		// TODO eek... this might be a bug -> bad things...
		tree.setTopic(null);

		return tree;
	}



	public TopicIdentifier createNew(String title, Topic prototype, Topic parent, int[] lnglat,
			Date dateCreated) throws HippoBusinessException {

		prototype = (Topic) topicService.createNew(title, prototype.getClass(), parent, lnglat,
				dateCreated, false);

		return prototype.getIdentifier();
	}

	public TopicIdentifier createNewIfNonExistent(String title) throws HippoBusinessException {

		Topic prototype = topicService.createNewIfNonExistent(title);

		return prototype.getIdentifier();
	}

	public TopicIdentifier createNewIfNonExistent(String title, Topic prototype, Topic parent,
			int[] lnglat, Date dateCreated) throws HippoBusinessException {

		prototype = topicService.createNewIfNonExistent(title, prototype.getClass(), parent,
				lnglat, dateCreated, false);

		return prototype.getIdentifier();
	}



	public void delete(long id) throws HippoException {
		topicService.delete(id);
	}

	public void editVisibility(List<TopicIdentifier> topics, boolean visible) throws HippoException {
		topicService.editVisibility(topics, visible);
	}

	public List<LocationDTO> getAllLocations() throws HippoException {
		return topicService.getAllLocations();
	}

	public List<Meta> getAllMetas() throws HippoException {
		return topicService.getAllMetas();
	}

	/**
	 * 
	 * 
	 * @throws HippoException
	 * 
	 */
	public List<DatedTopicIdentifier> getAllTopicIdentifiers(int start, int max, String startStr)
			throws HippoException {
		return topicService.getAllTopicIdentifiers(start, max, startStr);
	}

	/**
	 * had a problem with CGLIB if we return topics
	 */
	public List<TopicIdentifier> getDeleteList(long id) throws HippoException {
		return convertTopicToTIArray(topicService.getDeleteList(id));
	}

	public List<TopicIdentifier> getLinksTo(Topic topic) throws HippoException {
		return topicService.getLinksTo(topic);
	}

	public List<List<LocationDTO>> getLocationsForTags(List<TopicIdentifier> shoppingList)
			throws HippoException {
		return topicService.getLocationsForTags(shoppingList);
	}

	/**
	 * had a problem with CGLIB if we return topics
	 */
	public List<TopicIdentifier> getMakePublicList(long id) throws HippoException {
		return convertTopicToTIArray(topicService.getMakePublicList(id));
	}

	// public C test(C c) {
	// return c;
	// }


	public Root getRootTopic(User forUser) throws HippoException {

		return topicService.getRootTopic(forUser);


	}

	public TagStat[] getTagStats() throws HippoException {
		List<TagStat> stats = topicService.getTagStats();
		TagStat[] rtn = new TagStat[stats.size()];
		return stats.toArray(rtn);
	}

	public List<TimeLineObj> getTimeline() throws HippoException {
		return topicService.getTimeline();
	}

	public List<List<TimeLineObj>> getTimelineWithTags(List<TopicIdentifier> shoppingList)
			throws HippoException {
		return topicService.getTimelineWithTags(shoppingList);
	}

	public Topic getTopicByID(long topicID) throws HippoException {
		return convert(topicService.getForID(topicID));
	}

	public Topic getTopicForName(String topicName) {

		Topic t = topicService.getForNameCaseInsensitive(topicName);

		if (t != null) {
			log.debug("orig " + t.getId() + " " + t.getTitle());

			// just json the Abstract topic parts
			Topic converted = convert(t);
			log.debug("conv: " + t.getId() + " tit " + t.getTitle());
			return converted;
		} else {
			log.debug("NULL");
		}

		return t;

		// return convert(topicService.getForName(topicName));

	}

	/**
	 * 
	 * @throws HippoException
	 * 
	 */
	public List<FullTopicIdentifier> getTopicIdsWithTag(long id) throws HippoException {

		return topicService.getTopicIdsWithTag(id);

	}

	public List<List<FullTopicIdentifier>> getTopicsWithTags(List<TopicIdentifier> shoppingList)
			throws HippoException {
		return topicService.getTopicIdsWithTags(shoppingList);
	}

	public MindTree getTree(MindTreeOcc occ) throws HippoException {

		return convertTree(topicService.getTree(occ));

	}

	public LinkAndUser getWebLinkForURLAndUser(String url) throws HippoException {
		return topicService.getWebLinkForURLAndUser(url);
	}

	public List<TopicIdentifier> match(String match) {



		List<TopicIdentifier> l = topicService.getTopicsStarting(match);
		log.debug("match " + match + " " + l.size());
		return l;

	}

	public void saveCommand(AbstractCommand command) throws HippoException {
		log.info("command " + command.getClass() + " " + command);
		topicService.executeAndSaveCommand(command);
	}

	public void saveOccurrenceLocation(long topicID, long occurrenceID, int lat, int lng)
			throws HippoException {
		topicService.saveOccurrenceLocation(topicID, occurrenceID, lat, lng);
	}

	public void saveTopicLocation(long tagId, long topicId, int lat, int lng) throws HippoException {
		topicService.saveTopicLocation(tagId, topicId, lat, lng);
	}

	public MindTree saveTree(MindTree tree) throws HippoException {

		return convertTree(topicService.saveTree(tree));

	}

	public List<SearchResult> search(String searchString) throws HippoException {
		return searchService.search(searchString);
	}

	@Required
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	@Required
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}


}
