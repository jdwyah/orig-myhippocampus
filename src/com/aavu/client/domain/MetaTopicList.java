package com.aavu.client.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.NestedStdAsyncCallback;
import com.aavu.client.async.NestingCallbacks;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.widget.tags.MetaTopicListWidget;
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

	/**
	 * just a temp ref as well
	 */
	private transient List topicNames;


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
		this.topicNames = topicNames;		
	}

	public void addYourNestables(NestingCallbacks nest) {

		final StringBuffer sb = new StringBuffer();
		
		System.out.println("in meta topic list ");
		
		nest.addToNest(new NestedStdAsyncCallback(new StdAsyncCallback("FFF"){

			public void onSuccess(Object result) {
				System.out.println("DOING metaMap put");
				metaMap.put(this, sb.toString());
			}}));
		
		System.out.println("added metaMap put");
		
		
		for (Iterator iter = topicNames.iterator(); iter.hasNext();) {
			final String topicName = (String) iter.next();

			cache.addTopicLookupNested(nest,topicName,new StdAsyncCallback("DDD"){

				public void onSuccess(Object result) {
					
					System.out.println("DOING TopicForName rtn "+result);
					
					Topic topic = (Topic) result;
					if(topic != null){
						
						sb.append(topic.getId()+"");
						sb.append(";");
					}	
				}});
				
				
//			
//			NestedStdAsyncCallback n = new NestedStdAsyncCallback(new StdAsyncCallback("NNN"){
//
//				public void onSuccess(Object result) {
//					
//					System.out.println("DOING topic parse");
//					
//					cache.getTopicForNameA(topicName, new StdAsyncCallback("DDD"){
//
//						public void onSuccess(Object result) {
//							
//							System.out.println("DOING TopicForName rtn "+result);
//							
//							Topic topic = (Topic) result;
//							if(topic != null){
//								
//								sb.append(topic.getId()+"");
//								sb.append(";");
//							}	
//						}});
//				}});

			System.out.println("ADDING Topic Parser");
			
//			nest.addToNest(n);
		}
		
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
				
				
//				Topic t = cache.getTopicById(l);
//				if(t != null){
//					rtn.add(t);
//				}else{
//					GWT.log("null topic for "+string,null);
//				}
			}
		}
		return rtn;		
	}


}