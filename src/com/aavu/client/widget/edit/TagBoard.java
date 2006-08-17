package com.aavu.client.widget.edit;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagBoard extends Composite {

	private VerticalPanel tagPanel = new VerticalPanel();
	private Button addTagButton = new Button("Add");
	//private TextBox tagBox = new TextBox();
	private TagAutoCompleteBox tagBox = null;
	private List tags = new ArrayList();

	private GWTTagServiceAsync tagService;


	public void setTagService(GWTTagServiceAsync tagService) {
		this.tagService = tagService;
	}

	public TagBoard(GWTTagServiceAsync tagService) {
		setTagService(tagService);

		tagBox = new TagAutoCompleteBox(this,tagService);

		addTagButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				tagTopic(tagBox.getText());
			}
		});
		/*HorizontalPanel addTagPanel = new HorizontalPanel();
		addTagPanel.add(tagBox);
		addTagPanel.add(addTagButton);

		tagPanel.add(addTagPanel);*/

		tagPanel.add(tagBox);
		setWidget(tagPanel);
		setStyleName("ta-tagboard");
	}

	public void tagTopic(final String tagName){
		//need to check if it's a built in tag or not bla bla
		//for now just creates new (bland) Tag and adds it to list
		//topic needs to get tagged when its saved


		tagService.getTag(tagName, new AsyncCallback(){

			public void onFailure(Throwable caught) {
				System.out.println("fail tagservice.getTag "+caught);	
			}

			public void onSuccess(Object result) {
				Tag tag = (Tag) result;
				if (tag == null) {
					System.out.println("tag == null");
					tag = new Tag(tagName);				
					System.out.println("new tag "+tag.getName());
					
					tagService.saveTag(tag, new AsyncCallback(){
						public void onFailure(Throwable caught) {
							System.out.println("fail adding");
						}

						public void onSuccess(Object result) {
							System.out.println("added");
						}});
					System.out.println("addtag to board during async"+tag.getName());
					addTag(tag);
				}else{
					System.out.println("addtag to board ELSE"+tag.getName());
					addTag(tag);	  //add tag to tagBoard
				}
			}});


	}

	private void addTag(final Tag tag) {
		String name = tag.getName();
		Label tagLabel = new Label(name);
		//Hyperlink link = new Hyperlink(name, name);
		tagPanel.add(tagLabel);
		tagLabel.setStyleName("ta-tagboard-TagLabel");
		tags.add(tag);
		displayMetas(tag);
	}

	public void load(List list){
		
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			addTag(tag);
		}		
	}
	
	private void displayMetas(Tag tag) {
		List metas = tag.getMetas();

		for (Iterator iter = metas.iterator(); iter.hasNext();) {
			Meta element = (Meta) iter.next();
			tagPanel.add(element.getWidget(true));
		}

	}

	public List getTags() {
		return tags;
	}

}
