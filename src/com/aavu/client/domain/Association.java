package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractAssociation;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Association extends AbstractAssociation implements Serializable,IsSerializable{
	
	/**
	 * user set in DAO
	 *
	 */
	public Association(){
		
	}

	public Association(Topic topic,Meta meta) {
		addType(meta);
		setTitle(topic.getTitle()+" to "+meta.getTitle());
	}


	/**
	 * Most associations will only be of one type. Their Meta. This is just
	 * an accessor.
	 * 
	 * @return
	 */
	public Topic getFirstType(){
		return (Topic) getTypesAsTopics().iterator().next();
	}

}
