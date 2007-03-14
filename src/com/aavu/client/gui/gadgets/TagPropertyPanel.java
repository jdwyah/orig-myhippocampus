package com.aavu.client.gui.gadgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaDate;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagPropertyPanel extends Gadget {

	private VerticalPanel metaListPanel = new VerticalPanel();
	private List metas = new ArrayList();  //list of meta chooser objects of current tag
	
	
	private Tag tag;
	private Manager manager;
	private Button saveB;	
	
	public TagPropertyPanel(Manager _manager){
		
		super(ConstHolder.myConstants.island_property());
		
		this.manager = _manager;
			
		VerticalPanel mainPanel = new VerticalPanel();
		
		mainPanel.add(metaListPanel);
		
		Button addB = new Button(ConstHolder.myConstants.island_property_new());
		addB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {				
				addEditClick();				
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
	private void addEditClick(){		
		manager.editMetas(new EZCallback(){
			public void onSuccess(Object result) {			
				Meta m = (Meta) result;
				if(m != null){
					addMWidg(m);
					saveB.setVisible(true);
				}
			}			
		},
		null);		
	}
	private void addMWidg(final Meta meta) {
		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Label(meta.getTitle()));
		
		Button deleteButton = new Button("X");
		deleteButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {			
				metas.remove(meta);
			}
		});
		hp.add(deleteButton);
		metaListPanel.add(hp);
		
		metas.add(meta);
	}
	
	public int load(Topic topic){
		if(topic instanceof Tag){
			setVisible(true);

			this.tag = (Tag) topic;

			metaListPanel.clear();			
			metas.clear();
			saveB.setVisible(false);

			if(tag.getTagProperties() != null){
				if(tag.getTagProperties().size() > 0){
					saveB.setVisible(true);
				}
				for (Iterator iter = tag.getTagProperties().iterator(); iter.hasNext();) {
					Meta element = (Meta) iter.next();
					addMWidg(element);					
				}
			}
		}else{
			setVisible(false);
		}
		return 0;
	}
		
	private void saveProperties(){
		//selectedTag.setTitle(tagName.getText());
		
		
		Meta[] toSave = new Meta[metas.size()];
		tag.getTagProperties().clear();
		int i = 0;
		for (Iterator iter = metas.iterator(); iter.hasNext();) {			
			Meta meta = (Meta) iter.next();
			
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
