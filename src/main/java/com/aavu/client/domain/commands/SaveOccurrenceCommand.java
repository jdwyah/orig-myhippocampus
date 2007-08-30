package com.aavu.client.domain.commands;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.URI;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Use getAddTopics to get the topics we should be an occurrence of. Use getRemoveItems to get the
 * topics we should be removed from.
 * 
 * 
 * @author Jeff Dwyer
 * 
 */
public class SaveOccurrenceCommand extends AbstractCommand implements IsSerializable {

	protected Occurrence occurrence;

	// private int removeStartNumber;


	private transient Set affected = new HashSet();



	public SaveOccurrenceCommand() {
	};

	// public SaveOccurrenceCommand(List topics, Occurrence occurrence) {
	// this(topics, occurrence, -1);
	// }
	//
	// public SaveOccurrenceCommand(List topics, Occurrence occurrence, int removeStartNumber) {
	// super(topics);
	// this.occurrence = occurrence;
	// this.removeStartNumber = removeStartNumber;
	// }
	public SaveOccurrenceCommand(Occurrence occurrence, List forTopics) {
		this.occurrence = occurrence;
		forTopics.add(0, occurrence);

		System.out.println("create saveOCCComand " + forTopics.size() + " Data "
				+ occurrence.getData());
		setTopicIDsFromTopics(forTopics);
	}


	// @Override
	/**
	 * @throws HippoException
	 * 
	 */
	public void executeCommand() throws HippoException {

		Occurrence theOcc = (Occurrence) getTopic(0);

		// This occurs when we add a new Occ
		if (theOcc == null) {
			theOcc = occurrence;
			setTopic(0, theOcc);
		}

		System.out.println("SaveOccComm.the occ " + theOcc + " CopyProps");

		System.out.println("SaveOccComm.occ Data" + occurrence.getData() + " "
				+ occurrence.getDefaultName());

		// PEND MED ugly
		// w/o this we only call Occurrence.copyProps, even if our instance is a URI, because the
		// parameter doesn't get cast. I guess I was a bit vague on the ruels here. Could replace
		// the polymorphic call with visitor pattern.. sigh.
		//
		if (occurrence instanceof URI) {
			System.out.println("SaveOccComm.toURI");
			URI uri = (URI) occurrence;
			uri.copyPropsButNotIDIntoParam((URI) theOcc);
		} else {
			occurrence.copyPropsButNotIDIntoParam(theOcc);
		}

		List desiredList = subList(getTopics(), 1, getTopics().size());


		for (Iterator iterator = getTopics().iterator(); iterator.hasNext();) {
			Topic tt = (Topic) iterator.next();
			System.out.println("SaveOccComm.BaseList " + tt);
		}
		for (Iterator iterator = desiredList.iterator(); iterator.hasNext();) {
			Topic tt = (Topic) iterator.next();
			System.out.println("SaveOccComm.Desired " + tt);
		}


		// loop through the existing topics for this occ
		// Remove from the toAddList
		//
		for (Iterator iterator = theOcc.getTopics().iterator(); iterator.hasNext();) {
			TopicOccurrenceConnector toc = (TopicOccurrenceConnector) iterator.next();

			if (desiredList.contains(toc.getTopic())) {
				System.out.println("SaveOccComm.remove from desired " + toc.getTopic());

				// remove from need to add list
				desiredList.remove(toc.getTopic());
			} else {

				// delete reference
				System.out.println("SaveOccComm.delete ref to " + toc.getTopic());
				boolean found = toc.getTopic().removeOcc(theOcc);
				if (!found) {
					System.out.println("SaveOccComm.SaveOccCmd Couldn't remove " + theOcc);
					throw new HippoException("Couldn't delete " + theOcc);
				}
				iterator.remove();
			}

		}

		System.out.println("SaveOccComm.fin rem del Left:" + desiredList.size());

		// add things left in the forTopics list
		for (Iterator iterator = desiredList.iterator(); iterator.hasNext();) {
			Topic toAdd = (Topic) iterator.next();
			System.out.println("adding " + toAdd);
			toAdd.addOccurence(theOcc);

			System.out.println("cont: " + toAdd.getOccurenceObjs().contains(getOccurrence()));
		}

		System.out.println("SaveOccComm." + getTopic(1));
		System.out.println("SaveOccComm.contp: "
				+ getTopic(1).getOccurenceObjs().contains(getOccurrence()));
		System.out.println("SaveOccComm.occ: " + getOccurrence());


		affected.add(theOcc);

	}

