package com.aavu.client.widget.tags;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.local.TagLocalService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagOrganizerView extends Composite implements ClickListener{

	private Label tagListLabel = new Label("Tags: ");
	private FlowPanel tagClowd = new FlowPanel();

	private Button newMetaButton = new Button("New field");
	private Button saveButton = new Button("Save");
	private Button deleteTagButton = new Button("Delete Tag");
	private VerticalPanel tagClowdPanel = new VerticalPanel();
	private VerticalPanel metaListPanel = new VerticalPanel();
	private VerticalPanel metaList = new VerticalPanel();

	private VerticalPanel mainPanel = new VerticalPanel();

	private HippoCache hippoCache;
	private List metaChoosers = new ArrayList();  //list of meta chooser objects of current tag
	
	private Tag selectedTag;
	private TagLabel selectedTagLabel;
	
	private TagLocalService tagLocalService;
	private TextBox tagName;

	
	public void setHippoCache(HippoCache hippoCache) {
		this.hippoCache = hippoCache;
	}


	public TagOrganizerView(HippoCache hCache){
		setHippoCache(hCache);
		tagLocalService = new TagLocalService();
		
		
		mainPanel.setSpacing(4);

		newMetaButton.addClickListener(this);
		saveButton.addClickListener(this);
		deleteTagButton.addClickListener(this);

		
		
		tagClowdPanel.add(tagListLabel);
		tagClowdPanel.add(tagClowd);		

		tagClowd.setStyleName("H-TagClowd");
		
		populateTagList();
	
		Button newTag = new Button("Add New Tag");
		newTag.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				Tag t = new Tag();
				
				//hmmm... this isn't all that pretty
				selectedTag = t;
				displayMetas(t);
				Effect.grow(metaListPanel);
			}});
		tagClowdPanel.add(newTag);
		
		
		tagName = new TextBox();		
		
		HorizontalPanel editTagNamePanel = new HorizontalPanel();		
		editTagNamePanel.add(new Label("Name: "));
		editTagNamePanel.add(tagName);
		
		metaListPanel.add(editTagNamePanel);
		metaListPanel.add(deleteTagButton);
		metaListPanel.add(metaList);
		metaListPanel.add(newMetaButton);
		metaListPanel.add(saveButton);

		mainPanel.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
		mainPanel.setWidth("100%");
		mainPanel.add(tagClowdPanel);
		mainPanel.add(metaListPanel);

		//controls
		//buttonPanel.add(editTextButton);
		//buttonPanel.add(saveButton);

		metaListPanel.setVisible(false);
		
		initWidget(mainPanel);
	}


	public void onClick(Widget source) {
		if (source == deleteTagButton){			
			
			try {
				hippoCache.getTagCache().removeTag(selectedTag, new StdAsyncCallback("tagService remove"){
				//tagService.removeTag(tagList.getItemText(tagList.getSelectedIndex()), new AsyncCallback(){

					public void onSuccess(Object result) {
						super.onSuccess(result);
						System.out.println("success deleting tag");
					}});
			} catch (PermissionDeniedException e) {
				System.out.println("Tag removal failed!");
			}
			
			Effect.dropOut(metaListPanel);
			populateTagList();
		}
		else if (source == newMetaButton){
			MetaChooser newMeta = new MetaChooser(tagLocalService);
			//Meta newMeta = new Meta();
			showEditMetaWidget(new MetaChooser(tagLocalService));
		}
		else if (source == saveButton){
			
			selectedTag.setTitle(tagName.getText());
			
			selectedTag.getMetas().clear();
			for (Iterator iter = metaChoosers.iterator(); iter.hasNext();) {
				MetaChooser mc = (MetaChooser) iter.next();
				selectedTag.addMeta(mc.getMeta());
			}
			//selectedTag.setMetas(metaChoosers);
			
			System.out.println("Tag: " + selectedTag.getName());
			System.out.println("metas: "+selectedTag.getMetas().size());
			for (Iterator iter = selectedTag.getMetas().iterator(); iter.hasNext();) {
				Meta element = (Meta) iter.next();
				System.out.println(element.getName());
			}
			
			Effect.dropOut(metaListPanel);
			
			hippoCache.getTagCache().saveTag(selectedTag, new StdAsyncCallback("tagService saveTag"){

				public void onSuccess(Object result) {
					super.onSuccess(result);
					System.out.println("success in saving tag " + selectedTag.getName());
					populateTagList();
				}
				
			});
		}
	}

	public void populateTagList(){

		hippoCache.getTagCache().getAllTags(new StdAsyncCallback("populateTagList"){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				Tag[] tags = (Tag[]) result;
				tagClowd.clear();

				if(tags != null){
					for (int i=0; i<tags.length; i++) {
						tagClowd.add(new TagLabel((Tag)tags[i]));					
					}					
				}else{
					System.out.println("no tags found!");
				}
			}});


	}

	private void displayMetas(Tag tag){
		if(tag == null){
			return;
		}
		tagName.setText(tag.getName());
		
		metaList.clear();			
		metaChoosers.clear();
		
		if(tag.getMetas() != null){
			for (Iterator iter = tag.getMetas().iterator(); iter.hasNext();) {
				Meta element = (Meta) iter.next();
				MetaChooser mc = new MetaChooser(tagLocalService);
				mc.setMeta(element);								
				showEditMetaWidget(mc);
			}
		}
		
		
//		
//		if (metaChoosers != null) {
//			for (Iterator iter = metaChoosers.iterator(); iter.hasNext();) {
//				MetaChooser element = (MetaChooser) iter.next();
//				Meta m = element.getMeta();
//				
//				MetaChooser mc = new MetaChooser(tagLocalService);
//
//				mc.setMeta(element);
//				showEditMetaWidget(mc);
//			}		
//		}
	}

	//Add meta widget and a delete button to the meta list panel
	private void showEditMetaWidget(final MetaChooser chooser){
		final HorizontalPanel panel = new HorizontalPanel();
		Button deleteButton = new Button("X");

		panel.add(chooser);
		panel.add(deleteButton);

		deleteButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender){
				metaChoosers.remove(chooser);
				metaList.remove(panel);
			}
		});

		metaChoosers.add(chooser);
		metaList.add(panel);

	}

	private class TagLabel extends Label implements ClickListener{
		private Tag tag;

		public TagLabel(Tag tag){
			super(tag.getName());
			this.tag = tag;
			addClickListener(this);
			
		}

		public Tag getTag(){
			return tag;
		}
		
		public void setSelected(boolean b){
			if(b){
				setStyleName("");	
			}else{
				setStyleName("");
			}
		}
		
		/*
		 * TODO  odd call to tagcache, should just be in the cache..  
		 * 
		 * 
		 * (non-Javadoc)
		 * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
		 */
		public void onClick(Widget sender) {			
			
			if(selectedTagLabel != null){
				selectedTagLabel.setSelected(false);
			}
			
			selectedTag = tag;
			selectedTagLabel = this;
			
			setSelected(true);
			
			if(tag.getMetas() != null){				
				displayMetas(tag);
				Effect.grow(metaListPanel);
			}			
		}
	}
	
}
