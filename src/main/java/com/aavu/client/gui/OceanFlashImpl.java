package com.aavu.client.gui;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.gui.ext.FlashContainer;
import com.aavu.client.service.Manager;

public class OceanFlashImpl extends FlashContainer implements Ocean {

	private Manager manager;

	public OceanFlashImpl(Manager manager){
		super("islands.swf","ocean");
		this.manager = manager;
		
		setStyleName("H-Ocean");		
	}

	/* (non-Javadoc)
	 * @see com.aavu.client.gui.Ocean#load()
	 */
	public void load(LoadFinishedListener listener){
		System.out.println("Init Islands...");		
				
		manager.getTagCache().getTagStats(new StdAsyncCallback("Get Tag Stats"){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				TagStat[] tagStats = (TagStat[]) result;

				System.out.println("TagStat Result "+tagStats);

				//runCommandDeferred(getCommand(manager.getUser(),tagStats, manager.getTopicCache().getNumberOfTopics()));
				runCommandDeferred(getCommand(manager.getUser(),tagStats, 0));
			}});
		
	}
	
	protected void callbackOverride(String command, int int0,final int int1,final int int2){
		if(command.equals("islandClicked")){
			manager.showPreviews(int0);
		}
		if(command.equals("isleMovedTo")){
//			System.out.println("isleMovedTo "+int0+" "+int1+" "+int2);			
//			manager.getTopicCache().getTopicByIdA(int0, new StdAsyncCallback("GetTopicById"){
//
//				public void onSuccess(Object result) {
//					super.onSuccess(result);
//					Topic t = (Topic) result;
//					t.setLatitude(int2);
//					t.setLongitude(int1);					
//					manager.getTopicCache().save(t, new StdAsyncCallback("SaveLatLong"){});
//				}
//				
//			});
		}
		
	}
	
	
	
	protected String islandObj(long id, String name,int size, int latitude, int longitude){		
		return "<object>"+numberPropChangeZeroAndNeg1ToNull("id",id)+stringProp("tag",name)+numberPropChangeZeroAndNeg1ToNull("size",size)+numberPropChangeZeroAndNeg1ToNull("xx",latitude)+numberPropChangeZeroAndNeg1ToNull("yy",longitude)+"</object>";
	}

	/**
	initLand(userID, islandArray, uniqueTopics, worldSize);
	where islandArray contains elements containing:
	  -id:Number
	  -tag:String
	  -size:Number
	  -xx:Number
	  -yy:Number

	when a user drags an island, the island transmits to the external interface:
	isleMovedTo(islandID, xx, yy);
	*/
	protected String getCommand(User user,TagStat[] tagStats,int totalNumberOfTopics){
		StringBuffer sb = new StringBuffer();
		//sb.append("<invoke name=\"initLand\" returntype=\"javascript\"><arguments>");
		sb.append("<invoke name='initLand' returntype='javascript'><arguments>");
		sb.append(number(user.getId()));		
		sb.append("<array>");     	
		
		int listCount = 0;
		for (int i = 0; i < tagStats.length; i++) {
			TagStat stat = tagStats[i];
			
			sb.append("<property id='");
			sb.append(listCount);
			sb.append("'>");
			/*
			 * NOTE: stat.getNumberOfTopics() + 1
			 * otherwise we have blank islands 
			 */
			sb.append(islandObj(stat.getTagId(), stat.getTagName(), stat.getNumberOfTopics() + 1, stat.getLongitude(), stat.getLatitude()));
			sb.append("</property>");
			listCount++;
		}
		
//		sb.append("<property id='0'>"+islandObj(7,"Music",2)+"</property>");
//		sb.append("<property id='1'>"+islandObj(8,"Contacts",8)+"</property>");
//		sb.append("<property id='2'>"+islandObj(24,"Books",10)+"</property>");
		sb.append("</array>");
		sb.append(number(totalNumberOfTopics));
		sb.append(number(user.getWorldSize(totalNumberOfTopics,tagStats.length)));
		sb.append("</arguments></invoke>");    				
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see com.aavu.client.gui.Ocean#growIsland(com.aavu.client.domain.Tag)
	 */
	public void growIsland(Topic tag) {
		StringBuffer sb = new StringBuffer();
		sb.append("<invoke name=\"grow\" returntype=\"javascript\"><arguments>");
		sb.append(number(tag.getId()));				
		sb.append("</arguments></invoke>");    						
		runCommandDeferred(sb.toString());
		
		sb = new StringBuffer();
		sb.append("<invoke name=\"rename\" returntype=\"javascript\"><arguments>");
		sb.append(number(tag.getId()));				
		sb.append(string(tag.getTitle()));
		sb.append("</arguments></invoke>");    						
		runCommandDeferred(sb.toString());		
	}

	public int getLatitude() {
		// TODO Auto-generated method stub
		return 800;
	}

	public int getLongitude() {
		// TODO Auto-generated method stub
		return 600;
	}

	public void removeIsland(long id) {
		throw new UnsupportedOperationException("Not Yet Implemented");
	}

	public void unFocus() {
		throw new UnsupportedOperationException("Not Yet Implemented");		
	}

	public void showCloseup(long id, FullTopicIdentifier[] topics) {
		throw new UnsupportedOperationException("Not Yet Implemented");
	}

	public void zoomTo(double scale) {
		throw new UnsupportedOperationException("Not Yet Implemented");
	}	

	public void update(Topic t, AbstractCommand command) {
		throw new UnsupportedOperationException("Not Yet Implemented");		
	}

	public boolean centerOn(Topic topic) {
		// TODO Auto-generated method stub
		return false;
	}

	public void moveBy(int i, int j) {
		// TODO Auto-generated method stub
		
	}

	public void zoomIn() {
		// TODO Auto-generated method stub
		
	}

	public void zoomOut() {
		// TODO Auto-generated method stub
		
	}

	public double ensureZoomOfAtLeast(double scale) {
		// TODO Auto-generated method stub
		return 0;
	}
}
