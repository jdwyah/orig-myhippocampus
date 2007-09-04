package com.aavu.server.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.exception.HippoException;
import com.aavu.server.domain.DeliciousBundle;
import com.aavu.server.domain.DeliciousPost;
import com.aavu.server.service.DeliciousService;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;
import com.aavu.server.service.gwt.BaseTestNoTransaction;


public class DeliciousServiceImplTest extends BaseTestNoTransaction {

	private DeliciousService deliciousService;

	private TopicService topicService;

	private UserService userService;


	@Override
	protected String getUsername() {
		return "foooo";
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public void setDeliciousService(DeliciousService deliciousService) {
		this.deliciousService = deliciousService;
	}


	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}


	public void testDeliciousReq() throws IOException, DocumentException, HippoException,
			ParseException {


		// List<DeliciousPost> posts = deliciousService.deliciousReq("jdwyah", "internet.com");
		//
		// for (DeliciousPost post : posts) {
		// System.out.println("post " + post.getDescription() + " " + post.getExtended() + " "
		// + post.getHref() + " " + post.getTag_string() + " " + post.getDate());
		// }



	}

	public void testDeliciousFromFile() throws IOException, DocumentException, HippoException,
			ParseException {

		String path = "com/aavu/server/service/jeff_aFew.xml";

		Document doc = getDoc(path);


		List<DeliciousPost> posts = deliciousService.getPostsFromXML(doc);


		for (DeliciousPost post : posts) {
			System.out.println("post " + post.getDescription() + " " + post.getExtended() + " "
					+ post.getHref() + " " + post.getTag_string() + " " + post.getDate());
		}

		path = "com/aavu/server/service/jeffBundles.xml";
		Document bundleDoc = getDoc(path);

		List<DeliciousBundle> bundles = deliciousService.getBundlesFromXML(bundleDoc);


		Topic deliciousRoot = topicService
				.createNewIfNonExistent(DeliciousServiceImpl.DELICIOUS_STR);
		deliciousService.addBundles(deliciousRoot, bundles);

	}

	public void testDeliciousCreateFromFile() throws IOException, DocumentException,
			HippoException, ParseException {


		String path = "com/aavu/server/service/jeff_bundle.xml";
		Document bundleDoc = getDoc(path);

		List<DeliciousBundle> bundles = deliciousService.getBundlesFromXML(bundleDoc);


		Topic deliciousRoot = topicService
				.createNewIfNonExistent(DeliciousServiceImpl.DELICIOUS_STR);
		deliciousService.addBundles(deliciousRoot, bundles);



		path = "com/aavu/server/service/jeff_aFew.xml";
		// path = "com/aavu/server/service/jeff_all_huge.xml"; //~2500 posts

		Document doc = getDoc(path);


		List<DeliciousPost> posts = deliciousService.getPostsFromXML(doc);


		for (DeliciousPost post : posts) {
			System.out.println("post " + post.getDescription() + " " + post.getExtended() + " "
					+ post.getHref() + " " + post.getTag_string() + " " + post.getDate());
		}

		deliciousService.addDeliciousTags(posts, deliciousRoot);


		Topic delicious = topicService
				.getForNameCaseInsensitive(DeliciousServiceImpl.DELICIOUS_STR);

		assertEquals(128, delicious.getInstances().size());


	}


	private Document getDoc(String path) throws DocumentException, IOException {
		File f = new File(path);
		System.out.println(f.getAbsolutePath());

		InputStream in = ClassLoader.getSystemResourceAsStream(path);

		// FileReader in = new FileReader(path);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(in);
		in.close();
		return doc;
	}

	public void testDeliciousNewLinksForUser() throws IOException, DocumentException,
			HippoException {

		User u = userService.getCurrentUser();


		deliciousService.newLinksForUser("jdwyah", "internet.com");

		Root r = topicService.getRootTopic(u);

		StringBuffer sb = new StringBuffer();
		outP(r, "", sb);

		System.out.println("sb " + sb);



		assertEquals(1, r.getInstances().size());

		assertTrue(topicService.getAllTopicIdentifiers().size() > 40);

	}

	/**
	 * Simple test to make sure our debug outPrinter is working
	 */
	public void testOut() {
		User u = userService.getCurrentUser();

		Topic t = new Root(u);


		RealTopic t2 = new RealTopic(u, "A");
		t2.addOccurence(new WebLink(u, "foo", "foo", "foo"));
		t2.addOccurence(new WebLink(u, "foo2", "foo2", "foo2"));
		t2.tagTopic(t);


		RealTopic tt2 = new RealTopic(u, "B");
		tt2.addOccurence(new WebLink(u, "bar", "bar", "bar"));
		tt2.tagTopic(t2);

		StringBuffer sb = new StringBuffer();
		outP(t, "", sb);

		System.out.println("|" + sb);


	}

	private void outP(Topic t, String prep, StringBuffer sb) {
		for (Iterator iterator4 = t.getOccurenceObjs().iterator(); iterator4.hasNext();) {
			Occurrence occ = (Occurrence) iterator4.next();

			sb.append(prep);
			sb.append("->");
			sb.append(occ);
			sb.append(occ);
			sb.append("\n");
		}
		for (Iterator iterator = t.getInstances().iterator(); iterator.hasNext();) {
			TopicTypeConnector t2 = (TopicTypeConnector) iterator.next();
			sb.append(prep);
			sb.append("::");
			sb.append(t2.getTopic());
			sb.append("\n");
			outP(t2.getTopic(), prep + "         ", sb);
		}

	}
}
