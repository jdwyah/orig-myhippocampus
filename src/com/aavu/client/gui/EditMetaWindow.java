package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.tags.ChooseMetaW;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Window that let's users add new metas, edit existing etc.
 * 
 * Configurable to only show dates, or texts
 * 
 * @author Jeff Dwyer
 *
 */
public class EditMetaWindow extends PopupWindow {

	public static final int WIDTH = 350;
	private static final int HEIGHT = 250;
	
	private HorizontalPanel mainP;
	
	private Meta selected;
	private ChooseMetaW chooser;

	public EditMetaWindow(final Manager manager, final GInternalFrame frame, final Meta type,final StdAsyncCallback callback) {	
		super(frame,ConstHolder.myConstants.meta_title(),WIDTH,HEIGHT);
		
	
		mainP = new HorizontalPanel();
		
		mainP.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
		
		chooser = new ChooseMetaW(manager,type);
		
		mainP.add(chooser);
		
		Button selectB = new Button("Select");		
		selectB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				selected = chooser.getSelectedMeta();
				callback.onSuccess(selected);
				close();
			}});
		mainP.add(selectB);
		
		Button newB = new Button("New");		
		newB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.newMeta(new EZCallback(){
					public void onSuccess(Object result) {
						Meta newM = new MetaDate();
						newM.setTitle((String) result);
						
						//manager.createMeta(newM);
						
						chooser.add(newM);
					}});			
			}});
		mainP.add(newB);
		
		setContent(mainP);		
	}
	
	
}
