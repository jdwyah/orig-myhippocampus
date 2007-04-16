package com.aavu.client.domain.generated;
// Generated Nov 29, 2006 10:34:46 AM by Hibernate Tools 3.1.0.beta5


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.aavu.client.domain.User;
import com.aavu.client.domain.subjects.Subject;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * AbstractTopic generated by hbm2java
 */
public abstract class AbstractTopic  implements IsSerializable, java.io.Serializable {

    // Fields    
	/**
	 * NOTE: this is used in .equals() if true
	 */
	 public abstract boolean mustHaveUniqueName();
	
     private long id;
     private User user;
     /**
      * The title of this entry
     */
     private String title;
     /**
      * The latitude
     */
     private int latitude;
     /**
      * The longitude
     */
     private int longitude;
     /**
      * Last updated
     */
     private Date lastUpdated;
     /**
      * Created date
     */
     private Date created;
     /**
      * Shared?
     */
     private boolean publicVisible;
     /**
      * @gwt.typeArgs <com.aavu.client.domain.Topic>
      */
     private Set scopes = new HashSet();
     
     /**
      * @gwt.typeArgs <com.aavu.client.domain.TopicTypeConnector>
      */
     private Set instances = new HashSet();
     
//     /**
//      * @gwt.typeArgs <com.aavu.client.domain.Topic>
//      */
//     private Set types = new HashSet();
     /**
      * @gwt.typeArgs <com.aavu.client.domain.TopicTypeConnector>
      */
     private Set types = new HashSet();
     /**
      * @gwt.typeArgs <com.aavu.client.domain.Topic>
      */
     private Set occurences = new HashSet();
     /**
      * @gwt.typeArgs <com.aavu.client.domain.Topic>
      */
     private Set associations = new HashSet();

     private Subject subject;

     // Constructors

    /** default constructor */
    public AbstractTopic() {
    }

	/** minimal constructor */
    public AbstractTopic(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
   
    // Property accessors
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    /**       
     *      * The title of this entry
     */
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    /**       
     *      * The latitude
     */
    public int getLatitude() {
        return this.latitude;
    }
    
    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }
    /**       
     *      * The longitude
     */
    public int getLongitude() {
        return this.longitude;
    }
    
    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }
    /**       
     *      * Last updated
     */
    public Date getLastUpdated() {
        return this.lastUpdated;
    }
    
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    /**       
     *      * Created date
     */
    public Date getCreated() {
        return this.created;
    }
    
    public void setCreated(Date created) {
        this.created = created;
    }
    /**       
     *      * Shared?
     */
    public boolean isPublicVisible() {
        return this.publicVisible;
    }
    
    public void setPublicVisible(boolean publicVisible) {
        this.publicVisible = publicVisible;
    }
    public Set getScopes() {
        return this.scopes;
    }
    
    public void setScopes(Set scopes) {
        this.scopes = scopes;
    }
    public Subject getSubject() {
        return this.subject;
    }
    
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
    //<TopicTypeConnector>
    //@Deprecated
    public Set getInstances() {
        return this.instances;
    }
    
    public void setInstances(Set instances) {
        this.instances = instances;
    }
    
    public Set getTypes() {
		return types;
	}

	public void setTypes(Set types) {
		this.types = types;
	}

	public Set getOccurences() {
        return this.occurences;
    }
    
    public void setOccurences(Set occurences) {
        this.occurences = occurences;
    }
    public Set getAssociations() {
        return this.associations;
    }
    
    public void setAssociations(Set associations) {
        this.associations = associations;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AbstractTopic) ) return false;
		 AbstractTopic castOther = ( AbstractTopic ) other; 
         
		 if(this.getId() != 0 && 
				 this.getId() == castOther.getId()){
			 System.out.println("ID EQUALS! "+this.getId()+" "+castOther.getId());
			 return true;
		 }
		 if(this.mustHaveUniqueName()){			 
			 return ( (this.getTitle()==castOther.getTitle()) ||
					 ( this.getTitle()!=null && castOther.getTitle()!=null && this.getTitle().equals(castOther.getTitle()) ) )
					 && (this.isPublicVisible()==castOther.isPublicVisible())
					 && (this.getLatitude()==castOther.getLatitude())
					 && (this.getLongitude()==castOther.getLongitude());
		 }else{
			 
			 //TODO PEND HIGH. What should this be???? ID !=, object !=  
			 return false;
//			 return ((this.isPublicVisible()==castOther.isPublicVisible())
//					 && (this.getLatitude()==castOther.getLatitude())
//					 && (this.getLongitude()==castOther.getLongitude()));
		 }
   }
   
   public int hashCode() {
         int result = 17;
         
         
         
         result = 37 * result + ( getTitle() == null ? 0 : this.getTitle().hashCode() );
         
         
         
         
         result = 37 * result + (this.isPublicVisible()?1:0);
         
         
         
         
         
         
         return result;
   }   


}

