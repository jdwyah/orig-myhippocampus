package com.aavu.client.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.generated.AbstractTopic;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.widget.autocompletion.Completable;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.ComplexPanel;

public class Topic extends AbstractTopic  implements Completable, IsSerializable{


	public Topic(){
		setLastUpdated(new Date());
		setCreated(new Date());
		setTitle("");		
	}

	public Topic(JSONObject value) {
		setId((long) ((JSONNumber)value.get("id")).getValue());
		setTitle(((JSONString)value.get("title")).stringValue());
		setTitle(((JSONString)value.get("data")).stringValue());
	}

	
	public Topic(User u, String title) {
		this();
		setUser(u);
		setTitle(title);
	}

	/**
	 * basically a temporary wrapper for a TopicIdentifier to masquerade as a real Topic
	 * 
	 * Used by SeeAlso
	 */
	public Topic(TopicIdentifier topicIdent) {
		setId(topicIdent.getTopicID());
		setTitle(topicIdent.getTopicTitle());		
	}

	
	public void addMetaValue(Meta meta, Topic metaValue) {

		System.out.println("Topic.addMetaValue: "+metaValue);
		
//		Topic cur_val = getSingleMetaValueFor(meta);
//		if(cur_val != null){
//			
//		}		
		
		Association assoc = getAssociationForMetaOrNull(meta);
		if(assoc == null){
			System.out.println("addMetaValue: create new assoc");
			assoc = new Association();
			
			
			
		}else{
			Topic cur_val = (Topic) assoc.getMembers().iterator().next();
			System.out.println("cur_v "+cur_val);			
			
			if(cur_val.equals(metaValue)){
				System.out.println("Were Equal, Moving on. C: "+cur_val.getTitle()+" mv "+metaValue.getTitle());
				return;
			}else{
				System.out.println("cur "+cur_val.getIdentifier());
				System.out.println("new "+metaValue.getIdentifier());
			}
		}
		
		assoc.setTitle(getTitle()+" to "+metaValue.getTitle());
		
		//redundant if we've already created
		assoc.addType(meta);
		
		assoc.getMembers().clear();
		assoc.getMembers().add(metaValue);
						
		metaValue.addType(meta);
		
		//unecessary now that it's the same table
		//meta.getInstances().add(metaValue);
		
		
		
		//redundant if we've already created
		getAssociations().add(assoc);
		
		//System.out.println(topic+"size "+topic.getMetaValues());
	}
	
	

	public void setMetaValue(Meta meta, Topic metaValue){
		getMetaValuesFor(meta).add(metaValue);
	}


	public boolean tagTopic(Topic tag){


//		System.out.println("tagging with "+tag.getTitle());
//		System.out.println("alread tagged "+tag.getInstances());
//		System.out.println("alread tagged "+tag.getInstances().size());

//		Window.alert("FOO ");
//		Window.alert("f: "+tag.getInstances()+" ");
//		for (Iterator iter = tag.getInstances().iterator(); iter.hasNext();) {
//		Topic element = (Topic) iter.next();
//		Window.alert("Tag's instances: "+element.toPrettyString());
//		}
//		Window.alert("this: "+toPrettyString());

		
		
		//NOTE! shoudl be unecessary
		//tag.getInstances().add(this);
		
		
		//Window.alert("FOO2");
//		Window.alert("f: "+getTypes());

//		Window.alert("tag "+tag);
//		Window.alert("pretty "+tag.toPrettyString());


		return addType(tag);
		
//		Window.alert("FOO3");

	}

	public boolean removeTag(Tag tag) {

		System.out.println("REMOVE-------------------");
		System.out.println(tag.toPrettyString());
		System.out.println("FROM-------------------");
		System.out.println(toPrettyString());
		
		boolean b = true;//tag.getInstances().remove(this);
		
		boolean b2 = getTypesAsTopics().remove(tag);
		
		System.out.println("Remove Tag: "+b+" "+b2);
		return b && b2;
		
//		return tag.getInstances().remove(this)
//		&&
//		getTypes().remove(tag);
	}

	
	
	public Entry getLatestEntry(){
		Entry rtn = null;
		for (Iterator iter = getOccurences().iterator(); iter.hasNext();) {
			Occurrence occur = (Occurrence) iter.next();
			if(occur instanceof Entry){
				rtn = (Entry) occur;
			}
		}
		if(rtn == null){
			Entry initialEntry = new Entry();
			getOccurences().add(initialEntry);
			return initialEntry;
		}
		return rtn;
	}

