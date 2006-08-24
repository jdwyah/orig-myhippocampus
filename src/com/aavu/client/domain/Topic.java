package com.aavu.client.domain;
//Generated Jul 18, 2006 12:44:47 PM by Hibernate Tools 3.1.0.beta4

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.domain.generated.AbstractTopic;
import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * TopicGWT generated by hbm2java
 */

public class Topic extends AbstractTopic implements IsSerializable{



	public String toString(){
		return getTitle()+" "+getText();
	}

	public Object toPrettyString() {
		try{
			StringBuffer tagsStr = new StringBuffer();
			for (Iterator iter = getTags().iterator(); iter.hasNext();) {
				Tag element = (Tag) iter.next();
				tagsStr.append("Tag: ");
				tagsStr.append(element.getId()+" "+element.getName());
				tagsStr.append("\n");
				for (Iterator iterator = element.getMetas().iterator(); iterator.hasNext();) {
					Meta meta = (Meta) iterator.next();
					tagsStr.append("Meta: "+meta.getId()+" "+meta.getName());
					tagsStr.append("\n");
				}
			}

			StringBuffer metaVStr = new StringBuffer();
			metaVStr.append("Map:\n");
			for (Iterator iter = getMetaValueStrs().keySet().iterator(); iter.hasNext();) {
				String metaStr = (String) iter.next();
				String mv = (String) getMetaValueStrs().get(metaStr);
				if(mv != null){
					metaVStr.append(metaStr+" -> "+mv+"\n");
				}else{
					metaVStr.append(metaStr+" "+" -> null\n");
				}
			}

			return "ID "+getId()+" title "+getTitle()+"\n"+
			" "+tagsStr+"\n"+
			" "+metaVStr;
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return "Topic Pretty Errored";
		}
	}


}
