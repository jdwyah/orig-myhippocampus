package com.aavu.client.domain.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoBusinessException;



/**
 * Add a topic to a topic. if it's an occurrence, add to the occurrence list, otherwise add to the
 * type list
 * 
 * NOTE, this stores the topics in the topics list, but because there may or may not be a removeTag
 * in there and bc the list may be of any length, be carefull when assuming which topic is which
 */
public class AddToTopicCommand extends AbstractCommand implements Serializable {

	private boolean useRemove;

	public AddToTopicCommand() {
	};

	public AddToTopicCommand(Topic topic, Topic tagToAddThingsTo) {
		useRemove = false;

		List<Topic> topics = new ArrayList<Topic>();
		topics.add(tagToAddThingsTo);
		topics.add(topic);

		init(topics);
	}

	public AddToTopicCommand(List<Topic> _topics, Topic tagToAddThingsTo, Topic removeFromTag) {

		_topics.add(0, tagToAddThingsTo);

		if (removeFromTag != null) {
			useRemove = true;
			_topics.add(1, removeFromTag);
		}

		init(_topics);

	}


	/**
	 * 
	 * @param selectedTopics
	 * @param tagToAddThingsTo
	 * @param removeFromTag
	 */
	public AddToTopicCommand(Set<Topic> selectedTopics, Topic tagToAddThingsTo, Topic removeFromTag) {

		this(new ArrayList<Topic>(selectedTopics), tagToAddThingsTo, removeFromTag);
	}

	private List<Topic> getTopicsToProcess() {
		if (useRemove) {
			return subList(getTopics(), 2, getTopics().size());
		} else {
			return subList(getTopics(), 1, getTopics().size());
		}
	}

	private Topic getRemoveFromThis() {
		if (useRemove) {
			return getTopic(1);
		} else {
			return null;
		}
	}

	public Topic getTagToAddThingsTo() {
		return getTopic(0);
	}


	/**
	 * the difficulty.
	 * 
	 * The difficulty is that when we run this
	 * 
	 */
	// @Override
	public void executeCommand() throws HippoBusinessException {

		List<Topic> topicsToProcess = getTopicsToProcess();

		Topic tag = getTagToAddThingsTo();
		Topic removeFromThis = getRemoveFromThis();


		System.out.println(toString());

		for (Topic toProcess : topicsToProcess) {


			// System.out.println("AddToTagCommand to process " + toProcess + " occ "
			// + (toProcess instanceof Occurrence));

			if (toProcess instanceof Occurrence) {
				Occurrence occToProcces = (Occurrence) toProcess;

				// System.out.println("AddToTagCommand. Process as occ");
				tag.addOccurence(occToProcces);


			} else {

				// System.out.println("AddToTagCommand. Process as topic");
				toProcess.tagTopic(tag);

			}

			if (null != removeFromThis) {
				if (toProcess instanceof Occurrence) {
					Occurrence occToProcces = (Occurrence) toProcess;
					removeFromThis.removeOcc(occToProcces);
				} else {
					toProcess.removeType(removeFromThis);
				}
				// throw new HippoBusinessException("Error Removing Occurrence");
			}

		}
	}

	// @Override
	public String toString() {
		return "AddToTagCommand ID TagToTagWith " + getTagToAddThingsTo() + " Delete: "
				+ getRemoveFromThis() + " ToAdd " + getTopicsToProcess() + " total " + getTopics();
	}


	// @Override
	public Set<Topic> getAffectedTopics() {
		Set<Topic> s = super.getAffectedTopics();
		s.add(getTagToAddThingsTo());
		if (getRemoveFromThis() != null) {
			s.add(getRemoveFromThis());
		}
		return s;
	}

}
