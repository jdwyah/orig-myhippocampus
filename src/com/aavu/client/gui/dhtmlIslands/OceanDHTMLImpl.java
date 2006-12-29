package com.aavu.client.gui.dhtmlIslands;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.Ocean;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class OceanDHTMLImpl  extends AbsolutePanel implements Ocean {

	private int longitude = 800;
	private int latitude = 600;
	private Manager manager;

	private Map islands = new HashMap();
	private DragHandler dragHandler;
	
	public OceanDHTMLImpl(Manager manager) {
		super();
		this.manager = manager;
		
		setStyleName("H-Ocean");

		dragHandler = new DragHandler(this);

		
	}

	public void load() {
		manager.getTagCache().getTagStats(new StdAsyncCallback("Get Tag Stats"){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				TagStat[] tagStats = (TagStat[]) result;

				System.out.println("TagStat Result "+tagStats);

				addAll(tagStats);
			}
		});
	}
	
	private void addAll(TagStat[] tagStats) {

		
		for (int i = 0; i < tagStats.length; i++) {
			TagStat stat = tagStats[i];
						
			Island isle = new Island(stat,this,dragHandler,manager.getUser());
		
			addIsland(new Long(stat.getTagId()), isle);
			
		}
		
	}
	
	private void addIsland(Long id,Island isle){	
		add(isle,isle.getLeft(),isle.getTop());	
		islands.put(id, isle);
	}
	
	public void growIsland(Tag tag) {
		Island isle = (Island) islands.get(new Long(tag.getId()));
		if(isle == null){
			
			Island newIsle = new Island(tag,this,dragHandler,manager.getUser());		
			addIsland(new Long(tag.getId()), newIsle);
			
		}else{
			isle.grow();
		}
	}
	
	
	public int getLatitude() {
		return latitude;
	}

	public int getLongitude() {
		return longitude;
	}
	
	public Widget getWidget() {
		return this;
	}

	public void islandClicked(long tagId) {
		manager.showTopicsForTag(tagId);
	}

	public void islandMoved(long islandID, final int longitude, final int latitude){

		System.out.println("isleMovedTo "+longitude+" "+latitude+" ");			
		manager.getTopicCache().getTopicByIdA(islandID, new StdAsyncCallback("GetTopicById"){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				Topic t = (Topic) result;
				t.setLatitude(latitude);
				t.setLongitude(longitude);					
				manager.getTopicCache().save(t, new StdAsyncCallback("SaveLatLong"){});
			}

		});
	
}

	


}
