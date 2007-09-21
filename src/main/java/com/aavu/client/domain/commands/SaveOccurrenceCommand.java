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

		// System.out.println("SaveOccCommand.create saveOCCComand " + forTopics.size() + " Data "
		// + occurrence.getData());
		setTopicIDsFromTopics(forTopics);
	}


	// @Override
	/**
	 * @throws HippoException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * 
	 */
	public void executeCommand() throws HippoException {

		Occurrence theOcc = (Occurrence) getTopic(0);

		// This occurs when we add a new Occ
		if (theOcc == null) {
			System.out.println("SaveOccComm. loaded occ was null, use new WebLink");
			// don't user occurrence, because that may carry with it baggage that will confuse us
			// about what's saved and what's not. (ie it already has the occ's added, which makes us
			// think..

			// Arg! can't do new Weblink() bc maybe it's an Entry. can't do getClass().instance()
			// because this operates in GWT land.
			theOcc = occurrence;
			theOcc.getTopics().clear();
			theOcc.getTypes().clear();// the important one
			theOcc.getAssociations().clear();

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

		System.out.println("SaveOccComm.Existing: theOcc.getTopics().size() "
				+ theOcc.getTopics().size());

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
					// System.out.println("SaveOccComm.SaveOccCmd Couldn't remove " + theOcc);
					throw new HippoException("Couldn't delete " + theOcc);
				}
				iterator.remove();
			}

		}

		System.out.println("SaveOccComm.fin rem del Left:" + desiredList.size());

		// add things left in the forTopics list
		for (Iterator iterator = desiredList.iterator(); iterator.hasNext();) {
			Topic toAdd = (Topic) iterator.next();
			// System.out.println("adding " + toAdd);

			// TODO fix better
			// not sure why this ever happens, but have found it null in the logs
			if (toAdd != null) {
				toAdd.addOccurence(theOcc);

				// System.out.println("cont: " +
				// toAdd.getOccurenceObjs().contains(getOccurrence()));
			}
		}

		// try {
		// System.out.println("SaveOccComm." + getTopic(1));
		// System.out.println("SaveOccComm.contp: "
		// + getTopic(1).getOccurenceObjs().contains(getOccurrence()));
		// System.out.println("SaveOccComm.occ: " + getOccurrence());
		// } catch (NullPointerException e) {
		// System.out.println("SaveOccComm. ERR NPE");
		// }


		affected.add(theOcc);

	}

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



}
