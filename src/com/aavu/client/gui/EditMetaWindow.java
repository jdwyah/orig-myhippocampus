package com.aavu.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.gwm.client.GInternalFrame;

import com.aavu.client.async.EZCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.MetaLocation;
import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.tags.ChooseMetaW;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Window that let's users add new metas, edit existing etc.
 * 
 * Configurable to only show dates, or texts
 * 
 * @author Jeff Dwyer
 *
 */
public class EditMetaWindow extends PopupWindow implements ChangeListener {

	public static final int WIDTH = 350;
	private static final int HEIGHT = 250;
	
	private Grid mainP;
	
	private Meta selected;
//	private ChooseMetaW dateChooser;
//	private ChooseMetaW textChooser;
//	private ChooseMetaW locationChooser;
//	
	//private List choosers = new ArrayList();
	
	private SimplePanel editP;
	private Manager manager;

	public EditMetaWindow(final Manager manager, final GInternalFrame frame, final Meta type,final AsyncCallback callback) {	
		super(frame,ConstHolder.myConstants.meta_title(),WIDTH,HEIGHT);
		
		this.manager = manager;
	
		//CHECKSTYLE:OFF
		mainP = new Grid(3,4);
		//CHECKSTYLE:ON				
		
		
		if(type == null || type instanceof MetaDate){
			addChooser(0, new MetaDate(), ConstHolder.myConstants.meta_date(), ConstHolder.myConstants.meta_new());
		}
		if(type == null || type instanceof MetaText){
			addChooser(1, new MetaText(), ConstHolder.myConstants.meta_text(), ConstHolder.myConstants.meta_new());
		}
		if(type == null || type instanceof MetaLocation){
			addChooser(2, new MetaLocation(), ConstHolder.myConstants.meta_location(), ConstHolder.myConstants.meta_new());
			System.out.println("location chooser");
		}
		
//		if(type == null || type instanceof MetaDate){
//			dateChooser = new ChooseMetaW(manager,new MetaDate());
//			dateChooser.addChangeListener(this);
//			
//			mainP.setWidget(0, 0, new Label(ConstHolder.myConstants.meta_date()));
//			mainP.setWidget(1, 0, dateChooser);
//			
//			Button newDateB = new Button(ConstHolder.myConstants.meta_new());		
//			newDateB.addClickListener(new ClickListener(){
//				public void onClick(Widget sender) {
//					manager.newMeta(new MetaDate(),new EZCallback(){
//						public void onSuccess(Object result) {
//							TopicIdentifier res = (TopicIdentifier) result;						
//							
//							Meta newM = new MetaDate();
//							newM.setId(res.getTopicID());
//							newM.setTitle(res.getTopicTitle());
//							
//							dateChooser.add(newM);
//						}});			
//				}});
//			mainP.setWidget(2,0,newDateB);
//		}
//		if(type == null || type instanceof MetaText){
//			textChooser = new ChooseMetaW(manager,new MetaText());
//			textChooser.addChangeListener(this);
//			
//			mainP.setWidget(0, 1, new Label(ConstHolder.myConstants.meta_text()));
//			mainP.setWidget(1, 1, textChooser);
//			
//			Button newTextB = new Button(ConstHolder.myConstants.meta_new());		
//			newTextB.addClickListener(new ClickListener(){
//				public void onClick(Widget sender) {
//					manager.newMeta(new MetaText(),new EZCallback(){
//						public void onSuccess(Object result) {
//							TopicIdentifier res = (TopicIdentifier) result;						
//							
//							Meta newM = new MetaText();
//							newM.setId(res.getTopicID());
//							newM.setTitle(res.getTopicTitle());
//							
//							textChooser.add(newM);
//						}});			
//				}});
//			mainP.setWidget(2,1,newTextB);
//		}
		
		
		Button selectB = new Button(ConstHolder.myConstants.meta_select());		
		selectB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				callback.onSuccess(selected);
				close();
			}});
		mainP.setWidget(1,3,selectB);
		
		
		
		
		editP = new SimplePanel();
		mainP.setWidget(2, 3, editP);
		
		setContent(mainP);		
	}
	
	private void addChooser(int column, final Meta meta, String headerText, String newButtonText){
		final ChooseMetaW chooser = new ChooseMetaW(manager,meta);
		chooser.addChangeListener(this);
		
		mainP.setWidget(0, column, new Label(headerText));
		mainP.setWidget(1, column, chooser);
		
		Button newButton = new Button(newButtonText);		
		newButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.newMeta(meta,new EZCallback(){
					public void onSuccess(Object result) {
						TopicIdentifier res = (TopicIdentifier) result;						
						
						meta.setId(res.getTopicID());
						meta.setTitle(res.getTopicTitle());
						
						chooser.add(meta);
					}});			
			}});		
		mainP.setWidget(2,column,newButton);
		
		
	}

	public void onChange(Widget sender) {		
		selected = ((ChooseMetaW)sender).getSelectedMeta();		
		editP.setWidget(new Label(selected.getTitle()));	
	}
	
	
}
