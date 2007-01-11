package com.aavu.server.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassException;
import org.compass.core.CompassHighlightedText;
import org.compass.core.CompassHit;
import org.compass.core.CompassHits;
import org.compass.core.CompassHitsOperations;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryBuilder;
import org.compass.core.CompassQueryFilter;
import org.compass.core.CompassQueryFilterBuilder;
import org.compass.core.CompassSession;
import org.compass.core.CompassTemplate;
import org.compass.core.CompassQueryBuilder.CompassBooleanQueryBuilder;
import org.compass.core.CompassQueryBuilder.CompassQueryStringBuilder;
import org.compass.core.engine.SearchEngineException;
import org.compass.gps.CompassGps;
import org.compass.gps.MirrorDataChangesGpsDevice;
import org.springframework.beans.factory.InitializingBean;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.SearchResult;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.URI;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.service.SearchService;
import com.aavu.server.service.UserService;

public class SearchServiceImpl implements SearchService, InitializingBean {
	private static final Logger log = Logger.getLogger(SearchServiceImpl.class);
	
	private CompassTemplate compassTemplate;
	
	private Compass compass;
	
	private MirrorDataChangesGpsDevice mirrorGPS;
	private CompassGps compassGPS;
	private TopicDAO topicDAO;
	private UserService userService;
	
