package com.aavu.client.domain.generated;
// Generated Aug 18, 2006 9:39:33 PM by Hibernate Tools 3.1.0.beta4

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * AbstractMetaValue generated by hbm2java
 */

public abstract class AbstractMetaValue  implements java.io.Serializable,IsSerializable {


    // Fields    

     private long id;
     private Topic topic;
     /**
      * The value in its string representation
     */
     private String value;


    // Constructors

    /** default constructor */
    public AbstractMetaValue() {
    }

    
    /** full constructor */
    public AbstractMetaValue(Topic topic, String value) {
        this.topic = topic;
        this.value = value;
    }
    

   
    // Property accessors

    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public Topic getTopic() {
        return this.topic;
    }
    
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    /**       
     *      * The value in its string representation
     */

    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AbstractMetaValue) ) return false;
		 AbstractMetaValue castOther = ( AbstractMetaValue ) other; 
         
		 return ( (this.getValue()==castOther.getValue()) || ( this.getValue()!=null && castOther.getValue()!=null && this.getValue().equals(castOther.getValue()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         
         
         result = 37 * result + ( getValue() == null ? 0 : this.getValue().hashCode() );
         return result;
   }   





}
