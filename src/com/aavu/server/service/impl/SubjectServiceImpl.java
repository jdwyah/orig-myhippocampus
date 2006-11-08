package com.aavu.server.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.HippoCountry;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.domain.subjects.WikiSubject;
import com.aavu.client.exception.HippoException;
import com.aavu.server.service.SubjectService;


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



	public List<? extends Subject> lookup(Class type, String matchString) throws HippoException {
		log.debug("type: "+type+" match "+matchString);

		List<Subject> rtn = new ArrayList<Subject>();

		try{
			if(type == HippoCountry.class){
				return countryLookup(matchString);
			}else if(type == AmazonBook.class){
				return amazonLookup("Books",matchString);
			}else if(type == WikiSubject.class){
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

	private List<Subject> countryLookup(String matchString){

		List<Subject> rtn = new ArrayList<Subject>();

		HippoCountry count = countries.get(matchString);

		log.debug("country to rtn: "+count);

		if(count != null){
			rtn.add(count);	
		}

		return rtn;
	}

	private List<AmazonBook> amazonLookup(String type, String matchText) throws IOException, DocumentException{

		List<AmazonBook> rtn = new ArrayList<AmazonBook>();


		String url =	
			"http://ecs.amazonaws.com/onca/xml?Service=AWSECommerceService&Version=2005-03-23"+
			"&Operation=ItemSearch"+
			"&ContentType=text%2Fxml"+
			"&SubscriptionId=16HF7GQG686H29K1Y702"+
			"&ResponseGroup=Small";
		Vector<RestParam> params = new Vector<RestParam>();
		params.add(new RestParam("SearchIndex",type));
		params.add(new RestParam("Title",matchText));


		Document response = xmlRESTReq(url, params);

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



		return rtn;
	}

	/**
	 * 
	 * <Title>Madonna - Wikipedia, the free encyclopedia</Title>
	 * <Summary>
		Madonna (entertainer), an American pop
	   </Summary>
	   <Url>http://en.wikipedia.org/wiki/Madonna</Url>
	   <DisplayUrl>en.wikipedia.org/wiki/Madonna</DisplayUrl>
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public List<Subject> wikiLookup(String matchText) throws IOException, DocumentException{

		List<Subject> rtn = new ArrayList<Subject>();
		
		String baseURL = "http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=MyHippocampus";

		Vector<RestParam> params = new Vector<RestParam>();
		params.add(new RestParam("query",matchText));
		params.add(new RestParam("site","wikipedia.org"));

		Document doc = xmlRESTReq(baseURL, params);
		
		Element root = doc.getRootElement();		
		
		System.out.println("root" +root);
		List<Element> itemL = root.elements("Result");
		System.out.println("itemL"+itemL);
		
		for (Element element : itemL) {
			
			WikiSubject wiki = new WikiSubject(
					element.elementText("Title"),
					element.elementText("Url"),
					element.elementText("Summary"),
					element.elementText("DisplayUrl"));
			wiki.setForeignID(element.elementText("Url"));
			wiki.setName(element.elementText("DisplayUrl"));

			//book.setAuthor(element.elementText("Summary"));
			//book.setDetailURL(element.elementText("Url"));


			log.debug("Found Wiki: "+wiki);

			rtn.add(wiki);
		}
		
		

		return rtn;
	}
	/*
	 * <posts update="2006-11-08T14:38:11Z" user="jdwyah">
		<post href="http://beta.contactoffice.com/" description="Beta ContactOffice NUI" hash="c5cb22b7753489a15c924f04102c4b07" tag="gwt Web2.0" time="2006-11-07T14:06:47Z"/>
	   </posts>
	 * 
	 */
	public Document deliciousReq() throws IOException, DocumentException{
		String baseURL ="https://api.del.icio.us/v1/posts/all?";
		Vector<RestParam> params = new Vector<RestParam>();		
		

		Document doc = xmlRESTReq(baseURL, params,"jdwyah","internet.com");
		Element root = doc.getRootElement();		
		
		System.out.println("root" +root);
		
		List<Element> postList = root.elements("post");
		System.out.println("itemL"+postList);
		
		for (Element post : postList) {
			
			System.out.println("Post "+post.attributeValue("description")+" "+post.attributeValue("href")+" "+post.attributeValue("tag")+" ");
								
		}
		
		return doc;
	}
	
	
	private class RestParam {
		private String name;
		private String val;
		public RestParam(String name, String val) {
			super();
			this.name = name;
			this.val = val;
		}
		public void appendMe(StringBuilder sb) throws UnsupportedEncodingException{
			sb.append("&");
			sb.append(name);
			sb.append("=");
			sb.append(URLEncoder.encode(val,"UTF-8"));		
		}
	}


	
	private Document xmlRESTReq(String baseURL,Vector<RestParam> params) throws IOException, DocumentException{
		return xmlRESTReq(baseURL, params,null,null);
	}	
	private Document xmlRESTReq(String baseURL,Vector<RestParam> params,String username, String password) throws IOException, DocumentException{
		StringBuilder url = new StringBuilder(baseURL);

		for (RestParam p : params) {
			p.appendMe(url);
		}

		URL theRESTService;

		log.debug("RESTLink: "+url.toString());

		theRESTService = new URL(url.toString());
		//amazonRESTService = new URL(amazonRESTService.toURI().toASCIIString());

		URLConnection restConn = theRESTService.openConnection();

		//do auth
		if(username != null){
			String authString = username + ":" + password;
			String auth = "Basic " + new sun.misc.BASE64Encoder().encode(authString.getBytes());
			restConn.setRequestProperty  ("Authorization", auth);		        
		}		  
		
		InputStream in = restConn.getInputStream();

		SAXReader reader = new SAXReader();
		Document response = reader.read(in);
		in.close();

		return response;
	}

}