	/**
	 * limitted to just 1 for now
	 * 
	 * @return
	 */
	public MindTreeOcc getMindTree() {
		MindTreeOcc rtn = null;
		for (Iterator iter = getOccurences().iterator(); iter.hasNext();) {
			Occurrence occur = (Occurrence) iter.next();
			if(occur instanceof MindTreeOcc){
				rtn = (MindTreeOcc) occur;
				break;
			}
		}
		if(rtn == null){
			MindTreeOcc initialTree = new MindTreeOcc(this);
						
			//getOccurences().add(initialTree);
			
			return initialTree;
		}
		return rtn;
	}


	public String toString(){
		return getTitle();	
	}

	/**
	 * Create an Association of type Meta
	 * @param meta
	 */
	public void addMeta(Meta meta) {		
		Association a = new Association(this,meta);		
		getAssociations().add(a);		
	}

	public String getCompleteStr() {		
		return getTitle();
	}

	public Set getTags() {
		return getTypesAsTopics();
	}	

	

	public TopicIdentifier getIdentifier() {
		return new TopicIdentifier(getId(),getTitle());
	}



	public String toPrettyString() {
		return toPrettyString("",this);
	}
	public String toPrettyString(String indent, Topic prev) {
		
		StringBuffer tagsStr = new StringBuffer();
		StringBuffer metaVStr = new StringBuffer();
		StringBuffer instanceStr = new StringBuffer();
		StringBuffer occurencesStr = new StringBuffer();
		
		try{			
			tagsStr.append(indent+"Types: \n"+indent);
			for (Iterator iter = getTypesAsTopics().iterator(); iter.hasNext();) {
				Topic element = (Topic) iter.next();				
				tagsStr.append(element.getId()+" "+element.getTitle());
				tagsStr.append("\n");
				for (Iterator iterator = element.getMetas().iterator(); iterator.hasNext();) {
					Meta meta = (Meta) iterator.next();
					tagsStr.append(indent+"Meta: "+meta.getId()+" "+meta.getName());
					tagsStr.append("\n");
				}
				tagsStr.append(indent+"     -----Type-----");
				if(element == prev){
					tagsStr.append("Found original. No recurse.");
				}else{
					tagsStr.append(element.toPrettyString("     ",this));
				}
				tagsStr.append("\n"+indent+"     -----End----");
			}
		}catch(Exception e){
			tagsStr.append(indent+"Topic Pretty Types Errored");
		}
		try{			
			metaVStr.append(indent+"Associations:\n"+indent);
			for (Iterator iter = getAssociations().iterator(); iter.hasNext();) {
				Association assoc = (Association) iter.next();
				
				metaVStr.append("ASS: "+assoc.getTitle()+" "+assoc.getId()+"\n"+indent);
				metaVStr.append("Types:\n"+indent);
				for (Iterator iterator = assoc.getTypesAsTopics().iterator(); iterator
						.hasNext();) {
					Topic type = (Topic) iterator.next();
					metaVStr.append("T: "+type.getTitle()+" "+type.getId()+"\n"+indent);
				}
				metaVStr.append("Members:\n"+indent);
				for (Iterator iterator = assoc.getMembers().iterator(); iterator
						.hasNext();) {
					Topic member = (Topic) iterator.next();
					metaVStr.append("M: "+member.getTitle()+" "+member.getId()+"\n"+indent);
				}
								
			}
		}catch(Exception e){
			metaVStr.append(indent+"Topic Pretty Association Errored");
		}
		
//		try{			
//			instanceStr.append(indent+"Instances:\n"+indent);
//			for (Iterator iter = getInstances().iterator(); iter.hasNext();) {
//				TopicTypeConnector conn = (TopicTypeConnector) iter.next();
//				Topic instance = conn.getTopic();
//				instanceStr.append("Instance: "+instance.getTitle()+" "+instance.getId()+"\n"+indent);							
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			instanceStr.append(indent+"Topic Pretty Instances Errored "+e);
//		}
		try{			
			occurencesStr.append(indent+"Occurrences:\n"+indent);
			for (Iterator iter = getOccurences().iterator(); iter.hasNext();) {
				Occurrence occurence = (Occurrence) iter.next();
				
				occurencesStr.append("Occurrence: "+occurence.getTitle()+" "+occurence.getId()+" "+occurence.getData()+"\n"+indent);							
			}
		}catch(Exception e){
			occurencesStr.append(indent+"Topic Pretty Occurrences Errored");
		}
		return "\nID "+getId()+" title "+getTitle()+"\n"+indent+
		" "+tagsStr+"\n"+indent+
		" "+metaVStr+"\n"+indent+
		" "+instanceStr+"\n"+indent+
		" "+occurencesStr+"\n"+indent+
		"User:"+((getUser() == null)? "null" : getUser().getId()+"");

	}

