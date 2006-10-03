package com.aavu.client.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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

	public void addMetaValue(Topic meta, Topic metaValue) {

		metaValue.getParents().add(meta);
		meta.getChildren().add(metaValue);

		getMetaValues().put(meta, metaValue);

		//System.out.println(topic+"size "+topic.getMetaValues());
	}



	public void tagTopic(Topic tag){

		getParents().add(tag);
		tag.getChildren().add(this);

	}


	public void test(){

		Topic LINK = new Topic();
		
		Topic foo = new Topic();
		Topic bar = new Topic();
		
		Topic theLink = new Topic();
		theLink.getChildren().add(foo);
		theLink.getChildren().add(bar);
		theLink.addParent(LINK);
		
		foo.getAssociations().add(theLink);
		
		if(getParents().contains(LINK)){
			
		}
		
	}
	
	public void getMetaList(){
		getMetaList(this);
	}	
	private void getMetaList(Topic orig){


//		System.out.println("gl"+orig+"size "+orig.getMetaValues());
//		System.out.println("gl"+orig+"size "+orig.getParents());

		for (Iterator iter = this.getParents().iterator(); iter.hasNext();) {
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
		getParents().add(parent);		
	}

	public String getCompleteStr() {		
		return getTitle();
	}

	public Set getTags() {
		return getParents();
	}	

	public void setTags(HashSet tags) {
		setParents(tags);
	}

	public TopicIdentifier getIdentifier() {
		return new TopicIdentifier(getId(),getTitle());
	}



	public String toPrettyString() {
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
			for (Iterator iter = getMetaValues().keySet().iterator(); iter.hasNext();) {
				String metaStr = (String) iter.next();
				String mv = (String) getMetaValues().get(metaStr);
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
