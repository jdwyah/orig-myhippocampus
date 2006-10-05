package com.aavu.client.domain;

import com.google.gwt.user.client.ui.Widget;


public class MetaTopicList  {

//	private static final String TYPE = "Topic List";
//
//	/**
//	 * ick static cachce ref..
//	 * gwt threadLocal?
//	 * 
//	 */
//	private static HippoCache cache;
//
//
//	/**
//	 * transient, since this is just a ref to the currently referred to topic
//	 */
//	private transient Map metaMap;
//
//	//@Override
//	public boolean needsSaveCallback() {
//		return true;
//	}
//
//	//@Override
//	public Widget getEditorWidget(Map metaMap) {
//		this.metaMap = metaMap;
//
//		MetaTopicListWidget widget = new MetaTopicListWidget(this,true);		
//		return widget;		
//	}
//
//	//@Override
//	public String getType() {
//		return TYPE;
//	}
//
//	//@Override
//	public Widget getWidget(Map metaMap) {
//		this.metaMap = metaMap;
//		System.out.println("map "+metaMap);
//		MetaTopicListWidget widget = new MetaTopicListWidget(this,false);
//		return widget;
//	}
//
//
//
//
//	/**
//	 * takes care of what is essentially serializing the list of topics
//	 * 
//	 * this list of strings -> topcis is assumed to all be in the cache..
//	 * should be since we looked it up to get it here... 
//	 * ...unless they typed w/o complete
//	 *  
//	 * also need to allow them to add new topics on the fly.
//	 * ...that's tricky bc w/ this serialization scheme since we don't
//	 * hava an ID to save... 
//	 * 
//	 * 
//	 * @param topicNames
//	 */
//	public void setVals(List topicNames) {
//		
//		StringBuffer sb = new StringBuffer();
//		
//		System.out.println("in meta topic list "+topicNames);
//		
//		for (Iterator iter = topicNames.iterator(); iter.hasNext();) {
//			final String topicName = (String) iter.next();
//
//			System.out.println("lookup "+topicName);
//			
//			Topic topic = cache.getTopicCache().getTopicForName(topicName);
//
//			if(topic != null){
//				sb.append(topic.getId()+"");
//				sb.append(";");
//			}
//			System.out.println("found: +"+topic);
//
//		}
//		System.out.println("putting "+toMapIdx()+" | "+sb.toString());
//		metaMap.put(toMapIdx(), sb.toString());
//	}
//
//
//	public static void setCache(HippoCache cache) {
//		MetaTopicList.cache = cache;
//	}
//
//	/**
//	 * return list<topic> from topic_id ; sep list
//	 * 
//	 */
//	public List getVals() {
//		String s = (String) metaMap.get(toMapIdx());
//		List rtn = new ArrayList();
//
//		System.out.println("getVals "+s);
//		if(s != null){
//			String[] split = s.split(";");
//			for (int i = 0; i < split.length; i++) {
//				String string = split[i];
//				long l = Long.parseLong(string);			
//				
//				System.out.println("lookup "+l);
//				Topic t = cache.getTopicCache().getTopicById(l);
//				if(t != null){
//					rtn.add(t);
//				}else{
//					GWT.log("null topic for "+string,null);
//				}
//			}
//		}
//		return rtn;		
//	}


}