	// // @Override
	// /**
	// * This is a bit sqirrley. We can't do a get() on the set, since .equals() compares the data &
	// * won't find the match.
	// *
	// * Use copyProps instead of remove/add to avoid 'Object with ID already associated with
	// session'
	// * type errors.
	// *
	// * PEND MED. Make this less ugly.
	// */
	// public void executeCommand() {
	//
	// if (occurrence.getId() != 0) {
	// Occurrence existing = null;
	//
	// List newTopics = new ArrayList();
	//
	// // Step 1 Loop through the topics we're to be added to to find an existing reference to
	// // this occurrence.
	// //
	// //
	// for (Iterator iter = getAddTopics().iterator(); iter.hasNext();) {
	// Topic topic = (Topic) iter.next();
	//
	// // temp, make sure to set higher scoped 'existing'
	// Occurrence exhist = (Occurrence) SetUtils.getFromSetById(topic.getOccurenceObjs(),
	// occurrence.getId());
	//
	// if (exhist != null) {
	//
	// existing = exhist;
	//
	// System.out.println("Exist " + topic);
	// occurrence.copyPropsIntoParam(existing);
	// } else {
	// // can't simply add here, because that will NonUniqueObjectException
	// // (existing && occurence)
	// // We'll add later, because something should have an existing
	// System.out.println("NoExisting " + topic);
	// newTopics.add(topic);
	// }
	//
	// }
	//
	// // Add to topics that didn't already have it
	// //
	// if (!newTopics.isEmpty()) {
	// for (Iterator iter = newTopics.iterator(); iter.hasNext();) {
	// Topic topic = (Topic) iter.next();
	// if (existing != null) {
	// topic.addOccurence(existing);
	// } else {
	// System.out.println("SaveOccurrenceCommand WARN No existing occurrence");
	// topic.addOccurence(occurrence);
	// }
	//
	// }
	// }
	//
	//
	// System.out.println("Do Remove");
	// for (Iterator iter = getRemoveItems().iterator(); iter.hasNext();) {
	// Topic inLink = (Topic) iter.next();
	// System.out.println("still has link" + inLink);
	// TopicOccurrenceConnector exist2 = (TopicOccurrenceConnector) SetUtils
	// .getFromSetById(inLink.getOccurences(), occurrence.getId());
	// boolean r1 = inLink.getOccurences().remove(exist2);
	// boolean r2 = occurrence.getTopics().remove(inLink);
	// if (!(r1 && r2)) {
	// System.out.println("WARN SaveOccurrence Not Removing " + r1 + " " + r2);
	// }
	// }
	//
	// } else {
	// for (Iterator iter = getAddTopics().iterator(); iter.hasNext();) {
	// Topic topic = (Topic) iter.next();
	// System.out.println("topic add occurrence 1 "
	// + topic.getOccurenceObjs().contains(occurrence) + " " + topic + " | "
	// + occurrence);
	// topic.addOccurence(occurrence);
	// System.out.println("topic add occurrence 2 "
	// + topic.getOccurenceObjs().contains(occurrence) + " " + topic + " | "
	// + occurrence);
	// }
	// }
	//
	//
	// // System.out.println("LOOPING over "+occurrence+" topics");
	//
	//
	// /**
	// * loop over all the topics that the occurrence says it's related too. If those topics don't
	// * have this occurence already, add it.
	// *
	// * Can't do a basic contains() since we're .equals is not operating on ID
	// */
	// // for (Iterator iter = occurrence.getTopics().iterator(); iter.hasNext();) {
	// // Topic topic = (Topic) iter.next();
	// // System.out.println("found "+topic);
	// //
	// // if(null == getFromSetById(topic.getOccurences(), occurrence.getId())){
	// // System.out.println("Didn't contain "+occurrence+" ADD");
	// // topic.addOccurrence(occurrence);
	// // }
	// // else{
	// // System.out.println("Contained. do nothing");
	// // }
	// // affected.add(topic);
	// // }
	// }
	//
	// public List getRemoveItems() {
	// if (removeStartNumber == -1) {
	// return new ArrayList();
	// }
	// // return getTopics().subList(removeStartNumber, getTopics().size());
	// return subList(getTopics(), removeStartNumber, getTopics().size());
	// }
	//
	// public List getAddTopics() {
	// if (removeStartNumber == -1) {
	// return getTopics();
	// }
	// // return getTopics().subList(0, removeStartNumber);
	// return subList(getTopics(), 0, removeStartNumber);
	// }
	//



	/**
	 * make sure we save the other topics that we may have added ourselves to, since this is the
	 * inverse side of the relationship
	 */
	// @Override
	public Set getAffectedTopics() {
		Set s = super.getAffectedTopics();
		s.addAll(affected);
		return s;
	}


	// @Override
	public String toString() {
		return "SaveOccurrence ID " + getTopicID(0) + " ";
	}

	public Occurrence getOccurrence() {
		return (Occurrence) getTopic(0);
	}

	// public Occurrence getOccurrence() {
	// return occurrence;
	// }



}
