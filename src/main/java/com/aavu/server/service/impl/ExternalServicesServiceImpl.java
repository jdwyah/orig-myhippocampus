package com.aavu.server.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Required;

import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.HippoCountry;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.domain.subjects.WikiSubject;
import com.aavu.client.exception.HippoException;
import com.aavu.server.domain.DeliciousPost;
import com.aavu.server.service.ExternalServicesService;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;


public class ExternalServicesServiceImpl implements ExternalServicesService {
	private static final Logger log = Logger.getLogger(ExternalServicesServiceImpl.class);

	private UserService userService;
	private TopicService topicService;

	private String restUserAgent;

	private String delicousApiUrlAll;
	private String delicousApiAuthURL;
	private String delicousApiUrlGet;

	private static Map<String, HippoCountry> countries = new HashMap<String, HippoCountry>();

	static {
		countries.put("Ethiopia", new HippoCountry("COUNTRY#", "Ethiopia"));
		countries.put("Chad", new HippoCountry("COUNTRY#Chad", "Chad"));
		countries.put("Senegal", new HippoCountry("COUNTRY#Senegal", "Senegal"));
		countries.put("Guam", new HippoCountry("COUNTRY#Guam", "Guam"));
		countries.put("France", new HippoCountry("COUNTRY#France", "France"));
		countries.put("Germany", new HippoCountry("COUNTRY#Germany", "Germany"));
		countries.put("Spain", new HippoCountry("COUNTRY#Spain", "Spain"));
		countries.put("England", new HippoCountry("COUNTRY#England", "England"));
		countries.put("Ireland", new HippoCountry("COUNTRY#Ireland", "Ireland"));
		countries.put("Mexico", new HippoCountry("COUNTRY#Mexico", "Mexico"));
		countries.put("India", new HippoCountry("COUNTRY#India", "India"));
		countries.put("Russia", new HippoCountry("COUNTRY#Russia", "Russia"));
		countries.put("Italy", new HippoCountry("COUNTRY#Italy", "Italy"));
		countries.put("Romania", new HippoCountry("COUNTRY#Romania", "Romania"));
		countries.put("Egypt", new HippoCountry("COUNTRY#Egypt", "Egypt"));
		countries.put("Israel", new HippoCountry("COUNTRY#Israel", "Israel"));
		countries.put("Iraq", new HippoCountry("COUNTRY#Iraq", "Iraq"));
	}



	public List<? extends Subject> lookup(Class type, String matchString) throws HippoException {
		log.debug("type: " + type + " match " + matchString);

		List<Subject> rtn = new ArrayList<Subject>();

		try {
			if (type == HippoCountry.class) {
				return countryLookup(matchString);
			} else if (type == AmazonBook.class) {
				return amazonLookup("Books", matchString);
			} else if (type == WikiSubject.class) {
				return wikiLookup(matchString);
			}

		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
			throw new HippoException(e);

		} catch (DocumentException e) {
			log.error(e);
			e.printStackTrace();
			throw new HippoException(e);
		}

		return rtn;
	}

	private List<Subject> countryLookup(String matchString) {

		List<Subject> rtn = new ArrayList<Subject>();

		HippoCountry count = countries.get(matchString);

		log.debug("country to rtn: " + count);

		if (count != null) {
			rtn.add(count);
		}

		return rtn;
	}

	private List<AmazonBook> amazonLookup(String type, String matchText) throws IOException,
			DocumentException {

		List<AmazonBook> rtn = new ArrayList<AmazonBook>();


		String url = "http://ecs.amazonaws.com/onca/xml?Service=AWSECommerceService&Version=2005-03-23"
				+ "&Operation=ItemSearch"
				+ "&ContentType=text%2Fxml"
				+ "&SubscriptionId=16HF7GQG686H29K1Y702" + "&ResponseGroup=Small";
		Vector<RestParam> params = new Vector<RestParam>();
		params.add(new RestParam("SearchIndex", type));
		params.add(new RestParam("Title", matchText));


		Document response = xmlRESTReq(url, params);

		System.out.println("Response " + response.toString());

		Element root = response.getRootElement();
		Element items = root.element("Items");
		List<Element> itemL = items.elements("Item");
		for (Element element : itemL) {
			Element attrib = element.element("ItemAttributes");

			AmazonBook book = new AmazonBook();
			book.setForeignID(element.elementText("ASIN"));
			book.setName(attrib.elementText("Title"));
			book.setAuthor(attrib.elementText("Author"));
			book.setDetailURL(element.elementText("DetailPageURL"));
			book.setManufacturer(attrib.elementText("Manufacturer"));



			log.debug("Found Book: " + book);

			rtn.add(book);
		}
		// Items.Request.Errors.Error.Message



		return rtn;
	}

