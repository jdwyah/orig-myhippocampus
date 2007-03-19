package com.aavu.client.gui.gadgets;

import java.util.Iterator;
import java.util.Set;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.MetaLocation;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveMetaDateCommand;
import com.aavu.client.domain.commands.SaveMetaLocationCommand;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.gui.maps.HippoMapWidget;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.DeleteButton;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mapitz.gwt.googleMaps.client.GLatLng;

/**
 * Google Map Gadget.
 * 
 * @author Jeff Dwyer
 *
 */
public class MapGadget extends MetaGadget implements TopicLoader {
	

	private Meta selectedMeta;
	
	private Topic myTopic;	
	private Manager manager;

	private HippoMapWidget mapWidget;
	
	
	public MapGadget(Manager _manager) {		
		super(_manager, ConstHolder.myConstants.gadget_map_title(), new MetaLocation());
		
		this.manager = _manager;
		    
		
		mapWidget = new HippoMapWidget(this);

		extraPanel.add(mapWidget);
	}
	
	
	
	public int load(Topic topic){
		myTopic = topic;
		mapWidget.clear();
		
		int size = super.load(topic);		
		
		System.out.println("SIZE "+size);
		
		if(size > 0){			
			makeVisible();
		}else{
			//extraPanel.setVisible(false);
		}
		
		return size;
	}

	

	//@Override
	public ImageButton getPickerButton() {		
		ImageButton b = new ImageButton(ConstHolder.myConstants.img_gadget_map(),60,60);
		b.setBorderOnWidth(0);
		b.addMouseListener(new TooltipListener(0,40,ConstHolder.myConstants.gadget_map_title()));
		return b;
	}




	//@Override
	protected void addMWidg(final Meta meta, boolean showDelete) {
		
		makeVisible();
		
		
		MetaW metaWidge = new MetaW(meta,showDelete);		
		
		metasPanel.add(metaWidge);
		
		selectedMeta = meta;
		
		//HippoLocation mv = (HippoLocation) myTopic.getSingleMetaValueFor(meta);
		
	}
	

	private void makeVisible() {
		mapWidget.setSize(200);
		mapWidget.setVisible(true);
		extraPanel.setVisible(true);
	}

	private class MetaW extends VerticalPanel implements ClickListener {

		private Meta meta;
		private Label title;		

		public MetaW(Meta _meta, boolean showDelete) {
			this.meta = _meta;
			
			Set locations = myTopic.getMetaValuesFor(meta);
			
			
			HorizontalPanel titleP = new HorizontalPanel();
			
			title = new Label(meta.getTitle());
			title.addClickListener(this);
			titleP.add(title);

			if(showDelete){
				DeleteButton deleteButton = new DeleteButton();
				deleteButton.addClickListener(new ClickListener(){
					public void onClick(Widget sender) {
						removeMeta(meta,myTopic,MetaW.this);
					}
				});
				titleP.add(deleteButton);
			}
						
			add(titleP);
			
			for (Iterator iter = locations.iterator(); iter.hasNext();) {
				HippoLocation mv = (HippoLocation) iter.next();
				
				add(new LocationLabel(mv));
				
				mapWidget.add(meta,mv);				
			}
		}

		public void onClick(Widget sender) {
			selectedMeta = meta;
			//title.add
		}
		
	}
	
	/**
	 * 
	 * @author Jeff Dwyer
	 *
	 */
	private class LocationLabel extends Label implements ClickListener{

		public LocationLabel(HippoLocation mv) {
			
			setText(mv.toShortString());	
			
			addClickListener(this);
		}

		public void onClick(Widget sender) {
			
			//mapWidget.putInChooseMode();
		}		
	}

	
	public HippoLocation getNewLocationForPoint(GLatLng point) {
		if(selectedMeta != null){
			HippoLocation newLoc = new HippoLocation();
			newLoc.setLocation(point);
			
			saveLocation(selectedMeta,newLoc);
			
			return newLoc;
		}else{
			return null;
		}
	}

	private void saveLocation(Meta theSelectedMeta, HippoLocation newLoc) {

		System.out.println("Save "+newLoc);
		
		Set locations = myTopic.getAllMetas(theSelectedMeta);
		locations.add(newLoc);
		
		System.out.println("size "+locations.size());
		
		manager.getTopicCache().executeCommand(myTopic,new SaveMetaLocationCommand(myTopic,theSelectedMeta,locations),
				new StdAsyncCallback(ConstHolder.myConstants.save()){});

	
	}



	public void userSelected(HippoLocation selected) {
		// TODO Auto-generated method stub
		
	}



	public void update(HippoLocation dragged) {
		System.out.println("Update "+dragged+" selectedMeta "+selectedMeta);
		Set locations = myTopic.getMetaValuesFor(selectedMeta);		
		locations.add(dragged);
		
		System.out.println("size "+locations.size());
		
		manager.getTopicCache().executeCommand(myTopic,new SaveMetaLocationCommand(myTopic,selectedMeta,locations),
				new StdAsyncCallback(ConstHolder.myConstants.save()){});

	}


}
