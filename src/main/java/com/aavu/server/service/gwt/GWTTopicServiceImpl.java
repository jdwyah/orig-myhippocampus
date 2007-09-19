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

import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.LinkAndUser;
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

		try {
			log.debug("Scan turned up persistent: " + Converter.scan(t));
		} catch (Exception e) {
			log.error("Scanning error " + e);
			e.printStackTrace();
		}

		return t;
	}

	/**
	 * 1) Over-write dates 2) Null out the lazy loaded MindTree for MindTreeOcc's
	 * 
	 * @param in
	 * @return
	 */
	public static Set converterOccurenceSet(Set in) {
		HashSet<Occurrence> rtn = new HashSet<Occurrence>();
		try {
			for (Iterator iter = in.iterator(); iter.hasNext();) {
				Occurrence top = (Occurrence) iter.next();
				if (top.getLastUpdated() != null) {
					top.setLastUpdated(new Date(top.getLastUpdated().getTime()));
				}
				if (top.getCreated() != null) {
					top.setCreated(new Date(top.getCreated().getTime()));
				}
				if (top instanceof MindTreeOcc) {
					MindTreeOcc mto = (MindTreeOcc) top;
					mto.setMindTree(null);
				}
				rtn.add(top);
			}
		} catch (LazyInitializationException ex) {
			log.warn("caught lazy in convertOccurrence");
		}
		return rtn;
	}

	private TopicService topicService;
	private SearchService searchService;

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
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Topic topic = (Topic) iterator.next();
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
				dateCreated);

		return prototype.getIdentifier();
	}

	public TopicIdentifier createNewIfNonExistent(String title) throws HippoBusinessException {

		Topic prototype = topicService.createNewIfNonExistent(title);

		return prototype.getIdentifier();
	}

	public TopicIdentifier createNewIfNonExistent(String title, Topic prototype, Topic parent,
			int[] lnglat, Date dateCreated) throws HippoBusinessException {

		prototype = topicService.createNewIfNonExistent(title, prototype.getClass(), parent,
				lnglat, dateCreated);

		return prototype.getIdentifier();
	}



	// private Map converter(Map metaValues) {
	// return converter(metaValues,false);
	// }
	// private static Map converter(Map metaValues, boolean b) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public void delete(long id) throws HippoException {
		topicService.delete(id);
	}

	public void editVisibility(List topics, boolean visible) throws HippoException {

		try {
			topicService.editVisibility(topics, visible);
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e);
		}
	}

	public List getAllLocations() throws HippoException {
		try {
			return topicService.getAllLocations();
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e);
		}
	}

	public List getAllMetas() throws HippoException {
		return topicService.getAllMetas();
	}

	/**
	 * 
	 * 
	 * @throws HippoException
	 * 
	 */
	public List getAllTopicIdentifiers(int start, int max, String startStr) throws HippoException {
		try {
			return topicService.getAllTopicIdentifiers(start, max, startStr);
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e);
		}
	}

	public List getLinksTo(Topic topic) throws HippoException {
		try {
			return topicService.getLinksTo(topic);
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}

	public List getLocationsForTags(List shoppingList) throws HippoException {
		try {
			return topicService.getLocationsForTags(shoppingList);
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e);
		}
	}

	public TagStat[] getTagStats() throws HippoException {
		try {
			List<TagStat> stats = topicService.getTagStats();
			TagStat[] rtn = new TagStat[stats.size()];
			return stats.toArray(rtn);
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e);
		}
	}

	public List<TimeLineObj> getTimeline() throws HippoException {
		try {
			return topicService.getTimeline();
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}

	// public Topic[] save(Topic[] topics) throws HippoException {
	// try {
	// List<Topic> list = topicService.save(topics);
	// Topic[] rtn = new Topic[list.size()];
	// int i = 0;
	// for (Topic topic : list) {
	// log.debug("Converting "+topic.getId());
	// rtn[i++] = convert(topic);
	// }
	// log.debug("Save[] rtn "+Arrays.toString(rtn));
	// return rtn;
	// } catch (HippoException ex) {
	// throw ex;
	// } catch (Exception e) {
	// log.error("FAILURE: "+e);
	// e.printStackTrace();
	// throw new HippoException(e);
	// }
	// }

	public List<List<TimeLineObj>> getTimelineWithTags(List shoppingList) throws HippoException {
		try {
			return topicService.getTimelineWithTags(shoppingList);
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}

	public Topic getTopicByID(long topicID) throws HippoException {
		try {

			return convert(topicService.getForID(topicID));

		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e);
		}
	}

	public Topic getTopicForName(String topicName) {
		try {
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

		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @throws HippoException
	 * 
	 */
	public List getTopicIdsWithTag(long id) throws HippoException {
		try {

			return topicService.getTopicIdsWithTag(id);

		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e);
		}
	}

	public Root getRootTopic(User forUser) throws HippoException {
		try {

			return topicService.getRootTopic(forUser);

		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e);
		}
	}

	public List getTopicsWithTags(List shoppingList) throws HippoException {
		return topicService.getTopicIdsWithTags(shoppingList);
	}

	public MindTree getTree(MindTreeOcc occ) throws HippoException {
		try {
			return convertTree(topicService.getTree(occ));
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}

	public LinkAndUser getWebLinkForURLAndUser(String url) throws HippoException {
		return topicService.getWebLinkForURLAndUser(url);
	}

	public List match(String match) {

		try {

			List l = topicService.getTopicsStarting(match);
			log.debug("match " + match + " " + l.size());
			return l;

		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			return null;
		}
	}

	public void saveCommand(AbstractCommand command) throws HippoException {
		log.info("command " + command.getClass() + " " + command);
		topicService.executeAndSaveCommand(command);
	}

	public void saveTopicLocation(long tagId, long topicId, int lat, int lng) throws HippoException {
		topicService.saveTopicLocation(tagId, topicId, lat, lng);
	}

	public void saveOccurrenceLocation(long topicID, long occurrenceID, int lat, int lng)
			throws HippoException {
		topicService.saveOccurrenceLocation(topicID, occurrenceID, lat, lng);
	}

	public MindTree saveTree(MindTree tree) throws HippoException {
		try {
			return convertTree(topicService.saveTree(tree));
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}

	public List search(String searchString) throws HippoException {
		try {
			return searchService.search(searchString);
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}

	@Required
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	@Required
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	/**
	 * had a problem with CGLIB if we return topics
	 */
	public List getDeleteList(long id) throws HippoException {
		try {
			return convertTopicToTIArray(topicService.getDeleteList(id));
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}

	/**
	 * had a problem with CGLIB if we return topics
	 */
	public List getMakePublicList(long id) throws HippoException {
		try {
			return convertTopicToTIArray(topicService.getMakePublicList(id));
		} catch (Exception e) {
			log.error("FAILURE: " + e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}
	// public C test(C c) {
	// return c;
	// }


}