	/**
	 * 
	 * <Title>Madonna - Wikipedia, the free encyclopedia</Title> <Summary> Madonna (entertainer),
	 * an American pop </Summary> <Url>http://en.wikipedia.org/wiki/Madonna</Url>
	 * <DisplayUrl>en.wikipedia.org/wiki/Madonna</DisplayUrl>
	 * 
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public List<Subject> wikiLookup(String matchText) throws IOException, DocumentException {

		List<Subject> rtn = new ArrayList<Subject>();

		String baseURL = "http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=MyHippocampus";

		Vector<RestParam> params = new Vector<RestParam>();
		params.add(new RestParam("query", matchText));
		params.add(new RestParam("site", "wikipedia.org"));

		Document doc = xmlRESTReq(baseURL, params);

		Element root = doc.getRootElement();

		System.out.println("root" + root);
		List<Element> itemL = root.elements("Result");
		System.out.println("itemL" + itemL);

		for (Element element : itemL) {

			WikiSubject wiki = new WikiSubject(element.elementText("Title"), element
					.elementText("Url"), element.elementText("Summary"), element
					.elementText("DisplayUrl"));
			wiki.setForeignID(element.elementText("Url"));
			wiki.setName(element.elementText("DisplayUrl"));

			// book.setAuthor(element.elementText("Summary"));
			// book.setDetailURL(element.elementText("Url"));


			log.debug("Found Wiki: " + wiki);

			rtn.add(wiki);
		}



		return rtn;
	}

	/*
	 * <posts update="2006-11-08T14:38:11Z" user="jdwyah"> <post
	 * href="http://beta.contactoffice.com/" description="Beta ContactOffice NUI"
	 * hash="c5cb22b7753489a15c924f04102c4b07" tag="gwt Web2.0" time="2006-11-07T14:06:47Z"/>
	 * </posts>
	 * 
	 */
	public List<DeliciousPost> deliciousReq(String username, String password) throws HippoException {

		List<DeliciousPost> posts = new LinkedList<DeliciousPost>();

		Document doc;
		try {
			doc = xmlRESTReq(delicousApiUrlGet, new Vector<RestParam>(), username, password);
		} catch (Exception e) {
			log.error(e);
			throw new HippoException(e);
		}
		Element root = doc.getRootElement();

		System.out.println("root" + root);

		List<Element> postList = root.elements("post");
		System.out.println("itemL" + postList);

		for (Element post : postList) {
			Date date = getDateFromDeliciousString(post.attributeValue("time"));
			posts.add(new DeliciousPost(post.attributeValue("description"), post
					.attributeValue("href"), post.attributeValue("tag"), post
					.attributeValue("extended"), date));
		}

		return posts;
	}

	public static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String DELICIOUS_DATE_FORMAT = "yyyy-MM-dd";

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

	public void addDeliciousTags(String username, String password) throws HippoException {

		List<DeliciousPost> posts = deliciousReq(username, password);

		for (DeliciousPost post : posts) {
			WebLink ww = new WebLink(userService.getCurrentUser(), post.getDescription(), post
					.getHref(), post.getExtended());
			String[] tags = post.getTags();

			topicService.addLinkToTags(ww, tags);
		}

	}

	private class RestParam {
		private String name;
		private String val;

		public RestParam(String name, String val) {
			super();
			this.name = name;
			this.val = val;
		}

		public void appendMe(StringBuilder sb) throws UnsupportedEncodingException {
			sb.append("&");
			sb.append(name);
			sb.append("=");
			sb.append(URLEncoder.encode(val, "UTF-8"));
		}
	}



	private Document xmlRESTReq(String baseURL, Vector<RestParam> params) throws IOException,
			DocumentException {
		return xmlRESTReq(baseURL, params, null, null);
	}

	private Document xmlRESTReq(String baseURL, Vector<RestParam> params, String username,
			String password) throws IOException, DocumentException {
		StringBuilder url = new StringBuilder(baseURL);

		for (RestParam p : params) {
			p.appendMe(url);
		}

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();

		// Create a method instance.
		GetMethod method = new GetMethod(url.toString());

		// Provide custom retry handler is necessary
		// method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		// new DefaultHttpMethodRetryHandler(1, false));

		method.getParams().setParameter(HttpMethodParams.USER_AGENT, restUserAgent);
		if (username != null) {

			Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
			client.getState().setCredentials(new AuthScope(delicousApiAuthURL, AuthScope.ANY_PORT),
					defaultcreds);

			System.out.println("REAl CRED");

			client.getParams().setAuthenticationPreemptive(true);
			// client.getState().setCredentials(AuthScope.ANY, defaultcreds);
		}

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}

			// Read the response body.

			InputStream in = method.getResponseBodyAsStream();
			SAXReader reader = new SAXReader();
			Document response = reader.read(in);
			in.close();

			return response;

		} finally {
			// Release the connection.
			method.releaseConnection();
		}


	}

	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Required
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	@Required
	public void setRestUserAgent(String restUserAgent) {
		this.restUserAgent = restUserAgent;
	}

	@Required
	public void setDelicousApiUrlAll(String delicousApiUrlAll) {
		this.delicousApiUrlAll = delicousApiUrlAll;
	}

	@Required
	public void setDelicousApiAuthURL(String delicousApiAuthURL) {
		this.delicousApiAuthURL = delicousApiAuthURL;
	}

	@Required
	public void setDelicousApiUrlGet(String delicousApiUrlGet) {
		this.delicousApiUrlGet = delicousApiUrlGet;
	}



}