	/**
	 * Associations Contract will update our associations set in the DAO/Service layer.
	 * Don't need to take care of that here.
	 *
	 * Will return the SeeAlso association or Create a new one if necessary.
	 * 
	 * WARN: for the time being don't let a Topic have multiple SeeAlso's!
	 * (this would only be usefull if a Topic had one SeeAlso in one scope and 
	 * a different SeeAlso in another)
	 * 
	 */
	public Association getSeeAlsoAssociation() {

		for (Iterator iter = getAssociations().iterator(); iter.hasNext();) {
			Association association = (Association) iter.next();
			for (Iterator iterator = association.getTypesAsTopics().iterator(); iterator.hasNext();) {
				
				Topic possibleSee = (Topic) iterator.next();				
				
				//TODO why is the strcmp necessary? this used to work,
				//but now the possibleSee is class com.aavu.client.domain.Topic$$EnhancerByCGLIB$$b0a3c443
				//and not instanceof MetaSeeAlso
				if (possibleSee instanceof MetaSeeAlso
						||
						possibleSee.getTitle().equals(MetaSeeAlso.UBER_TITLE)){
					System.out.println("return existing assoc");
					return association;
				}				
			}
		}		
		System.out.println("getSeeAlsoAssociation: create new assoc");
		return new Association(this,new MetaSeeAlso());				
	}
	/**
	 * NOTE: calling getMetas().add() won't do anything. Use addMeta()
	 * 
	 * This was returning MetaValue associations as well, until we added members.size() == 0 
	 * is this an ok fix?
	 * 
	 * @return
	 */
	public Set getMetas() {
		Set metas = new HashSet();
		for (Iterator iter = getAssociations().iterator(); iter.hasNext();) {
			Association association = (Association) iter.next();
			
			for (Iterator iterator = association.getTypesAsTopics().iterator(); iterator.hasNext();) {
				Topic possibleMeta = (Topic) iterator.next();
				if (possibleMeta instanceof Meta
						&& association.getMembers().size() == 0) {					
					metas.add(possibleMeta);				
				}	
			}			
		}
		return metas;		
	}
	private Association getAssociationForMetaOrNull(Meta meta) {
		for (Iterator iter = getAssociations().iterator(); iter.hasNext();) {
			Association association = (Association) iter.next();
			
			for (Iterator iterator = association.getTypesAsTopics().iterator(); iterator.hasNext();) {
				Topic possible = (Topic) iterator.next();
				if (possible == meta) {	
					return association;									
				}	
			}			
		}
		return null;
	}
	
	public Set getMetaValuesFor(Meta meta){		
		Association assoc = getAssociationForMetaOrNull(meta);
		if(assoc != null){
			return assoc.getMembers();
		}else{
			return new HashSet();	
		}		
	}
	/**
	 * Convenience method for when you know there's only one member.
	 * 
	 * @param meta
	 * @return
	 */
	public Topic getSingleMetaValueFor(Meta meta){
		Set s =  getMetaValuesFor(meta);
		if(s.size() > 0)
			return (Topic)s.iterator().next();
		else
			return null;
	}

