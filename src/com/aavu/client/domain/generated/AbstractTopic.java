package com.aavu.client.domain.generated;
// Generated Oct 2, 2006 4:54:09 PM by Hibernate Tools 3.1.0.beta4

import com.aavu.client.domain.User;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * AbstractTopic generated by hbm2java
 */

public abstract class AbstractTopic  implements IsSerializable, java.io.Serializable {


    // Fields    

     private long id;
     private User user;
     /**
      * The title of this entry
     */
     private String title;
     /**
      * The optional data element
     */
     private String data;
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
      * @gwt.typeArgs <com.aavu.client.domain.Topic,com.aavu.client.domain.Topic>
      */
     private Map metaValues = new HashMap(0);
     /**
      * @gwt.typeArgs <com.aavu.client.domain.Topic>
      */
     private Set children = new HashSet(0);
     /**
      * @gwt.typeArgs <com.aavu.client.domain.Topic>
      */
     private Set parents = new HashSet(0);
     /**
      * @gwt.typeArgs <com.aavu.client.domain.Topic>
      */
     private Set metas = new HashSet(0);
     /**
      * @gwt.typeArgs <com.aavu.client.domain.Topic>
      */
     private Set occurences = new HashSet(0);
     /**
      * @gwt.typeArgs <com.aavu.client.domain.Topic>
      */
     private Set associations = new HashSet(0);


    // Constructors

    /** default constructor */
    public AbstractTopic() {
    }

    
    /** full constructor */
    public AbstractTopic(User user, String title, String data, Date lastUpdated, Date created, boolean publicVisible, Map metaValues, Set children, Set parents, Set metas, Set occurences, Set associations) {
        this.user = user;
        this.title = title;
        this.data = data;
        this.lastUpdated = lastUpdated;
        this.created = created;
        this.publicVisible = publicVisible;
        this.metaValues = metaValues;
        this.children = children;
        this.parents = parents;
        this.metas = metas;
        this.occurences = occurences;
        this.associations = associations;
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
     *      * The optional data element
     */

    public String getData() {
        return this.data;
    }
    
    public void setData(String data) {
        this.data = data;
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

    public Map getMetaValues() {
        return this.metaValues;
    }
    
    public void setMetaValues(Map metaValues) {
        this.metaValues = metaValues;
    }

    public Set getChildren() {
        return this.children;
    }
    
    public void setChildren(Set children) {
        this.children = children;
    }

    public Set getParents() {
        return this.parents;
    }
    
    public void setParents(Set parents) {
        this.parents = parents;
    }

    public Set getMetas() {
        return this.metas;
    }
    
    public void setMetas(Set metas) {
        this.metas = metas;
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
         
		 return ( (this.getTitle()==castOther.getTitle()) || ( this.getTitle()!=null && castOther.getTitle()!=null && this.getTitle().equals(castOther.getTitle()) ) )
 && ( (this.getData()==castOther.getData()) || ( this.getData()!=null && castOther.getData()!=null && this.getData().equals(castOther.getData()) ) )
 && (this.isPublicVisible()==castOther.isPublicVisible());
   }
   
   public int hashCode() {
         int result = 17;
         
         
         
         result = 37 * result + ( getTitle() == null ? 0 : this.getTitle().hashCode() );
         result = 37 * result + ( getData() == null ? 0 : this.getData().hashCode() );
         
         
         result = 37 * result + (this.isPublicVisible()?1:0);
         
         
         
         
         
         
         return result;
   }   





}
