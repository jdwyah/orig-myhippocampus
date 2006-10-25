package com.aavu.client.gui;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.User;
import com.aavu.client.gui.ext.FlashContainer;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public class Ocean extends FlashContainer {

	private Manager manager;

	public Ocean(Manager manager){
		super("islands.swf","ocean");
		this.manager = manager;
		
		setStyleName("H-Ocean");		
	}
	
	//@Override
	protected void onLoad() {
		initIslands();
	}

	/**
	 * Will init after async "getTagStats()" call
	 *
	 */
	private void initIslands(){
		System.out.println("Init Islands...");		
				
		manager.getTagCache().getTagStats(new StdAsyncCallback("Get Tag Stats"){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				TagStat[] tagStats = (TagStat[]) result;

				System.out.println("TagStat Result "+tagStats);

				runCommandDeferred(getCommand(manager.getUser(),tagStats, manager.getTopicCache().getNumberOfTopics()));

			}});
		
	}
	
	protected void callbackOverride(String command, int arg){
		if(command.equals("islandClicked")){
			manager.showTopicsForTag(arg);
		}
		
	}
	
	
	
	protected String islandObj(long id, String name,int size){
		return "<object>"+numberProp("id",id)+stringProp("tag",name)+numberProp("size",size)+"</object>";
	}


	protected String getCommand(User user,TagStat[] tagStats,int totalNumberOfTopics){
		StringBuffer sb = new StringBuffer();
		//sb.append("<invoke name=\"initLand\" returntype=\"javascript\"><arguments>");
		sb.append("<invoke name='initLand' returntype='javascript'><arguments>");
		sb.append(number(user.getId()));		
		sb.append("<array>");     	
		
		int listCount = 0;
		for (int i = 0; i < tagStats.length; i++) {
			TagStat stat = tagStats[i];
			
			if(stat.getNumberOfTopics() > 0){
				sb.append("<property id='");
				sb.append(listCount);
				sb.append("'>");
				sb.append(islandObj(stat.getTagId(), stat.getTagName(), stat.getNumberOfTopics()));
				sb.append("</property>");
				listCount++;
			}
		}
//		sb.append("<property id='0'>"+islandObj(7,"Music",2)+"</property>");
//		sb.append("<property id='1'>"+islandObj(8,"Contacts",8)+"</property>");
//		sb.append("<property id='2'>"+islandObj(24,"Books",10)+"</property>");
		sb.append("</array>");
		sb.append(number(totalNumberOfTopics));
		sb.append(number(user.getWorldSize()));
		sb.append("</arguments></invoke>");    				
		return sb.toString();
	}

	public void growIsland(Tag tag) {
		StringBuffer sb = new StringBuffer();
		sb.append("<invoke name=\"grow\" returntype=\"javascript\"><arguments>");
		sb.append(number(tag.getId()));				
		sb.append("</arguments></invoke>");    						
		runCommandDeferred(sb.toString());
		
		sb = new StringBuffer();
		sb.append("<invoke name=\"rename\" returntype=\"javascript\"><arguments>");
		sb.append(number(tag.getId()));				
		sb.append(string(tag.getName()));
		sb.append("</arguments></invoke>");    						
		runCommandDeferred(sb.toString());		
	}
}
