package com.aavu.client.gui.dhtmlIslands;

import java.util.Iterator;

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

	public OceanDHTMLImpl(Manager manager) {
		super();
		this.manager = manager;
		
		setStyleName("H-Ocean");
		
		//setPixelSize(longitude, latitude);

		
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

		DragHandler d = new DragHandler(this);

		
		for (int i = 0; i < tagStats.length; i++) {
			TagStat stat = tagStats[i];
			
			//incr
			stat.setNumberOfTopics(stat.getNumberOfTopics()+1);
			
			Island isle = new Island(stat,this,d,manager.getUser());
			add(isle,isle.getLeft(),isle.getTop());

		}
		
	}
	
	public void growIsland(Tag tag) {
		// TODO Auto-generated method stub

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
