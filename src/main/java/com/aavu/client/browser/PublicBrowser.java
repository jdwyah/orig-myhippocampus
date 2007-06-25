package com.aavu.client.browser;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.HippocampusBrowser;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.GadgetDisplayer;
import com.aavu.client.gui.GadgetDisplayerBarImpl;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetManager;
import com.aavu.client.service.BrowserManager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class PublicBrowser extends Composite implements GadgetDisplayer {

	private FlexTable panel;
	private HippocampusBrowser app;
	private GadgetManager 		gm;
	private BrowserManager bmanager;
	private Topic topic;
	

	public PublicBrowser(HippocampusBrowser browser, BrowserManager bmanager){
		this.app=browser;
		this.bmanager = bmanager;
		
		panel = new FlexTable();

		bmanager.getGadgetManager().setGadgetDisplayer(this);


		initWidget(panel);
	}



	public Widget getWidget() {	
		return this;
	}


	/**
	 * user && || topic can be null and we'll revert to browsing
	 * 
	 * @param user
	 * @param topic
	 */
	public void load(String user, String topic) {
		if(user!=null){

			browseUser(user);

			if(topic != null){
				showTopic(user,topic);
			}

		}else{
			browseAllUsers();
		}
	}



	private void showTopic(String user, String topic) {

		app.getHippoCache().getTopicCache().getTopicForNameA(topic,new StdAsyncCallback(ConstHolder.myConstants.topic_lookupAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);				
				displayTopic((Topic) result);				
			}});
	}



	protected void displayTopic(Topic topic) {
		if(topic != null){
			bmanager.getGadgetManager().load(topic);
		}else{
			System.out.println("Null topic");
		}
	}



	private void browseUser(String user) {
		// TODO Auto-generated method stub

	}



	private void browseAllUsers() {
		// TODO Auto-generated method stub

	}



	public void addGadget(Gadget gadget) {
		// TODO Auto-generated method stub		
	}

	public void load(Topic topic, List gadgetsToUse) {
		this.topic = topic;
		
		panel.clear();		
				
		
		for (Iterator iter = gadgetsToUse.iterator(); iter.hasNext();) {
			Gadget gadget = (Gadget) iter.next();
			
			gadget.load(topic);
			
			panel.add(gadget);			
		}
		
		setVisible(true);
		
		for (Iterator iter = gadgetsToUse.iterator(); iter.hasNext();) {
			Gadget gadget = (Gadget) iter.next();			
			gadget.nowVisible();			
		}
				
	}

	public void unload() {
		panel.clear();
	}

}