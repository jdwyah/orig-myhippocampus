package com.aavu.server.service.impl;

import java.util.List;

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
import com.aavu.server.service.SearchService;

public class SearchServiceImpl implements SearchService {

	private Compass compass;
	private CompassGps compassGPS;
	private CompassTemplate compassTemplate;
	
	public void setCompass(Compass compass) {
		this.compass = compass;
	}	
	public void setCompassGPS(CompassGps compassGPS) {
		this.compassGPS = compassGPS;
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
		System.out.println("STARTING SEACH "+searchString);
		try {
			afterPropertiesSet();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		CompassHits hits = compassTemplate.find(searchString);

		System.out.println("Results:\t" + hits.getLength());

		
		
//		compassTemplate.getCompass().getSearchEngineIndexManager().createIndex();

		try{
			for (int i = 0; i < hits.length(); i++) {
				System.out.println("i "+i);
				System.out.println("hi "+hits.highlighter(i).fragment("title"));
				hits.highlighter(i).fragment("title"); // this will cache the highlighted fragment
				
			}
		}catch(Exception e){
			System.out.println("FAILURE "+e);
		}
		
		
		CompassDetachedHits detachedHits = hits.detach();
		CompassHitIterator iterator = detachedHits.iterator();
		while(iterator.hasNext()){
			DefaultCompassHit defaultCompassHit =
				(DefaultCompassHit)iterator.next();
			System.out.println("alias: "+defaultCompassHit.getAlias());
			System.out.println("score: "+defaultCompassHit.getScore());

			if (defaultCompassHit.getData() instanceof Entry) {
				Entry entry = (Entry) defaultCompassHit.getData();
				
				System.out.println("data: "+entry.getData());
				System.out.println("id: "+entry.getId());
				System.out.println("topicid: "+entry.getTopic().getId());
			}else{
				System.out.println("data: "+defaultCompassHit.getData());
			}
			
			CompassHighlightedText text = defaultCompassHit.getHighlightedText();

			System.out.println("htext "+text);
			if(text != null){
				//System.out.println("hh"+text.getHighlightedText());
				String s = text.getHighlightedText("title");
				System.out.println("high: "+s);
			}
			
//			text.getHighlightedText("description");
//			CompassHits hits =  session.find("london");
//			for (int i = 0.; i < 10; i++) {
//			    hits.highlighter(i).fragment("description"); // this will cache the highlighted fragment
//			}
//			CompassHit[] detachedHits = hits.detach(0, 10).getHits();
//
////			 outside of a transaction (maybe in a view technology)
//			for (int i = 0; i < detachedHits.length; i++) {
//			    // this will return the first fragment
//			    detachedHits[i].getHighlightedText().getHighlightedText();
//			    // this will return the description fragment, note that the implementation
//			    // implements the Map interface, which allows it to be used simply in JSTL env and others
//			    detachedHits[i].getHighlightedText().getHighlightedText("description"); 
//			}

		}



		return null;
	}
}
