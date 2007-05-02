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
import com.google.gwt.user.client.ui.VerticalPanel;
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
	
	private Grid gripP;
	
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
		gripP = new Grid(3,4);
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
		
		
		Button selectB = new Button(ConstHolder.myConstants.meta_select());		
		selectB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				callback.onSuccess(selected);
				close();
			}});
		gripP.setWidget(1,3,selectB);
		
		
		
		
		editP = new SimplePanel();
		gripP.setWidget(2, 3, editP);
		
		
		VerticalPanel mainP = new VerticalPanel();
		mainP.add(gripP);
		mainP.add(new Label(ConstHolder.myConstants.meta_chooser_help()));
		setContent(mainP);		
	}
	
	private void addChooser(int column, final Meta meta, String headerText, String newButtonText){
		final ChooseMetaW chooser = new ChooseMetaW(manager,meta);
		chooser.addChangeListener(this);
		
		gripP.setWidget(0, column, new Label(headerText));
		gripP.setWidget(1, column, chooser);
		
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
		gripP.setWidget(2,column,newButton);
		
		
	}

	public void onChange(Widget sender) {		
		selected = ((ChooseMetaW)sender).getSelectedMeta();		
		editP.setWidget(new Label(selected.getTitle()));	
	}
	
	
}