	public void setMirrorGPS(MirrorDataChangesGpsDevice mirrorGPS) {
		this.mirrorGPS = mirrorGPS;
	}
	public void setCompass(Compass compass) {
		this.compass = compass;
	}	
	public void setCompassGPS(CompassGps compassGPS) {
		this.compassGPS = compassGPS;
	}
	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}	
	
	public void afterPropertiesSet() throws Exception {
		if (compass == null) {
			throw new IllegalArgumentException("Must set compass property");
		}
		this.compassTemplate = new CompassTemplate(compass);
		
		
		//TODO find another way to call this, since it's a bit inefficient
		//to do it everytime tomcat restarts
		//compassGPS.index();
	}

	public List<SearchResult> search(final String searchString){
		return search(searchString,userService.getCurrentUser().getUsername(), 0, 400);
	}
	private List<SearchResult> search(final String searchString,final String username,final int start, final int num){

		log.debug("-----"+searchString+"--------"+username+"-----");
				
		//
		//create search query 
		//add filter by username
		//TODO how secure is this really? say somebodie's username is 'j*' will they get to see 'jdwyah' stuff? eek.
		//Maybe do a check before we return results?
		//
		//should we be parsing that searchString? forums said '???' as a search string could make things explode
		//
		CompassHitsOperations hits = compassTemplate.executeFind(new CompassCallback(){
			public Object doInCompass(CompassSession session) throws CompassException {

			
//				CompassHits hits = queryBuilder.bool()
//				.addShould(queryBuilder.queryString(searchString).toQuery())
//				.addMust(queryBuilder.term("User.username", "test")).toQuery().hits();
				
//				queryBuilder.bool().addMust(queryBuilder.equals("name", "jack")).addMust(queryBuilder.lt("birthdate", "19500101"))
//  		    .toQuery().hits();
				
				
				Map<String,String> mustEqualMap = new HashMap<String, String>();
				mustEqualMap.put("username", username);				
				
				//create compass query with free text query that the user typed in.
				CompassQueryBuilder queryBuilder = session.queryBuilder();
				CompassQueryStringBuilder qStrBuilder = queryBuilder.queryString(searchString);
				CompassQuery compassQuery = qStrBuilder.toQuery();

				// if mustEqualMap passed in with name-value pairs, loop through the
				// map and create a query filter which is set with the free text query that
				// was just created with the free text string.
				if(mustEqualMap != null) {
					CompassQueryFilterBuilder queryFilterBuilder = session.queryFilterBuilder();
					CompassBooleanQueryBuilder booleanQueryBuilder = queryBuilder.bool();
					Set<String> searchPropSet = mustEqualMap.keySet();
					for(String searchProp : searchPropSet ) {
						String value = mustEqualMap.get(searchProp);
						booleanQueryBuilder.addMust(queryBuilder.term(searchProp,value));
					}
					CompassQueryFilter queryFilter =
						queryFilterBuilder.query(booleanQueryBuilder.toQuery());

					compassQuery.setFilter(queryFilter);
				}

				CompassHits hits = compassQuery.hits(); 
				
			    
				//
				//need to do this before we unattach the hits
				//http://www.opensymphony.com/compass/versions/1.1M2/html/core-workingwithobjects.html#CompassHighlighter
				//
				for (int i = 0; i < hits.length(); i++) {
					try{
						log.debug("HIT "+i+" T:"+hits.highlighter(i).fragment("text"));					
						//huh, guess this is ${hippo.title} since entry has only data
						hits.highlighter(i).fragment("text"); // this will cache the highlighted fragment
					}catch(SearchEngineException see){
						log.warn("Search Engine Exception: "+see+" search term "+searchString+" username "+username);
					}
				}				
				return hits.detach(start,num);
			}});
		
		log.debug("Search: "+searchString+"Results:\t" + hits.getLength());
				
		List<SearchResult> returnList = new ArrayList<SearchResult>(hits.length());
				
		
		//
		//Turn results into SearchResults
		//
		for(int i = 0;i < hits.length();i++){
			CompassHit defaultCompassHit = hits.hit(i);
			
			
			log.debug(i+" score: "+defaultCompassHit.getScore());
			log.debug("alias: "+defaultCompassHit.getAlias());
			
			if(defaultCompassHit.getScore() < .05){
				log.debug("skip <.05");//.041?
				continue;
			}
			
			SearchResult res = null;
			
			Object obj = defaultCompassHit.getData();
			System.out.println("DATA: "+defaultCompassHit.getData());
			
			if (obj instanceof Entry) {
				Entry entry = (Entry) obj;
				
				System.out.println("id: "+entry.getId());

				List<TopicIdentifier> topicIDList = topicDAO.getTopicForOccurrence(entry.getId());
				
				if(topicIDList.size() > 0){
					//TODO what if it has multiple refs?
					TopicIdentifier topicID = topicIDList.get(0);

					CompassHighlightedText text = defaultCompassHit.getHighlightedText();
					
					//PEND take this out   == null only when we do the search for a term that is a username...
					if(text == null){
						res = new SearchResult(topicID.getTopicID(),defaultCompassHit.getScore(),topicID.getTopicTitle(),null);
					}else{
						res = new SearchResult(topicID.getTopicID(),defaultCompassHit.getScore(),topicID.getTopicTitle(),text.getHighlightedText("text"));
					}
				}
			}else if (obj instanceof URI) {
				URI uri = (URI) obj;
				List<TopicIdentifier> topicIDList = topicDAO.getTopicForOccurrence(uri.getId());
				
				//PEND errored when we searched for a username... ie "test"
				if(topicIDList.size() > 0){
					//TODO what if it has multiple refs?				
					TopicIdentifier topicID = topicIDList.get(0);
					res = new SearchResult(topicID.getTopicID(),defaultCompassHit.getScore(),uri.getTitle(),uri.getData());
				}
			}
			else if (obj instanceof Topic) {
				Topic top = (Topic) obj;
				
				//TODO doesn't work!! need to exclude in cpm. Returning as a Topic.class
				//TODO messy. Maybe we need TopLevelTopic.class?
				if(!(top instanceof Association)){
					System.out.println("found top: "+top);
					res = new SearchResult(top.getId(),defaultCompassHit.getScore(),top.getTitle(),null);
				}else{
					System.out.println("was assoc, skip "+top);
				}
				
				
			}
			//I think root == false takes care of this..
			else if (obj instanceof User) {
				log.warn("Shouldn't Happen");
				User user = (User) obj;		
				//TODO user.getID() will break this
				//res = new SearchResult(user.getId(),defaultCompassHit.getScore(),user.getUsername(),null);
			}
			else{				
				log.warn("???"+ obj);				
			}
			log.debug("Found: "+res);
			
			
			if(res != null){
				returnList.add(res);
			}

		}



		return returnList;
	}
	public void indexNow() {
		System.out.println("Index Now");
		mirrorGPS.setMirrorDataChanges(true);		
	}
}
