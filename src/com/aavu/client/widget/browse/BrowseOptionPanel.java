package com.aavu.client.widget.browse;

import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BrowseOptionPanel extends VerticalPanel {

	private DockPanel mainView;
	private Widget lastSelected;
	
	public BrowseOptionPanel(DockPanel mainView, GWTTopicServiceAsync topicService) {
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
					if(lastSelected != null){
						lastSelected.removeStyleName("ks-SinkItem-selected");
					}
					sender.setStyleName("ks-List");
					sender.addStyleName("ks-SinkItem-selected");
					
					mainView.clear();
					mainView.add(view,DockPanel.CENTER);
					lastSelected = sender;
				}});
			
			initWidget(lab);
		}
		
	}

}
