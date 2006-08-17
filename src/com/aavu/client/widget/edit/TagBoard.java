package com.aavu.client.widget.edit;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaValue;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.TagServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TagBoard extends Composite {

	private VerticalPanel tagPanel = new VerticalPanel();
	//private Button addTagButton = new Button("Add");
	//private TextBox tagBox = new TextBox();
	private TagAutoCompleteBox tagBox = null;
	private List tags = new ArrayList();
	private Topic topic = null;

	private TagServiceAsync tagService;


	public void setTagService(TagServiceAsync tagService) {
		this.tagService = tagService;
	}
	
	public void setTopic(Topic topic){
		this.topic = topic;
	}

	public TagBoard(Topic topic, TagServiceAsync tagService) {
		setTagService(tagService);
		setTopic(topic);

		tagBox = new TagAutoCompleteBox(this, tagService);

		tagPanel.add(tagBox);
		setWidget(tagPanel);
		setStyleName("ta-tagboard");
	}

	//TODO: associate topic with tag (this just creates/retrieves a tag and displays it on the board)
	public void tagTopic(final String tagName){
		
		tagService.getTag(tagName, new AsyncCallback(){

			public void onFailure(Throwable caught) {
				System.out.println("fail tagservice.getTag " + caught);	
			}

			public void onSuccess(Object result) {
				Tag tag = (Tag) result;
				if (tag == null) {
					System.out.println("tag == null");
					tag = new Tag(tagName);	//if tag doesn't exist in database, create a new tag with given name
					System.out.println("new tag "+tag.getName());
					
					tagService.saveTag(tag, new AsyncCallback(){  //add newly created tag to DB
						public void onFailure(Throwable caught) {
							System.out.println("fail adding tag to db");
						}

						public void onSuccess(Object result) {
							System.out.println("added tag to db");
						}});
				}
				topic.tag(tag);
				addTag(tag); //add tag to board (new or retrieved)
			}});


	}


	public void load(){
		List tagList = topic.getTags();
		
		for (Iterator iter = tagList.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			addTag(tag);
		}		
	}
	
	private void addTag(final Tag tag) {
		String name = tag.getName();
		Label tagLabel = new Label(name);
		tagPanel.add(tagLabel);
		tagLabel.setStyleName("ta-tagboard-TagLabel");
		tags.add(tag);
		displayMetas(tag);
	}
	
	private void displayMetas(Tag tag) {
		List metas = tag.getMetas();
		Map metaValues = topic.getMetaValues();

		for (Iterator iter = metas.iterator(); iter.hasNext();) {
			Meta meta = (Meta) iter.next();
			MetaValue value = (MetaValue) metaValues.get(meta);
			tagPanel.add(value.getWidget());
		}
	}
	
	public void saveValues(){
		Map metaValues = topic.getMetaValues();
		
		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			for (Iterator iterator = tag.getMetas().iterator(); iterator.hasNext();) {
				Meta meta = (Meta) iterator.next();
				MetaValue value = (MetaValue) metaValues.get(meta);
				value.save();
			}
			
		}
	}

	public List getTags() {
		return tags;
	}
	
	public void clear() {
		tags.clear();
		tagPanel.clear();
		tagPanel.add(tagBox);
	}

}
