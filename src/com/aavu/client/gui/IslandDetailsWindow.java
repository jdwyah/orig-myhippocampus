package com.aavu.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.edit.TopicWidget;
import com.aavu.client.widget.tags.MetaChooser;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class IslandDetailsWindow extends PopupWindow {

	private HorizontalPanel mainPanel;	

	public IslandDetailsWindow(Tag tag, TopicIdentifier[] topics,Manager manager) {
		
		super(manager.newFrame(),manager.myConstants.tagContentsTitle(tag.getTitle()));
		
		//TODO tag title is null since we loaded this from flash by ID only.
		
		mainPanel = new HorizontalPanel();		
		
		HorizontalPanel buttons = new HorizontalPanel();		
		buttons.add(new Button("Add to this Island"));
		
		VerticalPanel leftSide = new VerticalPanel();		
		leftSide.add(buttons);		
		
		
		PopupPreview previewPop = new PopupPreview();
		
		if(topics != null){
			for (int i = 0; i < topics.length; i++) {
				TopicIdentifier topic = topics[i];
				leftSide.add(new TopicPreviewLink(manager,topic,previewPop));
			}
		}
		
		
		
		VerticalPanel rightSide = new VerticalPanel();
		rightSide.add(new HeaderLabel(manager.myConstants.island_property()));
		
		
		rightSide.add(new TagPropertyPanel(manager,tag));
		
		mainPanel.add(leftSide);
		mainPanel.add(rightSide);
		
		setContent(mainPanel);

	}
	
	private class TagPropertyPanel extends Composite {

		private VerticalPanel metaList = new VerticalPanel();
		private List metaChoosers = new ArrayList();  //list of meta chooser objects of current tag
		
		private TagLocalService tagLocalService;
		
		private Tag tag;
		private Manager manager;
		
		public TagPropertyPanel(Manager _manager, Tag _tag){
			this.tag = _tag;
			this.manager = _manager;
			
			tagLocalService = _manager.getTagLocalService();
			
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
			
			
			VerticalPanel mainPanel = new VerticalPanel();
			
			mainPanel.add(metaList);
			
			Button addB = new Button(_manager.myConstants.island_property_new());
			addB.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					showEditMetaWidget(new MetaChooser(tagLocalService));					
				}});
			Button saveB = new Button(_manager.myConstants.island_property_save());
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
					metaList.remove(panel);
				}
			});

			metaChoosers.add(chooser);
			metaList.add(panel);

		}
		
		private void saveProperties(){
			//selectedTag.setTitle(tagName.getText());
			
			tag.getMetas().clear();
			for (Iterator iter = metaChoosers.iterator(); iter.hasNext();) {
				MetaChooser mc = (MetaChooser) iter.next();
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


	private class TopicPreviewLink extends Composite {

		public TopicPreviewLink(final Manager manager, final TopicIdentifier ident, final PopupPreview preview) {
			HorizontalPanel mainPanel = new HorizontalPanel();
			
			Label l = new Label(ident.getTopicTitle());
//			l.addMouseListener(new MouseListenerAdapter(){
//				  public void onMouseEnter(Widget sender) {
//					  manager.getTopicCache().getTopic(ident, new StdAsyncCallback("Preview"){
//						public void onSuccess(Object result) {
//							super.onSuccess(result);
//
//							preview.setPopupPosition(getAbsoluteLeft()+30, getAbsoluteTop()+30);
//							preview.setTopic((Topic)result);
//							preview.show();
//							
//						}});
//				  }});
			l.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					manager.bringUpChart(ident);
				}				
			});
			
			mainPanel.add(l);
			
			initWidget(mainPanel);
		}
	}


	private static class PopupPreview extends PopupPanel {

		public PopupPreview() {
			super(true);		
			setStyleName("H-PopupPreview");
		}

		public void setTopic(Topic topic) {
			setWidget(new TextDisplay(topic.getLatestEntry().getData()));			
		}
	}



	
}
