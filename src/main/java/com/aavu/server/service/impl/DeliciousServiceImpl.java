package com.aavu.server.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TimeZone;
import java.util.Vector;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AddToTopicCommand;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.server.domain.DeliciousBundle;
import com.aavu.server.domain.DeliciousPost;
import com.aavu.server.domain.RestParam;
import com.aavu.server.service.DeliciousService;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;

/**
 * TODO currently NOT transactional. was leading to HeapSpace errors
 * 
 * @author Jeff Dwyer
 * 
 */
public class DeliciousServiceImpl extends AbstractRestService implements DeliciousService {


	/**
	 * With a litle help from http://sourceforge.net/projects/delicious-java/
	 * 
	 */
	public static final String DELICIOUS_DATE_FORMAT = "yyyy-MM-dd";
	private static final Logger log = Logger.getLogger(DeliciousServiceImpl.class);
	public static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/**
	 * UTC format <p/> SimpleDateFormats are not threadsafe, but we should not need more than one
	 * per thread.
	 */
	private static final ThreadLocal UTC_DATE_FORMAT_OBJECT = new ThreadLocal() {
		protected Object initialValue() {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(UTC_DATE_FORMAT);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			return simpleDateFormat;
		}
	};

	/**
	 * del.icio.us date format (yyyy-MM-dd) SimpleDateFormats are not threadsafe, but we should not
	 * need more than one per thread.
	 */
	private static final ThreadLocal DELICIOUS_DATE_FORMAT_OBJECT = new ThreadLocal() {
		protected Object initialValue() {
			return new SimpleDateFormat(DELICIOUS_DATE_FORMAT);
		}
	};


	public static Date getDateFromDeliciousString(String time) {
		Date result = null;

		try {
			((SimpleDateFormat) UTC_DATE_FORMAT_OBJECT.get()).setCalendar(Calendar
					.getInstance(TimeZone.getTimeZone("UTC")));
			result = ((SimpleDateFormat) UTC_DATE_FORMAT_OBJECT.get()).parse(time);
		} catch (ParseException e) {
		}

		return result;
	}

	public static String getDeliciousDate(Date date) {
		return ((SimpleDateFormat) DELICIOUS_DATE_FORMAT_OBJECT.get()).format(date);
	}


	private static final String delicousApiUrlAll = "https://api.del.icio.us/v1/posts/all?";
	private static final String delicousApiBundleGet = "https://api.del.icio.us/v1/tags/bundles/all?";
	private static final String delicousApiUrlGet = "https://api.del.icio.us/v1/posts/get?";
	public static final String DELICIOUS_STR = "Del.icio.us Links";
	private static final int BATCH_SIZE = 200;

	private TopicService topicService;
	private UserService userService;

	public DeliciousServiceImpl(String userAgent, String authURL, int waitBetweenReq) {
		super(userAgent, authURL, waitBetweenReq);
	}

	/**
	 * NOT transactional. If it is, we end up grinding to a halt for big lists > ~300 Instead, run
	 * addBatch as a little transactional task.
	 */
	public void addDeliciousTags(Queue<DeliciousPost> posts, Topic parent) throws HippoException {

		log.info("Found " + posts.size() + " Posts.");

		Date start = new Date();


		Map<String, Topic> cache = new HashMap<String, Topic>();

		while (!posts.isEmpty()) {
			addBatch(posts, BATCH_SIZE, parent, cache);
		}

		// original 17 posts 2 sec
		// original 217 posts 64 sec
		// w/ user and not dupe check 217 posts 38 sec
		// w/ user, !dupe, cache 217 posts 24 sec
		// w/ userServiceCache, !dupe, cache 217 posts 8 sec
		// w/ userServiceCache, !dupe, cache & createNewIfURINonexistant 217 posts 25 sec

		Date finish = new Date();
		long duration = finish.getTime() - start.getTime();
		log.info("Add DeliciousTags in " + (duration / 1000) + " sec.");

	}

	@Transactional
	private void addBatch(Queue<DeliciousPost> posts, int numToDo, Topic parent,
			Map<String, Topic> cache) throws HippoBusinessException {

		DeliciousPost post = null;

		int i = 0;
		Date start = new Date();

		while (i < numToDo && null != (post = posts.poll())) {

			WebLink ww = topicService.createNewIfURINonexistant(WebLink.class, post.getHref(), post
					.getDescription(), post.getDate(), post.getExtended());

			String[] tags = post.getTags();

			topicService.addLinkToTags(ww, tags, parent, cache);

			log.debug("Added " + ww);
			i++;
		}

		Date finish = new Date();
		long duration = finish.getTime() - start.getTime();
		log.info("Add Batch size " + i + " " + (duration / 1000) + " sec. Cache " + cache.size());
	}

