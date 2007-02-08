package com.aavu.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.commands.SaveTagPropertiesCommand;
import com.aavu.client.service.Manager;
import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.widget.HeaderLabel;
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
	private Button saveB;	
	
	public TagPropertyPanel(Manager _manager){
		
		this.manager = _manager;
		
		tagLocalService = _manager.getTagLocalService();
		
			
		VerticalPanel mainPanel = new VerticalPanel();
		
		mainPanel.add(new HeaderLabel(Manager.myConstants.island_property()));
		
		mainPanel.add(metaListPanel);
		
		Button addB = new Button(Manager.myConstants.island_property_new());
		addB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				showEditMetaWidget(new MetaChooser(tagLocalService));	
				saveB.setVisible(true);
			}});
		saveB = new Button(Manager.myConstants.island_property_save());
		saveB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				saveProperties();				
			}});
		
		HorizontalPanel bP = new HorizontalPanel();
		bP.add(addB);
		bP.add(saveB);
		mainPanel.add(bP);		
		
		initWidget(mainPanel);
	}
	
	public void load(Tag tag){
		this.tag = tag;

		metaListPanel.clear();			
		metaChoosers.clear();
		saveB.setVisible(false);
		
		if(tag.getMetas() != null){
			if(tag.getMetas().size() > 0){
				saveB.setVisible(true);
			}
			for (Iterator iter = tag.getMetas().iterator(); iter.hasNext();) {
				Meta element = (Meta) iter.next();
				MetaChooser mc = new MetaChooser(tagLocalService);
				mc.setMeta(element);								
				showEditMetaWidget(mc);
			}
		}		
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
		
		
		Meta[] toSave = new Meta[metaChoosers.size()];
		tag.getMetas().clear();
		int i = 0;
		for (Iterator iter = metaChoosers.iterator(); iter.hasNext();) {
			MetaChooser mc = (MetaChooser) iter.next();
			System.out.println("adding back mc "+mc.getMeta().getId()+" "+mc.getMeta().getType()+" "+mc.getMeta().getTitle());
			Meta meta = mc.getMeta();

			
			//in command tag.addMeta(meta);
			toSave[i] = meta;
			i++;
		}
		//selectedTag.setMetas(metaChoosers);
		
		System.out.println("Tag: " + tag.getName());
		System.out.println("metas: "+tag.getMetas().size());
		for (Iterator iter = tag.getMetas().iterator(); iter.hasNext();) {
			Meta element = (Meta) iter.next();
			System.out.println(element.getName());
		}
		
		//Effect.dropOut(metaListPanel);
		
		manager.getTopicCache().save(tag,new SaveTagPropertiesCommand(tag,toSave),
				new StdAsyncCallback("tagService saveTag"){});
	}
	
}
