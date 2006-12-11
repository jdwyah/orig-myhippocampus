package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.gui.mapper.MapperWidget;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MindMapBoard extends Composite {

	protected static final int MAP_HEIGHT = 400;
	protected static final int MAP_WIDTH = 600;
	
	private MapperWidget mapper;
	private MindTreeOcc treeOcc;
	private Topic topic;

	public MindMapBoard(final Manager manager, Topic topic, TopicEditWidget widget) {

		this.topic = topic;
		
		VerticalPanel mainPanel = new VerticalPanel();
		
		treeOcc = topic.getMindTree();
		
				
		Button mapB = new Button(manager.myConstants.mapperAddMap());
		mapB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {		
				if(mapper == null){
					mapper = new MapperWidget(manager,MAP_WIDTH,MAP_HEIGHT);
					mapper.hide();					
				}

				/*
				 * We're specifically nulling out the MindTree elements before serialization 
				 * so we need to fetch & save explicitly 
				 */
				if(treeOcc.getMindTree() == null){
					manager.getTopicCache().getTreeFor(treeOcc,new StdAsyncCallback(manager.myConstants.mapperAsyncGet()){
						public void onSuccess(Object result) {
							super.onSuccess(result);
							MindTree tree = (MindTree) result;
							treeOcc.setMindTree(tree);
							loadNShow(treeOcc);
						}						
					});
				}else{
					loadNShow(treeOcc);
				}
				
			}});
		
		//This avoid a transientObject save problem when saving the 
		//mind map
		//TODO get rid of this requirement
		if(topic.getId() <= 0){
			mainPanel.add(new Label(manager.myConstants.mapperSaveFirst()));			
		}else{
			mainPanel.add(mapB);
		}
		
		initWidget(mainPanel);
	}

	
	private void loadNShow(MindTreeOcc treeOcc2) {
		mapper.loadTree(topic,treeOcc2);		
	
	}

	
}
