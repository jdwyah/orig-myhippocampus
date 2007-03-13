package com.aavu.client.gui.gadgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTagPropertiesCommand;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.tags.MetaTypeChooser;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagPropertyPanel extends Gadget {

	private VerticalPanel metaListPanel = new VerticalPanel();
	private List metaChoosers = new ArrayList();  //list of meta chooser objects of current tag
	
	private TagLocalService tagLocalService;
	
	private Tag tag;
	private Manager manager;
	private Button saveB;	
	
	public TagPropertyPanel(Manager _manager){
		
		super(ConstHolder.myConstants.island_property());
		
		this.manager = _manager;
		
		tagLocalService = _manager.getTagLocalService();
		
			
		VerticalPanel mainPanel = new VerticalPanel();
		
		mainPanel.add(metaListPanel);
		
		Button addB = new Button(ConstHolder.myConstants.island_property_new());
		addB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				showEditMetaWidget(new MetaTypeChooser(tagLocalService));	
				saveB.setVisible(true);
			}});
		saveB = new Button(ConstHolder.myConstants.island_property_save());
		saveB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				saveProperties();				
			}});
		
		HorizontalPanel bP = new HorizontalPanel();
		bP.add(addB);
		bP.add(saveB);
		mainPanel.add(bP);		
		
		initWidget(mainPanel);

		addStyleName("H-TagProperty");
	}
	
	public int load(Topic topic){
		if(topic instanceof Tag){
			setVisible(true);

			this.tag = (Tag) topic;

			metaListPanel.clear();			
			metaChoosers.clear();
			saveB.setVisible(false);

			if(tag.getTagProperties() != null){
				if(tag.getTagProperties().size() > 0){
					saveB.setVisible(true);
				}
				for (Iterator iter = tag.getTagProperties().iterator(); iter.hasNext();) {
					Meta element = (Meta) iter.next();
					MetaTypeChooser mc = new MetaTypeChooser(tagLocalService);
					mc.setMeta(element);								
					showEditMetaWidget(mc);
				}
			}
		}else{
			setVisible(false);
		}
		return 0;
	}
	
	private void showEditMetaWidget(final MetaTypeChooser chooser){
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
		tag.getTagProperties().clear();
		int i = 0;
		for (Iterator iter = metaChoosers.iterator(); iter.hasNext();) {
			MetaTypeChooser mc = (MetaTypeChooser) iter.next();
			System.out.println("adding back mc "+mc.getMeta().getId()+" "+mc.getMeta().getType()+" "+mc.getMeta().getTitle());
			Meta meta = mc.getMeta();

			
			//in command tag.addMeta(meta);
			toSave[i] = meta;
			i++;
		}
		//selectedTag.setMetas(metaChoosers);
		
		System.out.println("Tag: " + tag.getName());
		System.out.println("metas: "+tag.getTagProperties().size());
		for (Iterator iter = tag.getTagProperties().iterator(); iter.hasNext();) {
			Meta element = (Meta) iter.next();
			System.out.println(element.getName());
		}
		
		//Effect.dropOut(metaListPanel);
		
		manager.getTopicCache().executeCommand(tag,new SaveTagPropertiesCommand(tag,toSave),
				new StdAsyncCallback("tagService saveTag"){});
	}


	//@Override
	public ImageButton getPickerButton() {		
		ImageButton b = new ImageButton(ConstHolder.myConstants.img_gadget_tagProperties(),39,36);
		b.addMouseListener(new TooltipListener(0,40,ConstHolder.myConstants.island_property()));
		return b;
	}

	//@Override
	public boolean isOnForTopic(Topic topic) {
		return topic.hasTagProperties();
	}

	
}
