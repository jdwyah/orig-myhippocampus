package com.aavu.client.domain;

import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * Changed around what I was doing when I realized that if Feynman has a SeeAlso of Physics & Stanfors, and Physics has a SeeAlso of Chemistry, I didn't want this
 * to be one big SeeAlso fest, (Feynman, Stanford, Chemisty,Physics) which is what it was going to be. Now there'll be two see also, one for Feynman and one for 
 * Physics. If you want to see who's see also's you're included in, you can use List<TopicIdent> topicDAO.getLinksTo()
 * 
 * This is saved as a Singleton with some ugly ugly in HibernateDAO.
 * The other Meta's are not Singletons, since they have title data.
 * 
 * @author Jeff Dwyer
 *
 */
public class MetaSeeAlso extends Meta {

	
	public static final String UBER_TITLE = "SEEALSO UBER";

	public MetaSeeAlso(){
		setTitle(UBER_TITLE);
	}

	//@Override
	public Widget getEditorWidget(Topic top, Manager manager) {
		//throw new OperationNotSupportedException()
		return null;
	}

	//@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
