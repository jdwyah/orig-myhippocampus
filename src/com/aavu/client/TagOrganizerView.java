package com.aavu.client;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagOrganizerView extends Composite implements ClickListener{

	private Label tagListLabel = new Label("Tags: ");
	private ListBox tagList = new ListBox();

	private Button newMetaButton = new Button("New field");
	private Button saveButton = new Button("Save");
	private Button deleteTagButton = new Button("Delete Tag");
	private VerticalPanel tagListPanel = new VerticalPanel();
	private VerticalPanel metaListPanel = new VerticalPanel();
	private VerticalPanel metaList = new VerticalPanel();

	private HorizontalPanel panel = new HorizontalPanel();

	private TagServiceAsync tagService;
	private List metas = new ArrayList();  //list of meta objects of current tag
	private Tag selectedTag;

	public void setTagService(TagServiceAsync tagService2) {
		this.tagService = tagService2;
	}


	public TagOrganizerView(TagServiceAsync tagService2){
		setTagService(tagService2);

		panel.setSpacing(4);

		newMetaButton.addClickListener(this);
		saveButton.addClickListener(this);
		deleteTagButton.addClickListener(this);

		tagListPanel.add(tagListLabel);
		tagListPanel.add(tagList);
		populateTagList();

		tagList.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender) {
				tagService.getTag(tagList.getItemText(tagList.getSelectedIndex()), new AsyncCallback(){
					public void onFailure(Throwable caught) {
						System.out.println("failed getting tag " + caught);
					}

					public void onSuccess(Object result) {
						System.out.println("success getting tag " + ((Tag)result).getName());
						selectedTag = (Tag) result;
					}
				});

				displayMetas(selectedTag);
			}
		});
		/*tagList.addClickListener(new ClickListener(){
			public void onClick(Widget arg0) {
				System.out.println("in onClick");
				displayMetas(selectedTag);
			}			   
		});*/

		metaListPanel.add(deleteTagButton);
		metaListPanel.add(metaList);
		metaListPanel.add(newMetaButton);
		metaListPanel.add(saveButton);

		panel.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
		panel.setWidth("100%");
		panel.add(tagListPanel);
		panel.add(metaListPanel);

		//controls
		//buttonPanel.add(editTextButton);
		//buttonPanel.add(saveButton);

		setWidget(panel);
	}


	public void onClick(Widget source){
		if (source == deleteTagButton){			
			tagService.removeTag(tagList.getItemText(tagList.getSelectedIndex()), new AsyncCallback(){

				public void onFailure(Throwable caught) {
					System.out.println("fail: "+ caught);
				}

				public void onSuccess(Object result) {
					System.out.println("success");
				}});

			populateTagList();
		}
		else if (source == newMetaButton){
			Meta newMeta = new Meta();
			showEditMetaWidget(newMeta.getEditorWidget(true), newMeta);
		}
		else if (source == saveButton){
			selectedTag.setMetas(metas);
			
			System.out.println("Tag: " + selectedTag.getName());
			System.out.println("metas: ");
			for (Iterator iter = selectedTag.getMetas().iterator(); iter.hasNext();) {
				Meta element = (Meta) iter.next();
				System.out.println(element.getName());
			}
			
			tagService.saveTag(selectedTag, new StdAsyncCallback(){

				public void onSuccess(Object result) {
					System.out.println("success in saving tag " + selectedTag.getName());
					
				}
				
			});
		}
	}

	public void populateTagList(){

		tagService.getAllTags(new AsyncCallback(){

			public void onFailure(Throwable caught) {
				System.out.println("fail "+caught);
			}

			public void onSuccess(Object result) {
				Tag[] tags = (Tag[]) result;
				tagList.clear();

				if(tags != null){
					for (int i=0; i<tags.length; i++) {
						tagList.addItem(((Tag)tags[i]).getName());
					}
					tagList.setVisibleItemCount(tags.length + 1);
				}else{
					System.out.println("no tags found!");
				}
			}});


	}

	private void displayMetas(Tag tag){
		metaList.clear();			
		metas = tag.getMetas();
		if (metas != null) {
			for (Iterator iter = metas.iterator(); iter.hasNext();) {
				Meta element = (Meta) iter.next();
				showEditMetaWidget(element.getEditorWidget(false), element);
			}		
		}
	}

	//Add meta widget and a delete button to the meta list panel
	private void showEditMetaWidget(Widget widget, final Meta meta){
		final HorizontalPanel panel = new HorizontalPanel();
		Button deleteButton = new Button("X");

		panel.add(widget);
		panel.add(deleteButton);

		deleteButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender){
				metas.remove(meta);
				metaList.remove(panel);
			}
		});

		metas.add(meta);
		metaList.add(panel);

	}

}
