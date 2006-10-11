package com.aavu.client.domain;

import java.util.Collection;

public class SeeAlso extends Association {

	public SeeAlso(){}
	
		
	public SeeAlso(User user) {
		setUser(user);
		setTitle("seealso");
	}


	public void add(TopicIdentifier top){
		getMembers().put((getMembers().keySet().size()+1)+"", new Topic(top));
	}
		
	/**
	 * If you're looking for a Topic's seealso, remember,
	 * this will return the original topic too!
	 * 
	 * @return
	 */
	public Collection getAll(){
		return getMembers().values();
	}
	
}
