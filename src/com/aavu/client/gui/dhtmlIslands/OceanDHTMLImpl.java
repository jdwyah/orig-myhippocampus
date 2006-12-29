package com.aavu.client.gui.dhtmlIslands;

import java.util.HashMap;
import java.util.Map;

import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.Ocean;
import com.aavu.client.gui.ext.GUIEffects;
import com.aavu.client.gui.ext.MultiDivPanel;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
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

		/*
		 * override the AbsolutePanel position: relative
		 * otherwise we got a left: 8px; top: 8px;
		 */		
		DOM.setStyleAttribute(getElement(), "position", "absolute");		
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

			Island isle = new Island(stat,this,manager.getUser());

			addIsland(stat, isle);

		}

	}

	private void addIsland(TagInfo info,Island isle){			
		
		
		//IslandBanner banner = new IslandBanner(info.getTagName());				
		//IslandWidg w = new IslandWidg(isle,banner);		
		//dragHandler.add(w);
		
		
		dragHandler.add(isle);
		
		//dragHandler.add(isle,isle,banner);		
		add(isle,isle.getLeft(),isle.getTop());
		//add(banner,isle.getLeft(),isle.getTop());
		
		Effect.appear(isle);
		islands.put(new Long(info.getTagId()), isle);
	}
	
	
	

	public void growIsland(Tag tag) {
		Island isle = (Island) islands.get(new Long(tag.getId()));
		if(isle == null){

			Island newIsle = new Island(tag,this,manager.getUser());		
			addIsland(tag, newIsle);

		}else{
			isle.grow();
		}
	}
	public void removeIsland(long id) {
		Island isle = (Island) islands.get(new Long(id));
		if(isle != null){

			GUIEffects.fadeAndRemove(isle, 3000);
			
			
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
