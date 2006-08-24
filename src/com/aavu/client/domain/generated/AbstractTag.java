package com.aavu.client.domain.generated;
// Generated Aug 24, 2006 9:19:32 AM by Hibernate Tools 3.1.0.beta4

import com.aavu.client.domain.User;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;


/**
 * AbstractTag generated by hbm2java
 */

public abstract class AbstractTag  implements java.io.Serializable, IsSerializable {


    // Fields    

     private long id;
     /**
      * The user which created us
     */
     private User user;
     /**
      * Shared?
     */
     private boolean publicVisible;
     /**
      * @gwt.typeArgs <com.aavu.client.domain.Meta> 
      */
     private List metas = new ArrayList();
     /**
      * The type.
     */
     private String name;


    // Constructors

    /** default constructor */
    public AbstractTag() {
    }

    
    /** full constructor */
    public AbstractTag(User user, boolean publicVisible, List metas, String name) {
        this.user = user;
        this.publicVisible = publicVisible;
        this.metas = metas;
        this.name = name;
    }
    

   
    // Property accessors

    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    /**       
     *      * The user which created us
     */

    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
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

    public List getMetas() {
        return this.metas;
    }
    
    public void setMetas(List metas) {
        this.metas = metas;
    }
    /**       
     *      * The type.
     */

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AbstractTag) ) return false;
		 AbstractTag castOther = ( AbstractTag ) other; 
         
		 return ( (this.getUser()==castOther.getUser()) || ( this.getUser()!=null && castOther.getUser()!=null && this.getUser().equals(castOther.getUser()) ) )
 && (this.isPublicVisible()==castOther.isPublicVisible())
 && ( (this.getName()==castOther.getName()) || ( this.getName()!=null && castOther.getName()!=null && this.getName().equals(castOther.getName()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         
         result = 37 * result + ( getUser() == null ? 0 : this.getUser().hashCode() );
         result = 37 * result + (this.isPublicVisible()?1:0);
         
         result = 37 * result + ( getName() == null ? 0 : this.getName().hashCode() );
         return result;
   }   





}
