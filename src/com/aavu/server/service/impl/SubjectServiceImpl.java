package com.aavu.server.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.HippoCountry;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.server.service.SubjectService;
import com.sun.org.apache.xml.internal.utils.DOMBuilder;


public class SubjectServiceImpl implements SubjectService {
	private static final Logger log = Logger.getLogger(SubjectServiceImpl.class);

	private static Map<String, HippoCountry> countries = new HashMap<String, HippoCountry>();

	static{		
		countries.put("Ethiopia",new HippoCountry("COUNTRY#","Ethiopia"));
		countries.put("Chad",new HippoCountry("COUNTRY#Chad","Chad"));
		countries.put("Senegal",new HippoCountry("COUNTRY#Senegal","Senegal"));
		countries.put("Guam",new HippoCountry("COUNTRY#Guam","Guam"));
		countries.put("France",new HippoCountry("COUNTRY#France","France"));
		countries.put("Germany",new HippoCountry("COUNTRY#Germany","Germany"));		
		countries.put("Spain",new HippoCountry("COUNTRY#Spain","Spain"));
		countries.put("England",new HippoCountry("COUNTRY#England","England"));
		countries.put("Ireland",new HippoCountry("COUNTRY#Ireland","Ireland"));
		countries.put("Mexico",new HippoCountry("COUNTRY#Mexico","Mexico"));
		countries.put("India",new HippoCountry("COUNTRY#India","India"));
		countries.put("Russia",new HippoCountry("COUNTRY#Russia","Russia"));
		countries.put("Italy",new HippoCountry("COUNTRY#Italy","Italy"));
		countries.put("Romania",new HippoCountry("COUNTRY#Romania","Romania"));
		countries.put("Egypt",new HippoCountry("COUNTRY#Egypt","Egypt"));
		countries.put("Israel",new HippoCountry("COUNTRY#Israel","Israel"));
		countries.put("Iraq",new HippoCountry("COUNTRY#Iraq","Iraq"));
	}



	public List<? extends Subject> lookup(Class type, String matchString) {
		log.debug("type: "+type+" match "+matchString);

		List<Subject> rtn = new ArrayList<Subject>();

		
		if(type == HippoCountry.class){
			return countryLookup(matchString);
		}else if(type == AmazonBook.class){
			return amazonLookup("Books",matchString);	
		}
		
		

		return rtn;
	}

	private List<Subject> countryLookup(String matchString){

		List<Subject> rtn = new ArrayList<Subject>();

		HippoCountry count = countries.get(matchString);

		log.debug("country to rtn: "+count);

		if(count != null){
			rtn.add(count);	
		}
		
		return rtn;
	}
	
	private List<AmazonBook> amazonLookup(String type, String matchText){

		List<AmazonBook> rtn = new ArrayList<AmazonBook>();

		
		String url =	
			"http://ecs.amazonaws.com/onca/xml?Service=AWSECommerceService&Version=2005-03-23"+
			"&Operation=ItemSearch"+
			"&ContentType=text%2Fxml"+
			"&SubscriptionId=16HF7GQG686H29K1Y702"+
			"&ResponseGroup=Small&SearchIndex=";

		String MID = "&Title=";

		

		

		URL amazonRESTService;
		try {
			StringBuffer sb = new StringBuffer(url);
			
			sb.append(URLEncoder.encode(type,"UTF-8"));		
			sb.append(MID);
			sb.append(URLEncoder.encode(matchText,"UTF-8"));
		
			log.debug("AmazonRESTLink: "+sb.toString());
			
			
			amazonRESTService = new URL(sb.toString());
			//amazonRESTService = new URL(amazonRESTService.toURI().toASCIIString());
			
			URLConnection yahooConnection = amazonRESTService.openConnection();

			InputStream in = yahooConnection.getInputStream();

			SAXReader reader = new SAXReader();
			Document response = reader.read(in);
			in.close();

			System.out.println("Response "+response.toString());
			
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
				
				
				
				log.debug("Found Book: "+book);
								
				rtn.add(book);
			}
			//Items.Request.Errors.Error.Message


		} catch (MalformedURLException e) {
			log.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		} catch (DocumentException e) {
			log.error(e);
			e.printStackTrace();
		} 

		return rtn;
	}
}
