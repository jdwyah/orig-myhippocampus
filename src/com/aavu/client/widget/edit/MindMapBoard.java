package com.aavu.client.widget.edit;

import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.gui.mapper.MapperWidget;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MindMapBoard extends Composite {

	protected static final int MAP_HEIGHT = 400;
	protected static final int MAP_WIDTH = 600;
	
	private MapperWidget mapper;
	private MindTreeOcc tree;

	public MindMapBoard(final Manager manager, Topic topic, TopicEditWidget widget) {

		VerticalPanel mainPanel = new VerticalPanel();
		
		tree = topic.getMindTree();
		
				
		Button mapB = new Button(manager.myConstants.mapperAddMap());
		mapB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {		
				if(mapper == null){
					mapper = new MapperWidget(manager,MAP_WIDTH,MAP_HEIGHT);
					mapper.hide();					
				}

				if(tree.getMindTree() == null){
					System.out.println("GO GET SAVED TREE");
				}
				
				mapper.loadTree(tree.getMindTree());
				
				mapper.setPopupPosition(200, 200);
				mapper.show();
			}});
		
		
		mainPanel.add(mapB);		
		
		initWidget(mainPanel);
	}

}
