package com.aavu.client.domain.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.Topic;

public class SetUtils {

	/**
	 * often necessary if you find that getOccurrences.remove(o) isn't removing anything.
	 * Compares solely on id. 
	 * 
	 * @param topics
	 * @param id
	 * @return
	 */
	public static Object getFromSetById(Set topics,long id){
		for (Iterator iter = topics.iterator(); iter.hasNext();) {			
			Object o = (Object) iter.next();
			if (o instanceof Occurrence) {
				Occurrence occ = (Occurrence) o;
				System.out.println("check "+occ.getId());
				if(occ.getId() == id){
					return occ;
				}
			}
			if (o instanceof Topic) {
				Topic top = (Topic) o;
				System.out.println("check "+top.getId());
				if(top.getId() == id){
					return top;
				}
			}			
		}			
		return null;
	}
	/**
	 * bizarro 
	 * 
	 * couldn't delete occurrences when we hash the getData()
	 * see hashCode in abstractoccurrences.java
	 * 
	 * @param theSet
	 * @param id
	 * @return
	 */
	public static boolean removeFromSetById(Set theSet,long id){
		boolean foundOne = false;
		int size = theSet.size();
		
		for (Iterator iter = theSet.iterator(); iter.hasNext();) {
			Object o = (Object) iter.next();
			if (o instanceof Occurrence) {
				Occurrence occ = (Occurrence) o;
				if(occ.getId() == id){
					//System.out.println("REMOVE!! "+o.hashCode()+" "+occ.hashCode());
					iter.remove();
					foundOne = true;		
				}
			}
			if (o instanceof TopicOccurrenceConnector) {
				TopicOccurrenceConnector occ = (TopicOccurrenceConnector) o;
				if(occ.getOccurrence().getId() == id){
					System.out.println("REMOVE!! "+o.hashCode()+" "+occ.hashCode());
					iter.remove();
					foundOne = true;		
				}
			}
			if (o instanceof Topic) {
				Topic top = (Topic) o;
				if(top.getId() == id){
					iter.remove();
					foundOne = true;
				}
			}			
		}			
		if(foundOne && theSet.size() == size){
			System.out.println("WTF??? Found but couldn't delete");
			return false;			
		}		
		
		return foundOne;
	}
}
