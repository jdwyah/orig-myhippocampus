package com.aavu.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.compass.core.Compass;
import org.compass.core.CompassDetachedHits;
import org.compass.core.CompassHighlightedText;
import org.compass.core.CompassHitIterator;
import org.compass.core.CompassHits;
import org.compass.core.CompassTemplate;
import org.compass.core.impl.DefaultCompassHit;
import org.compass.gps.CompassGps;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.SearchResult;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.service.SearchService;

public class SearchServiceImpl implements SearchService {
	private static final Logger log = Logger.getLogger(SearchServiceImpl.class);
	
	private CompassTemplate compassTemplate;
	
	private Compass compass;
	private CompassGps compassGPS;
	private TopicDAO topicDAO;
	
	public void setCompass(Compass compass) {
		this.compass = compass;
	}	
	public void setCompassGPS(CompassGps compassGPS) {
		this.compassGPS = compassGPS;
	}
	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}
	
	
	public void afterPropertiesSet() throws Exception {
		if (compass == null) {
			throw new IllegalArgumentException("Must set compass property");
		}
		this.compassTemplate = new CompassTemplate(compass);
		compassGPS.start();

		//must call this once after new data arrives
		//compassGPS.index();
	}

	public List<SearchResult> search(String searchString){
		
		try {
			afterPropertiesSet();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		CompassHits hits = compassTemplate.find(searchString);
		log.debug("Search: "+searchString+"Results:\t" + hits.getLength());

		//
		//need to do this before we unattach the hits
		//http://www.opensymphony.com/compass/versions/1.1M2/html/core-workingwithobjects.html#CompassHighlighter
		//
		for (int i = 0; i < hits.length(); i++) {
			log.debug("hi "+hits.highlighter(i).fragment("title"));
			//huh, guess this is ${hippo.title} since entry has only data
			hits.highlighter(i).fragment("title"); // this will cache the highlighted fragment
		}
				
		List<SearchResult> returnList = new ArrayList<SearchResult>(hits.length());
		
		CompassDetachedHits detachedHits = hits.detach();
		CompassHitIterator iterator = detachedHits.iterator();
		while(iterator.hasNext()){
			DefaultCompassHit defaultCompassHit =
				(DefaultCompassHit)iterator.next();
			
			log.debug("alias: "+defaultCompassHit.getAlias());
			log.debug("score: "+defaultCompassHit.getScore());

			
			SearchResult res = null;
			
			Object obj = defaultCompassHit.getData();
			
			if (obj instanceof Entry) {
				Entry entry = (Entry) obj;
				
				System.out.println("id: "+entry.getId());

				List<TopicIdentifier> topicIDList = topicDAO.getTopicForOccurrence(entry.getId());
				
				//TODO what if it has multiple refs?
				TopicIdentifier topicID = topicIDList.get(0);
				
				CompassHighlightedText text = defaultCompassHit.getHighlightedText();
				res = new SearchResult(topicID.getTopicID(),defaultCompassHit.getScore(),topicID.getTopicTitle(),text.getHighlightedText("title"));
				
			}else if (obj instanceof Topic) {
				Topic top = (Topic) obj;
				
				res = new SearchResult(top.getId(),defaultCompassHit.getScore(),top.getTitle(),null);
			}else{				
				log.warn("???"+ obj);				
			}
			log.debug("Found: "+res);
			
			returnList.add(res);

		}



		return returnList;
	}
}
