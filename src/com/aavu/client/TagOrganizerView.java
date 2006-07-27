package com.aavu.client;


import java.util.Iterator;
import java.util.List;

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
	private Button deleteTagButton = new Button("Delete Tag");
	private VerticalPanel tagListPanel = new VerticalPanel();
	private VerticalPanel metaListPanel = new VerticalPanel();
	private VerticalPanel metaList = new VerticalPanel();

	private HorizontalPanel panel = new HorizontalPanel();

	private TagServiceAsync tagService;

	public void setTagService(TagServiceAsync tagService2) {
		this.tagService = tagService2;
	}


	public TagOrganizerView(TagServiceAsync tagService2){
		setTagService(tagService2);

		panel.setSpacing(4);

		newMetaButton.addClickListener(this);
		deleteTagButton.addClickListener(this);

		tagListPanel.add(tagListLabel);
		tagListPanel.add(tagList);
		populateTagList();

		tagList.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender) {
				displayMetas(tagList.getItemText(tagList.getSelectedIndex()));
			}
		});
		tagList.addClickListener(new ClickListener(){
			public void onClick(Widget arg0) {
				System.out.println("in onClick");
				displayMetas(tagList.getItemText(tagList.getSelectedIndex()));
			}			   
		});

		metaListPanel.add(deleteTagButton);
		metaListPanel.add(metaList);

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
					System.out.println("fail: "+caught);
				}

				public void onSuccess(Object result) {
					System.out.println("success");
				}});

			populateTagList();
		}
		else if (source == newMetaButton){
			//activateEditView();
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

	private void displayMetas(String tagName){
		metaList.clear();

		tagService.getTag(tagName, new AsyncCallback(){

			public void onFailure(Throwable caught) {
				System.out.println("fail");
			}

			public void onSuccess(Object result) {
				Tag tag = (Tag) result;				
				List metas = tag.getMetas();
				for (Iterator iter = metas.iterator(); iter.hasNext();) {
					Meta element = (Meta) iter.next();
					metaList.add(element.getWidget());
				}
			}});		
	}

}
