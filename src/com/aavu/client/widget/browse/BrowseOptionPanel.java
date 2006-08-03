package com.aavu.client.widget.browse;

import com.aavu.client.service.remote.TopicServiceAsync;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BrowseOptionPanel extends VerticalPanel {

	private DockPanel mainView;

	public BrowseOptionPanel(DockPanel mainView, TopicServiceAsync topicService) {
		this.mainView = mainView;
		
		Linker l = new Linker("Blog",new BrowseBlogView(topicService));
		Linker l2 = new Linker("Search",new BrowseSearchView());
		
		add(l);
		add(l2);
	}
	
	private class Linker extends Composite{
		
		private Widget view;

		public Linker(String string, Widget viewW) {
		
			this.view = viewW;
			
			Label lab = new Label(string);
			
			lab.addClickListener(new ClickListener(){

				public void onClick(Widget sender) {
					mainView.clear();
					mainView.add(view);
				}});
			
			setWidget(lab);
		}
		
	}

}
