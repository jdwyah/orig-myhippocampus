package com.aavu.client.domain.subjects.generated;
// Generated Nov 3, 2006 8:10:46 AM by Hibernate Tools 3.1.0.beta5


import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * AbstractSubject generated by hbm2java
 */
public abstract class AbstractSubject  implements IsSerializable, java.io.Serializable {

    // Fields    

     private long id;
     private String foreignID;
     private String name;
     /**
 	 * @gwt.typeArgs <com.aavu.client.domain.Topic>
 	 */
 	private Set topics = new HashSet();
     

     // Constructors

    /** default constructor */
    public AbstractSubject() {
    }

    /** full constructor */
    public AbstractSubject(String foreignID, String name, Set topics) {
       this.foreignID = foreignID;
       this.name = name;
       this.topics = topics;
    }
   

	// Property accessors
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    public String getForeignID() {
        return this.foreignID;
    }
    
    public void setForeignID(String foreignID) {
        this.foreignID = foreignID;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Set getTopics() {
        return this.topics;
    }
    
    public void setTopics(Set topics) {
        this.topics = topics;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AbstractSubject) ) return false;
		 AbstractSubject castOther = ( AbstractSubject ) other; 
         
		 return ( (this.getForeignID()==castOther.getForeignID()) || ( this.getForeignID()!=null && castOther.getForeignID()!=null && this.getForeignID().equals(castOther.getForeignID()) ) )
 && ( (this.getName()==castOther.getName()) || ( this.getName()!=null && castOther.getName()!=null && this.getName().equals(castOther.getName()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         
         result = 37 * result + ( getForeignID() == null ? 0 : this.getForeignID().hashCode() );
         result = 37 * result + ( getName() == null ? 0 : this.getName().hashCode() );
         
         return result;
   }   


}


