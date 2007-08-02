package com.aavu.server.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Required;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.server.domain.DeliciousBundle;
import com.aavu.server.domain.DeliciousPost;
import com.aavu.server.domain.RestParam;
import com.aavu.server.service.DeliciousService;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;


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


	private static final String delicousApiUrlAll = "https://api.del.icio.us/v1/posts/all?";
	private static final String delicousApiBundleGet = "https://api.del.icio.us/v1/tags/bundles/all?";
	private static final String delicousApiUrlGet = "https://api.del.icio.us/v1/posts/get?";
	static final String DELICIOUS_STR = "Del.icio.us Links";

	private TopicService topicService;
	private UserService userService;

	public DeliciousServiceImpl(String userAgent, String authURL) {
		super(userAgent, authURL);
	}


	public void addDeliciousTags(List<DeliciousPost> posts, Topic parent) throws HippoException {

		log.info("Found " + posts.size() + " Posts.");

		for (DeliciousPost post : posts) {
			WebLink ww = new WebLink(userService.getCurrentUser(), post.getDescription(), post
					.getHref(), post.getExtended());
			String[] tags = post.getTags();

			topicService.addLinkToTags(ww, tags, parent);
		}

	}

	public void newLinksForUser(String username, String password) throws HippoException {

		log.info("user: " + username);

		Topic deliciousRoot = topicService.createNewIfNonExistent(DELICIOUS_STR);

		List<DeliciousBundle> bundles = getBundles(username, password);

		log.info("Found " + bundles.size() + " Bundles.");



		addBundles(deliciousRoot, bundles);


		List<DeliciousPost> posts = getAllPosts(username, password);

		addDeliciousTags(posts, deliciousRoot);

		userService.setDeliciousUpdate();

	}


	public void addBundles(Topic deliciousRoot, List<DeliciousBundle> bundles)
			throws HippoBusinessException {
		for (DeliciousBundle deliciousBundle : bundles) {

			Topic bundle = topicService.createNewIfNonExistent(deliciousBundle.getName(),
					deliciousRoot);

			log.info("New Bundle: " + bundle);

			for (String bundleTag : deliciousBundle.getTags()) {

				Topic tag = topicService.createNewIfNonExistent(bundleTag, bundle);

				tag.addType(bundle);

				topicService.save(tag);

				log.debug("with tag: " + tag);

			}

			log.debug("New Bundle: " + bundle + " tags: " + bundle.getInstances().size());

			topicService.save(bundle);
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
	public List<DeliciousPost> getAllPosts(String username, String password) throws HippoException {


		Document doc;
		try {
			doc = xmlRESTReq(delicousApiUrlAll, new Vector<RestParam>(), username, password);
		} catch (Exception e) {
			log.error(e);
			throw new HippoException(e);
		}

		return getPostsFromXML(doc);
	}

	public List<DeliciousPost> getPostsFromXML(Document doc) {

		Element root = doc.getRootElement();
		List<DeliciousPost> posts = new LinkedList<DeliciousPost>();
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
