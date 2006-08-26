package com.aavu.client.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.widget.tags.MetaTopicListWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

public class MetaTopicList extends Meta {

	private static final String TYPE = "Topic List";
		
	/**
	 * ick static cachce ref..
	 * gwt threadLocal?
	 * 
	 */
	private static HippoCache cache;
		
	
	/**
	 * transient, since this is just a ref to the currently referred to topic
	 */
	private transient Map metaMap;
	
	
	//@Override
	public boolean needsSaveCallback() {
		return true;
	}

	//@Override
	public Widget getEditorWidget(Map metaMap) {
		this.metaMap = metaMap;
		
		MetaTopicListWidget widget = new MetaTopicListWidget(this);		
		return widget;		
	}

	//@Override
	public String getType() {
		return TYPE;
	}

	//@Override
	public Widget getWidget(Map metaMap) {
		this.metaMap = metaMap;
		System.out.println("map "+metaMap);
		MetaTopicListWidget widget = new MetaTopicListWidget(this);
		return widget;
	}

	
	
	
	/**
	 * takes care of what is essentially serializing the list of topics
	 * 
	 * this list of strings -> topcis is assumed to all be in the cache..
	 * should be since we looked it up to get it here... 
	 * ...unless they typed w/o complete
	 *  
	 * also need to allow them to add new topics on the fly.
	 * ...that's tricky bc w/ this serialization scheme since we don't
	 * hava an ID to save... 
	 * 
	 * 
	 * @param topicNames
	 */
	public void setVals(List topicNames) {
		StringBuffer sb = new StringBuffer();
		GWT.log("setVals ", null);
		for (Iterator iter = topicNames.iterator(); iter.hasNext();) {
			String topicName = (String) iter.next();
			GWT.log("-"+topicName, null);
			Topic topic = cache.getTopicForName(topicName);
			
			GWT.log("t:"+topic, null);
			if(topic != null){
				sb.append(topic.getId());
				sb.append(";");
			}else{
				GWT.log("setVals, topic "+topicName+" not in cache", null);
			}
		}
		
		metaMap.put(this, sb.toString());
	}

	public static void setCache(HippoCache cache) {
		MetaTopicList.cache = cache;
	}

	/**
	 * return list<string> from topic_id ; sep list
	 * 
	 */
	public List getVals() {
		String s = (String) metaMap.get(this);
		List rtn = new ArrayList();
		
		if(s != null){
			String[] split = s.split(";");
			for (int i = 0; i < split.length; i++) {
				String string = split[i];
				long l = Long.parseLong(string);			
				Topic t = cache.getTopicById(l);
				if(t != null){
					rtn.add(t);
				}else{
					GWT.log("null topic for "+string,null);
				}
			}
		}
		return rtn;		
	}
	

}