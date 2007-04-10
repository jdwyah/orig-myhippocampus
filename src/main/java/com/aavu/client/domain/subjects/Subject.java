package com.aavu.client.domain.subjects;

import java.util.HashSet;
import java.util.List;

import com.aavu.client.domain.ReallyCloneable;
import com.aavu.client.domain.subjects.generated.AbstractSubject;

/**
 * NOTE: This class should be thought of as abstract, but we needed to be able to create 
 * objects of this type to get rid of CGLIB in GWT serialization. 
 * 
 * @author Jeff Dwyer
 *
 */
public class Subject extends AbstractSubject implements ReallyCloneable {

	//List<SubjectInfo>
	public List getInfos() {
		return null;
	}
	
	/**
	 * This should return the name of the Tag that we automatically 
	 * assign to things with this Subject. 
	 * 
	 * @return
	 */
	public String getTagName(){
		return "abstract";
	}
	
	 //@Override
    /**
     * This is a leaked concern of NewConverter and our GWT serialization.
     * Without this, we'll end up returning a hibernate persistent set.
     */
	public Object clone() {		
		Subject o = new Subject();
		o.setId(getId());
		o.setForeignID(getForeignID());
		o.setName(getName());
		o.setTopics(new HashSet());
		return o;
	}
}
