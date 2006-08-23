package com.aavu.client.domain.generated;

import com.google.gwt.user.client.rpc.IsSerializable;
// Generated Aug 23, 2006 3:06:00 PM by Hibernate Tools 3.1.0.beta4



/**
 * AbstractUser generated by hbm2java
 */

public abstract class AbstractUser  implements java.io.Serializable, IsSerializable {


    // Fields    

     private long id;
     private String username;
     private String password;
     private boolean enabled;
     private boolean supervisor;


    // Constructors

    /** default constructor */
    public AbstractUser() {
    }

    
    /** full constructor */
    public AbstractUser(String username, String password, boolean enabled, boolean supervisor) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.supervisor = supervisor;
    }
    

   
    // Property accessors

    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSupervisor() {
        return this.supervisor;
    }
    
    public void setSupervisor(boolean supervisor) {
        this.supervisor = supervisor;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AbstractUser) ) return false;
		 AbstractUser castOther = ( AbstractUser ) other; 
         
		 return ( (this.getUsername()==castOther.getUsername()) || ( this.getUsername()!=null && castOther.getUsername()!=null && this.getUsername().equals(castOther.getUsername()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         
         result = 37 * result + ( getUsername() == null ? 0 : this.getUsername().hashCode() );
         
         
         
         return result;
   }   





}