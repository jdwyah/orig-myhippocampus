package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractAssociation;

public class Association extends AbstractAssociation implements Serializable {

	/**
	 * user set in DAO.
	 * 
	 */
	public Association() {

	}

	/**
	 * used to add a tagProperty as a type of the association, but it should really be the member of
	 * an association of type TagProperty for now, we'll assume (dangerous?) that association w/ no
	 * type, but member of type Meta is a TagPropertyAssociation.
	 * 
	 * @param topic
	 * @param meta
	 */
	// public Association(Topic topic,Meta meta) {
	// setUser(topic.getUser());
	// getMembers().add(meta);
	//		
	// setTitle(topic.getTitle()+" to "+meta.getTitle());
	// }

	public Association(Topic topic) {
		setUser(topic.getUser());
	}

	/**
	 * Most associations will only be of one type. Their Meta. This is just an accessor.
	 * 
	 * @return
	 */
	public Topic getFirstType() {
		return (Topic) getTypesAsTopics().iterator().next();
	}

	/**
	 * Note, this will change the .equals method that we use to not associate titles as eq
	 * 
	 */
	// @Override
	public boolean mustHaveUniqueName() {
		return false;
	}

}
