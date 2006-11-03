package com.aavu.client.domain.generated;
// Generated Nov 3, 2006 8:10:46 AM by Hibernate Tools 3.1.0.beta5


import com.aavu.client.domain.User;
import com.aavu.client.domain.subjects.Subject;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AbstractAssociation generated by hbm2java
 */
public abstract class AbstractAssociation extends com.aavu.client.domain.Topic implements IsSerializable, java.io.Serializable {

    // Fields    

	/**
	 * @gwt.typeArgs <com.aavu.client.domain.Topic>
	 */
	private Set members = new HashSet();

     // Constructors

    /** default constructor */
    public AbstractAssociation() {
    }

    /** full constructor */
    public AbstractAssociation(User user, String title, Date lastUpdated, Date created, boolean publicVisible, Set scopes, Subject subject, Set instances, Set types, Set occurences, Set associations, Set members) {
        super(user, title, lastUpdated, created, publicVisible, scopes, subject, instances, types, occurences, associations);        
       this.members = members;
    }
    
   
    // Property accessors
    public Set getMembers() {
        return this.members;
    }
    
    public void setMembers(Set members) {
        this.members = members;
    }




}


