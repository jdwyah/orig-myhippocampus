package com.aavu.client.domain;

import java.util.Collection;
import java.util.Iterator;

import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Changed around what I was doing when I realized that if Feynman has a SeeAlso of Physics & Stanfors, and Physics has a SeeAlso of Chemistry, I didn't want this
 * to be one big SeeAlso fest, (Feynman, Stanford, Chemisty,Physics) which is what it was going to be. Now there'll be two see also, one for Feynman and one for 
 * Physics. If you want to see who's see also's you're included in, you can use List<TopicIdent> topicDAO.getLinksTo()
 * 
 * @author Jeff Dwyer
 *
 */
public class MetaSeeAlso extends Meta {

	
	public MetaSeeAlso(){
		setTitle("SEEALSO UBER");
	}

	//@Override
	public Widget getEditorWidget(Topic top) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public Widget getWidget(Topic top) {

		Association assoc = top.getSeeAlsoAssociation();
		
		HorizontalPanel horizP = new HorizontalPanel();
		
		horizP.add(new HeaderLabel(Manager.myConstants.seeAlsos()));
				
		for (Iterator iter = assoc.getMembers().iterator(); iter.hasNext();) {
			Topic link = (Topic) iter.next();
			horizP.add(new TopicLink(link));	
		}
		
		return horizP;		
	}
	
}
