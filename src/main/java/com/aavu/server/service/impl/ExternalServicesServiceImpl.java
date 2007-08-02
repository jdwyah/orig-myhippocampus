package com.aavu.server.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Required;

import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.HippoCountry;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.domain.subjects.WikiSubject;
import com.aavu.client.exception.HippoException;
import com.aavu.server.domain.RestParam;
import com.aavu.server.service.ExternalServicesService;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;


public class ExternalServicesServiceImpl extends AbstractRestService implements
		ExternalServicesService {

	private static final Logger log = Logger.getLogger(ExternalServicesServiceImpl.class);

	private UserService userService;
	private TopicService topicService;

	private String restUserAgent;

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



}
