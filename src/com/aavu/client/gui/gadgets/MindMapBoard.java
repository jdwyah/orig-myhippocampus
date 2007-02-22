package com.aavu.client.gui.gadgets;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.gui.mapper.MapperWidget;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.SaveNeededListener;
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
	
	private SaveNeededListener saveNeeded;
	private VerticalPanel mainPanel;
	private Manager manager;


	public MindMapBoard(Manager manager, SaveNeededListener _saveNeeded) {
		this.saveNeeded = _saveNeeded;
		this.manager = manager;
				
		mainPanel = new VerticalPanel();
		
		initWidget(mainPanel);
	}

	


	private void loadNShow(Topic topic,MindTreeOcc treeOcc2) {
		mapper.loadTree(topic,treeOcc2);		
	
	}


	public void load(final Topic topic) {
		
		mainPanel.clear();
		
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
							loadNShow(topic,treeOcc);
						}						
					});
				}else{
					loadNShow(topic, treeOcc);
				}
				saveNeeded.onChange(MindMapBoard.this);
				
			}});
		
		//This avoid a transientObject save problem when saving the 
		//mind map
		//TODO get rid of this requirement
		if(topic.getId() <= 0){
			mainPanel.add(new Label(manager.myConstants.mapperSaveFirst()));			
		}else{
			mainPanel.add(mapB);
		}
	}

	
}
