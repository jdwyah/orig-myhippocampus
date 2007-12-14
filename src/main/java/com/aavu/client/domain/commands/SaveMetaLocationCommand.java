package com.aavu.client.domain.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.util.CollectionUtils;

/**
 * Save a List of HippoLocations. Must pass all locations for the given Meta.
 * 
 * We'll try to match the passed in list with the saved list, modifying everything that needs be. If
 * things are passed in but not saved, we add. If things are saved, but not passed in, we delete.
 * 
 * Note: Remember that addMetaValue() will clear members unless called with false.
 * 
 * @author Jeff Dwyer
 * 
 */
public class SaveMetaLocationCommand extends AbstractCommand implements Serializable {


	private Set<HippoLocation> values;

	private Set<Topic> toDelete = new HashSet<Topic>();

	public SaveMetaLocationCommand() {
	};

	public SaveMetaLocationCommand(Topic topic, Meta meta, Set<HippoLocation> values) {
		super(topic, meta);
		this.values = values;
	}

	// @Override
	public void executeCommand() {

		System.out.println("\nSaveMetaLocationCommand EXECUTE SAVE META");
		System.out.println("SaveMetaLocationCommand values " + values.size());

		Set<Topic> curLocations = getTopic(0).getMetaValuesFor((Meta) getTopic(1));

		// assume that we'll delete everything, than remove what we won't delete
		toDelete.addAll(curLocations);

		System.out.println("SaveMetaLocationCommand CurLocations.size " + curLocations.size());

		for (HippoLocation location : values) {

			System.out.println("SaveMetaLocationCommand Processing " + location);

			// get and modify
			// remove, found from curLocations, dregs will be deleted
			if (location.getId() > 0) {
				System.out.println("SaveMetaLocationCommand ID>0 -> MODIFY");
				HippoLocation curForThatID = (HippoLocation) CollectionUtils.getFromCollectionById(
						curLocations, location.getId());

				location.copyPropsButNotIDIntoParam(curForThatID);

				System.out.println("remove " + location.getId());
				CollectionUtils.removeFromCollectionById(toDelete, location.getId());
			}
			// new, add
			else {
				System.out.println("SaveMetaLocationCommand ADD");
				location.setUser(getTopic(0).getUser());
				getTopic(0).addMetaValue((Meta) getTopic(1), location, false);
			}

		}


		//		
		// HippoDate mv = (HippoDate) getTopic(0).getSingleMetaValueFor((Meta) getTopic(1));
		//		
		// boolean clear = true;
		// for (Iterator iter = values.iterator(); iter.hasNext();) {
		// HippoLocation location = (HippoLocation) iter.next();
		// System.out.println("----"+location);
		// location.setUser(getTopic(0).getUser());
		// getTopic(0).addMetaValue((Meta) getTopic(1), location,clear);
		//			
		// clear = false;
		// }
		//		
		// System.out.println("topic assoc "+getTopic(0).getAssociations().size());
		// System.out.println("topic metas "+getTopic(0).getMetas().size());

	}

	// @Override
	public List<Topic> getTopics() {
		System.out.println("\n\n\nRETURN SMALL SUBLIST");

		List<Topic> rtn = new ArrayList<Topic>();
		rtn.add(getTopic(0));

		return rtn;
		// return topics.subList(0, 1);
	}

	// @Override
	public Set<Topic> getDeleteSet() {
		return toDelete;
	}

	// @Override
	public String toString() {
		return "SaveLocationCommand " + getTopicID(0) + " " + getTopic(1) + " " + values;
	}



}
