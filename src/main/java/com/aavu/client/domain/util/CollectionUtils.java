package com.aavu.client.domain.util;

import java.util.Collection;
import java.util.Iterator;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;

public class CollectionUtils {

	/**
	 * often necessary if you find that getOccurrences.remove(o) isn't removing anything. Compares
	 * solely on id.
	 * 
	 * @param topics
	 * @param id
	 * @return
	 */
	public static Object getFromCollectionById(Collection topics, long id) {
		for (Iterator iter = topics.iterator(); iter.hasNext();) {
			Object o = (Object) iter.next();
			if (o instanceof Occurrence) {
				Occurrence occ = (Occurrence) o;
				System.out.println("check " + occ.getId());
				if (occ.getId() == id) {
					return occ;
				}
			}
			if (o instanceof Topic) {
				Topic top = (Topic) o;
				System.out.println("check " + top.getId());
				if (top.getId() == id) {
					return top;
				}
			}
		}
		return null;
	}

	/**
	 * bizarro
	 * 
	 * couldn't delete occurrences when we hash the getData() see hashCode in
	 * abstractoccurrences.java
	 * 
	 * @param theSet
	 * @param id
	 * @return
	 */
	public static boolean removeFromCollectionById(Collection theSet, long id) {
		boolean foundOne = false;
		int size = theSet.size();

		for (Iterator iter = theSet.iterator(); iter.hasNext();) {
			Object o = (Object) iter.next();
			if (o instanceof Occurrence) {
				Occurrence occ = (Occurrence) o;
				if (occ.getId() == id) {
					// System.out.println("REMOVE!! "+o.hashCode()+" "+occ.hashCode());
					iter.remove();
					foundOne = true;
				}
			} else if (o instanceof TopicOccurrenceConnector) {
				TopicOccurrenceConnector occ = (TopicOccurrenceConnector) o;
				if (occ.getOccurrence().getId() == id) {
					System.out.println("REMOVE!! " + o.hashCode() + " " + occ.hashCode());
					iter.remove();
					foundOne = true;
				}
			} else if (o instanceof Topic) {
				Topic top = (Topic) o;
				if (top.getId() == id) {
					iter.remove();
					foundOne = true;
				}
			} else {
				System.out.println("removeDromSetById for unknown type");
			}
		}
		if (foundOne && theSet.size() == size) {
			System.out.println("WTF??? Found but couldn't delete");
			return false;
		}

		return foundOne;
	}

	public static boolean contains(Collection theSet, Topic t) {
		for (Iterator iter = theSet.iterator(); iter.hasNext();) {
			Object o = (Object) iter.next();
			if (o.equals(t)) {
				return true;
			} else {
				System.out.println("not eq " + t + " " + o);
			}
		}
		return false;
	}
}
