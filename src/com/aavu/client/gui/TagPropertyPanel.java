package com.aavu.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.service.Manager;
import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.widget.tags.MetaChooser;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagPropertyPanel extends Composite {

	private VerticalPanel metaListPanel = new VerticalPanel();
	private List metaChoosers = new ArrayList();  //list of meta chooser objects of current tag
	
	private TagLocalService tagLocalService;
	
	private Tag tag;
	private Manager manager;
	
	public TagPropertyPanel(Manager _manager, Tag _tag){
		this.tag = _tag;
		this.manager = _manager;
		
		tagLocalService = _manager.getTagLocalService();
		
		metaListPanel.clear();			
		metaChoosers.clear();
		
		if(tag.getMetas() != null){
			for (Iterator iter = tag.getMetas().iterator(); iter.hasNext();) {
				Meta element = (Meta) iter.next();
				MetaChooser mc = new MetaChooser(tagLocalService);
				mc.setMeta(element);								
				showEditMetaWidget(mc);
			}
		}
		
		
		VerticalPanel mainPanel = new VerticalPanel();
		
		mainPanel.add(metaListPanel);
		
		Button addB = new Button(Manager.myConstants.island_property_new());
		addB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				showEditMetaWidget(new MetaChooser(tagLocalService));					
			}});
		Button saveB = new Button(Manager.myConstants.island_property_save());
		saveB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				saveProperties();				
			}});
		
		mainPanel.add(addB);
		mainPanel.add(saveB);
		
		initWidget(mainPanel);
	}
	
	private void showEditMetaWidget(final MetaChooser chooser){
		final HorizontalPanel panel = new HorizontalPanel();
		Button deleteButton = new Button("X");

		panel.add(chooser);
		panel.add(deleteButton);

		deleteButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender){
				metaChoosers.remove(chooser);
				metaListPanel.remove(panel);
			}
		});

		metaChoosers.add(chooser);
		metaListPanel.add(panel);

	}
	
	private void saveProperties(){
		//selectedTag.setTitle(tagName.getText());
		
		tag.getMetas().clear();
		for (Iterator iter = metaChoosers.iterator(); iter.hasNext();) {
			MetaChooser mc = (MetaChooser) iter.next();
			System.out.println("adding back mc "+mc.getMeta().getId()+" "+mc.getMeta().getType()+" "+mc.getMeta().getTitle());
			tag.addMeta(mc.getMeta());
		}
		//selectedTag.setMetas(metaChoosers);
		
		System.out.println("Tag: " + tag.getName());
		System.out.println("metas: "+tag.getMetas().size());
		for (Iterator iter = tag.getMetas().iterator(); iter.hasNext();) {
			Meta element = (Meta) iter.next();
			System.out.println(element.getName());
		}
		
		//Effect.dropOut(metaListPanel);
		
		manager.getTagCache().saveTag(tag, new StdAsyncCallback("tagService saveTag"){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				System.out.println("success in saving tag " + tag.getName());					
			}
			
		});
	}
	
}