	/**
	 * NOTE: this will return before tags are actually added.
	 * 
	 * adding the tags in another thread, because it's such a slow op. Not really ideal though,
	 * because we can't return any error messages..
	 */
	public int newLinksForUser(String username, String password) throws HippoException {

		log.info("user: " + username);

		final Topic deliciousRoot = topicService.createNewIfNonExistent(DELICIOUS_STR);

		List<DeliciousBundle> bundles = getBundles(username, password);

		log.info("Found " + bundles.size() + " Bundles.");


		addBundles(deliciousRoot, bundles);

		User u = userService.getCurrentUser();

		Queue<DeliciousPost> posts = null;



		if (null == u.getLastDeliciousDate() || u.getLastDeliciousDate().getYear() == -900) {
			log.info("GetAllPosts Last del.icio.us date: " + u.getLastDeliciousDate());
			posts = getAllPosts(username, password);
		} else {
			log.info("GetRecentPosts Last del.icio.us date: " + u.getLastDeliciousDate());
			posts = getPostFromPosts(username, password, u.getLastDeliciousDate());
		}

		log.info("Found " + posts.size() + " Posts.");



		final Queue<DeliciousPost> postsf = posts;


		final Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		// Copy the authentication to the new thread
		Thread addTagThread = new Thread() {
			public void run() {
				try {
					SecurityContextHolder.getContext().setAuthentication(authentication);
					log.info("Starting Add Tags Thread for " + postsf.size() + " posts.");

					addDeliciousTags(postsf, deliciousRoot);

				} catch (HippoException e) {
					log.error("addDeliciousTags thread " + e);
				}
				log.info("Delicious Add Complete");
				userService.setDeliciousUpdate();
			}
		};
		addTagThread.setPriority(Thread.MIN_PRIORITY);
		addTagThread.start();

		log.info("Returning to caller.");

		return posts.size();

	}


	public void addBundles(Topic deliciousRoot, List<DeliciousBundle> bundles)
			throws HippoException {
		for (DeliciousBundle deliciousBundle : bundles) {

			Topic bundle = topicService.createNewIfNonExistent(deliciousBundle.getName(),
					deliciousRoot);

			log.info("New Bundle: " + bundle);

			for (String tagString : deliciousBundle.getTags()) {

				Topic tag = topicService.createNewIfNonExistent(tagString, bundle);

				topicService.executeAndSaveCommand(new AddToTopicCommand(tag, bundle));

				log.debug("with tag: " + tag);

			}

			log.debug("New Bundle: " + bundle + " tags: " + bundle.getInstances().size());

		}
	}

	/**
	 * sample return: <bundles> <bundle name="music" tags="ipod mp3 music" /> </bundles>
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws HippoException
	 */
	public List<DeliciousBundle> getBundles(String username, String password) throws HippoException {


		Document doc;
		try {
			doc = xmlRESTReq(delicousApiBundleGet, new Vector<RestParam>(), username, password);
		} catch (Exception e) {
			log.error(e);
			throw new HippoException(e);
		}

		return getBundlesFromXML(doc);
	}

	/*
	 * <posts update="2006-11-08T14:38:11Z" user="jdwyah"> <post
	 * href="http://beta.contactoffice.com/" description="Beta ContactOffice NUI"
	 * hash="c5cb22b7753489a15c924f04102c4b07" tag="gwt Web2.0" time="2006-11-07T14:06:47Z"/>
	 * </posts>
	 * 
	 */
	public Queue<DeliciousPost> getAllPosts(String username, String password) throws HippoException {


		Document doc;
		try {
			doc = xmlRESTReq(delicousApiUrlAll, new Vector<RestParam>(), username, password);
		} catch (Exception e) {
			log.error(e);
			throw new HippoException(e);
		}

		return getPostsFromXML(doc);
	}

	public Queue<DeliciousPost> getPostFromPosts(String username, String password, Date lastUpdate)
			throws HippoException {


		Document doc;
		try {
			Vector<RestParam> params = new Vector<RestParam>();
			params.add(new RestParam("dt", getDeliciousDate(lastUpdate)));

			doc = xmlRESTReq(delicousApiUrlGet, new Vector<RestParam>(), username, password);
		} catch (Exception e) {
			log.error(e);
			throw new HippoException(e);
		}

		return getPostsFromXML(doc);
	}

	public Queue<DeliciousPost> getPostsFromXML(Document doc) {

		Element root = doc.getRootElement();
		Queue<DeliciousPost> posts = new LinkedList<DeliciousPost>();
		List<Element> postList = root.elements("post");

		// log.debug("itemL" + postList);

		for (Element post : postList) {
			Date date = getDateFromDeliciousString(post.attributeValue("time"));
			posts.add(new DeliciousPost(post.attributeValue("description"), post
					.attributeValue("href"), post.attributeValue("tag"), post
					.attributeValue("extended"), date));
		}

		return posts;
	}

	public List<DeliciousBundle> getBundlesFromXML(Document doc) {

		Element root = doc.getRootElement();
		List<DeliciousBundle> posts = new LinkedList<DeliciousBundle>();
		List<Element> postList = root.elements("bundle");
		// log.debug("itemL" + postList);

		for (Element post : postList) {
			posts
					.add(new DeliciousBundle(post.attributeValue("name"), post
							.attributeValue("tags")));
		}

		return posts;
	}

	@Required
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
