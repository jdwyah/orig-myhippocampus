package com.aavu.client.service.cache;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.TopicIdentifier;

public class CacheUtils {

	public static TopicIdentifier searchTopics(List topicIdentifiers, String linkTo) {
		int index = indexedBinarySearch(topicIdentifiers, linkTo);
		
		System.out.println("index! "+index);
	
		for (Iterator iter = topicIdentifiers.iterator(); iter.hasNext();) {
			TopicIdentifier ident = (TopicIdentifier) iter.next();
			System.out.println("ident "+ident.getTopicTitle()+" "+ident.getTopicID());
		}
		
		if(index < 0){
			return null;
		}else{
			return (TopicIdentifier) topicIdentifiers.get(index);			
		}
	}


	private static int indexedBinarySearch(List list, Comparable key)
	{
		int low = 0;
		int high = list.size()-1;

		while (low <= high) {
			int mid = (low + high) >> 1;
		Comparable midVal = (Comparable) list.get(mid);
		int cmp = midVal.compareTo(key);
				
		if (cmp < 0)
			low = mid + 1;
		else if (cmp > 0)
			high = mid - 1;
		else
			return mid; // key found
		}
		return -(low + 1);  // key not found
	}
}
