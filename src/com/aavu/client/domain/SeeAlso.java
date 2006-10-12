package com.aavu.client.domain;

import java.util.Collection;

/**
 * Changed around what I was doing when I realized that if Feynman has a SeeAlso of Physics & Stanfors, and Physics has a SeeAlso of Chemistry, I didn't want this
 * to be one big SeeAlso fest, (Feynman, Stanford, Chemisty,Physics) which is what it was going to be. Now there'll be two see also, one for Feynman and one for 
 * Physics. If you want to see who's see also's you're included in, you can use List<TopicIdent> topicDAO.getLinksTo()
 * 
 * @author Jeff Dwyer
 *
 */
public class SeeAlso extends Association {

	public SeeAlso(){}
	
		
	public SeeAlso(User user) {
		setUser(user);
		setTitle("seealso");
		getTypes().add(new Topic(user,"SeeAlso"));
	}


	public void add(TopicIdentifier top){
		getMembers().add(new Topic(top));		
	}
		
	/**
	 * If you're looking for a Topic's seealso, remember,
	 * this will return the original topic too!
	 * 
	 * @return
	 */
	public Collection getAll(){
		return getMembers();
	}
	
}
