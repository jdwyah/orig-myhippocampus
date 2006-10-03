package com.aavu.client.domain;

import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;



/**
 * Meta generated by hbm2java
 */

public abstract class Meta extends Topic implements IsSerializable{

     
     public abstract Widget getEditorWidget(Map map); 
     public abstract Widget getWidget(Map mmp);
     public abstract String getType();
     
   /**
    * return the Index into a topic's MetaValue map
    * @return
    */
   public String toMapIdx(){
	   return getId()+"";
   }
   
   public String getName(){
	   return getTitle();
   }
   
   /**
    * Does our editor widget need a saveCallback?
    * 
    * This is done because I don't think we can user instanceof
    * to determine this on the fly.
    * 
    * @return
    */
   public boolean needsSaveCallback() {
	return false;
   }

}
