package com.aavu.client.gui;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.User;
import com.aavu.client.gui.ext.FlashContainer;
import com.aavu.client.service.Manager;

public class Ocean extends FlashContainer {

	private Manager manager;

	public Ocean(Manager manager){
		super("preOcean.swf","ocean");
		this.manager = manager;
		
		setStyleName("GuiTest-Ocean");		
	}
	
	/**
	 * Will init after async "getTagStats()" call
	 *
	 */
	public void initIslands(){
		System.out.println("Init Islands");
		manager.getTagCache().getTagStats(new StdAsyncCallback("Get Tag Stats"){
			public void onSuccess(Object result) {
				TagStat[] tagStats = (TagStat[]) result;
				
				System.out.println("TagStat Result "+tagStats);
				
				runCommand(getCommand(manager.getUser(),tagStats, manager.getTopicCache().getNumberOfTopics()));
				
				System.out.println("command run ");
			}});		
	}
	
	protected String islandObj(long id, String name,int size){
		return "<object>"+numberProp("id",id)+stringProp("tag",name)+numberProp("size",size)+"</object>";
	}


	protected String getCommand(User user,TagStat[] tagStats,int totalNumberOfTopics){
		StringBuffer sb = new StringBuffer();
		sb.append("<invoke name=\"initLand\" returntype=\"javascript\"><arguments>");
		sb.append(number(user.getId()));		
		sb.append("<array>");     	
		for (int i = 0; i < tagStats.length; i++) {
			TagStat stat = tagStats[i];
			sb.append("<property id='");
			sb.append(i);
			sb.append("'>");
			sb.append(islandObj(stat.getTagId(), stat.getTagName(), stat.getNumberOfTopics()));
			sb.append("</property>");			
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
}
