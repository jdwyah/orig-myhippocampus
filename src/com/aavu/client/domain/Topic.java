package com.aavu.client.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aavu.client.domain.generated.AbstractTopic;
import com.aavu.client.widget.autocompletion.Completable;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Topic extends AbstractTopic  implements Completable, IsSerializable{

	public Topic(){
		setLastUpdated(new Date());
		setCreated(new Date());
		setTitle("");
		
		//important. helps activates edit area
		setData("<BODY contentEditable=true></BODY>");		
	}
	
	public Topic(JSONObject value) {
		setId((long) ((JSONNumber)value.get("id")).getValue());
		setTitle(((JSONString)value.get("title")).stringValue());
		setTitle(((JSONString)value.get("data")).stringValue());
	}

	public Topic(User user, String title, String data, Date lastUpdated, Date created, boolean publicVisible, Map metaValues, Set children, Set parents, Set metas, Set occurences, Set associations) {
		super(user, title, data, lastUpdated, created, publicVisible, metaValues, children, parents, metas, occurences, associations);
	}

	public void addMetaValue(Topic meta, Topic metaValue) {

		
		metaValue.getTypes().add(meta);
		meta.getInstances().add(metaValue);

		getMetaValues().put(meta, metaValue);

		//System.out.println(topic+"size "+topic.getMetaValues());
	}



	public void tagTopic(Topic tag){

		tag.getInstances().add(this);
		getTypes().add(tag);
		
		
	}


	
	public void getMetaList(){
		getMetaList(this);
	}	
	private void getMetaList(Topic orig){


//		System.out.println("gl"+orig+"size "+orig.getMetaValues());
//		System.out.println("gl"+orig+"size "+orig.getParents());

		
		for (Iterator iter = this.getTypes().iterator(); iter.hasNext();) {
			Topic parent = (Topic) iter.next();			
			parent.getMetaList(orig);			
		}

		for (Iterator iter = this.getMetas().iterator(); iter.hasNext();) {
			Topic key = (Topic) iter.next();


			//System.out.println("size "+orig.getMetaValues());

			for (Iterator iterator = orig.getMetaValues().keySet().iterator(); iterator.hasNext();) {
				Topic metavaluekey = (Topic) iterator.next();
				//System.out.println("meta value key "+metavaluekey);
			}



			Topic value = (Topic) orig.getMetaValues().get(key);

			System.out.println("key "+key+" val "+value);

		}

	}

	public String toString(){
		return getTitle();	
	}

	public void addMeta(Topic dateRead) {
		getMetas().add(dateRead);
	}

	public void addParent(Topic parent) {
		//getParents().add(parent);		
	}

	public String getCompleteStr() {		
		return getTitle();
	}

	public Set getTags() {
		return getTypes();
	}	

	public void setTags(HashSet tags) {
		setTypes(tags);
	}

	public TopicIdentifier getIdentifier() {
		return new TopicIdentifier(getId(),getTitle());
	}



	public String toPrettyString() {
		return toPrettyString("");
	}
	public String toPrettyString(String indent) {
		try{
			StringBuffer tagsStr = new StringBuffer();
			for (Iterator iter = getTags().iterator(); iter.hasNext();) {
				Tag element = (Tag) iter.next();
				tagsStr.append(indent+"Tag: ");
				tagsStr.append(element.getId()+" "+element.getName());
				tagsStr.append("\n");
				for (Iterator iterator = element.getMetas().iterator(); iterator.hasNext();) {
					Meta meta = (Meta) iterator.next();
					tagsStr.append(indent+"Meta: "+meta.getId()+" "+meta.getName());
					tagsStr.append("\n");
				}
				tagsStr.append(indent+"     -----Tag-----");
				tagsStr.append(element.toPrettyString("     "));
				tagsStr.append("\n"+indent+"     -----End----");
			}

			StringBuffer metaVStr = new StringBuffer();
			metaVStr.append(indent+"Map:\n"+indent);
			for (Iterator iter = getMetaValues().keySet().iterator(); iter.hasNext();) {
				String metaStr = (String) iter.next();
				String mv = (String) getMetaValues().get(metaStr);
				if(mv != null){
					metaVStr.append(metaStr+" -> "+mv+"\n"+indent);
				}else{
					metaVStr.append(metaStr+" "+" -> null\n"+indent);
				}
			}

			return "\nID "+getId()+" title "+getTitle()+"\n"+indent+
			" "+tagsStr+"\n"+indent+
			" "+metaVStr+"\n"+indent+"User:"+((getUser() == null)? "null" : getUser().getId()+"");
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return "Topic Pretty Errored";
		}
	}


}