	public String compare(Object other) {
		StringBuffer sb = new StringBuffer();
		if ( (this == other ) ) sb.append("true obj id");
		if ( (other == null ) ) sb.append("false other null");
		if ( !(other instanceof Topic) ) sb.append("false other diff type");
		Topic castOther = ( Topic ) other; 
		if(sb.length() > 0){
			return sb.toString();
		}
		sb.append("Compare:\n");
		sb.append(this.getId()+" id "+castOther.getId()+"\n");
		sb.append(this.getTitle()+" title "+castOther.getTitle()+"\n");
		sb.append(this.getCreated()+" created "+castOther.getCreated()+"\n");
		sb.append(this.getLatestEntry()+" data "+castOther.getLatestEntry()+"\n");
		sb.append(this.getUser()+" user "+castOther.getUser()+"\n");
		sb.append(this.isPublicVisible()+" public "+castOther.isPublicVisible()+"\n");
		
		sb.append("EQ: "+(( (this.getTitle()==castOther.getTitle()) || ( this.getTitle()!=null && castOther.getTitle()!=null && this.getTitle().equals(castOther.getTitle()) ) )		
		 && (this.isPublicVisible()==castOther.isPublicVisible())));
		
		return sb.toString();
	}

	public void addSeeAlso(TopicIdentifier identifier) {
		Association cur = getSeeAlsoAssociation();
		
		cur.getMembers().add(new Topic(identifier));
		
		getAssociations().add(cur);
	}


	/**
	 * do we need to update both sides of this association? 
	 * 
	 * @param type
	 * @return
	 */
	public boolean addType(Topic type) {
		TopicTypeConnector conn = new TopicTypeConnector(this,type,-1.0,-1.0);
		//type.getInstances().add(conn);
		return getTypes().add(conn);
		//return getTypes().add(new Topic(topic,-1,-1));		
	}
	
	/**
	 * TODO please help me understand this. see commented function below. hashcode == and TTC == && .eq(),
	 * but it won't remove it from the set. why???
	 * 
	 * Workaround here is to make a new set and add everything that shouldn't be deleted. 
	 * 
	 * @param topic
	 */
	public void removeType(Topic topic){
		//System.out.println("Remove T size "+getTypes().size());
		Set replacementSet = new HashSet();
		
		for (Iterator iter = getTypes().iterator(); iter.hasNext();) {
			
			TopicTypeConnector twl = (TopicTypeConnector) iter.next();
			
			if(!twl.getType().equals(topic)){
				replacementSet.add(twl);
			}			
		}
		setTypes(replacementSet);
				
		//System.out.println("Remove T size "+getTypes().size());
	}
	
//	public void removeType2(Topic topic){
//		
//		System.out.println("Remove T size "+getTypes().size());
//		
//		TopicTypeConnector ttc = null;
//		
//		for (Iterator iter = getTypes().iterator(); iter.hasNext();) {
//			
//			TopicTypeConnector twl = (TopicTypeConnector) iter.next();
//			
//			System.out.println("remove "+topic+" Top "+twl.getTopic()+" type "+twl.getType());
//			
//			if(twl.getType().equals(topic)){
//				System.out.println("eq, removing");
//				
//				System.out.println("twl.getHash "+twl.hashCode());
//				ttc = twl;
//				break;
//			}			
//		}
//		
//		System.out.println("2nd.getHash "+getTypes().iterator().next().hashCode());
//		
//	//	HashSet<E>k;
//		System.out.println("rem res "+getTypes().remove(ttc));
//		
//		for (Iterator iter = getTypes().iterator(); iter.hasNext();) {
//		
//			TopicTypeConnector twl = (TopicTypeConnector) iter.next();
//			
//			System.out.println("H"+(ttc.hashCode() == twl.hashCode()));
//			System.out.println("=="+(twl == ttc));
//			System.out.println("=="+twl.equals(ttc));
//			
//			System.out.println("rem twl "+getTypes().remove(twl));
//			System.out.println("rem ttc "+getTypes().remove(ttc));
//		}
//		
//		System.out.println("Remove T size "+getTypes().size());
//	}
	
	public Set getTypesAsTopics(){
		
		Set rtn = new HashSet();
		for (Iterator iter = getTypes().iterator(); iter.hasNext();) {
			TopicTypeConnector twl = (TopicTypeConnector) iter.next();
			rtn.add(twl.getType());
		}
		return rtn;
	}
	
	
	/**
	 * Overridable. 
	 * 
	 * Used by TopicDAO to ensure unique names (if we want them) 
	 * 
	 * @return
	 */
	public boolean mustHaveUniqueName() {		
		return true;
	}

	public Set getWebLinks() {
		Set rtn = new HashSet();
		for (Iterator iter = getOccurences().iterator(); iter.hasNext();) {
			Occurrence occ = (Occurrence) iter.next();			
			if(occ instanceof WebLink){
				rtn.add(occ);
			}
		}
		return rtn;
	}


	
}
