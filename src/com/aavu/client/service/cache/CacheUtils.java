package com.aavu.client.service.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.TopicIdentifier;

public class CacheUtils {

	public static TopicIdentifier searchTopics(List topicIdentifiers, String linkTo) {
		int index = indexedBinarySearch(topicIdentifiers, linkTo);
		
		System.out.println("index! "+index);
		
		if(index < 0){
			return null;
		}else{
			return (TopicIdentifier) topicIdentifiers.get(index);			
		}
	}

	/**
	 * This will just search the keyList of a sorted map.
	 * @param map
	 * @param linkTo
	 * @return
	 */
	public static TopicIdentifier searchTopics(GWTSortedMap map, String linkTo) {		
		return searchTopics(map.getKeyList(), linkTo);
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
