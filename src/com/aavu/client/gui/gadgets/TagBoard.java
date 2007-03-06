package com.aavu.client.gui.gadgets;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.RemoveTagFromTopicCommand;
import com.aavu.client.domain.commands.SaveTagtoTopicCommand;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TagCache;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.edit.CompleteListener;
import com.aavu.client.widget.edit.DeletableTopicLabel;
import com.aavu.client.widget.edit.RemoveListener;
import com.aavu.client.widget.edit.TagAutoCompleteBox;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagBoard extends Composite implements CompleteListener, RemoveListener {

	private CellPanel tagPanel = new VerticalPanel();
	
	//private TextBox tagBox = new TextBox();
	private TagAutoCompleteBox tagBox = null;
	
	
//	private List tags = new ArrayList();
//	private Map metaMap = new HashMap();

	private Topic cur_topic;

	private TagCache tagCache;
	private Manager manager;
	
	private Set tagsToSave = new HashSet();

	private HeaderLabel header;

	//private SaveNeededListener saveNeeded; 

	public TagBoard(Manager manager) {
		this.manager = manager;
		this.tagCache = manager.getTagCache();
		//this.saveNeeded = saveNeeded;

		tagBox = new TagAutoCompleteBox(this,tagCache);

		EnterInfoButton addTagButton = new EnterInfoButton();		
		addTagButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				completed(tagBox.getText());
			}
		});

		CellPanel mainPanel = new VerticalPanel();
		
		header = new HeaderLabel(ConstHolder.myConstants.tags());
		mainPanel.add(header);
				
		
//		
//		CellPanel tagPanelS = new HorizontalPanel();		
//		tagPanelS.add(tagPanel);
		
		mainPanel.add(tagPanel);
				
		HorizontalPanel tagBoxP = new HorizontalPanel();		
		tagBoxP.add(new Label(ConstHolder.myConstants.addTag()));
		tagBoxP.add(tagBox);
		tagBoxP.add(addTagButton);		
		mainPanel.add(tagBoxP);
		
		initWidget(mainPanel);
		
		addStyleName("H-Gadget");
		addStyleName("H-TagBoard");		
		
	}

	public void tagTopic(final String tagName){
		//need to check if it's a built in tag or not bla bla
		//for now just creates new (bland) Tag and adds it to list
		//topic needs to get tagged when its saved

		//First, do a name lookup on this tag
		//
		tagCache.getTagAddIfNew(tagName, new StdAsyncCallback("tagservice.getTagAddIfNew"){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				Tag tag = (Tag) result;				
				addTag(tag);
			}});


	}

	public void completed(String completeText) {
		tagTopic(completeText);
		tagBox.setText("");
	}	
	
	/**
	 * load the topic into the GUI 
	 * 
	 * @param topic
	 */
	public int load(Topic topic){
		
		//this isn't really true, and we may end up hiding information,
		//but I think it's confusing that islands can be on islands.
		//
		if(topic instanceof Tag){
			setVisible(false);
		}else{
			setVisible(true);
		}
		
		header.setText(ConstHolder.myConstants.tags());
		
		tagPanel.clear();
		
		cur_topic = topic;
		
		int rtnSize = 0;
		for (Iterator iter = topic.getTags().iterator(); iter.hasNext();) {
			
			//TODO o will not be a Tag when we are a topic that is another 
			//topic's MetaTopic value. capiche? 
			//TODO make getTags deal with that.
			Object o = iter.next();
			if(o instanceof Tag){
				Tag tag = (Tag) o;		
				showTag(tag);
				rtnSize++;	
			}						
		}		
		return rtnSize;
	}
	
	private void showTag(final Tag tag){
		
		DeletableTopicLabel tagLabel = new DeletableTopicLabel(tag,this);
				
		TagGadget tg = new TagGadget(tagLabel);
					
		displayMetas(tag,tg);
		
		tagPanel.add(tg);
	}

	/**
	 * Remove the tag and add the tag to the list of things that need to be saved.
	 * Need to do a load to make sure that we have all necessary Data.
	 */
	public void remove(Topic tag,final Widget widgetToRemoveOnSuccess) {

		manager.getTopicCache().save(cur_topic,new RemoveTagFromTopicCommand(cur_topic,
				tag),
				new StdAsyncCallback(ConstHolder.myConstants.delete_async()){
					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						Window.alert("Problem Removing Tag "+caught);
					}
					public void onSuccess(Object result) {
						super.onSuccess(result);
						widgetToRemoveOnSuccess.removeFromParent();
					}});				
	}
	
	/**
	 * Typically we don't touch a topics tags on save. only if they've been 
	 * updated. This is because unless they've been updated, they won't be full
	 * fledged objects (due to the fact that we didn't JOIN and 
	 * serialize topic.types.instances... etc
	 * 
	 * If we're tagging though, it's a full obj and we need to save it.
	 * 
	 * @param tag
	 */
	private void addTag(final Tag tag) {

		if(cur_topic.tagTopic(tag)){
			showTag(tag);			
		}
		//incommand tagsToSave.add(tag);
		manager.getTopicCache().save(cur_topic,new SaveTagtoTopicCommand(cur_topic,tag), 
				new StdAsyncCallback(ConstHolder.myConstants.save()){});		
	}
	
	private void displayMetas(Tag tag, TagGadget tg) {
		Set metas = tag.getMetas();
		
		for (Iterator iter = metas.iterator(); iter.hasNext();) {		
			Meta element = (Meta) iter.next();
		
			Widget w = element.getEditorWidget(cur_topic,manager);
			w.addStyleName("H-MetaEditorWidget");
			tg.add(w);

		}

	}

	public void saveThingsNowEvent(StdAsyncCallback callback) {
		
		callback.onSuccess(tagsToSave);
							
	}

	
	private class TagGadget extends Composite {

		private CellPanel mainP;

		public TagGadget(DeletableTopicLabel tagLabel) {
			mainP = new VerticalPanel();
			
			add(tagLabel);
			
			initWidget(mainP);
			
			//addStyleName("H-Gadget");
			//addStyleName("H-TagGadget");
		}

		public void add(Widget w) {
			mainP.add(w);
		}

	}

}
