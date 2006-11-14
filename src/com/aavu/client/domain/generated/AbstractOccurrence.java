package com.aavu.client.domain.generated;
// Generated Nov 6, 2006 10:20:40 AM by Hibernate Tools 3.1.0.beta5


import java.util.Date;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * AbstractOccurrence generated by hbm2java
 */
public abstract class AbstractOccurrence  implements IsSerializable, java.io.Serializable {

    // Fields    

     private long id;
     private User user;
     private String title;
     private String data;
     //private Topic topic;
     
     /**
      * Last updated
     */
     private Date lastUpdated;
     /**
      * Created date
     */
     private Date created;

     // Constructors

    /** default constructor */
    public AbstractOccurrence() {
    }

    /** full constructor */
    public AbstractOccurrence(User user, String title, String data, Date lastUpdated, Date created) {
       this.user = user;
       this.title = title;
       this.data = data;
       this.lastUpdated = lastUpdated;
       this.created = created;
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
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public String getData() {
        return this.data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
//    public Topic getTopic() {
//		return topic;
//	}
//
//	public void setTopic(Topic topic) {
//		this.topic = topic;
//	}

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


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AbstractOccurrence) ) return false;
		 AbstractOccurrence castOther = ( AbstractOccurrence ) other; 
         
		 return ( (this.getUser()==castOther.getUser()) || ( this.getUser()!=null && castOther.getUser()!=null && this.getUser().equals(castOther.getUser()) ) )
 && ( (this.getTitle()==castOther.getTitle()) || ( this.getTitle()!=null && castOther.getTitle()!=null && this.getTitle().equals(castOther.getTitle()) ) )
 && ( (this.getData()==castOther.getData()) || ( this.getData()!=null && castOther.getData()!=null && this.getData().equals(castOther.getData()) ) )
 && ( (this.getCreated()==castOther.getCreated()) || ( this.getCreated()!=null && castOther.getCreated()!=null && this.getCreated().equals(castOther.getCreated()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         
         result = 37 * result + ( getUser() == null ? 0 : this.getUser().hashCode() );
         result = 37 * result + ( getTitle() == null ? 0 : this.getTitle().hashCode() );
         result = 37 * result + ( getData() == null ? 0 : this.getData().hashCode() );
         
         result = 37 * result + ( getCreated() == null ? 0 : this.getCreated().hashCode() );
         return result;
   }   


}


